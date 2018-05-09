package com.example.basma.wishlist;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class UserProfileActivity extends AppCompatActivity {

    Button searchBtn;
    private FirebaseAuth mAuth;
    FirebaseDatabase database ;
    String email="";
    TextView nameTxt;
    TextView emailTxt;
    TextView yearTxt;
    TextView facultyTxt;
    Button updateProfile;
    String   nameR;
    String   facultyR;
    String idR;
    String genderR;
    String yearR;
    String user_emailR;
    String encodedImage;
    User profileUser;
    ListView booksList;
    List<Book> userBooks=new ArrayList<>();
    Button addBook;
    ImageView profilePic;
    BooksList adapter;
    Button saveChanges;
    String imageUpdatedURI="";
    private static int RESULT_LOAD_IMAGE = 1;

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ppdialog.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            Bitmap bitmap=BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            String imageEncoded2 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

            imageUpdatedURI = imageEncoded2;

        }




    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        booksList=(ListView)findViewById(R.id.booksList);
        nameTxt=(TextView)findViewById(R.id.nameTxt);
        emailTxt=(TextView)findViewById(R.id.emailTxt);
        yearTxt=(TextView)findViewById(R.id.yearTxt);
        facultyTxt=(TextView)findViewById(R.id.facultyTxt);
        updateProfile=(Button)findViewById(R.id.button5);
        addBook = (Button)findViewById(R.id.button);
        searchBtn=(Button)findViewById(R.id.goSearch);
        profilePic=(ImageView)findViewById(R.id.imageView);
        saveChanges=(Button)findViewById(R.id.button7);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = database.getReference("User");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        adapter = new BooksList(UserProfileActivity.this,userBooks);
        booksList.setAdapter(adapter);
        if(currentUser==null)
        {
            Intent i = new Intent(UserProfileActivity.this,SignUpActivity.class);
            startActivity(i);
        }
        else
        {
            email = currentUser.getEmail();
        }

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(UserProfileActivity.this, galleryPermissions)) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else {
                    EasyPermissions.requestPermissions(UserProfileActivity.this, "Access for storage", 101, galleryPermissions);
                }

            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> bookInAdapter = adapter.getBooksList();
                userBooks=bookInAdapter;
                updateUser(idR,nameR,user_emailR,genderR,facultyR,yearR,userBooks);

            }
        });
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showUpdateDialog(idR,nameR);

            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this,SearchBookActivity.class);
                startActivity(intent);
            }
        });

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showaddBookDialog();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean userFound = false;

                userBooks.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    user_emailR = data.child("email").getValue(String.class);
                    if(email.equals(user_emailR)) {
                        profileUser=data.getValue(User.class);
                        Toast.makeText(UserProfileActivity.this, " user found"+ user_emailR, Toast.LENGTH_SHORT).show();
                        userFound = true;
                        nameR = data.child("name").getValue(String.class);
                        facultyR = data.child("faculty").getValue(String.class);
                        yearR = data.child("years").getValue(String.class);
                        idR=data.child("id").getValue(String.class);
                        genderR = data.child("gender").getValue(String.class);
                        encodedImage = data.child(("profilePicture")).getValue(String.class);
                        nameTxt.setText(nameR);
                        emailTxt.setText(user_emailR);
                        facultyTxt.setText(facultyR);
                        yearTxt.setText(yearR);

                        try
                        {
                            Bitmap imageBitmap = decodeFromFirebaseBase64(encodedImage);
                            profilePic.setImageBitmap(imageBitmap);
                        }
                        catch (Exception e)
                        {

                        }

                        DataSnapshot ds=data.child("books");
                        for (DataSnapshot dsBook: ds.getChildren()) {
                            Book b = dsBook.getValue(Book.class);
                            userBooks.add(b);
                            //       String bookName=dsBook.child("book_Name").getValue().toString();
                            //  userBooks.add()
                        }
                        adapter.notifyDataSetChanged();





                    }

                }
                if(!userFound){
                    Toast.makeText(UserProfileActivity.this, "No user found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void showaddBookDialog()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater= getLayoutInflater();
        final View displayView = inflater.inflate(R.layout.add_book,null);
        dialogBuilder.setView(displayView);

        final EditText bookName = (EditText)displayView.findViewById(R.id.editTextName);
        final Spinner   status = (Spinner)displayView.findViewById(R.id.statusSpinner);
        Button register=(Button)displayView.findViewById(R.id.doneBtn);



        dialogBuilder.setTitle("Adding a new book:");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String n =bookName.getText().toString().trim();
                String g =status.getSelectedItem().toString();


                if(!TextUtils.isEmpty(n))
                {
                    Book book = new Book(n,g);
                    userBooks.add(book);
                    // profileUser
                    if(imageUpdatedURI.equals(""))
                        updateUser(idR,nameR,user_emailR,genderR,facultyR,yearR,userBooks);
                    adapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                }

                else
                {
                    bookName.setError("Name Required");
                    return;
                }
            }
        });





    }
    ImageView ppdialog;

    private void showUpdateDialog(final String userId,String userName)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater= getLayoutInflater();
        final View displayView = inflater.inflate(R.layout.update_user_dialog,null);
        dialogBuilder.setView(displayView);

        final EditText namedialog = (EditText)displayView.findViewById(R.id.editTextName);
        final   EditText emaildialog = (EditText)displayView.findViewById(R.id.editTextEmail);
        final   Spinner  genderdialog=(Spinner)displayView. findViewById(R.id.editTextGender);
        final EditText facultydialog=(EditText)displayView.findViewById(R.id.editTextFaculty);
        final  Spinner   yearsdialog=(Spinner)displayView.findViewById(R.id.editTextYear);
        ppdialog = (ImageView)displayView.findViewById(R.id.ppView);
        Button registerdialog=(Button)displayView.findViewById(R.id.doneBtn);
        try
        {
            Bitmap imageBitmap = decodeFromFirebaseBase64(encodedImage);
            ppdialog.setImageBitmap(imageBitmap);
        }
        catch (Exception e)
        {

        }
        ppdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(UserProfileActivity.this, galleryPermissions)) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else {
                    EasyPermissions.requestPermissions(UserProfileActivity.this, "Access for storage", 101, galleryPermissions);
                }

            }
        });


        namedialog.setText(nameR);
        emaildialog.setText(user_emailR);
        facultydialog.setText(facultyR);


        dialogBuilder.setTitle("Updating Artist:" + userName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        registerdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String n =namedialog.getText().toString().trim();
                String g =genderdialog.getSelectedItem().toString();
                String e =emaildialog.getText().toString().trim();
                String f =facultydialog.getText().toString().trim();
                String y =yearsdialog.getSelectedItem().toString();


                if(!TextUtils.isEmpty(n))
                {

                    if(imageUpdatedURI.equals(""))
                        updateUser(userId,n,e,g,f,y);
                    else
                        updateUser(userId,n,e,g,f,y,imageUpdatedURI);

                    alertDialog.dismiss();
                }

                else
                {
                    namedialog.setError("Name Required");
                    return;
                }
            }
        });





    }


    private boolean updateUser(String id, String name_str, String email_str, String gender_str, String faculty_str, String years_str,String imageUpdated)
    {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User").child(id);
        User user = new User(id,name_str,email_str,gender_str,faculty_str,years_str,imageUpdated,userBooks);
        dbRef .setValue(user);
        Toast.makeText(this,"User Updated",Toast.LENGTH_SHORT).show();
        return true;
    }


    private boolean updateUser(String id, String name_str, String email_str, String gender_str, String faculty_str, String years_str)
    {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User").child(id);
        User user = new User(id,name_str,email_str,gender_str,faculty_str,years_str,userBooks);

        dbRef .setValue(user);

        Toast.makeText(this,"User Updated",Toast.LENGTH_SHORT).show();

        return true;


    }


    private boolean updateUser(String id, String name_str, String email_str, String gender_str, String faculty_str, String years_str, List<Book>userBooks)
    {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User").child(id);
        User user = new User(id,name_str,email_str,gender_str,faculty_str,years_str,encodedImage,userBooks);

        dbRef .setValue(user);

        Toast.makeText(this,"User Updated",Toast.LENGTH_SHORT).show();

        return true;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
            {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(UserProfileActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
                return true;}
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
