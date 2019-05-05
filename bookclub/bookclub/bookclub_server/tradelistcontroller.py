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
def index(request): # WORKS
    # the user in the session gets all tradelist
    tradelist_index = []
    if "user" in request.session:
        tradelist = TradeList.objects.filter(Q(user_id=request.session['user'])).select_related(
            "givingBook_id")
        if tradelist.exists():
            status = "success"
            message = "Tradelist will be displayed"
            index = 0
            for book in tradelist:
                index += 1
                tradelist_index.append({"tradelist_info": model_to_dict(book),
                                        "giving_book_info": model_to_dict(book.givingBook_id)
                                        })
                if index > 50:
                    break
        else:
            status = "error"
            message = "you do not have anything in the tradelist"
            tradelist_index = None
    else:
        status = "error"
        message = "you should login first"
        tradelist_index = None

    json_data = {"status": status, "message": message, "tradelist": tradelist_index}
    return JsonResponse(json_data, safe=False)


@api_view(['DELETE'])
def delete(request): # WORKS
    # first checking if the row exists in the table, if yes then delete or return error
    user_data = json.loads(request.body)  # {"tradelist_id":"1"}
    if "user" in request.session:
        if TradeList.objects.filter(Q(user_id=request.session['user']) & Q(id=user_data['tradelist_id'])).exists():
            status = 'success'
            message = 'the trade was deleted from the tradelist'
            TradeList.objects.filter(Q(user_id=request.session['user']) & Q(id=user_data['tradelist_id'])).delete()
        else:
            status = 'error'
            message = 'this trade is not in your tradelist'
    else:
        status = 'error'
        message = 'you should login first'

    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)


@api_view(['POST'])
def add(request): # WORKS
    user_data = json.loads(request.body)  # {"givingBook_id":1, "user_id":1 }
    if "user" in request.session:
        if TradeList.objects.filter(Q(givingBook_id=user_data['givingBook_id']) &
                                    Q(user_id=request.session['user'])).exists():
            status = 'error'
            message = 'this trade already exists'
        else:
            if request.session['user'] == user_data['user_id']:
                new_row = TradeList(id=None, givingBook_id=Book.objects.get(id=user_data['givingBook_id']),
                                    user_id=User.objects.get(id=request.session['user']))
                new_row.save()
                status = 'success'
                message = 'the trade was successfully added to the tradelist'
            else:
                status = 'error'
                message = 'you cannot add a trade for another user'
    else:
        status = 'error'
        message = 'you should login first'

    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)


# @api_view(['POST'])
# def update(request):
#     user_data = json.loads(request.body)  # {"tradelist_id":1, "givingBook_id":1, "user_id": 2 }
#     if "user" in request.session:
#         if TradeList.objects.filter(Q(id=user_data['tradelist_id']) &
#                                     Q(user_id=request.session['user'])).exists():
#             row = TradeList.objects.get(id=user_data['tradelist_id'])
#             if row.givingBook_id_id != user_data['givingBook_id']:
#                     row.givingBook_id_id = user_data['givingBook_id']
#                     row.save()
#                     status = 'success'
#                     message = 'tradelist is updated successfully'
#                 else:
#                     status = 'error'
#                     message = 'you cannot give and take the same book in a trade'
#             else:
#                 status = 'error'
#                 message = 'this trade already exists in your tradelist'
#         else:
#             if user_data['user_id'] != request.session['user']:
#                 status = 'error'
#                 message = 'you cannot update trade of another user'
#             else:
#                 status = 'error'
#                 message = 'this trade does not exist'
#     else:
#         status = 'error'
#         message = 'you should login first'

#     json_data = {"status": status, "message": message}
#     return JsonResponse(json_data)