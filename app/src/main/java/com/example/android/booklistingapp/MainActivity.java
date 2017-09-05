package com.example.android.booklistingapp;


import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private static final int BOOK_LOADER_ID = 1;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String BOOK_REQUEST = "https://www.googleapis.com/books/v1/volumes?maxResults=20&intitle&inauthor&q=";
    private TextView mEmptyStateTextView;
    private String searchWord;
    private EditText mSearchWord;

    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isConnected()) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            View progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);

            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            mEmptyStateTextView.setText(R.string.no_internet);
        }

        ListView bookListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);

        mSearchWord = (EditText) findViewById(R.id.search_term);
        final Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()){
                    searchWord = mSearchWord.getText().toString();
                    mSearchWord.setText("");
                    searchWord = searchWord.replace(" ", "+");
                    Log.v(LOG_TAG, "New search word is" + searchWord);
                    getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    mSearchWord.clearFocus();
                } else{

                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setText(R.string.no_internet);
                }
            }
        });
    }
    public boolean isConnected(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public android.content.Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        String bookRequest = "";
        if (searchWord != null) {
            bookRequest = BOOK_REQUEST + searchWord;
        } else {
            String defaultBook = "java";
            bookRequest = BOOK_REQUEST + defaultBook;
            Log.v(LOG_TAG, bookRequest);
        }
        return new BookLoader(this, bookRequest);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Book>> loader, List<Book> books) {
        mAdapter.clear();
        mEmptyStateTextView.setText(R.string.no_books);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}
