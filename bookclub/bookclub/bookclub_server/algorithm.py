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

@api_view(['GET'])
def match_algorithm(request):
    # books = pd.read_csv('datasets/BX-Books.csv', sep = ';',error_bad_lines=False,  encoding='latin-1')
    # books = books.loc[0:25663, :]
    qs = User.objects.all()
    df = read_frame(qs)
    return JsonResponse({"size": str(df.shape)})

