package com.bookclub.app.bookclub;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.app.AlertDialog;

import java.io.InputStream;

import dmax.dialog.SpotsDialog;

/**
 * Created by pc on 17.04.2019.
 */

public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {

    String url;
    Context context;


    public ImageLoader(Context context, String url){
        this.url = url;
        this.context = context;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }



    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = null;
        try{
            InputStream in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }
}