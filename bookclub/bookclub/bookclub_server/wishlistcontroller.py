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
            index = 0
            for book in wishlist:
                index += 1
                wishlist_index.append({"wishlist_info": model_to_dict(book),
                                       "book_info": model_to_dict(book.book_id)})
                if index > 50:
                    break
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
            count = WishList.objects.filter(Q(user_id=request.session['user'])).count()
            new_row = WishList(id=None, book_id=Book.objects.get(id=user_data['book_id']), user_id=User.objects.get(id=request.session['user']), order=count+1)
            new_row.save()
            status = 'success'
            message = 'the book was succesfully added to the wishlist'
    else:
        status = 'error'
        message = 'you should login first'

    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)


@api_view(['POST'])
def drag(request):
    user_data = json.loads(request.body) # {"action":"up", "wishlist_id":"1" (+1) or "action":"down", "wishlist_id":"1" (-1)}
    if "user" in request.session:
        if WishList.objects.filter(Q(id=user_data['wishlist_id']) & Q(user_id=request.session['user'])).exists():
            wishlist = WishList.objects.get(Q(id=user_data['wishlist_id']) & Q(user_id=request.session['user']))
            ord=wishlist.order

            if user_data['action'] == 'up':
                wishlist2 = WishList.objects.get(Q(order=ord - 1) & Q(user_id=request.session['user']))
                status = 'success'
                message = 'the book was succesfully dragged up'
                wishlist.order = wishlist.order - 1
                wishlist2.order= wishlist2.order + 1
                wishlist.save()
                wishlist2.save()
            elif user_data['action'] == 'down':
                wishlist2 = WishList.objects.get(Q(order=ord + 1) & Q(user_id=request.session['user']))
                status = 'success'
                message = 'the book was succesfully dragged down'
                wishlist.order = wishlist.order + 1
                wishlist2.order = wishlist2.order - 1
                wishlist.save()
                wishlist2.save()
        else:
            status = 'error'
            message = 'this wishlist entry does not exist'
    else:
        status = 'error'
        message = 'you should login first'

    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)
"""
@api_view(['POST'])
def drag(request):
    user_data = json.loads(request.body)  # {"action":"up", "wishlist_id":"1" (+1) or "action":"down", "wishlist_id":"1" (-1)}
    if "user" in request.session:
        if user_data is not None:
            wishlist_row = WishList.objects.filter(Q(id=user_data['wishlist_id']))
            if wishlist_row.exists():
                for row in wishlist_row:
                    if row.user_id_id == request.session['user']:
                        if user_data['action'] == 'up':
                            status = 'success'
                            message = 'the book was succesfully dragged up'
                            row.order = row.order - 1
                            row.save()
                        elif user_data['action'] == 'down':
                            status = 'success'
                            message = 'the book was succesfully dragged down'
                            row.order = row.order + 1
                            row.save()
                        else:
                            status = 'error'
                            message = 'wishlist cannot be updated'
                    else:
                        status = 'error'
                        message = 'this wishlist entry does not belong to you'
            else:
                status = 'error'
                message = 'this wishlist entry does not exist'
        else:
            status = 'error'
            message = 'invalid request'
    else:
        status = 'error'
        message = 'you should login first'
    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)
"""