from django.db import models


# Create your models here.

# User table
class User(models.Model):
    name = models.CharField(max_length=100)
    # surname = models.CharField(max_length=100)
    country = models.CharField(max_length=100)
    mail = models.CharField(max_length=250)
    phoneNumber = models.CharField(max_length=100, blank=True, null=True)
    dateOfBirth = models.DateField(blank=True, null=True)
    username = models.CharField(max_length=50)
    password = models.CharField(max_length=50)
    long = models.DecimalField(max_digits=8, decimal_places=3)  # location longitude
    lat = models.DecimalField(max_digits=8, decimal_places=3)  # location latitude
    onlineState = models.BooleanField(default=True)
    profilePicture = models.CharField(default="default.jpg", max_length=250)


# AccountSettings Table
class AccountSettings(models.Model):
    user_id = models.ForeignKey(User, on_delete=models.CASCADE, to_field='id')
    userAvailability = models.BooleanField(default=True)
    userMessagable = models.BooleanField(default=True)
    lastSeen = models.BooleanField(default=True)

# Book Table
class Book(models.Model):
    title = models.CharField(max_length=250)
    authorName = models.CharField(max_length=250)
    isbn = models.CharField(max_length=100)
    publisher = models.CharField(max_length=250)
    # originalPrice = models.DecimalField(max_digits=8, decimal_places=2, default=30)
    publishDate = models.CharField(max_length=250)
    bookPhoto = models.CharField(default="defaultBook.jpg", max_length=250)


# Match Table
class Match(models.Model):
    user_id1 = models.ForeignKey(User, on_delete=models.CASCADE, related_name='user_id1')
    user_id2 = models.ForeignKey(User, on_delete=models.CASCADE, related_name='user_id2')
    matchInformation = models.CharField(max_length=250)
    book_id = models.ForeignKey(Book, on_delete=models.CASCADE)
    state = models.CharField(max_length=250, default="nothing")
    matchDate = models.DateField(blank=False)


# Suggestion Table
class Suggestion(models.Model):
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    suggestionInformation = models.CharField(max_length=250)
    book_id = models.ForeignKey(Book, on_delete=models.CASCADE)
    suggestionDate = models.DateField(blank=False)
    recommendation_score = models.IntegerField(default=0)


# Chat Table
class Chat(models.Model):
    user_id_1 = models.ForeignKey(User, on_delete=models.CASCADE, related_name='user_id_1')
    user_id_2 = models.ForeignKey(User, on_delete=models.CASCADE, related_name='user_id_2')
    state_1 = models.CharField(max_length=250, default="nothing") # iksi de confirmed olmayinca chat kapanmiyor ve puanlanmiyor
    state_2 = models.CharField(max_length=250, default="nothing")

# Message Table
class Message(models.Model):
    messageText = models.CharField(max_length=250)
    messageDate = models.DateField(blank=False)
    isSeen = models.BooleanField(default=False)
    sender_id = models.ForeignKey(User, on_delete=models.CASCADE, related_name='sender_id')
    chat_id = models.ForeignKey(Chat, on_delete=models.CASCADE, related_name='chat_id')


# TradeList Table
class TradeList(models.Model):
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    givingBook_id = models.ForeignKey(Book, on_delete=models.CASCADE, related_name='givingBook_id')


# WishList Table
class WishList(models.Model):
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    book_id = models.ForeignKey(Book, on_delete=models.CASCADE)
    order = models.IntegerField(default=1)


# History Table
class History(models.Model):
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    match_id = models.ForeignKey(Match, on_delete=models.CASCADE, null=True, related_name='match_id')
    state = models.CharField(max_length=250, default="nothing") # should be confirmed both
    dateOfAction = models.DateField(blank=False)

# BookRating Table
class BookRating(models.Model):
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    book_isbn = models.ForeignKey(Book, on_delete=models.CASCADE)
    raiting = models.IntegerField(default=0)

# UserRating Table
class UserRating(models.Model):
    rating_user = models.ForeignKey(User, on_delete=models.CASCADE, related_name='rating_user')
    rated_user = models.ForeignKey(User, on_delete=models.CASCADE, related_name='rated_user')
    rating = models.IntegerField(default=0)