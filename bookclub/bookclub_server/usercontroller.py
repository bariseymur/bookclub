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


@api_view(['GET'])
def get_session(request):
    # this method returns the id of the user who is in the session, 
    # if there is no user in the session it returns -1
    if "user" in request.session:
        return JsonResponse({'session_id': model_to_dict(User.objects.get(id=request.session['user']))})
    else:
        return JsonResponse({'session_id': -1})


@api_view(['POST'])
def login(request):
    # this method logs in the user whose cridentials are correct and opens up a new session
    # the session is not closed until the user logs out
    user_data = json.loads(request.body)
    if User.objects.filter(username=user_data['username']).exists():
        user = User.objects.get(username=user_data['username']) 
        if user.password == user_data['password']:
            status = 'success'
            message = 'you are logged in'
            request.session['user'] = user.id # opened a session
        else:
            status = 'error'
            message = 'the password is incorrect'
    else:
        status = 'error'
        message = 'there is no user with this username'

    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)


@api_view(['POST'])
def signup(request): 
    # this method signs up the user and opens a session for them
    # it also adds the user's account settings into the accountsettings table
    user_data = json.loads(request.body)
    if User.objects.filter(username=user_data['username']).exists() or User.objects.filter(mail=user_data['mail']).exists():
        status = 'error'
        message = 'This username or email address already exists'
    else:
        status = 'success'
        message = 'User successfully signed up'
        user = User(name=user_data['name'], country=user_data['country'],
                    mail=user_data['mail'], phoneNumber=user_data['phoneNumber'], dateOfBirth=user_data['dateOfBirth'],
                    username=user_data['username'], password=user_data['password'], long=user_data['long'],
                    lat=user_data['lat'], onlineState=user_data['onlineState'],
                    profilePicture=user_data['profilePicture'])
        user.save()
        user_settings = AccountSettings(user_id=user)
        user_settings.save()
        request.session['user'] = user.id # opened a session
        emailservice.signup_email(request) # signup mail is sent

    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)


@api_view(['POST'])
def forgot_password(request):
    # this method is for resetting the password if it is forgotten
    data = json.loads(request.body)
    user_row = User.objects.filter((Q(username=data['username']) | Q(mail=data['mail'])))
    # if data['mail'] == user.mail:
    if user_row.exists():
        for user in user_row:
            status = 'success'
            message = 'new password will be sent'
            s = "abcdefghijklmnopqrstuvwxyz01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ!?."
            p = "".join(random.sample(s, 8))
            new_password = p
            new_request = {"username": user.username, "mail": user.mail, "new_password": new_password}
            emailservice.forgot_password_email(json.dumps(new_request))
            user.password = new_password
            user.save()
    else:
        status = 'error'
        message = 'user does not exist'
        new_password = -1

    json_data = {"status": status, "message": message, "password": new_password}
    return JsonResponse(json_data)


@api_view(['GET'])
def sign_out(request):
    # this method signs out the user and closes session
    if "user" in request.session:
        del request.session['user'] # session is closed
        status = 'success'
        message = 'you signed out'
    else:
        status = 'error'
        message = 'there is no user in the session'

    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)


@api_view(['POST'])
def confirm_match(request):
    # needs to be added to the history (to be discussed)
    # buraya matchler mlden geliyor, do we need to add two matches for both users? (Question to be discussed on Sunday)
    # this method confirms match
    user_data = json.loads(request.body)
    match = Match.objects.get(id=user_data['match_id'])
    user = User.objects.get(id=match.user_id1.id) or User.object.get(id=match.user_id2.id)
    if "user" in request.session:
        if request.session['user'] == user.id:
            if match.state == 'pending':
                match.state = 'confirmed'
                match.save()
                date = datetime.datetime.now().strftime("%Y-%m-%d")
                new_history_row = History(id=None, user_id=User.objects.get(id=request.session['user']), matchConfirmation_id=match, matchRejection_id=None, dateOfAction=date)
                new_history_row.save()
                status = 'success'
                message = 'the match was confirmed succesfully'
            else:
                status = 'error'
                message = 'could not confirm the match'
        else:
            status = 'error'
            message = 'this action cannot be done'
    else:
        status = 'error'
        message = 'there is no user in the session'

    json_data = {"status": status, "message": message, "match_info": model_to_dict(match)}
    return JsonResponse(json_data)


@api_view(['POST'])
def reject_match(request):
    # add to the history - not complete yet
    # this method rejects match
    user_data = json.loads(request.body)
    match = Match.objects.get(id=user_data['match_id'])
    user = User.objects.get(id=match.user_id1.id) or User.object.get(id=match.user_id2.id)
    if "user" in request.session:
        if request.session['user'] == user.id:
            if match.state == 'pending':
                match.state = 'rejected'
                match.save()
                date = datetime.datetime.now().strftime("%Y-%m-%d")
                new_history_row = History(id=None, user_id=User.objects.get(id=request.session['user']), matchConfirmation_id=None, matchRejection_id=match, dateOfAction=date)
                new_history_row.save()
                status = 'success'
                message = 'the match was rejected succesfully'
            else:
                status = 'error'
                message = 'could not reject the match'
        else:
            status = 'error'
            message = 'this action cannot be done'
    else:
        status = 'error'
        message = 'there is no user in the session'

    json_data = {"status": status, "message": message, "match_info": model_to_dict(match)}
    return JsonResponse(json_data)


@api_view(['POST'])
def see_other_user_profile(request):
    # returns the profile of a user
    data = json.loads(request.body)
    if User.objects.filter(username=data['username']).exists():
        user = User.objects.get(username=data['username'])
        status = 'success'
        message = 'other user data send successfully'
        json_data = {"status": status, "message": message, "user_info": model_to_dict(user)}
    else:
        status = 'error'
        message = 'there is no user with this name'
        json_data = {"status": status, "message": message}

    return JsonResponse(json_data)

@api_view(['POST'])
def get_user_profile(request):
    # returns the profile of a user in the session
    if "user" in request.session:
        status = 'success'
        message = 'other user data send successfully'
        user = User.objects.get(id=request.session['user'])
        json_data = {"status": status, "message": message, "user_info": model_to_dict(user)}
    else:
        status = 'error'
        message = 'there is no user with this name'
        json_data = {"status": status, "message": message}

    return JsonResponse(json_data)

    
# confirm ve reject matchde serializible eklemeliyiz ???(discuss)
# this function is used to obtain match list index of a user
@api_view(['GET'])
def match_list_index(request):
    # does not need any json loading because checking with session already
    if "user" in request.session:
        matchlistIndex = []
        matchlistRows = Match.objects.filter(
            (Q(user_id1=request.session['user']) | Q(user_id2=request.session['user'])) & Q(state='pending'))
        if matchlistRows.exists():
            status = 'success'
            message = 'Match list will be displayed'
            index = 0
            for match in matchlistRows:
                index += 1
                matchlistIndex.append({"matchlist_info": model_to_dict(match),
                                       "book_info": model_to_dict(match.book_id)})
                if index > 50: # limited for 50 matches only - no randomizing
                    break
        else:
            status = "error"
            message = "there is no match list to display"
            matchlistIndex = None
    else:
        status = 'error'
        message = 'you should login first'
        matchlistIndex = None

    json_data = {"status": status, "message": message, "matchlistIndex": matchlistIndex}
    return JsonResponse(json_data)


@api_view(['GET'])
def suggestion_list_index(request):
    # does not need any json loading
    if "user" in request.session:
        suggests = Suggestion.objects.filter(user_id=request.session['user'])
        if suggests.exists():
            suggest_list = []
            index = 0
            for suggest in suggests:
                index += 1
                suggest_list.append({
                    "suggest_info": model_to_dict(suggest),
                    "book_info": model_to_dict(suggest.book_id)
                })
                if index > 50: # limited for 50 suggestions only - no randomizing
                    break
            status = 'success'
            message = 'suggestion data sent successfully'
        else:
            status = 'error'
            message = 'no suggestion for this user'
            suggest_list = None
    else:
        status = 'error'
        message = 'you should login first'
        suggest_list = None

    json_data = {"status": status, "message": message, "suggestionList": suggest_list}
    return JsonResponse(json_data)


@api_view(['GET'])
def main_menu_index(request):
    # this function returns the books from the menu screen of the user
    # user_data = json.loads(request.body) # json = { "action":"main_menu" } it is optional is not needed actually
    # if there is a user in the session
    menu_index = []
    if "user" in request.session:
        tradelist = TradeList.objects.filter(~Q(user_id=request.session['user']))
        if tradelist.exists():
            status = "success"
            message = "here are the books for your main menu"
        else:
            status = "error"
            message = "there is nothing to show for the main menu"
            menu_index = None
    else:
        tradelist = TradeList.objects.all()
        if tradelist.exists():
            status = "success"
            message = "here are the books for your main menu"
        else:
            status = "error"
            message = "there is nothing to show for the main menu"
            menu_index = None
    index = 0
    for trade in tradelist:
        index += 1
        menu_index.append({
            "trade_info": model_to_dict(trade),
            "book_info": model_to_dict(trade.givingBook_id),
            "user_info": model_to_dict(trade.user_id)
        })
        if index > 50:
            break

    json_data = {"status": status, "message": message, "mainMenuIndex": menu_index}
    return JsonResponse(json_data, safe=False)


@api_view(['POST'])
def search_index(request):
    user_data = json.loads(request.body)  # json = { "search_query":"something" }
    search_index = []
    if "user" in request.session:
        tradelist = TradeList.objects.filter(~Q(user_id=request.session['user'])).select_related(
            "givingBook_id").filter(givingBook_id__title__icontains=user_data['search_query'])
        if tradelist.exists():
            status = "success"
            message = "the search query is found successfully"
        else:
            status = "error"
            message = "nothing was found for this search query"
            search_index = None
    else:
        tradelist = TradeList.objects.all().select_related("givingBook_id").filter(
            givingBook_id__title__icontains=user_data['search_query'])
        if tradelist.exists():
            status = "success"
            message = "the search query is found successfully"
        else:
            status = "error"
            message = "nothing was found for this search query"
            search_index = None

    index = 0
    for trade in tradelist:
        index += 1
        search_index.append({
            "trade_info": model_to_dict(trade),
            "book_info": model_to_dict(trade.givingBook_id),
            "user_info": model_to_dict(trade.user_id)
        })
        if index > 50:
            break

    json_data = {"status": status, "message": message, "searchIndex": search_index}
    return JsonResponse(json_data, safe=False)


@api_view(['POST'])
def rate_user(request):
    data = json.loads(request.body) # {"user_id":"1", "rating":"10"}
    test = UserRating.objects.filter(rating_user=User.objects.get(id=request.session['user']), rated_user=User.objects.get(id=data['user_id']), rating=data['rating'])
    if 'user' in request.session:
        if Chat.objects.filter((Q(user_id_1=request.session['user']) & Q(user_id_2=data['user_id'])) | (Q(user_id_2=request.session['user']) & Q(user_id_1=data['user_id']))).exists():
            chat = Chat.objects.get((Q(user_id_1=request.session['user']) & Q(user_id_2=data['user_id'])) | (Q(user_id_2=request.session['user']) & Q(user_id_1=data['user_id'])))
            if chat.state_1 == 'confirmed' and chat.state_2 == 'confirmed':
                if data['rating'] > 10 or data['rating'] < 0:
                    status = 'error'
                    message = 'the rating input is invalid'
                elif test.exists():
                    status = 'error'
                    message = 'you have already rated this user'
                else:
                    status = 'success'
                    message = 'you succesfully rated the user'
                    rating = UserRating(rating_user=User.objects.get(id=request.session['user']), rated_user=User.objects.get(id=data['user_id']), rating=data['rating'])
                    rating.save()
        else:
            status = 'error'
            message = 'you cannot rate a user that you never had a trade with'
    else:
        status = 'error'
        message = 'you should login first'

    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)

@api_view(['POST'])
def get_book(request):
    data = json.loads(request.body)  # json = { "search_query":"something" }
    if Book.objects.filter(id=data['book_id']).exists():
        book = Book.objects.get(id=data['book_id'])
        status = 'success'
        message = 'other user data send successfully'
        json_data = {"status": status, "message": message, "book_info": model_to_dict(book)}
    else:
        status = 'error'
        message = 'there is no book with this id'
        json_data = {"status": status, "message": message}

    return JsonResponse(json_data)

# @api_view(['POST'])
# def add_books(request):
#     with open('C:\\Users\\Mehin\\Desktop\\book\\bookclub\\bookclub\\bookclub_server\\datasets\\BX-Books.csv') as csvfile:
#         reader = csv.DictReader(csvfile, delimiter=';')
#         for row in reader:
#             p = Book(isbn=row['isbn'], title=row['title'], authorName=row['authorName'], publishDate=row['publishDate'], publisher=row['publisher'], bookPhoto=row['bookPhoto'])
#             p.save()
#     return JsonResponse({'success':'yes'})

# @api_view(['POST'])
# def seed_user(request):
#     i = 0
#     while i < 500:
#         faker = Factory.create('tr_TR')
#         name = faker.name()
#         country = 'Turkey'
#         mail = faker.email()
#         phoneNumber = faker.phone_number()
#         dateOfBirth = faker.date_of_birth(minimum_age=18, maximum_age=100)
#         username = faker.user_name()
#         password = faker.password(length=6, special_chars=False, digits=False, upper_case=False, lower_case=True)
#         longitude = random.uniform(36, 42) 
#         latitude = random.uniform(26,45)

#         user = User(name=name, country=country, mail=mail, phoneNumber=phoneNumber, dateOfBirth=dateOfBirth, username=username, password=password,
#                     long=longitude, lat=latitude, onlineState=1, profilePicture='noimage.jpg')

#         user.save()
#         user_settings = AccountSettings(user_id=user)
#         user_settings.save()
#         i += 1
#     return JsonResponse({'success': 'yes'})

# @api_view(['POST'])
# def seed_wishlist(request):
#     i = 1
#     while i <= 489:
#         wishlistSize = random.randint(0, 10)
#         # print(tradelistSize)
#         index = 0
#         while index < wishlistSize:
#             if i <= 50:
#                 book_id = random.randint(1, 30)
#             else:
#                 book_id = random.randint(1, 25665)
#             if WishList.objects.filter(book_id_id=book_id, user_id_id=i).count() == 1:
#                 continue
#             else:
#                 wishlistRow = WishList(book_id_id=book_id, user_id_id=i, order=(index+1))
#                 wishlistRow.save()
#             index = index + 1
#         i = i + 1
#     return JsonResponse({'success': 'yes'})

# @api_view(['POST'])
# def seed_tradelist(request):
#     i = 1
#     while i <= 489:
#         tradelistSize = random.randint(0, 10)
#        # print(tradelistSize)
#         index = 0
#         while index < tradelistSize:
#             if i <= 50:
#                 add_book_id = random.randint(1, 30)
#             else:
#                 add_book_id = random.randint(1, 25665)
#             wishlistRow = WishList.objects.filter(Q(user_id=i) & Q(book_id=add_book_id))
#             if (wishlistRow.exists()) or (TradeList.objects.filter(givingBook_id_id=add_book_id, user_id_id=i).count() == 1):
#                 continue
#             else:
#                 tradelistRow = TradeList(givingBook_id_id=add_book_id, user_id_id=i)
#                 tradelistRow.save()
#             index = index + 1
#         i = i + 1
#     return JsonResponse({'success': 'yes'})