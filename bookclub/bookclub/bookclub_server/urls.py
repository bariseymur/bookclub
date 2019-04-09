from django.contrib import admin
from django.urls import path, include
from . import chatservice, usercontroller, wishlistcontroller, accountsettingscontroller, tradelistcontroller, historycontroller, messagecontroller, chatcontroller
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
    path('searchIndex/', usercontroller.search_index),
    path('getUserProfile/', usercontroller.get_user_profile),

    # wishlistcontroller
    path('wishlist/index/', wishlistcontroller.index),
    path('wishlist/delete/', wishlistcontroller.delete),
    path('wishlist/add/', wishlistcontroller.add),
    # wishlist/update olacak mi?
    # wishlist/drag olacak mi?

    # accountsettingscontroller
    path('accountSettings/index/', accountsettingscontroller.index),
    path('accountSettings/reset/', accountsettingscontroller.reset),
    path('accountSettings/changeAvailability/', accountsettingscontroller.change_user_availability),
    path('accountSettings/changeMessagable/', accountsettingscontroller.change_user_messagable),
    path('accountSettings/lastSeen/', accountsettingscontroller.change_last_seen_state),
    path('accountSettings/changePicture/', accountsettingscontroller.change_profile_picture),
    path('accountSettings/changePhoneNumber/', accountsettingscontroller.change_phone_number),
    path('accountSettings/changeMail/', accountsettingscontroller.change_email),
    path('accountSettings/changeLocation/', accountsettingscontroller.change_location),
    path('accountSettings/onlineState/', accountsettingscontroller.change_online_state),
    path('accountSettings/changePassword/', accountsettingscontroller.change_password),

    # tradelistcontroller
    path('tradelist/index/', tradelistcontroller.index),
    path('tradelist/delete/', tradelistcontroller.delete),
    path('tradelist/add/', tradelistcontroller.add),
    path('tradelist/update/', tradelistcontroller.update),

    # historycontroller
    path('history/index/', historycontroller.index),
    path('history/clear/', historycontroller.clear),

    # chatcontroller
    path('chat/index/', chatcontroller.index),
    path('chat/delete/', chatcontroller.delete),

    # messagecontroller
    path('message/index/', messagecontroller.index),
    path('message/delete/', messagecontroller.delete),

    # chatservice
    path('chatService/messageList/', chatservice.message_list),
    path('chatService/read/', chatservice.read),
    path('chatService/send/', chatservice.send),
]
