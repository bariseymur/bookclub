package com.bookclub.app.bookclub;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.bookclub.app.bookclub.bookclubapi.User;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChatListActivity extends AppCompatActivity {

    ArrayList<ChatListContent> chatListContents;
    ListView listView;
    ArrayAdapter<ChatListContent> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        populateChatList();

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("chatListActivity", "adasdad");
            Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
            intent.putExtra("ChatID", chatListContents.get(position).getChatID());
            startActivity(intent);
        });

        adapter = new ChatListAdapter(this );
        listView.setAdapter(adapter);
    }

    private void populateChatList(){
        chatListContents = new ArrayList<>();

        chatListContents.add(new ChatListContent("Muhittin Topalak", "excalibur17", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 473));
        chatListContents.add(new ChatListContent("Muhittin Topalak", "excalibur17", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 473));
        chatListContents.add(new ChatListContent("Muhittin Topalak", "excalibur17", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 473));
        chatListContents.add(new ChatListContent("Muhittin Topalak", "excalibur17", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 473));
        chatListContents.add(new ChatListContent("Muhittin Topalak", "excalibur17", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 473));
        chatListContents.add(new ChatListContent("Muhittin Topalak", "excalibur17", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 473));
        chatListContents.add(new ChatListContent("Muhittin Topalak", "excalibur17", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 473));
        chatListContents.add(new ChatListContent("Muhittin Topalak", "excalibur17", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 473));
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

            vh.name.setText(chatListContent.getName());
            vh.userName.setText(chatListContent.getUserName());
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
                    intent.putExtra("ChatID", chatListContent.getChatID());
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

        String userName, name;
        int chatID;
        Bitmap profilePic;


        public ChatListContent(User user, int chatID) {
            this.userName= user.getUsername();
            this.chatID = chatID;
            this.name = user.getName();


            try {

                ImageLoader imageLoader = new ImageLoader( user.getProfilePictureUrl());
                profilePic = imageLoader.execute().get();

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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
    }

}
