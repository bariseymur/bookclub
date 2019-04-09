from django.core.serializers.json import DjangoJSONEncoder
from django.forms import model_to_dict
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework.utils import json
from .models import User, Match, TradeList, Suggestion, WishList, Book
from django.http import JsonResponse
from django.db.models import Q
import random


@api_view(['GET'])
def index(request):
    # the user in the session gets all wishlist
    wishlist_index = []
    if "user" in request.session:
        wishlist = WishList.objects.filter(Q(user_id=request.session['user'])).select_related(
            "book_id")
        if wishlist.exists():
            status = "success"
            message = "here is the wishlist"
            for book in wishlist:
                wishlist_index.append({"wishlist_info": model_to_dict(book),
                                       "book_info": model_to_dict(book.book_id)})
        else:
            status = "error"
            message = "you do not have anything in the wishlist"
            wishlist_index = None
    else:
        status = "error"
        message = "you should login first"
        wishlist_index = None

    json_data = {"status": status, "message": message, "wishlist": wishlist_index}
    return JsonResponse(json_data, safe=False)


@api_view(['DELETE'])
def delete(request):
    # first checking if the row exists in the table, if yes then delete or return error
    user_data = json.loads(request.body)  # {"wishlist_id":"1"}
    if "user" in request.session:
        if WishList.objects.filter(Q(user_id=request.session['user']) & Q(id=user_data['wishlist_id'])).exists():
            status = 'success'
            message = 'the book was deleted from the wishlist'
            WishList.objects.filter(Q(user_id=request.session['user']) & Q(id=user_data['wishlist_id'])).delete()
        else:
            status = 'error'
            message = 'the book is not in your wishlist'
    else:
        status = 'error'
        message = 'you should login first'

    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)


@api_view(['POST'])
def add(request):
    user_data = json.loads(request.body) # {"book_id":"1"}
    if "user" in request.session:
        if WishList.objects.filter(Q(book_id=user_data['book_id']) & Q(user_id=request.session['user'])).exists():
            status = 'error'
            message = 'this book is already in your wishlist'
        else:
            new_row = WishList(id=None, book_id=Book.objects.get(id=user_data['book_id']), user_id=User.objects.get(id=request.session['user']))
            new_row.save()
            status = 'success'
            message = 'the book was succesfully added to the wishlist'
    else:
        status = 'error'
        message = 'you should login first'

    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)