package com.bookclub.app.bookclub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bookclub.app.bookclub.bookclubapi.BookClubAPI;
import com.bookclub.app.bookclub.bookclubapi.User;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import dmax.dialog.SpotsDialog;

public class ChatListActivity extends AppCompatActivity {

    ArrayList<ChatListContent> chatListContents;
    ListView listView;
    ArrayAdapter<ChatListContent> adapter;
    AlertDialog alertDialog;
    BookClubAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        api = new BookClubAPI();
        alertDialog = new SpotsDialog(this);
        alertDialog.show();

        new CreateChatListTask().execute();

    }

    public class CreateChatListTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);



            listView = findViewById(R.id.listView);
            listView.setOnItemClickListener((parent, view, position, id) -> {
                Log.d("chatListActivity", "adasdad");
                Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
                intent.putExtra("ChatID", chatListContents.get(position).getChatId());
                startActivity(intent);
            });

            adapter = new ChatListAdapter(ChatListActivity.this);
            listView.setAdapter(adapter);

            alertDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            chatListContents = new ArrayList<>();

            User currentuser = (User)api.getSession().get(2);
            int userId = currentuser.getId();
            ArrayList<Object> chatlist = (ArrayList<Object>) api.chat_index().get(2);
            if (chatlist == null) return null;

            int chattedUserId = 0, chattedUserIndex = 0, currentUserIndex = 0;
            for (int i = 0; i < chatlist.size(); i++ ){
                ArrayList<Object> chat = (ArrayList<Object>) chatlist.get(i);
                if ((int)chat.get(1) == userId){
                    chattedUserId = (int) chat.get(2);
                    chattedUserIndex = 2;
                    currentUserIndex = 1;
                }
                else if ((int)chat.get(2) == userId){
                    chattedUserId = (int) chat.get(1);
                    chattedUserIndex = 1;
                    currentUserIndex = 2;
                }

                User currentUser = new User(3, "asd","asd", "asd", "asd", "asd", "asd", "asd", true, "1992-05-22", 38, 34);

                ChatListContent chatListContent = new ChatListContent(
                        (int) chat.get(0),
                        (int)chat.get(5),
                        (int)chat.get(6),
                        currentUser,
                        !chat.get(currentUserIndex + 2).equals("not_confirmed"),
                        !chat.get(chattedUserIndex + 2).equals("not_confirmed")
                        );

                chatListContents.add(chatListContent);

            }


            return null;
        }
    }


    private static class ViewHolder{
        TextView userName, name;
        ImageButton profilePicture;
        CardView cardView;
    }

    public class ChatListAdapter extends ArrayAdapter<ChatListContent>{

        Context context;

        public ChatListAdapter(@NonNull Context context) {
            super(context, R.layout.chat_list_item);

        }




        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ChatListContent chatListContent = chatListContents.get(position);

            Log.d("getView", "GetView at " + position);

            View result;
            ViewHolder vh;

            if (convertView == null){
                vh = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.chat_list_item, parent, false);

                vh.userName = convertView.findViewById(R.id.userNameText);
                vh.name = convertView.findViewById(R.id.nameText);
                vh.profilePicture = convertView.findViewById(R.id.bookImage);
                vh.cardView = convertView.findViewById(R.id.pad);

                result = convertView;
                convertView.setTag(vh);

            }
            else{
                vh = (ViewHolder)convertView.getTag();
                result = convertView;
            }

            vh.name.setText(chatListContent.getChattedUser().getName());
            vh.userName.setText(chatListContent.getChattedUser().getUsername());
            vh.profilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChatListActivity.this, ProfileActivity.class);
                    startActivity(intent);

                }
            });

            vh.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("chatListActivity", "adasdad");
                    Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
                    intent.putExtra("ChatID", chatListContent.getChatId());
                    startActivity(intent);
                }
            });

            return result;
        }

        @Override
        public int getCount() {
            return chatListContents.size();
        }
    }





    public class ChatListContent{

        int chatId;
        int matchID, suggestionID;
        User chattedUser;
        boolean currentUserState;
        boolean chattedUserState;

        public ChatListContent(int chatId, int matchID, int suggestionID, User chattedUser, boolean currentUserState, boolean chattedUserState) {
            this.chatId = chatId;
            this.matchID = matchID;
            this.chattedUser = chattedUser;
            this.currentUserState = currentUserState;
            this.chattedUserState = chattedUserState;
            this.suggestionID = suggestionID;
        }

        public int getChatId() {
            return chatId;
        }

        public void setChatId(int chatId) {
            this.chatId = chatId;
        }

        public int getMatchID() {
            return matchID;
        }

        public void setMatchID(int matchID) {
            this.matchID = matchID;
        }

        public User getChattedUser() {
            return chattedUser;
        }

        public void setChattedUser(User chattedUser) {
            this.chattedUser = chattedUser;
        }

        public boolean isCurrentUserState() {
            return currentUserState;
        }

        public void setCurrentUserState(boolean currentUserState) {
            this.currentUserState = currentUserState;
        }

        public boolean isChattedUserState() {
            return chattedUserState;
        }

        public void setChattedUserState(boolean chattedUserState) {
            this.chattedUserState = chattedUserState;
        }

        /* String userName, name;
        int chatID;
        Bitmap profilePic;


        public ChatListContent(User user, int chatID) {
            this.userName= user.getUsername();
            this.chatID = chatID;
            this.name = user.getName();


        }

        public ChatListContent(String userName, String name, String profilePicURL, int chatID){
            this.userName = userName;
            this.name = name;
            this.chatID = chatID;

            try {

                ImageLoader imageLoader = new ImageLoader( profilePicURL);
                profilePic = imageLoader.execute().get();

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getChatID() {
            return chatID;
        }

        public void setChatID(int chatID) {
            this.chatID = chatID;
        }
        */
    }



}
