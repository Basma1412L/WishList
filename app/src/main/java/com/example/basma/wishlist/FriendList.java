package com.example.basma.wishlist;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FriendList extends ArrayAdapter {


    private FirebaseAuth mAuth;
    FirebaseDatabase database ;

    private Activity context;
    private List<Book> searchList;
    private String userName;

    public FriendList(Activity context1, List<Book> searchList) {
        super(context1, R.layout.book_item_friend,searchList);
        this.context = context1;
        this.searchList = searchList;
    }

    public FriendList(Activity context1,String userName, List<Book> searchList) {
        super(context1, R.layout.book_item_friend,searchList);
        this.context = context1;
        this.searchList = searchList;
        this.userName=userName;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View  listViewItem = inflater.inflate(R.layout.book_item_friend,null,true);

        final TextView txtName=(TextView)listViewItem.findViewById(R.id.bookName2);
        final  TextView txtStatus=(TextView)listViewItem.findViewById(R.id.bookStatus2);
        // Button viewProfile = (Button)listViewItem.findViewById(R.id.viewprofile);
        Button exchange = (Button)listViewItem.findViewById(R.id.exchange2);
        final Book book =searchList.get(position);

        txtName.setText(book.getBook_Name());
        txtStatus.setText(book.getBook_Status());




        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                /////




                final   AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = context.getLayoutInflater();
                final View displayView = inflater.inflate(R.layout.exchange_source, null);
                dialogBuilder.setView(displayView);
                final Spinner requested = (Spinner) displayView.findViewById(R.id.mybooks);
                final Spinner exchanged = (Spinner) displayView.findViewById(R.id.friendbooks);
                String selectedRequest="";
                Button register = (Button) displayView.findViewById(R.id.doneBtn);


                mAuth = FirebaseAuth.getInstance();

                final  FirebaseUser currentUser = mAuth.getCurrentUser();
                String userID = currentUser.getUid();
                final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference ref = database.child("User").child(userID);




                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean userFound = false;
                        User profileUser = dataSnapshot.getValue(User.class);
                        List<Book> userBooks = profileUser.getBooks();
                        int size = userBooks.size();
                        String[] arraySpinner = new String[size];
                        for (int i = 0; i < size; i++) {
                            arraySpinner[i] = userBooks.get(i).getBook_Name();
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arraySpinner);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        exchanged.setAdapter(adapter);


                        int size2 = searchList.size();
                        String[] requestedArray=new String[size2];

                        if (size2==0)
                        {
                            for (int i = 0; i < 4; i++) {
                                requestedArray[i] ="Nothing";
                            }
                        }
                        else
                        {
                            for (int i = 0; i < size2; i++) {
                                requestedArray[i] = searchList.get(i).getBook_Name();
                            }
                        }

                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, requestedArray);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        requested.setAdapter(adapter2);
                        dialogBuilder.setTitle("Updating book status");
                        userFound = true;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();




                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef2 = database.getReference("User");

                        myRef2.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                for(DataSnapshot data : dataSnapshot.getChildren())
                                {
                                    String usernameR = data.child("name").getValue(String.class);
                                    if(usernameR.equals(userName)) {


                                        DataSnapshot ds=data.child("books");
                                        for (DataSnapshot dsBook: ds.getChildren()) {
                                            Book b = dsBook.getValue(Book.class);
                                            if(b.getBook_Name().equals(requested.getSelectedItem().toString()))
                                            { b.setBook_Status("Requested");
                                            dsBook.getRef().setValue(b);
                                            break;}
                                        }
                                        alertDialog.dismiss();


                                    }
                                }



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


/*
                        final DatabaseReference ref2 = database.child("User").child(userID);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                User profileUser = dataSnapshot.getValue(User.class);
                                List<Book>userBooks=profileUser.getBooks();
                               String requestedBook=requested.getSelectedItem().toString();
                                for (Book b: userBooks)
                                {
                                    if(b.getBook_Name().equals(requestedBook))
                                    {
                                        b.setBook_Status("Reqested");
                                        b.setExchanger_name(currentUser.getDisplayName());
                                        break;
                                    }

                                }
                                alertDialog.dismiss();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

*/



                    }
                });





                //  database = FirebaseDatabase.getInstance();




                //////


            }
        });

        return listViewItem;
    }
}




   /*     changeStatusBtn.setOnClickListener(new View.OnClickListener() {
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

        */



