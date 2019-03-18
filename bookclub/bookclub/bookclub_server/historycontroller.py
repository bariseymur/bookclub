from django.forms import model_to_dict
from rest_framework.decorators import api_view
from rest_framework.utils import json
from .models import User, History
from django.http import JsonResponse



@api_view(['GET'])
def index(request):
    user_data = json.loads(request.body)
    if User.objects.filter(id=user_data['id']).exists():
        user = User.objects.get(id=user_data['id'])
    if "user" in request.session:
        if request.session['user'] == user.id:
            history = History.objects.filter(user_id_id=user.id)
            if history.exists():
                history_list = []
                for line in history:
                    history_list.append(model_to_dict(line))
                status = 'success'
                message = 'history data send successfully'
            else:
                status = 'error'
                message = 'no history for this user'
                history_list = None
        else:
            status = 'error'
            message = 'this action cannot be done'
            history_list = None
    else:
        status = 'error'
        message = 'there is no user in the session'
        history_list = None
    json_data = {"status": status,
                 "message": message,
                 "history": history_list,
                 }
    return JsonResponse(json_data)


@api_view(['GET'])
def clear(request):
    user_data = json.loads(request.body)
    if User.objects.filter(id=user_data['id']).exists():
        user = User.objects.get(id=user_data['id'])
    if "user" in request.session:
        if request.session['user'] == user.id:
            history = History.objects.filter(user_id_id=user.id)
            if history.exists():
                rows = History.objects.filter(user_id_id=user.id)
                for item in rows:
                    item.delete()
                status = 'success'
                message = 'history data deleted successfully'
            else:
                status = 'error'
                message = 'no history for this user'
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
