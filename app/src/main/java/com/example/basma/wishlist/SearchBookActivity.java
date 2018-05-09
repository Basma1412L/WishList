package com.example.basma.wishlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchBookActivity extends AppCompatActivity {

    EditText bookName;
    String bookName_str;
    Button search;
    ListView searchListView;
    private FirebaseAuth mAuth;
    List<searchItem> resultedUsers=new ArrayList<>();
    searchList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        bookName = (EditText)findViewById(R.id.editText);
        search=(Button)findViewById(R.id.button3);
        searchListView = (ListView)findViewById(R.id.booksList);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        adapter = new searchList(SearchBookActivity.this,resultedUsers);
        searchListView.setAdapter(adapter);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookName_str=bookName.getText().toString().trim();
                DatabaseReference myRef = database.getReference("User");

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean bookFound = false;
                        resultedUsers.clear();
                        for(DataSnapshot data : dataSnapshot.getChildren())
                        {

                            DataSnapshot ds=data.child("books");
                            for (DataSnapshot dsBook: ds.getChildren()) {
                                Book b = dsBook.getValue(Book.class);

                                String book_name = b.getBook_Name();
                                String book_status = b.getBook_Status();
                                if(bookName_str.equals(book_name)) {

                                    bookFound = true;
                                    User profileUser=data.getValue(User.class);
                                    String nameR = data.child("name").getValue(String.class);
                                    String emailR = data.child("email").getValue(String.class);
                                    String yearR = data.child("years").getValue(String.class);
                                    String idR=data.child("id").getValue(String.class);
                                    String genderR = data.child("gender").getValue(String.class);
                                    Toast.makeText(SearchBookActivity.this, " user found"+nameR, Toast.LENGTH_SHORT).show();

                                    searchItem current = new searchItem(profileUser,book_status);
                                    resultedUsers.add(current);

                                }


                            }

                            adapter.notifyDataSetChanged();



                        }
                        if(!bookFound){
                            Toast.makeText(SearchBookActivity.this, "No user found", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





            }
        });
    }
}
