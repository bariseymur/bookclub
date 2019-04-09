package com.bookclub.app.bookclub;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookclub.app.bookclub.Model.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChatActivity extends AppCompatActivity {

    final int CURRENT_USER_ID = 1;

    private RecyclerView recyclerView;
    private MessageListAdapter messageListAdapter;
    private ArrayList<Message> messages;
    private int chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        populateMessages();
        recyclerView = findViewById(R.id.reyclerview_message_list);
        messageListAdapter = new MessageListAdapter(this, messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageListAdapter);
    }

    private void populateMessages(){

        messages = new ArrayList<>();
        messages.add(new Message("wassup", "Deniz Şen", 1, new Date(43242345), false));
        messages.add(new Message("Haylo", "Mehmet", 2, new Date(8537384), true));

        messages.add(new Message("wassup", "Deniz Şen", 1, new Date(432445), false));
        messages.add(new Message("Haylo", "Mehmet", 2, new Date(85374384), true));

        messages.add(new Message("Haylo", "Mehmet", 2, new Date(85884), true));
        messages.add(new Message("Haylo", "Mehmet", 2, new Date(8537384), true));

        messages.add(new Message("wassup", "Deniz Şen", 1, new Date(432445), false));
        messages.add(new Message("Haylo", "Mehmet", 2, new Date(85374384), true));

        messages.add(new Message("wassup", "Deniz Şen", 1, new Date(42345), false));
        messages.add(new Message("Haylo", "Mehmet", 2, new Date(85884), true));

        messages.add(new Message("wassup", "Deniz Şen", 1, new Date(43242345), false));
        messages.add(new Message("Haylo", "Mehmet", 2, new Date(8537384), true));

        messages.add(new Message("wassup", "Deniz Şen", 1, new Date(432445), false));
        messages.add(new Message("Haylo", "Mehmet", 2, new Date(85374384), true));

        messages.add(new Message("wassup", "Deniz Şen", 1, new Date(42345), false));
        messages.add(new Message("Haylo", "Mehmet", 2, new Date(85884), true));

        messages.add(new Message("wassup", "Deniz Şen", 1, new Date(43242345), false));
        messages.add(new Message("Haylo", "Mehmet", 2, new Date(8537384), true));

        messages.add(new Message("wassup", "Deniz Şen", 1, new Date(432445), false));
        messages.add(new Message("Haylo", "Mehmet", 2, new Date(85374384), true));

        messages.add(new Message("wassup", "Deniz Şen", 1, new Date(42345), false));
        messages.add(new Message("Haylo", "Mehmet", 2, new Date(85884), true));


    }

    public class MessageListAdapter extends RecyclerView.Adapter{
        private static final int VIEW_TYPE_MESSAGE_SENT = 1;
        private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

        Context context;
        ArrayList<Message> messages;
        public MessageListAdapter(Context context, ArrayList<Message> messages){

            Log.d("pizza", "MessageListAdapter Constructor");
            this.context = context;
            this.messages = messages;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            Log.d("pizza", "onCreateViewHolder");
            if (viewType == VIEW_TYPE_MESSAGE_SENT) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent, false);
                return new SentMessageHolder(view);
            } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received, parent, false);
                return new ReceivedMessageHolder(view);
            }

            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            Message message = messages.get(i);
            Log.d("pizza", "onBindViewHolder");
            switch (viewHolder.getItemViewType()) {
                case VIEW_TYPE_MESSAGE_SENT:
                    ((SentMessageHolder) viewHolder).bind(message);
                    break;
                case VIEW_TYPE_MESSAGE_RECEIVED:
                    ((ReceivedMessageHolder) viewHolder).bind(message);
            }
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public int getItemViewType(int position) {
            Message message = messages.get(position);
            Log.d("pizza", "getItemViewType");
            if (message.getUserID()==1) {
                // If the current user is the sender of the message
                return VIEW_TYPE_MESSAGE_SENT;
            } else {
                // If some other user sent the message
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }

    }


    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            Log.d("pizza", "receviedMessageHolder");
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(Message message) {
            Log.d("pizza", "bind received");
            messageText.setText(message.getText());

            // Format the stored timestamp into a readable String using method.

            Date date = message.getDate();
            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
            String strDate = dateFormat.format(date);

            timeText.setText(strDate);
            nameText.setText(message.getName());

            // Insert the profile image from the URL into the ImageView.
            // Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);

        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);
            Log.d("pizza", "SentMessageHolder");
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(Message message) {
            Log.d("pizza", "bind sent");
            messageText.setText(message.getText());

            // Format the stored timestamp into a readable String using method.

            Date date = message.getDate();
            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
            String strDate = dateFormat.format(date);

            timeText.setText(strDate);


        }
    }


}
