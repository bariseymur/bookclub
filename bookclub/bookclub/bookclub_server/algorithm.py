import math
import re

import pandas as pd
from django.core.serializers.json import DjangoJSONEncoder
from django.forms import model_to_dict
from django_pandas.io import read_frame
from django.core.serializers.json import DjangoJSONEncoder
from django.forms import model_to_dict
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework.utils import json
from .models import User, Match, TradeList, Suggestion, History, AccountSettings, UserRating, Chat, Book, WishList
from . import emailservice
from django.http import JsonResponse
from django.db.models import Q
import random
import datetime
import csv
from faker import Faker
from faker import Factory
import factory
import factory.django
import pandas as pd, numpy as np
from sklearn.cluster import DBSCAN


@api_view(['GET'])
def match_algorithm(request):
    # latitude: 0.135 = 15 km, 0.009 = 1 km, 0.45 = 50 km
    # longitude: 0.35 = 15 km, 0.023 = 1 km, 1.15 = 50 km

    user = User.objects.get(id=request.session['user'])

    user_lat = user.lat
    user_long = user.long
    #    lat_difference = 0.318
    #    long_difference = 0.813

    radius_lat_gt = user_lat + 0.318  # 0.81
    radius_long_gt = user_long + 0.813  # 2.1
    radius_lat_lt = user_lat - 0.318
    radius_long_lt = user_long - 0.813

    # get the users that are in the radius
    qs = User.objects.filter(
        (Q(lat__gte=user_lat) & Q(long__gte=user_long) & Q(lat__lte=radius_lat_gt) & Q(long__lte=radius_long_gt)) |
        (Q(lat__lte=user_lat) & Q(long__lte=user_long) & Q(lat__gte=radius_lat_lt) & Q(long__gte=radius_long_lt)))
    df = read_frame(qs)
    # get their ids from the df
    user_ids = []
    ids = df.loc[:, 'id'].to_dict()
    for index in ids:
        user_ids.append(ids[index])

    user_wishlist = WishList.objects.filter(user_id=user.id)
    df2 = read_frame(user_wishlist)
    user_desired_books = []
    books = df2.loc[:, 'book_id'].to_dict()
    for i in books:
        user_desired_books.append(re.findall('\d+', books[i]))
    first_matches = []
    for other_user_id in user_ids:
        other_user_tradelist = TradeList.objects.filter(user_id=other_user_id)
        df3 = read_frame(other_user_tradelist)
        other_user_giving_books = []
        books = df3.loc[:, 'givingBook_id'].to_dict()
        common_books = []
        for i in books:
            other_user_giving_books.append(re.findall('\d+', books[i]))
        for giving_book in other_user_giving_books:
            for wanted_book in user_desired_books:
                if wanted_book == giving_book:
                    #  print("Match found " + str(wanted_book[0]) + " " + str(other_user_id))
                    common_books.append(int(wanted_book[0]))
        if common_books:
            first_matches.append([other_user_id, common_books])
    print(first_matches)  # ondan alacaklarim

    # if match is confirmed delete those books from tradelist and wishlist
    # if chat is confirmed delete the chat row

    user_tradelist = TradeList.objects.filter(user_id=user.id)
    df2 = read_frame(user_tradelist)
    user_giving_books = []
    books = df2.loc[:, 'givingBook_id'].to_dict()
    for i in books:
        user_giving_books.append(re.findall('\d+', books[i]))  # userin vermek istedigi kitaplarin idsi
    second_matches = []
    for i in range(len(first_matches)):
        other_user_id = (first_matches[i])[0]
        other_user_wishlist = WishList.objects.filter(user_id=other_user_id)
        df3 = read_frame(other_user_wishlist)
        other_user_wanted_books = []
        books = df3.loc[:, 'book_id'].to_dict()
        common_books_2 = []
        for i in books:
            other_user_wanted_books.append(re.findall('\d+', books[i]))
        for wanted_book in other_user_wanted_books:
            for giving_book in user_giving_books:
                if giving_book == wanted_book:
                    #  print("Match found " + str(wanted_book[0]) + " " + str(other_user_id))
                    common_books_2.append(int(giving_book[0]))
        if common_books_2:
            second_matches.append([other_user_id, common_books_2])
    print(second_matches)  # ona vereceklerim
    final_matches = []
    for i in range(len(second_matches)):
        for j in range(len(first_matches)):
            if (second_matches[i])[0] == (first_matches[j])[0]:
                final_matches.append([(second_matches[i])[0], (second_matches[i])[1],
                                      (first_matches[j])[1]])  # user_id, onu verceklerim, ondan alcaklarım

    print(final_matches)  # user_id, ona verceklerim, ondan alcaklarım
    final_match_scores = []
    for i in range(len(final_matches)):
        matched_id = (final_matches[i])[0]
        matched = UserRating.objects.filter(rated_user_id=matched_id)
        rate_sum = 0
        for rates in matched:
            rate_sum = rate_sum + rates.rating
        if len(matched) == 0:
            final_rate = 2.5
        else:
            final_rate = rate_sum / len(matched)
        final_rate = final_rate * 10
        print("user_rating point: " + str(final_rate))
        long_distance = abs(User.objects.get(id=matched_id).long - user.long) / 0.023
        lat_distance = abs(User.objects.get(id=matched_id).lat - user.lat) / 0.009
        # print("long " + str(long_distance))
        # print("lat " + str(lat_distance))
        distance = math.sqrt((long_distance ** 2) + (lat_distance ** 2))
        print("distance: " + str(distance))
        distance_point = 50 - distance
        final_match_scores.append([matched_id, int(final_rate + distance_point)])
    print(final_match_scores)
    for j in range(len(final_matches)):
        matched_user_id = final_matches[j][0]
        for n in range(len(final_match_scores)):
            if final_match_scores[n][0] == matched_user_id:
                match_score = final_match_scores[n][1]
                break
        user_id = request.session["user"]
        for k in range(len(final_matches[j][1])):
            giving_book_id = final_matches[j][1][k]
            #print(giving_book_id)
            for m in range(len(final_matches[j][2])):
                wanted_book_id = final_matches[j][2][m]
                match_table_row = Match.objects.filter(giving_book_id=giving_book_id, matched_user_id=matched_user_id,
                                                       user_id_id=user_id,
                                                       wanted_book_id=wanted_book_id)
                if match_table_row.exists():
                    continue
                else:
                    match_table_row = Match(match_score=match_score, state="pending",
                                            match_date=datetime.datetime.now().strftime("%Y-%m-%d"),
                                            giving_book_id=giving_book_id, matched_user_id=matched_user_id,
                                            user_id_id=user_id,
                                            wanted_book_id=wanted_book_id)
                    match_table_row.save()
    return JsonResponse({"size_before": 'yes'})


































# @api_view(['GET'])
# def match_algorithm(request):
#     # books = pd.read_csv('datasets/BX-Books.csv', sep = ';',error_bad_lines=False,  encoding='latin-1')
#     # books = books.loc[0:25663, :]
#     # 1. select the users whose loc is in only 15 km from the session user's loc
#     # 2. from the filtered df, find the users whose tradelist books are matched with the ses
#     # latitude: 0.135 = 15 km
#     # longitude: 0.35 = 15 km
#     # get the user in the session
#     user = User.objects.get(id=request.session['user'])
#     # user's lat and long
#     user_lat = user.lat
#     user_long = user.long
#     # radius for lat and long - will be a while loop in the end
#     # do we need to add point section into match or will be ordered in decreasing order anyway?
#     radius_lat_gt = user_lat + 0.5
#     radius_long_gt = user_long + 1.1
#     radius_lat_lt = user_lat - 0.5
#     radius_long_lt = user_long - 1.1
#     # get the users that are in the radius
#     qs = User.objects.filter(
#         (Q(lat__gte=user_lat) & Q(long__gte=user_long) & Q(lat__lte=radius_lat_gt) & Q(long__lte=radius_long_gt)) |
#         (Q(lat__lte=user_lat) & Q(long__lte=user_long) & Q(lat__gte=radius_lat_lt) & Q(long__gte=radius_long_lt)))
#     df = read_frame(qs)
#     user_ids = []
#     ids = df.loc[:, 'id'].to_dict()

#     for index in ids:
#         if ids[index] != user.id:
#             user_ids.append(ids[index])

#     # wishlist= WishList.objects.filter(user_id=request.session['user'])
#     # wish_df= read_frame(wishlist)
#     # wish_ids = wish_df.loc[:, 'book_id']
#     # item_ids = []
#     # for index in user_ids:
#     #     fr = TradeList.objects.filter(user_id=index)
#     #     fr_df = read_frame(fr)
#     #     fr_ids = fr_df.loc[:, 'givingBook_id']

#     #     for item in wish_ids:
#     #         matches= fr_ids.isin([item])
#     #         if not matches.empty:
#     #             print(item)
#     #             print(index)
#     #             item_ids.append(item)








#     # book_ids = []
#     # matched_users = []
#     # for i in user_ids:

#     # coords = df.as_matrix(columns=['lat', 'long'])
#     # kms_per_radian = 6371.0088
#     # epsilon = 15 / kms_per_radian
#     # db = DBSCAN(eps=epsilon, min_samples=1, algorithm='ball_tree', metric='haversine').fit(np.radians(coords))
#     # cluster_labels = db.labels_
#     # num_clusters = len(set(cluster_labels))
#     # clusters = pd.Series([coords[cluster_labels == n] for n in range(num_clusters)])
#     # print('Number of clusters: {}'.format(num_clusters))



#     return JsonResponse({"number_of_close_users": user_ids})