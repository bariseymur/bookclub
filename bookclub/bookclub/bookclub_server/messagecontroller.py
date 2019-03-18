from django.forms import model_to_dict
from rest_framework.decorators import api_view
from rest_framework.utils import json
from .models import User, Chat, Message
from django.http import JsonResponse


@api_view(['GET'])
def index(request):
    user_data = json.loads(request.body)
    if Message.objects.filter(id=user_data['id']).exists():
        messages = Message.objects.get(id=user_data['id'])
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

    json_data = {"status": status, "message": message, "message_info": message_info}
    return JsonResponse(json_data)


@api_view(['GET'])
def delete(request):
    user_data = json.loads(request.body)
    if Message.objects.filter(id=user_data['id']).exists():
        Message.objects.filter(id=user_data['id']).delete()
        status = 'success'
        message = 'chat data deleted successfully'
    else:
        status = 'error'
        message = 'no chat for this user'

    json_data = {"status": status,
                 "message": message
                 }
    return JsonResponse(json_data)

