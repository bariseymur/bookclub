@api_view(['POST'])
def action_on_match(request):
    # check if there is a user in the session
    if "user" in request.session:
        # load user data: {"match_id":1, "state":'confirmed'}
        user_data = json.loads(request.body)

        # find that match row in the match list
        match = Match.objects.get(id=user_data['match_id'])

        # if there is such match
        if match.exists():
            # check if the user in the session has a previlege for proceeding and action
            if match.user_id1.id == request.session['user'] or match.user_id2.id == request.session['user']:
                # other user's history action
                history_row = History.objects.get(Q(user_id=match.user_id2.id) & Q(match_id=match.id)) or History.objects.get(Q(user_id=match.user_id1.id) & Q(match_id=match.id))
                # if there was an action of other user and he/she confirmed the match
                if history_row.exists() and history_row.state == 'confirmed':
                    # if the user in the session also confirmed the match
                    if user_data['state'] == 'confirmed':
                        match.state = 'confirmed'
                        match.save()
                        date = datetime.datetime.now().strftime("%Y-%m-%d")
                        new_history_row = History(id=None, user_id=User.objects.get(id=request.session['user']), match_id=match, state='confirmed', dateOfAction=date)
                        new_history_row.save()
                        new_chat = Chat(id=None, state_1=None, state_2=None, user_id1=match.user_id1, user_id2=match.user_id2)
                        new_chat.save()
                        status = 'success'
                        message = 'the match was confirmed'
                    # if the user in the session rejected the match
                    elif user_data['state'] == 'rejected':
                        match.state = 'rejected'
                        match.save()
                        date = datetime.datetime.now().strftime("%Y-%m-%d")
                        new_history_row = History(id=None, user_id=User.objects.get(id=request.session['user']), match_id=match, state='rejected', dateOfAction=date)
                        new_history_row.save()
                        status = 'success'
                        message = 'the match was rejected'
                # if there was an action of other user and he/she rejected the match
                elif history_row.exists() and history_row.state == 'rejected':
                    # there is no point in analyzing the user's data anymore because match is rejected
                    match.state = 'rejected'
                    match.save()
                    status = 'error'
                    message = 'the match was already rejected by the other user, you cannot proceed further'
                # if there was no action of other user 
                else:
                    # this user confirms the match
                    if user_data['state'] == 'confirmed':
                        date = datetime.datetime.now().strftime("%Y-%m-%d")
                        new_history_row = History(id=None, user_id=User.objects.get(id=request.session['user']), match_id=match, state='confirmed', dateOfAction=date)
                        new_history_row.save()
                        status = 'success'
                        message = 'the match was confirmed'
                    # if the user in the session rejected the match
                    elif user_data['state'] == 'rejected':
                        match.state = 'rejected'
                        match.save()
                        date = datetime.datetime.now().strftime("%Y-%m-%d")
                        new_history_row = History(id=None, user_id=User.objects.get(id=request.session['user']), match_id=match, state='rejected', dateOfAction=date)
                        new_history_row.save()
                        status = 'success'
                        message = 'the match was rejected'
            else:
                status = 'error'
                message = 'you do not have a previlege to do this action'
        else:
            status = 'error'
            message = 'this match does not exist'
    else:
        status = 'error'
        message = 'you should login first'

    json_data = {"status": status, "message": message}
    return JsonResponse(json_data)


@api_view(['POST'])
def reject_match(request):
    # add to the history - not complete yet
    # this method rejects match
    user_data = json.loads(request.body)
    match = Match.objects.get(id=user_data['match_id'])
    user = User.objects.get(id=match.user_id1.id) or User.object.get(id=match.user_id2.id)
    if "user" in request.session:
        if request.session['user'] == user.id:
            if match.state == 'pending':
                match.state = 'rejected'
                match.save()
                date = datetime.datetime.now().strftime("%Y-%m-%d")
                new_history_row = History(id=None, user_id=User.objects.get(id=request.session['user']), matchConfirmation_id=None, matchRejection_id=match, dateOfAction=date)
                new_history_row.save()
                status = 'success'
                message = 'the match was rejected succesfully'
            else:
                status = 'error'
                message = 'could not reject the match'
        else:
            status = 'error'
            message = 'this action cannot be done'
    else:
        status = 'error'
        message = 'there is no user in the session'

    json_data = {"status": status, "message": message, "match_info": model_to_dict(match)}
    return JsonResponse(json_data)