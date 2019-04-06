# read mmethdu -> is seeni updatelicez
# send -> messagei message hem chat
# message_list->

from django.forms import model_to_dict
from rest_framework.decorators import api_view
from rest_framework.utils import json
from .models import User, Chat, Message
from django.http import JsonResponse
import datetime
from django.db.models import Q


@api_view(['POST'])
def send(request):
    user_data = json.loads(request.body)  # {"messageText": "selam", "chat_id": "3"}
    if "user" in request.session:
        if Chat.objects.filter(Q(id=user_data['chat_id'])).exists():
            row = Chat.objects.get(id=user_data['chat_id'])
            if row.user_id_1_id == request.session['user'] or row.user_id_2_id == request.session['user']:
                message = Message(messageText=user_data['messageText'], messageDate=datetime.datetime.now(), isSeen=0,
                                  chat_id=Chat.objects.get(id=user_data['chat_id']), sender_id=User.objects.get(id=request.session['user']))
                message.save()
                status = 'success'
                message = 'Message successfully sent'
            else:
                status = 'error'
                message = 'You cannot send messages in this chat'
        else:
            status = 'error'
            message = 'There is no such chat'
    else:
        status = 'error'
        message = 'You should login first'
    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)


@api_view(['POST'])
def message_list(request):
    user_data = json.loads(request.body)  # {"user_id":"1"}
    if "user" in request.session:
        if Message.objects.filter(id=user_data['message_id']).exists():
            messages = Message.objects.get(id=user_data['message_id'])
            message_info = []
            message_info.append({
                "date_info": messages.messageDate,
                "isSeen_info": messages.isSeen,
                "text_info": messages.messageText,
            })
            status = 'success'
            message = 'message data sent successfully'
        else:
            status = 'error'
            message = 'there is no message data with this id'
            message_info = None
    else:
        status = "error"
        message = "you should login first"
        message_info = None

    json_data = {"status": status, "message": message, "message_info": message_info}
    return JsonResponse(json_data)


@api_view(['DELETE'])
def delete(request):
    user_data = json.loads(request.body)  # {"id":"1"}
    if "user" in request.session:
        if Message.objects.filter(id=user_data['message_id']).exists():
            Message.objects.filter(id=user_data['message_id']).delete()
            status = 'success'
            message = 'chat data deleted successfully'
        else:
            status = 'error'
            message = 'no chat for this user'
    else:
        status = "error"
        message = "you should login first"

    json_data = {"status": status,
                 "message": message
                 }
    return JsonResponse(json_data)
