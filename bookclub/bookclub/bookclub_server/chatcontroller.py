from django.forms import model_to_dict
from rest_framework.decorators import api_view
from rest_framework.utils import json
from .models import User, Chat, Message
from django.http import JsonResponse
from django.db.models import Q


@api_view(['GET'])
def index(request):
    # does not need any json loading
    if "user" in request.session:
        chat = Chat.objects.filter(receiver_id=request.session['user']) \
               | Chat.objects.filter(sender_id=request.session['user'])
        if chat.exists():
            chat_list = []
            for line in chat:
                chat_list.append({
                    "chat_info": model_to_dict(line),
                    "message_info": model_to_dict(line.message_id),
                    "sender_info": model_to_dict(line.sender_id),
                    "receiver_info": model_to_dict(line.receiver_id)

                })
            status = 'success'
            message = 'chat data sent successfully'
        else:
            status = 'error'
            message = 'no chat data for this user'
            chat_list = None
    else:
        status = 'error'
        message = 'you should login first'
        chat_list = None

    json_data = {"status": status, "message": message, "chat_info": chat_list}
    return JsonResponse(json_data)


@api_view(['DELETE'])
def delete(request):
    user_data = json.loads(request.body)  # {"id":"1"} silmek istedigi userin chati
    if "user" in request.session:
        chat = (Chat.objects.filter(receiver_id=request.session['user']) & Chat.objects.filter(sender_id=user_data['id']) )\
                   | (Chat.objects.filter(sender_id=request.session['user']) & Chat.objects.filter(receiver_id=user_data['id']))
        if chat.exists():
            for item in chat:
                (Message.objects.filter(id=item.message_id_id)).delete()
                item.delete()
            status = 'success'
            message = 'chat data deleted successfully'
        else:
            status = 'error'
            message = 'no chat for this user'
    else:
        status = 'error'
        message = 'there is no user in the session'
    json_data = {"status": status,
                 "message": message
                 }
    return JsonResponse(json_data)