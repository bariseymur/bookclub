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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookclub.app.bookclub.bookclubapi.Book;
import com.bookclub.app.bookclub.bookclubapi.BookClubAPI;
import com.bookclub.app.bookclub.bookclubapi.User;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


import dmax.dialog.SpotsDialog;

import static android.widget.Toast.LENGTH_SHORT;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MatchListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MatchListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    BookClubAPI api;

    //our variables
    private ArrayList<MatchListContent> matchListContents;
    private OnFragmentInteractionListener mListener;
    private ImageButton preferencesButton, transactionButton, chatButton;
    private ListView listView;
    ArrayAdapter<MatchListContent> matchListContentArrayAdapter;
    AlertDialog alertDialog;
    public MatchListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MatchListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchListFragment newInstance(String param1, String param2) {
        MatchListFragment fragment = new MatchListFragment();
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

        View view = inflater.inflate(R.layout.fragment_match_list, container, false);
        api = new BookClubAPI();
        alertDialog = new SpotsDialog(getActivity());
        alertDialog.show();
        new GetMatchListTask().execute();

        listView = view.findViewById(R.id.matchList);


        preferencesButton = view.findViewById(R.id.preferencesButton);
        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PreferencesActivity.class);
                startActivity(intent);
            }
        });

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener = new SwipeToDismissTouchListener<>(
                new ListViewAdapter(listView),
                new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListViewAdapter view, int position) {
                        new RejectMatchTask(matchListContents.get(position).getMatchID()).execute();
                        matchListContents.remove(position);
                        matchListContentArrayAdapter.notifyDataSetChanged();
                    }
                });

        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Toast.makeText(getActivity(), "Position " + position, LENGTH_SHORT).show();
                }
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


        Log.d("Fragment Created", "MatchListFragment Created");
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
    *
    * class MatchListAdapter
    *
    * */

    private static class ViewHolder{
        TextView user1Name, user2Name, author1Name, author2Name, book1Title, book2Title;
        ImageView book1Image, book2Image;
        ImageButton transactionButton;

    }

    public class MatchListAdapter extends ArrayAdapter<MatchListFragment.MatchListContent> implements View.OnClickListener{

        private ArrayList<MatchListFragment.MatchListContent> dataSet;
        Context context;



        public MatchListAdapter(ArrayList<MatchListFragment.MatchListContent> data, Context context) {
            super(context, R.layout.match_list_item, data);
            this.dataSet = data;
            this.context=context;

        }

        public void remove(int position) {
            matchListContents.remove(position);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final MatchListFragment.MatchListContent matchListContent= getItem(position);

            MatchListFragment.ViewHolder viewHolder;

            final View result;


            if (convertView == null){
                viewHolder = new MatchListFragment.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.match_list_item, parent, false);

                //Text Views
                viewHolder.user1Name = convertView.findViewById(R.id.userName1);
                viewHolder.user2Name = convertView.findViewById(R.id.userName2);
                viewHolder.author1Name = convertView.findViewById(R.id.author1Name);
                viewHolder.author2Name = convertView.findViewById(R.id.author2Name);
                viewHolder.book1Title = convertView.findViewById(R.id.book1Title);
                viewHolder.book2Title = convertView.findViewById(R.id.book2Title);

                //Image Views
                viewHolder.book1Image = convertView.findViewById(R.id.book1Image);
                viewHolder.book2Image = convertView.findViewById(R.id.book2Image);

                //Image Buttons
                viewHolder.transactionButton = convertView.findViewById(R.id.transactionButton);

                result = convertView;
                convertView.setTag(viewHolder);

                //item content is defined here
                viewHolder.user1Name.setText(matchListContent.getUser().getUsername());
                viewHolder.user2Name.setText(matchListContent.getMatchedUser().getUsername());
                viewHolder.author1Name.setText(matchListContent.getGivenBook().getAuthorName());
                viewHolder.author2Name.setText(matchListContent.getWantedBook().getAuthorName());
                viewHolder.book1Title.setText(matchListContent.getGivenBook().getTitle());
                viewHolder.book2Title.setText(matchListContent.getWantedBook().getTitle());

                Picasso.get()
                        .load(matchListContent.getGivenBook().getBookPhotoUrl())
                        .resize(300, 400)
                        .error(R.drawable.error)
                        .placeholder(R.drawable.loading)
                        .into(viewHolder.book1Image);


                Picasso.get()
                        .load(matchListContent.getWantedBook().getBookPhotoUrl())
                        .resize(300, 400)
                        .error(R.drawable.error)
                        .placeholder(R.drawable.loading)
                        .into(viewHolder.book2Image);

              //  viewHolder.book1Image.setImageBitmap(Bitmap.createScaledBitmap(matchListContent.getBook1Image(), 300, 400, false));
               // viewHolder.book2Image.setImageBitmap(Bitmap.createScaledBitmap(matchListContent.getBook2Image(), 300, 400, false));
            }
            else{
                viewHolder = (MatchListFragment.ViewHolder)convertView.getTag();
                result = convertView;
            }


            viewHolder.transactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AcceptMatchTask(matchListContent.getMatchID()).execute();
                    matchListContents.remove(position);
                    matchListContentArrayAdapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }

        @Override
        public void onClick(View v) {

        }



    }

    public class RejectMatchTask extends AsyncTask<Void, Void, Void>{

        int matchID;

        public RejectMatchTask(int matchID){
            this.matchID = matchID;
        }


        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<Object> arr = api.rejectMatch(matchID);
            System.out.println(arr);
            return null;
        }
    }


    /*
    *
    * class MatchListContent
    *
    *
    * */

    class MatchListContent {

        private int matchID;
        private User user;
        private User matchedUser;
        private int score;
        private Book givenBook;
        private Book wantedBook;
        private String matchDate;
        private String match;


        public MatchListContent(int matchID, User user, User matchedUser, int score, Book givenBook,
                                Book wantedBook){

            this.matchID = matchID;
            this.user = user;
            this.matchedUser = matchedUser;
            this.score = score;
            this.givenBook = givenBook;
            this.wantedBook = wantedBook;

        }


        public int getMatchID() {
            return matchID;
        }

        public void setMatchID(int matchID) {
            this.matchID = matchID;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public User getMatchedUser() {
            return matchedUser;
        }

        public void setMatchedUser(User matchedUser) {
            this.matchedUser = matchedUser;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public Book getGivenBook() {
            return givenBook;
        }

        public void setGivenBook(Book givenBook) {
            this.givenBook = givenBook;
        }

        public Book getWantedBook() {
            return wantedBook;
        }

        public void setWantedBook(Book wantedBook) {
            this.wantedBook = wantedBook;
        }

        public String getMatchDate() {
            return matchDate;
        }

        public void setMatchDate(String matchDate) {
            this.matchDate = matchDate;
        }

        public String getMatch() {
            return match;
        }

        public void setMatch(String match) {
            this.match = match;
        }
    }


    public class AcceptMatchTask extends AsyncTask<Void, Void, Void>{

        int matchID;

        public AcceptMatchTask(int matchID){
            this.matchID = matchID;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<Object> result =  api.confirmMatch(matchID);
            System.out.println(result);
            return null;
        }
    }

    public class GetMatchListTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            matchListContentArrayAdapter = new MatchListFragment.MatchListAdapter(matchListContents, getContext());
            listView.setAdapter(matchListContentArrayAdapter);

            if (alertDialog.isShowing())alertDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            ArrayList<Object> arr = api.matchListIndex();
            ArrayList<Object> matches = (ArrayList<Object>) arr.get(2);
            matchListContents = new ArrayList<>();
            if (matches != null && matches.size() > 0){

                int userID = (int)((ArrayList)(((ArrayList<Object>)matches.get(0)).get(0))).get(1);
                System.out.println("User ID" + userID);
                System.out.println(api.getUserProfile(userID));
                //User currentUser = (User)(api.getUserProfile(userID).get(2));
                User currentUser = new User(3, "asd","asd", "asd", "asd", "asd", "asd", "asd", true, "1992-05-22", 38, 34);

                for (int i = 0; i < matches.size(); i++){
                    ArrayList<Object> match = (ArrayList<Object>) matches.get(i);
                    ArrayList<Object> matchlistInfo = ((ArrayList)(((ArrayList<Object>)matches.get(i)).get(0)));
                    MatchListContent m = new MatchListContent(
                            (int)matchlistInfo.get(0),
                            currentUser,
                            currentUser,
                            (int) matchlistInfo.get(3),
                            (Book)match.get(1),
                            (Book)match.get(2)
                    );
                    matchListContents.add(m);
                }

            }


            return null;
        }
    }



}
