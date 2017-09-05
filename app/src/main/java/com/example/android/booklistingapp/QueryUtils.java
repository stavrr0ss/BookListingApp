package com.example.android.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bogdan on 6/26/2017.
 */

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static String author;


    private QueryUtils() {

    }

    public static List<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with the HTTP request .", e);
        }
        List<Book> books = extractFeaturesFromJson(jsonResponse);
        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem with creating the URL.", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON response .", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Book> extractFeaturesFromJson(String bookJson) {
        if (TextUtils.isEmpty(bookJson)) {
            return null;
        }
        List<Book> books = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(bookJson);
            JSONArray booksArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < booksArray.length(); i++) {
                JSONObject currentBook = booksArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                String title = volumeInfo.getString("title");

                if (volumeInfo.has("authors")){
                    JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                    author = authorsArray.getString(0);
                }
                JSONObject imagelinks = volumeInfo.getJSONObject("imageLinks");
                String imageUrl = imagelinks.getString("thumbnail");

                Book book = new Book(title, author, imageUrl);
                books.add(book);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error parsing the Json response .", e);
        }
        return books;
    }
}

