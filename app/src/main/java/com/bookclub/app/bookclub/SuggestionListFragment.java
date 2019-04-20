package com.bookclub.app.bookclub;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SuggestionListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SuggestionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuggestionListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<SuggestionListContent> suggestionListContents;
    ArrayAdapter<SuggestionListContent> adapter;
    ListView listView;
    ImageButton preferencesButton;
    private OnFragmentInteractionListener mListener;
    AlertDialog alertDialog;
    public SuggestionListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SuggestionListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuggestionListFragment newInstance(String param1, String param2) {
        SuggestionListFragment fragment = new SuggestionListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

  /*      suggestionListContents = new ArrayList<>();

        Log.d("onCreate", "Before alert dialog");

        AlertDialog alertDialog = new SpotsDialog(getActivity());

        Log.d("onCreate", "Show alert dialog");
      //  populateSuggestionList();

        CreateSuggestionList c = new CreateSuggestionList();
        alertDialog.show();
        c.execute();

        Log.d("onCreate", "After alert dialog");
*/
        // populateSuggestionList();
    }




    public class CreateSuggestionList extends AsyncTask<Void,Void,Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("onPreExecute", "before");

        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            Log.d("onPostExecute", "before");

            alertDialog.dismiss();
            Log.d("onPostExecute", "after");
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            populateSuggestionList();
            return 1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Fragment Created", "SuggestionListFragment Created");
        View view = inflater.inflate(R.layout.fragment_suggestion_list, container, false);

        suggestionListContents = new ArrayList<>();

        Log.d("onCreateView", "Before alert dialog");

        AlertDialog alertDialog = new SpotsDialog(getActivity());
        alertDialog.show();

        Log.d("onCreateView", "Show alert dialog");
        populateSuggestionList();

        Log.d("onCreateView", "Dissmiss alert dialog");
        alertDialog.dismiss();
        //CreateSuggestionList c = new CreateSuggestionList();
        //c.execute();

        Log.d("onCreateView", "After alert dialog");


        listView = view.findViewById(R.id.suggestionList);

        adapter = new SuggestionListAdapter(suggestionListContents, getContext());
        listView.setAdapter(adapter);

        preferencesButton = view.findViewById(R.id.preferencesButton);
        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PreferencesActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private void populateSuggestionList(){
        suggestionListContents = new ArrayList<>();
        suggestionListContents.add(new SuggestionListFragment.SuggestionListContent("faruq476", "1984", "George Orwell", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", "YaraliCocuq", "Harry Potter", "J.K. Rowling", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", null));

        suggestionListContents = new ArrayList<>();
        suggestionListContents.add(new SuggestionListFragment.SuggestionListContent("faruq476", "1984", "George Orwell", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 14.90, null));
        suggestionListContents.add(new SuggestionListFragment.SuggestionListContent("faruq476", "1984", "George Orwell", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 14.90, null));

        suggestionListContents.add(new SuggestionListFragment.SuggestionListContent("faruq476", "Küçük Prens", "Saint-exupery", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", "KaraBela02", "Şeytan Ayrıntıda Saklıdır", "Ahmet Ümit", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", null));
        suggestionListContents.add(new SuggestionListFragment.SuggestionListContent("faruq476", "Küçük Prens", "Saint-exupery", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", "KaraBela02", "Şeytan Ayrıntıda Saklıdır", "Ahmet Ümit", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", null));

        suggestionListContents.add(new SuggestionListFragment.SuggestionListContent("faruq476", "1984", "George Orwell", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 14.90, null));
        suggestionListContents.add(new SuggestionListFragment.SuggestionListContent("faruq476", "1984", "George Orwell", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 14.90, null));

        suggestionListContents.add(new SuggestionListFragment.SuggestionListContent("faruq476", "Küçük Prens", "Saint-exupery", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", "KaraBela02", "Şeytan Ayrıntıda Saklıdır", "Ahmet Ümit", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", null));
        suggestionListContents.add(new SuggestionListFragment.SuggestionListContent("faruq476", "Küçük Prens", "Saint-exupery", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", "KaraBela02", "Şeytan Ayrıntıda Saklıdır", "Ahmet Ümit", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", null));

        suggestionListContents.add(new SuggestionListFragment.SuggestionListContent("faruq476", "1984", "George Orwell", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 14.90, null));
        suggestionListContents.add(new SuggestionListFragment.SuggestionListContent("faruq476", "1984", "George Orwell", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 14.90, null));

        suggestionListContents.add(new SuggestionListFragment.SuggestionListContent("faruq476", "Küçük Prens", "Saint-exupery", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", "KaraBela02", "Şeytan Ayrıntıda Saklıdır", "Ahmet Ümit", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", null));
        suggestionListContents.add(new SuggestionListFragment.SuggestionListContent("faruq476", "Küçük Prens", "Saint-exupery", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", "KaraBela02", "Şeytan Ayrıntıda Saklıdır", "Ahmet Ümit", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", null));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /*
    * Class SuggestionListAdapter
    * */
    private static class ExchangeViewHolder{
        TextView user1Name, user2Name, author1Name, author2Name, book1Title, book2Title, price;
        ImageButton book1Image, book2Image;
        ImageButton transactionButton;
        
    }

    private static class SellViewHolder{
        TextView userName, authorName, bookTitle, price;
        ImageButton bookImage;
        ImageButton transactionButton;
    }

    public class SuggestionListAdapter extends ArrayAdapter<SuggestionListFragment.SuggestionListContent> implements View.OnClickListener{

        private ArrayList<SuggestionListFragment.SuggestionListContent> dataSet;
        Context context;



        public SuggestionListAdapter(ArrayList<SuggestionListFragment.SuggestionListContent> data, Context context) {
            super(context, R.layout.suggestion_list_exchange_item, data);
            this.dataSet = data;
            this.context=context;

        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final SuggestionListFragment.SuggestionListContent suggestionListContent= getItem(position);
            final View result;
            if (suggestionListContent.getTransactionType() == SuggestionListContent.TRANSACTION_EXCHANGE) {
                SuggestionListFragment.ExchangeViewHolder viewHolder;

                if (convertView == null){
                    viewHolder = new SuggestionListFragment.ExchangeViewHolder();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.suggestion_list_exchange_item, parent, false);

                    //Text Views
                    viewHolder.user1Name = convertView.findViewById(R.id.userName1);
                    viewHolder.user2Name = convertView.findViewById(R.id.userName2);
                    viewHolder.author1Name = convertView.findViewById(R.id.author1Name);
                    viewHolder.author2Name = convertView.findViewById(R.id.authorName);
                    viewHolder.book1Title = convertView.findViewById(R.id.book1Title);
                    viewHolder.book2Title = convertView.findViewById(R.id.bookTitle);
                    viewHolder.price = convertView.findViewById(R.id.priceText);
                    //Image Button
                    viewHolder.book1Image = convertView.findViewById(R.id.bookImage1);
                    viewHolder.book2Image = convertView.findViewById(R.id.bookImage);
                    viewHolder.transactionButton = convertView.findViewById(R.id.transactionButton);

                    result = convertView;
                    convertView.setTag(viewHolder);
                }
                else{
                    viewHolder = (SuggestionListFragment.ExchangeViewHolder)convertView.getTag();
                    result = convertView;
                }


                //item content is defined here
                viewHolder.user1Name.setText(suggestionListContent.getUserName1());
                viewHolder.user2Name.setText(suggestionListContent.getUserName2());
                viewHolder.author1Name.setText(suggestionListContent.getAuthorName1());
                viewHolder.author2Name.setText(suggestionListContent.getAuthorName2());
                viewHolder.book1Title.setText(suggestionListContent.getBookTitle1());
                viewHolder.book2Title.setText(suggestionListContent.getBookTitle2());

                viewHolder.book1Image.setImageBitmap(Bitmap.createScaledBitmap(suggestionListContent.getBook1Image(), 300, 400, false));
                viewHolder.book2Image.setImageBitmap(Bitmap.createScaledBitmap(suggestionListContent.getBook2Image(), 300, 400, false));
            
                viewHolder.transactionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, suggestionListContent.getUserName1() + " " + suggestionListContent.getBookTitle1() + "-" + suggestionListContent.getUserName2() + " " + suggestionListContent.getBookTitle2(), Snackbar.LENGTH_LONG).show();
                    }
                });

                return convertView;
            }
            else if (suggestionListContent.getTransactionType() == SuggestionListContent.TRANSACTION_SELL){
                SuggestionListFragment.SellViewHolder viewHolder;


                if (convertView == null){
                    viewHolder = new SuggestionListFragment.SellViewHolder();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.suggestion_list_sell_item, parent, false);

                    //Text Views
                    viewHolder.userName = convertView.findViewById(R.id.userName);
                    viewHolder.authorName = convertView.findViewById(R.id.authorName);
                    viewHolder.bookTitle = convertView.findViewById(R.id.bookTitle);

                    //Image Views
                    viewHolder.bookImage = convertView.findViewById(R.id.bookImage);

                    //Image Buttons
                    viewHolder.transactionButton = convertView.findViewById(R.id.transactionButton);
                    viewHolder.price = convertView.findViewById(R.id.priceText);

                    result = convertView;
                    convertView.setTag(viewHolder);
                }
                else{
                    viewHolder = (SuggestionListFragment.SellViewHolder)convertView.getTag();
                    result = convertView;
                }


                //item content is defined here
                viewHolder.userName.setText(suggestionListContent.getUserName1());
                viewHolder.authorName.setText(suggestionListContent.getAuthorName1());
                viewHolder.bookTitle.setText(suggestionListContent.getBookTitle1());
                viewHolder.price.setText(""+suggestionListContent.getPrice());
                viewHolder.bookImage.setImageBitmap(Bitmap.createScaledBitmap(suggestionListContent.getBook1Image(), 300, 400, false));
                viewHolder.transactionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        startActivity(intent);
                    }
                });
            }
            else{
                return null;
            } 

            return convertView;
        }

        @Override
        public void onClick(View v) {

        }
    }
    

    /*
    * Class SuggestionListContent
    * */

    public class SuggestionListContent{

        public static final int TRANSACTION_EXCHANGE = 0;
        public static final int TRANSACTION_SELL = 1;

        private double price;
        private String userName1, userName2;
        private String bookTitle1, bookTitle2;
        private String authorName1, authorName2;
        private Bitmap book1Image, book2Image;
        private int transactionType;
        private String reason;

        public SuggestionListContent(String userName1, String bookTitle1, String authorName1, String book1ImageURL,
                                String userName2, String bookTitle2, String authorName2, String book2ImageURL, String reason) {
            this.transactionType = TRANSACTION_EXCHANGE;
            this.userName1 = userName1;
            this.userName2 = userName2;
            this.bookTitle1 = bookTitle1;
            this.bookTitle2 = bookTitle2;
            this.authorName1 = authorName1;
            this.authorName2 = authorName2;
            this.book1Image = book1Image;
            this.book2Image = book2Image;
            this.reason = reason;


            try {

                ImageLoader imageLoader = new ImageLoader(getContext(), book1ImageURL);
                book1Image = imageLoader.execute().get();
                imageLoader = new ImageLoader(getContext(), book2ImageURL);
                book2Image = imageLoader.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }

        public SuggestionListContent( String userName, String bookTitle, String authorName,  String bookImageURL, double price, String reason) {
            this.transactionType = TRANSACTION_SELL;
            this.userName1 = userName;
            this.bookTitle1 = bookTitle;
            this.authorName1 = authorName;
            this.price = price;
            this.reason = reason;


            try {

                ImageLoader imageLoader = new ImageLoader(getContext(), bookImageURL);
                book1Image = imageLoader.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getUserName1() {
            return userName1;
        }

        public void setUserName1(String userName1) {
            this.userName1 = userName1;
        }

        public String getUserName2() {
            return userName2;
        }

        public void setUserName2(String userName2) {
            this.userName2 = userName2;
        }

        public String getBookTitle1() {
            return bookTitle1;
        }

        public void setBookTitle1(String bookTitle1) {
            this.bookTitle1 = bookTitle1;
        }

        public String getBookTitle2() {
            return bookTitle2;
        }

        public void setBookTitle2(String bookTitle2) {
            this.bookTitle2 = bookTitle2;
        }

        public String getAuthorName1() {
            return authorName1;
        }

        public void setAuthorName1(String authorName1) {
            this.authorName1 = authorName1;
        }

        public String getAuthorName2() {
            return authorName2;
        }

        public void setAuthorName2(String authorName2) {
            this.authorName2 = authorName2;
        }

        public Bitmap getBook1Image() {
            return book1Image;
        }

        public void setBook1Image(Bitmap book1Image) {
            this.book1Image = book1Image;
        }

        public Bitmap getBook2Image() {
            return book2Image;
        }

        public void setBook2Image(Bitmap book2Image) {
            this.book2Image = book2Image;
        }

        public int getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(int transactionType) {
            this.transactionType = transactionType;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }


}
