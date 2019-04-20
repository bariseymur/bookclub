package com.bookclub.app.bookclub;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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

    //our variables
    private ArrayList<MatchListContent> matchListContents;
    private OnFragmentInteractionListener mListener;
    private ImageButton preferencesButton, transactionButton;



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
        populateMatchList();
        ListView listView = view.findViewById(R.id.matchList);
        ArrayAdapter<MatchListContent> matchListContentArrayAdapter = new MatchListFragment.MatchListAdapter(matchListContents, getContext());
        listView.setAdapter(matchListContentArrayAdapter);

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

        Log.d("Fragment Created", "MatchListFragment Created");
        // Inflate the layout for this fragment
        return view;
    }


    private void populateMatchList(){
        matchListContents = new ArrayList<>();
        matchListContents.add(new MatchListContent("faruq476", "1984", "George Orwell", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", "YaraliCocuq", "Harry Potter", "J.K. Rowling", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 44));
        matchListContents.add(new MatchListContent("faruq476", "Küçük Prens", "Saint-exupery", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", "KaraBela02", "Şeytan Ayrıntıda Saklıdır", "Ahmet Ümit", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 43));
        matchListContents.add(new MatchListContent("faruq476", "Küçük Prens", "Saint-exupery", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", "KaraBela02", "Şeytan Ayrıntıda Saklıdır", "Ahmet Ümit", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 55));
        matchListContents.add(new MatchListContent("faruq476", "Küçük Prens", "Saint-exupery", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", "KaraBela02", "Şeytan Ayrıntıda Saklıdır", "Ahmet Ümit", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 1));
        matchListContents.add(new MatchListContent("faruq476", "Küçük Prens", "Saint-exupery", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", "KaraBela02", "Şeytan Ayrıntıda Saklıdır", "Ahmet Ümit", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg", 12));

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
                viewHolder.book1Image = convertView.findViewById(R.id.bookImage1);
                viewHolder.book2Image = convertView.findViewById(R.id.bookImage2);

                //Image Buttons
                viewHolder.transactionButton = convertView.findViewById(R.id.transactionButton);

                result = convertView;
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (MatchListFragment.ViewHolder)convertView.getTag();
                result = convertView;
            }


            //item content is defined here
            viewHolder.user1Name.setText(matchListContent.getUserName1());
            viewHolder.user2Name.setText(matchListContent.getUserName2());
            viewHolder.author1Name.setText(matchListContent.getAuthorName1());
            viewHolder.author2Name.setText(matchListContent.getAuthorName2());
            viewHolder.book1Title.setText(matchListContent.getBookTitle1());
            viewHolder.book2Title.setText(matchListContent.getBookTitle2());



            viewHolder.book1Image.setImageBitmap(Bitmap.createScaledBitmap(matchListContent.getBook1Image(), 300, 400, false));
            viewHolder.book2Image.setImageBitmap(Bitmap.createScaledBitmap(matchListContent.getBook2Image(), 300, 400, false));



            viewHolder.transactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Snackbar.make(v, matchListContent.getUserName1() + " " + matchListContent.getBookTitle1() + "-" + matchListContent.getUserName2() + " " + matchListContent.getBookTitle2(), Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("MatchID", matchListContent.getMatchID());
                    intent.putExtra("ChatID", 8342);
                    startActivity(intent);

                }
            });

            return convertView;
        }

        @Override
        public void onClick(View v) {

        }



    }

    /*
    *
    * class MatchListContent
    *
    *
    * */

    class MatchListContent {

        private String userName1, userName2;
        private String bookTitle1, bookTitle2;
        private String authorName1, authorName2;
        private String book1ImageURL, book2ImageURL;
        private long matchID;
        private Bitmap book1Image, book2Image;

        public MatchListContent(String userName1, String bookTitle1, String authorName1, String book1ImageURL,
                                String userName2, String bookTitle2, String authorName2, String book2ImageURL, long matchID) {
            this.userName1 = userName1;
            this.userName2 = userName2;
            this.bookTitle1 = bookTitle1;
            this.bookTitle2 = bookTitle2;
            this.authorName1 = authorName1;
            this.authorName2 = authorName2;
            this.book1ImageURL = book1ImageURL;
            this.book2ImageURL = book2ImageURL;
            this.matchID = matchID;


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


        public Bitmap getBook1Image() {
            return book1Image;
        }

        public Bitmap getBook2Image() {
            return book2Image;
        }

        public String getBook1ImageURL() {
            return book1ImageURL;
        }

        public void setBook1ImageURL(String book1ImageURL) {
            this.book1ImageURL = book1ImageURL;
        }

        public String getBook2ImageURL() {
            return book2ImageURL;
        }

        public void setBook2ImageURL(String book2ImageURL) {
            this.book2ImageURL = book2ImageURL;
        }

        public void setBook1Image(Bitmap book1Image) {
            this.book1Image = book1Image;
        }

        public void setBook2Image(Bitmap book2Image) {
            this.book2Image = book2Image;
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


        public long getMatchID() {
            return matchID;
        }

        public void setMatchID(long matchID) {
            this.matchID = matchID;
        }



    }



}
