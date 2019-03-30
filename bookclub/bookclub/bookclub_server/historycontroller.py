from django.forms import model_to_dict
from rest_framework.decorators import api_view
from rest_framework.utils import json
from .models import User, History
from django.http import JsonResponse


@api_view(['GET'])
def index(request):
    if "user" in request.session:
        history = History.objects.filter(user_id=request.session['user'])
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
        message = 'there is no user in the session'
        history_list = None
    json_data = {"status": status,
                 "message": message,
                 "history": history_list,
                 }
    return JsonResponse(json_data)


@api_view(['DELETE'])
def clear(request):
    if "user" in request.session:
        history = History.objects.filter(user_id=request.session['user'])
        if history.exists():
            for item in history:
                item.delete()
            status = 'success'
            message = 'history data deleted successfully'
        else:
            status = 'error'
            message = 'no history for this user'
    else:
        status = 'error'
        message = 'there is no user in the session'
    json_data = {"status": status,
                 "message": message
                 }
    return JsonResponse(json_data)
