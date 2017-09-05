package com.example.android.booklistingapp;

/**
 * Created by Bogdan on 6/26/2017.
 */

public class Book {

    private String mTitle;// used to store the title of the book.
    private String mAuthors;// used to store the author od the book.
    private String mImage;//used to store the thumbnail .



    public Book(String title, String authors,String image) {
        mTitle = title;
        mAuthors = authors;
        mImage = image;


    }

    public String getAuthors() {
        return mAuthors;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImage(){return mImage;}


}
