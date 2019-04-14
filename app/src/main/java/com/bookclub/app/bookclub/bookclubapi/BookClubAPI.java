/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bookclub.app.bookclub.bookclubapi;

import okhttp3.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;

/**
 *
 * @author mosma
 */
public class BookClubAPI {

    private static OkHttpClient client = new OkHttpClient();
    private String serverUrl = "http://bookclub-mertdonmezyurek297604.codeanyapp.com:8000/bookclub_server/";
    private static String cookie = null;

    public BookClubAPI() {

    }

    private String get(String url) {

        try {
            Request request = null;

            if (this.cookie == null) {
                request = new Request.Builder()
                        .url(this.serverUrl + url)
                        .get()
                        .build();
            } else {
                request = new Request.Builder()
                        .url(this.serverUrl + url)
                        .get()
                        .addHeader("Cookie", this.cookie)
                        .build();
            }

            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (IOException ex) {
            Logger.getLogger(BookClubAPI.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private String post(String url, String json) {
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, json);

        try {
            Request request = null;
            if (this.cookie == null) {
                request = new Request.Builder()
                        .url(this.serverUrl + url)
                        .post(body)
                        .build();
            } else {
                request = new Request.Builder()
                        .url(this.serverUrl + url)
                        .post(body)
                        .addHeader("Cookie", this.cookie)
                        .build();
            }

            Response response = client.newCall(request).execute();

            if (this.cookie == null) {
                this.cookie = response.headers().get("Set-Cookie");
            }

//            System.out.println("---\npost():\n" + response.headers().get("Set-Cookie").toString() + "\n---");
            return response.body().string();

//            Request firstRequest = new Request.Builder()
//                    .url(this.serverUrl + url)
//                    .post(body)
//                    .build();
//            Response firstResponse = client.newCall(firstRequest).execute();
//
//            Request.Builder requestBuilder = new Request.Builder()
//                    .url(this.serverUrl + url)
//                    .post(body);
//
//            Headers headers = firstResponse.headers();
//
//            requestBuilder = requestBuilder.addHeader("Cookie", headers.get("Set-Cookie"));
//
//            Request secondRequest = requestBuilder.get().build();
//            Response secondResponse = client.newCall(secondRequest).execute();
//            return secondResponse.body().string();
        } catch (IOException ex) {
            Logger.getLogger(BookClubAPI.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ArrayList<Object> login(String username, String password) {

        ArrayList<Object> arr = new ArrayList<>();
        String json = "{"
                + "\"username\": \"" + username + "\","
                + "\"password\": \"" + password + "\""
                + "}";

        try {
            JSONObject reader = new JSONObject(this.post("login/", json));
//            System.out.println("---\nlogin():\n" + reader.toString() + "\n---");
            String status = reader.getString("status");
            String message = reader.getString("message");

            arr.add(status);
            arr.add(message);

        } catch (JSONException e) {
            arr.add("error");
            arr.add("failed to login to server. JSON error.");
            e.printStackTrace();
            return arr;
        }

        return arr;
    }

    public ArrayList<Object> seeOtherUserProfile(String username) {

        String url = "seeOtherUserProfile/";
        ArrayList<Object> arr = new ArrayList<>();
        String json = "{"
                + "\"username\": \"" + username + "\""
                + "}";

        try {
            JSONObject reader = new JSONObject(this.post(url, json));

            String status = reader.getString("status");
            String message = reader.getString("message");
            JSONObject user_info_json = reader.getJSONObject("user_info");

//            ArrayList<Object> user_info = new ArrayList<>();
//            user_info.add(user_info_json.getInt("id"));
//            user_info.add(user_info_json.getString("name"));
//            user_info.add(user_info_json.getString("surname"));
//            user_info.add(user_info_json.getString("country"));
//            user_info.add(user_info_json.getString("mail"));
//            user_info.add(user_info_json.getString("phoneNumber"));
//            user_info.add(user_info_json.getString("dateOfBirth"));
//            user_info.add(user_info_json.getString("username"));
//            user_info.add(user_info_json.getString("password"));
//            user_info.add(user_info_json.getString("long"));
//            user_info.add(user_info_json.getString("lat"));
//            user_info.add(user_info_json.getBoolean("onlineState"));
//            user_info.add(user_info_json.getString("profilePicture"));
            User user_info = new User(
                    user_info_json.getInt("id"),
                    user_info_json.getString("username"),
                    user_info_json.getString("password"),
                    user_info_json.getString("mail"),
                    user_info_json.getString("name"),
                    user_info_json.getString("surname"),
                    user_info_json.getString("country"),
                    user_info_json.getString("phoneNumber"),
                    user_info_json.getString("profilePicture"),
                    user_info_json.getBoolean("onlineState"),
                    user_info_json.getString("dateOfBirth"),
                    user_info_json.getDouble("long"),
                    user_info_json.getDouble("lat")
            );

            arr.add(status);
            arr.add(message);
            arr.add(user_info);

        } catch (JSONException e) {
            arr.add("error");
            arr.add("seeOtherUserProfile failed. JSON error.");
            e.printStackTrace();
            return arr;
        }

        return arr;
    }

    public ArrayList<Object> forgotPassword(String username, String email) {

        if (username == null && email == null) {
            throw new Error("Username and email cannot be null at the same time");
        }

        ArrayList<Object> arr = new ArrayList<>();
        String json = "{"
                + "\"username\": \"" + username + "\","
                + "\"mail\": \"" + email + "\""
                + "}";

        try {
            JSONObject reader = new JSONObject(this.post("forgotPassword/", json));
            String status = reader.getString("status");
            String message = reader.getString("message");
            String newPass = reader.getString("password");

            arr.add(status);
            arr.add(message);
            arr.add(newPass);

        } catch (JSONException e) {
            arr.add("error");
            arr.add("forgotPassword failed. JSON error.");
            e.printStackTrace();
            return arr;
        }

        return arr;
    }

    public ArrayList<Object> signout() {

        ArrayList<Object> arr = new ArrayList<>();

        try {
            JSONObject reader = new JSONObject(this.get("signout/"));
            String status = reader.getString("status");
            String message = reader.getString("message");

            this.cookie = null;
            arr.add(status);
            arr.add(message);

        } catch (JSONException e) {
            arr.add("error");
            arr.add("signout failed. JSON error.");
            e.printStackTrace();
            return arr;
        }

        return arr;
    }

    public ArrayList<Object> getSession() {

        ArrayList<Object> arr = new ArrayList<>();

        try {
            JSONObject reader = new JSONObject(this.get("getSession/"));
            int result = reader.getInt("session_id");

            arr.add("success");
            arr.add("session returned");
            arr.add(result);

        } catch (JSONException e) {
            arr.add("error");
            arr.add("getSession failed. JSON error.");
            e.printStackTrace();
            return arr;
        }

        return arr;
    }

    public ArrayList<Object> signup(
            String username,
            String password,
            String mail,
            String name,
            String surname,
            String country,
            String phoneNumber,
            Date dateOfBirth
    ) {

        //Temp variables
        String lon = "4.000";
        String lat = "4.000";
        String onlineState = "0";
        String profilePicture = "default.jpg";
        ////////////

        ArrayList<Object> arr = new ArrayList<>();
        String json = "{"
                + "\"username\": \"" + username + "\","
                + "\"password\": \"" + password + "\","
                + "\"mail\": \"" + mail + "\","
                + "\"name\": \"" + name + "\","
                + "\"surname\": \"" + surname + "\","
                + "\"country\": \"" + country + "\","
                + "\"phoneNumber\": \"" + phoneNumber + "\","
                + "\"dateOfBirth\": \"" + (new SimpleDateFormat("yyyy-MM-dd")).format(dateOfBirth) + "\","
                + "\"long\": \"" + lon + "\","
                + "\"lat\": \"" + lat + "\","
                + "\"onlineState\": \"" + onlineState + "\","
                + "\"profilePicture\": \"" + profilePicture + "\""
                + "}";

        System.out.println(json);

        try {
            JSONObject reader = new JSONObject(this.post("signup/", json));
            String status = reader.getString("status");
            String message = reader.getString("message");

            arr.add(status);
            arr.add(message);

        } catch (JSONException e) {
            arr.add("error");
            arr.add("signup failed. JSON error.");
            e.printStackTrace();
            return arr;
        }

        return arr;
    }


    // result variable needs to be handled
    public ArrayList<Object> suggestionListIndex() {

        ArrayList<Object> arr = new ArrayList<>();

        try {
            JSONObject reader = new JSONObject(this.get("suggestionListIndex/"));

            JSONArray result = reader.optJSONArray("suggestionList");
            String status = reader.getString("status");
            String message = reader.getString("message");

            arr.add(status);
            arr.add(message);
            arr.add(result);

        } catch (JSONException e) {
            arr.add("error");
            arr.add("suggestionListIndex failed. JSON error.");
            e.printStackTrace();
            return arr;
        }

        return arr;
    }

}
