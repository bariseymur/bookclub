package com.bookclub.app.bookclub;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<HistoryListContent> historyListContents;
    HistoryListAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        populateHistoryList();

        listView = findViewById(R.id.listView);
        adapter = new HistoryListAdapter(this);
        listView.setAdapter(adapter);
        final SwipeToDismissTouchListener<ListViewAdapter> touchListener = new SwipeToDismissTouchListener<>(
            new ListViewAdapter(listView),
            new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                @Override
                public boolean canDismiss(int position) {
                    return true;
                }

                @Override
                public void onDismiss(ListViewAdapter view, int position) {
                    historyListContents.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
        listView.setOnTouchListener(touchListener);
    }


    private void populateHistoryList() {
        historyListContents = new ArrayList<>();
        historyListContents.add(new HistoryListContent("excalibur17", "15 Mart'a 5 Kala", "Celil Gürkan", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", true, new Date(56345)));
        historyListContents.add(new HistoryListContent("excalibur17", "15 Mart'a 5 Kala", "Celil Gürkan", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", false, new Date(56345)));
        historyListContents.add(new HistoryListContent("excalibur17", "15 Mart'a 5 Kala", "Celil Gürkan", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", true, new Date(56345)));
        historyListContents.add(new HistoryListContent("excalibur17", "15 Mart'a 5 Kala", "Celil Gürkan", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", true, new Date(56345)));
        historyListContents.add(new HistoryListContent("excalibur17", "15 Mart'a 5 Kala", "Celil Gürkan", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", false, new Date(56345)));
        historyListContents.add(new HistoryListContent("excalibur17", "15 Mart'a 5 Kala", "Celil Gürkan", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", false, new Date(56345)));

    }


    private static class ViewHolder {
        TextView username, author, bookTitle, date;
        ImageView state;
        ImageButton bookImage;

    }


    public class HistoryListAdapter extends ArrayAdapter<HistoryListContent> {

        Context context;

        public HistoryListAdapter(Context context) {
            super(context, R.layout.history_list_item, historyListContents);
            this.context = context;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final HistoryListContent historyListContent= getItem(position);
            final View result;
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.history_list_item, parent, false);

                //Text Views
                viewHolder.username = convertView.findViewById(R.id.userNameText);
                viewHolder.bookTitle= convertView.findViewById(R.id.bookTitle);
                viewHolder.author= convertView.findViewById(R.id.authorName);
                viewHolder.date = convertView.findViewById(R.id.dateText);
                //Image View
                viewHolder.state = convertView.findViewById(R.id.stateImage);
                //Image Button
                viewHolder.bookImage = convertView.findViewById(R.id.bookImage);
                result = convertView;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result = convertView;
            }

            viewHolder.username.setText(historyListContent.getUserName());
            viewHolder.bookTitle.setText(historyListContent.getBookTitle());
            viewHolder.author.setText(historyListContent.getAuthorName());
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            viewHolder.date.setText(sdf.format(historyListContent.getDate()));
            viewHolder.bookImage.setImageBitmap(Bitmap.createScaledBitmap(historyListContent.getBookImage(), 300, 300, false));

            if (historyListContent.isAccepted()){
                viewHolder.state.setImageResource(R.drawable.thumb_up_green);
            }
            else{
                viewHolder.state.setImageResource(R.drawable.thumb_down);
            }

            return convertView;
        }

        @Override
        public int getCount() {
            return historyListContents.size();
        }
    }


    public class HistoryListContent {

        Bitmap bookImage;
        String userName, bookTitle, authorName;
        boolean accepted;
        Date date;

        public HistoryListContent(String userName, String bookTitle, String authorName, String bookImageURL, boolean accepted, Date date) {
            this.userName = userName;
            this.bookTitle = bookTitle;
            this.authorName = authorName;
            this.accepted = accepted;
            this.date = date;


            try {

                ImageLoader imageLoader = new ImageLoader(HistoryActivity.this, bookImageURL);
                bookImage = imageLoader.execute().get();

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        public Bitmap getBookImage() {
            return bookImage;
        }

        public void setBookImage(Bitmap bookImage) {
            this.bookImage = bookImage;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getBookTitle() {
            return bookTitle;
        }

        public void setBookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public boolean isAccepted() {
            return accepted;
        }

        public void setAccepted(boolean accepted) {
            this.accepted = accepted;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }


}