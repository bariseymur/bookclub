from django.contrib import admin
from django.urls import path, include

from . import tradelistcontroller
from . import usercontroller
urlpatterns = [
    # usercontroller
    path('login/', usercontroller.login),
    path('signup/', usercontroller.signup),
    path('forgotPassword/', usercontroller.forgot_password),
    path('signout/', usercontroller.sign_out),
    path('getSession/', usercontroller.get_session),
    path('confirmMatch/', usercontroller.confirm_match),
    path('rejectMatch/', usercontroller.reject_match),
    path('seeOtherUserProfile/', usercontroller.see_other_user_profile),
    path('matchListIndex/', usercontroller.match_list_index),
    path('suggestionListIndex/', usercontroller.suggestion_list_index),
    path('mainMenuIndex/', usercontroller.main_menu_index),
    # tradelistcontroller
    path('tradelist/index/', tradelistcontroller.index),
    path('tradelist/delete/', tradelistcontroller.delete),
    path('tradelist/add/', tradelistcontroller.add),
    path('tradelist/update/', tradelistcontroller.update)
]