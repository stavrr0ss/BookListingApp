package com.example.android.booklistingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Bogdan on 6/26/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private Bitmap bmp ;

    public BookAdapter(Context context, ArrayList<Book> books ){
        super(context,0 , books);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView ;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Book currentBook = getItem(position);
        TextView author = (TextView)listItemView.findViewById(R.id.authors);
        author.setText(currentBook.getAuthors().toString());

        TextView title =(TextView)listItemView.findViewById(R.id.title);
        title.setText(currentBook.getTitle());

        ImageView image = (ImageView)listItemView.findViewById(R.id.image);
        Picasso.with(getContext()).load(currentBook.getImage()).into(image);

        return listItemView;
    }
}
