package com.bookclub.app.bookclub;

import android.content.Context;

import android.graphics.Bitmap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bookclub.app.bookclub.bookclubapi.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class TradeBookActivity extends AppCompatActivity {


    ListView listView;
    ArrayList<RequestBookListItem> tradeBookListItems;
    SearchView searchBar;
    ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_book);
        searchBar = findViewById(R.id.searchBar);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchBookTask(searchBar.getQuery().toString()).execute();
            }
        });

    }



    public static class ViewHolder{
        TextView author, bookTitle;
        ImageButton bookImage, tradeButton;
        CardView cardView;
    }

    public class TradeBookListAdapter extends ArrayAdapter<RequestBookListItem>{

        Animation scaleUp;


        public TradeBookListAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Nullable
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final RequestBookListItem tradeBookListItem= getItem(position);
            ViewHolder viewHolder;

            final View result;
            Bitmap bitmap = null;
            if (convertView == null){
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.request_book_list_item, parent, false);
                viewHolder.cardView = convertView.findViewById(R.id.pad);
                viewHolder.author = convertView.findViewById(R.id.authorName);
                viewHolder.bookTitle = convertView.findViewById(R.id.bookTitle);
                viewHolder.bookImage = convertView.findViewById(R.id.bookImage);
                viewHolder.tradeButton = convertView.findViewById(R.id.wishButton);
                result = convertView;
                convertView.setTag(viewHolder);

            }
            else{
                viewHolder = (ViewHolder)convertView.getTag();
                result = convertView;
            }

            Picasso.get()
                    .load(tradeBookListItem.getBook().getBookPhotoUrl())
                    .resize(300, 400)
                    .error(R.drawable.book)
                    .placeholder(R.drawable.ic_get_app_black_24dp)
                    .into(viewHolder.bookImage);
            System.out.println(tradeBookListItem.getBook().getBookPhotoUrl());

            viewHolder.author.setText(tradeBookListItem.getBook().getAuthorName());
            viewHolder.bookTitle.setText(tradeBookListItem.getBook().getTitle());


            viewHolder.tradeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO trade operation
                    new TradeBookTask(tradeBookListItem.getBook()).execute();
                }
            });

            //  viewHolder.cardView.startAnimation(scaleUp);

            return convertView;

        }


        @Override
        public int getCount() {
            return tradeBookListItems.size();
        }
    }

    public class RequestBookListItem{

        Book book;

        public RequestBookListItem(Book book) {
            this.book = book;
        }

        public Book getBook() {
            return book;
        }

        public void setBook(Book book) {
            this.book = book;
        }
    }

    public class SearchBookTask extends AsyncTask<Void, Void, Void>{

        String query;

        public SearchBookTask(String query) {
            this.query = query;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

    public class TradeBookTask extends AsyncTask<Void, Void, Void>{

        Book book;

        public TradeBookTask(Book book) {
            this.book = book;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }


}
