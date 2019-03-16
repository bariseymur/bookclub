from django.core.serializers.json import DjangoJSONEncoder
from django.forms import model_to_dict
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework.utils import json
from .models import User, Match, TradeList, Suggestion
from django.http import JsonResponse
from django.db.models import Q
import random


@api_view(['GET'])
def get_session(request):
    if "user" in request.session:
        return JsonResponse({'session_id': request.session['user']})
    else:
        return JsonResponse({'session_id': -1})


@api_view(['GET'])
def login(request):
    # hash eklenecek
    user_data = json.loads(request.body)
    if User.objects.filter(username=user_data['username']).exists() or User.objects.filter(
            mail=user_data['mail']).exists():
        user = User.objects.get(username=user_data['username'])
        if user.password == user_data['password']:
            status = 'success'
            message = 'you are logged in'
            request.session['user'] = user.id
        else:
            status = 'error'
            message = 'the password is incorrect'
    else:
        status = 'error'
        message = 'there is no user with this username'

    json_data = {"status": status, "message": message, "session_user": request.session['user']}
    return JsonResponse(json_data)


@api_view(['POST'])
def signup(request):
    user_data = json.loads(request.body)
    if User.objects.filter(username=user_data['username']).exists() or User.objects.filter(
            mail=user_data['mail']).exists():
        status = 'error'
        message = 'This username or email address already exists'
    else:
        status = 'success'
        message = 'User successfully signed up'
        user = User(name=user_data['name'], surname=user_data['surname'], country=user_data['country'],
                    mail=user_data['mail'], phoneNumber=user_data['phoneNumber'], dateOfBirth=user_data['dateOfBirth'],
                    username=user_data['username'], password=user_data['password'], long=user_data['long'],
                    lat=user_data['lat'], onlineState=user_data['onlineState'],
                    profilePicture=user_data['profilePicture'])
        user.save()
        request.session['user'] = user.id

    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)


@api_view(['POST'])
def forgot_password(request):
    data = json.loads(request.body)
    user = User.objects.get(username=data['username']) or User.objects.get(mail=data['mail'])
    if data['mail'] == user.mail:
        status = 'success'
        message = 'new password will be send'
        s = "abcdefghijklmnopqrstuvwxyz01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ!?."
        p = "".join(random.sample(s, 8))
        new_password = p
        user.password = new_password
        user.save()
    else:
        status = 'error'
        message = 'there is no user with this email'
        new_password = -1

    json_data = {"status": status, "message": message, "password": new_password}
    return JsonResponse(json_data)


@api_view(['GET'])
def sign_out(request):
    if "user" in request.session:
        del request.session['user']
        status = 'success'
        message = 'you signed out'
    else:
        status = 'error'
        message = 'there is no user in the session'

    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)


@api_view(['POST'])
def confirm_match(request):
    user_data = json.loads(request.body)
    match = Match.objects.get(id=user_data['match_id'])
    user = User.objects.get(id=match.user_id1.id) or User.object.get(id=match.user_id2.id)
    if "user" in request.session:
        if request.session['user'] == user.id:
            if match.state == 'pending':
                match.state = 'confirmed'
                match.save()
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
    user_data = json.loads(request.body)
    match = Match.objects.get(id=user_data['match_id'])
    user = User.objects.get(id=match.user_id1.id) or User.object.get(id=match.user_id2.id)
    if "user" in request.session:
        if request.session['user'] == user.id:
            if match.state == 'pending':
                match.state = 'rejected'
                match.save()
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


@api_view(['GET'])
def see_other_user_profile(request):
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


# confirm ve reject matchde serializible eklemeliyiz
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
            for match in matchlistRows:
                matchlistIndex.append({"matchlist_info": model_to_dict(match),
                                       "book_info": model_to_dict(match.book_id)})
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
            for suggest in suggests:
                suggest_list.append({
                    "suggest_info": model_to_dict(suggest),
                    "book_info": model_to_dict(suggest.book_id)
                })
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

    for trade in tradelist:
        menu_index.append({
            "trade_info": model_to_dict(trade),
            "book_info": model_to_dict(trade.givingBook_id),
            "user_info": model_to_dict(trade.user_id)
        })

    json_data = {"status": status, "message": message, "mainMenuIndex": menu_index}
    return JsonResponse(json_data, safe=False)


@api_view(['GET'])
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

    for trade in tradelist:
        search_index.append({
            "trade_info": model_to_dict(trade),
            "book_info": model_to_dict(trade.givingBook_id),
            "user_info": model_to_dict(trade.user_id)
        })

    json_data = {"status": status, "message": message, "searchIndex": search_index}
    return JsonResponse(json_data, safe=False)
