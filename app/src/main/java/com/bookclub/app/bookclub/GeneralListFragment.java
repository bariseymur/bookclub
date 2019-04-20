package com.bookclub.app.bookclub;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GeneralListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GeneralListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<GeneralListContent> generalListContent;
    private ImageButton preferencesButton;
    private AlertDialog alertDialog;
    private OnFragmentInteractionListener mListener;

    public GeneralListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GeneralListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GeneralListFragment newInstance(String param1, String param2) {
        GeneralListFragment fragment = new GeneralListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void populateGeneralList(){

        generalListContent = new ArrayList<>();

        generalListContent.add(new GeneralListContent(1, "1984", "George Orwell", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg"));
        generalListContent.add(new GeneralListContent(2, "Harry Potter", "J.K. Rowling", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg"));
        generalListContent.add(new GeneralListContent(1, "Küçük Prens", "Saint-exupery", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg"));
        generalListContent.add(new GeneralListContent(1, "Şeytan Ayrıntıda Saklıdır", "Ahmet Ümit", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg"));
        generalListContent.add(new GeneralListContent(1, "1984", "George Orwell", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg"));
        generalListContent.add(new GeneralListContent(2, "Harry Potter", "J.K. Rowling", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg"));
        generalListContent.add(new GeneralListContent(1, "Küçük Prens", "Saint-exupery", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg"));
        generalListContent.add(new GeneralListContent(1, "Şeytan Ayrıntıda Saklıdır", "Ahmet Ümit", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg"));
        generalListContent.add(new GeneralListContent(1, "1984", "George Orwell", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg"));
        generalListContent.add(new GeneralListContent(2, "Harry Potter", "J.K. Rowling", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg"));
        generalListContent.add(new GeneralListContent(1, "Küçük Prens", "Saint-exupery", "http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg"));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //get items from server here
        alertDialog = new SpotsDialog(getActivity());
        alertDialog.show();
        new GeneralListCreator().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_general_list, container, false);
        //generalListContent = new ArrayList<>();

       // populateGeneralList();
        ListView listView = view.findViewById(R.id.generalList);
        ArrayAdapter<GeneralListContent> generalListContentArrayAdapter = new GeneralListAdapter(generalListContent, getContext());
        listView.setAdapter(generalListContentArrayAdapter);

        preferencesButton = view.findViewById(R.id.preferencesButton);
        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PreferencesActivity.class);
                startActivity(intent);
            }
        });
        Log.d("Fragment Created", "GeneralListFragment Created");
        //alertDialog.dismiss();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // if (alertDialog != null && alertDialog.isShowing())alertDialog.dismiss();
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

    private static class ViewHolder{
        ImageButton transactionImageButton;
        TextView authorNameTextView;
        TextView bookTitleTextView;
        ImageButton bookImageButton;
    }


    public class GeneralListAdapter extends ArrayAdapter<GeneralListContent> implements View.OnClickListener{

        private ArrayList<GeneralListContent> dataSet;
        Context context;



        public GeneralListAdapter(ArrayList<GeneralListContent> data, Context context) {
            super(context, R.layout.general_list_item, data);
            this.dataSet = data;
            this.context=context;

        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final GeneralListContent generalListContent = getItem(position);

            ViewHolder viewHolder;

            final View result;
            Bitmap bitmap = null;
            if (convertView == null){
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.general_list_item, parent, false);
                viewHolder.authorNameTextView = (TextView)convertView.findViewById(R.id.authorTextView);
                viewHolder.bookTitleTextView= (TextView)convertView.findViewById(R.id.bookTitleTextView);
                viewHolder.bookImageButton= (ImageButton) convertView.findViewById(R.id.bookImageButton);

                try {
                    ImageLoader imageLoader = new ImageLoader(getContext(), generalListContent.getBookImageURL());
                    bitmap = imageLoader.execute().get();

                    viewHolder.bookImageButton.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 400, false));

                } catch (Exception e) {
                    // Log.e("Error Message", e.getMessage());
                    e.printStackTrace();
                }

                viewHolder.transactionImageButton = (ImageButton) convertView.findViewById(R.id.transactionImageButton);
                result = convertView;
                convertView.setTag(viewHolder);

            }
            else{
                viewHolder = (ViewHolder)convertView.getTag();
                result = convertView;
            }

            viewHolder.authorNameTextView.setText(generalListContent.getAuthorName());
            viewHolder.bookTitleTextView.setText(generalListContent.getBookTitle());


            if (generalListContent.getTransactionType() == 2){
                viewHolder.transactionImageButton.setImageResource(R.drawable.ic_compare_arrows_black_24dp);
            }
            else
                viewHolder.transactionImageButton.setImageResource(R.drawable.ic_shopping_cart_black_24dp);
            viewHolder.bookImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                    intent.putExtra("title", generalListContent.getBookTitle());
                    intent.putExtra("author", generalListContent.getAuthorName());
                    startActivity(intent);
                }
            });
            viewHolder.transactionImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (generalListContent.getTransactionType() == 1){
                        Snackbar.make(v, "Transaction Type : Sell", Snackbar.LENGTH_SHORT).show();
                    }
                    else{

                        Snackbar.make(v, "Transaction Type : Trade", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });



            // viewHolder.bookImageButton.setImageDrawable(sadasd);
            //viewHolder.transactionImageButton;


            return convertView;
        }

        @Override
        public void onClick(View v) {

        }
    }


    public class GeneralListCreator extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {

            populateGeneralList();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (alertDialog.isShowing())alertDialog.dismiss();
        }
    }


    class GeneralListContent{

        int transactionType;
        String bookTitle;
        String authorName;
        String bookImageURL;


        public GeneralListContent(int transactionType, String bookTitle, String authorName, String bookImageURL) {
            this.transactionType = transactionType;
            this.bookTitle = bookTitle;
            this.authorName = authorName;

            this.bookImageURL = bookImageURL;
        }

        public int getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(int transactionType) {
            this.transactionType = transactionType;
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

        public String getBookImageURL() {
            return bookImageURL;
        }

        public void setBookImageURL(String bookImageURL) {
            this.bookImageURL = bookImageURL;
        }
    }
}
