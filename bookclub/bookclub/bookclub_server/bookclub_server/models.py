from django.db import models


# Create your models here.

# User table
class User(models.Model):
    name = models.CharField(max_length=100)
    surname = models.CharField(max_length=100)
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


# Message Table
class Message(models.Model):
    messageText = models.CharField(max_length=250)
    messageDate = models.DateField(blank=False)
    isSeen = models.BooleanField(default=False)


# Book Table
class Book(models.Model):
    title = models.CharField(max_length=250)
    authorName = models.CharField(max_length=250)
    isbn = models.CharField(max_length=100)
    publisher = models.CharField(max_length=250)
    originalPrice = models.DecimalField(max_digits=8, decimal_places=2)
    publishDate = models.DateField(blank=False)
    edition = models.CharField(max_length=100)
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


# Chat Table
class Chat(models.Model):
    receiver_id = models.ForeignKey(User, on_delete=models.CASCADE, related_name='receiver_id')
    sender_id = models.ForeignKey(User, on_delete=models.CASCADE, related_name='sender_id')
    message_id = models.ForeignKey(Message, on_delete=models.CASCADE)


# TradeList Table
class TradeList(models.Model):
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    wantedBook_id = models.ForeignKey(Book, on_delete=models.CASCADE, related_name='wantedBook_id')
    givingBook_id = models.ForeignKey(Book, on_delete=models.CASCADE, related_name='givingBook_id')


# WishList Table
class WishList(models.Model):
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    book_id = models.ForeignKey(Book, on_delete=models.CASCADE)


# History Table
class History(models.Model):
    user_id = models.ForeignKey(User, on_delete=models.CASCADE)
    matchConfirmation_id = models.ForeignKey(Match, on_delete=models.CASCADE, null=True, related_name='matchConfirmation_id')
    matchRejection_id = models.ForeignKey(Match, on_delete=models.CASCADE, null=True, related_name='matchRejection_id')
    dateOfAction = models.DateField(blank=False)
