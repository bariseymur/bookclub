from django.forms import model_to_dict
from rest_framework.decorators import api_view
from rest_framework.utils import json
from .models import User, Chat, Message
from django.http import JsonResponse

@api_view(['GET'])
def index(request):
    # does not need any json loading
    if "user" in request.session:
        chat = Chat.objects.filter(receiver_id_id=request.session['user']) |Chat.objects.filter(sender_id_id=request.session['user'])
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


@api_view(['GET'])
def delete(request):
    user_data = json.loads(request.body)
    if User.objects.filter(id=user_data['id']).exists():
        user = User.objects.get(id=user_data['id'])
    if "user" in request.session:
        if request.session['user'] == user.id:
            chat = Chat.objects.filter(receiver_id_id=request.session['user']) |Chat.objects.filter(sender_id_id=request.session['user'])
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
            message = 'this action cannot be done'
    else:
        status = 'error'
        message = 'there is no user in the session'
    json_data = {"status": status,
                 "message": message
                 }
    return JsonResponse(json_data)
