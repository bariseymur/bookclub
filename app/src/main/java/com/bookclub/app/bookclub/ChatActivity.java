package com.bookclub.app.bookclub;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bookclub.app.bookclub.Model.Message;
import com.bookclub.app.bookclub.bookclubapi.BookClubAPI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class ChatActivity extends AppCompatActivity {

    final int CURRENT_USER_ID = 1;

    private RecyclerView recyclerView;
    private MessageListAdapter messageListAdapter;
    private ArrayList<Message> messages;
    private int chat;
    private Button sendButton, acceptButton, rejectButton;
    private EditText editText;
    TimerTask doAsynchronousTask;
    Handler handler;
    Runnable handlerTask;
    Dialog dialog;


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
        editText = findViewById(R.id.edittext_chatbox);
        sendButton = findViewById(R.id.button_chatbox_send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText() != null && !editText.getText().equals("")){
                    Log.d("send pressed", editText.getText().toString());
                    messages.add(new Message(editText.getText().toString(), "Deniz Şen", 1, new Date(42345), false));
                    messageListAdapter.notifyDataSetChanged();
                    editText.setText("");
                    recyclerView.scrollToPosition(messages.size()-1);

                    new SendMessageTask().execute();

                }
            }
        });

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.rating_popup);
        TextView dialogUserName = dialog.findViewById(R.id.username);
        RatingBar ratingBar  = dialog.findViewById(R.id.ratingBar);
        ImageButton dialogAccept = dialog.findViewById(R.id.confirmButton);
        ImageButton dialogCancel = dialog.findViewById(R.id.cancelButton);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        acceptButton = findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*final AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setMessage("You are about to confirm this trade. Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                //confirmation process


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
                */
                dialog.show();
            }
        });

        rejectButton = findViewById(R.id.rejectButton);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                builder.setMessage("You are about to reject this trade. Are you sure?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                //rejection process

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        });

        handler = new Handler();
        handlerTask = new Runnable() {
            @Override
            public void run() {
                new ReceiveNewMessages().execute();
                handler.postDelayed(this, 2000);
            }
        };
        handlerTask.run();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // doAsynchronousTask.cancel();
        handler.removeCallbacks(handlerTask);
    }

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            ReceiveNewMessages performBackgroundTask = new ReceiveNewMessages();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 5000); //execute in every 5000 ms

    }


    public class ReceiveNewMessages extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            messageListAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("Hello");
            return null;
        }
    }

    public class SendMessageTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //update the chat list
            //messageListAdapter.notifyDataSetChanged();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            //send message
            BookClubAPI api = new BookClubAPI();
            // api.sendMessage(userID, editText.getText());

            return null;
        }
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
        messages.add(new Message("Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo Haylo aylo Haylo ", "Mehmet", 2, new Date(85374384), true));

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

            this.context = context;
            this.messages = messages;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
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
            if (message.getUserID()==1) {
                // If the current user is the sender of the message
                return VIEW_TYPE_MESSAGE_SENT;
            } else {
                // If some other user sent the message
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }

    }

    public class ChatCreatorTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            populateMessages();

            return null;
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
