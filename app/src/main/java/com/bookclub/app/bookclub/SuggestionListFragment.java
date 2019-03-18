package com.bookclub.app.bookclub;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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

    private OnFragmentInteractionListener mListener;

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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggestion_list, container, false);
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
    * Class SuggestionListContent
    * */

    public class SuggestionListContent{

        public final int TRANSACTION_EXCHANGE = 0;
        public final int TRANSACTION_SELL = 1;

        private double price;
        private String userName1, userName2;
        private String bookTitle1, bookTitle2;
        private String authorName1, authorName2;
        private Drawable book1Image, book2Image;
        private int transactionType;

        public SuggestionListContent(String userName1, String bookTitle1, String authorName1, Drawable book1Image,
                                String userName2, String bookTitle2, String authorName2, Drawable book2Image) {
            this.transactionType = 0;
            this.userName1 = userName1;
            this.userName2 = userName2;
            this.bookTitle1 = bookTitle1;
            this.bookTitle2 = bookTitle2;
            this.authorName1 = authorName1;
            this.authorName2 = authorName2;
            this.book1Image = book1Image;
            this.book2Image = book2Image;
        }

        public SuggestionListContent( String userName1, String userName2, String bookTitle2, String authorName2, Drawable book2Image, double price) {
            this.transactionType = 1;
            this.userName1 = userName1;
            this.userName2 = userName2;
            this.bookTitle2 = bookTitle2;
            this.authorName2 = authorName2;
            this.book2Image = book2Image;
            this.price = price;
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

        public Drawable getBook1Image() {
            return book1Image;
        }

        public void setBook1Image(Drawable book1Image) {
            this.book1Image = book1Image;
        }

        public Drawable getBook2Image() {
            return book2Image;
        }

        public void setBook2Image(Drawable book2Image) {
            this.book2Image = book2Image;
        }

        public int getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(int transactionType) {
            this.transactionType = transactionType;
        }
    }


}
