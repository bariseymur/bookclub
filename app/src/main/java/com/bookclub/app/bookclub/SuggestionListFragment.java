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

import com.bookclub.app.bookclub.bookclubapi.Book;
import com.bookclub.app.bookclub.bookclubapi.BookClubAPI;
import com.bookclub.app.bookclub.bookclubapi.User;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
    ImageButton preferencesButton, chatButton;
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

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Fragment Created", "SuggestionListFragment Created");
        View view = inflater.inflate(R.layout.fragment_suggestion_list, container, false);

        Log.d("onCreateView", "Before alert dialog");

        alertDialog = new SpotsDialog(getActivity());
        alertDialog.show();

        new CreateSuggestionListTask().execute();
        listView = view.findViewById(R.id.suggestionList);
        Log.d("onCreateView", "After alert dialog");


        preferencesButton = view.findViewById(R.id.preferencesButton);
        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PreferencesActivity.class);
                startActivity(intent);
            }
        });


        chatButton = view.findViewById(R.id.chatButton);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatListActivity.class);
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
    private static class ViewHolder{
        TextView user1Name, user2Name, author1Name, author2Name, book1Title, book2Title, causeText;
        ImageButton book1Image, book2Image;
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
            ViewHolder viewHolder;

            if (convertView == null){
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.suggestion_list_exchange_item, parent, false);

                //Text Views
                viewHolder.user1Name = convertView.findViewById(R.id.userName1);
                viewHolder.user2Name = convertView.findViewById(R.id.userName2);
                viewHolder.author1Name = convertView.findViewById(R.id.author1Name);
                viewHolder.author2Name = convertView.findViewById(R.id.author2Name);
                viewHolder.book1Title = convertView.findViewById(R.id.book1Title);
                viewHolder.book2Title = convertView.findViewById(R.id.book2Title);
                viewHolder.causeText = convertView.findViewById(R.id.causeText);

                //Image Button
                viewHolder.book1Image = convertView.findViewById(R.id.book1Image);
                viewHolder.book2Image = convertView.findViewById(R.id.book2Image);
                viewHolder.transactionButton = convertView.findViewById(R.id.transactionButton);

                viewHolder.user1Name.setText(suggestionListContent.getUser().getUsername());
                viewHolder.user2Name.setText(suggestionListContent.getSuggestedUser().getUsername());
                viewHolder.author1Name.setText(suggestionListContent.getGivingBook().getAuthorName());
                viewHolder.author2Name.setText(suggestionListContent.getWantedBook().getAuthorName());
                viewHolder.book1Title.setText(suggestionListContent.getGivingBook().getTitle());
                viewHolder.book2Title.setText(suggestionListContent.getWantedBook().getTitle());
                viewHolder.causeText.setText("You may like this trade because of this book you liked: "
                        + suggestionListContent.getSuggestedBook().getTitle());

                Picasso.get()
                        .load(suggestionListContent.getGivingBook().getBookPhotoUrl())
                        .resize(300, 400)
                        .error(R.drawable.book)
                        .placeholder(R.drawable.account)
                        .into(viewHolder.book1Image);

                Picasso.get()
                        .load(suggestionListContent.getWantedBook().getBookPhotoUrl())
                        .resize(300, 400)
                        .error(R.drawable.book)
                        .placeholder(R.drawable.account)
                        .into(viewHolder.book2Image);

                //viewHolder.book1Image.setImageBitmap(Bitmap.createScaledBitmap(suggestionListContent.getBook1Image(), 300, 400, false));
                //viewHolder.book2Image.setImageBitmap(Bitmap.createScaledBitmap(suggestionListContent.getBook2Image(), 300, 400, false));

/*                viewHolder.transactionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, suggestionListContent.getUserName1() + " " + suggestionListContent.getBookTitle1() + "-" + suggestionListContent.getUserName2() + " " + suggestionListContent.getBookTitle2(), Snackbar.LENGTH_LONG).show();
                    }
                });
*/
                result = convertView;
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder)convertView.getTag();
                result = convertView;
            }



            return convertView;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class CreateSuggestionListTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("onPreExecute", "before");

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            adapter = new SuggestionListAdapter(suggestionListContents, getContext());
            listView.setAdapter(adapter);
            alertDialog.dismiss();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            BookClubAPI api = new BookClubAPI();

            ArrayList<Object> suggestions = (ArrayList<Object>) api.suggestionListIndex().get(2);
            suggestionListContents = new ArrayList<>();

            if (suggestions != null && suggestions.size() > 0){

             //   int userID = (int)((ArrayList)(((ArrayList<Object>)suggestions.get(0)).get(0))).get(1);

                User currentUser = new User(3, "asd","asd", "asd", "asd", "asd", "asd", "asd", true, "1992-05-22", 38, 34);
                System.out.println(suggestions + "\nSize : " + suggestions.size());
                for (int i = 0; i < suggestions.size(); i++){
                    ArrayList<Object> suggestion = (ArrayList<Object>) suggestions.get(i);
                    System.out.println("Suggestion: " + suggestion);
                    // ArrayList<Object> suggestionListinfo = ((ArrayList<Object>)(((ArrayList<Object>)suggestions.get(i)).get(0)));
                    ArrayList<Object> suggestionListinfo = (ArrayList<Object>) suggestion.get(0);
                    SuggestionListContent suggestionListContent = new SuggestionListContent(
                            (int)suggestionListinfo.get(0),
                            currentUser,
                            currentUser,
                            (Book)suggestion.get(1),
                            (Book)suggestion.get(2),
                            (Book) suggestion.get(3),
                            (int)suggestionListinfo.get(3)
                    );
                    suggestionListContents.add(suggestionListContent);
                }


            }


            return null;
        }
    }

    /*
    * Class SuggestionListContent
    * */

    public class SuggestionListContent{

        public static final int TRANSACTION_EXCHANGE = 0;
        public static final int TRANSACTION_SELL = 1;


          int suggestID;
          User user, suggestedUser;
          Book givingBook, wantedBook, suggestedBook;
          int score;

        public SuggestionListContent(int suggestID, User user, User suggestedUser, Book givingBook, Book wantedBook, Book suggestedBook, int score) {
            this.suggestID = suggestID;
            this.user = user;
            this.suggestedUser = suggestedUser;
            this.givingBook = givingBook;
            this.wantedBook = wantedBook;
            this.suggestedBook = suggestedBook;
            this.score = score;
        }

        public int getSuggestID() {
            return suggestID;
        }

        public void setSuggestID(int suggestID) {
            this.suggestID = suggestID;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public User getSuggestedUser() {
            return suggestedUser;
        }

        public void setSuggestedUser(User suggestedUser) {
            this.suggestedUser = suggestedUser;
        }

        public Book getGivingBook() {
            return givingBook;
        }

        public void setGivingBook(Book givingBook) {
            this.givingBook = givingBook;
        }

        public Book getWantedBook() {
            return wantedBook;
        }

        public void setWantedBook(Book wantedBook) {
            this.wantedBook = wantedBook;
        }

        public Book getSuggestedBook() {
            return suggestedBook;
        }

        public void setSuggestedBook(Book suggestedBook) {
            this.suggestedBook = suggestedBook;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

    }



}
