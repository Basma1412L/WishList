package com.example.basma.wishlist;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BooksList extends ArrayAdapter {



    private Activity context;
    private List<Book> booksList;

    @NonNull
    @Override
    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public List<Book> getBooksList() {
        return booksList;
    }

    public void setBooksList(List<Book> booksList) {
        this.booksList = booksList;
    }

    public BooksList(@NonNull Context context, int resource, Activity context1, List<Book> booksList) {
        super(context, resource);
        this.context = context1;
        this.booksList = booksList;
    }

    public BooksList(@NonNull Context context, int resource, int textViewResourceId, Activity context1, List<Book> booksList) {
        super(context, resource, textViewResourceId);
        this.context = context1;
        this.booksList = booksList;
    }

    public BooksList(@NonNull Context context, int resource, @NonNull Object[] objects, Activity context1, List<Book> booksList) {
        super(context, resource, objects);
        this.context = context1;
        this.booksList = booksList;
    }

    public BooksList(@NonNull Context context, int resource, int textViewResourceId, @NonNull Object[] objects, Activity context1, List<Book> booksList) {
        super(context, resource, textViewResourceId, objects);
        this.context = context1;
        this.booksList = booksList;
    }

    public BooksList(@NonNull Context context, int resource, @NonNull List objects, Activity context1, List<Book> booksList) {
        super(context, resource, objects);
        this.context = context1;
        this.booksList = booksList;
    }

    public BooksList(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects, Activity context1, List<Book> booksList) {
        super(context, resource, textViewResourceId, objects);
        this.context = context1;
        this.booksList = booksList;
    }


    public BooksList(Activity context1, List<Book> booksList) {
        super(context1, R.layout.user_book,booksList);
        this.context = context1;
        this.booksList = booksList;
    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View  listViewItem = inflater.inflate(R.layout.user_book,null,true);

        final   TextView txtName=(TextView)listViewItem.findViewById(R.id.bookName);
        final  TextView txtStatus=(TextView)listViewItem.findViewById(R.id.bookStatus);
        Button changeStatusBtn = (Button)listViewItem.findViewById(R.id.editBtn);
        Button deleteBtn = (Button)listViewItem.findViewById(R.id.delBtn);

        final Book book =booksList.get(position);

        txtName.setText(book.getBook_Name());
        txtStatus.setText(book.getBook_Status());

        changeStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater=context.getLayoutInflater();
                final View displayView = inflater.inflate(R.layout.add_book,null);
                dialogBuilder.setView(displayView);

                final EditText bookName = (EditText)displayView.findViewById(R.id.editTextName);
                final Spinner status = (Spinner)displayView.findViewById(R.id.statusSpinner);
                Button register=(Button)displayView.findViewById(R.id.doneBtn);

                dialogBuilder.setTitle("Updating book status");

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String n =bookName.getText().toString().trim();
                        String g =status.getSelectedItem().toString();


                        if(!TextUtils.isEmpty(n))
                        {

                            Book bookUpdated = new Book(n,g);
                            booksList.set(position,bookUpdated);
                            notifyDataSetChanged();

                            //txtName.setText(bookUpdated.getBook_Name());
                            //txtStatus.setText(bookUpdated.getBook_Status());
                            alertDialog.dismiss();
                        }

                    }
                });






            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                booksList.remove(book);
                notifyDataSetChanged();

            }
        });

        return listViewItem;
    }









}

