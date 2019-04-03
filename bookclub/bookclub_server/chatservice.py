# read mmethdu -> is seeni updatelicez
# send -> messagei message hem chat
# message_list->

from django.forms import model_to_dict
from rest_framework.decorators import api_view
from rest_framework.utils import json
from .models import User, Chat, Message
from django.http import JsonResponse
from django.db.models import Q


@api_view(['POST'])
def message_list(request):
    user_data = json.loads(request.body) # {"user_id":"1"}
    if "user" in request.session:
        if Message.objects.filter(id=user_data['message_id']).exists():
            messages = Message.objects.get(id=user_data['message_id'])
            message_info= []
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
    user_data = json.loads(request.body) # {"id":"1"}
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