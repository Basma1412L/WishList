package com.example.basma.wishlist;


import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

import pub.devrel.easypermissions.EasyPermissions;

public class RegisterationActivity extends AppCompatActivity {

    EditText name;
    EditText email;
    Spinner gender;
    EditText faculty;
    Spinner years;

    ImageView profilePic;
    String imageURI;

    String name_str;
    String email_str;
    String gender_str;
    String faculty_str;
    String years_str;
    String uid;
    private static int RESULT_LOAD_IMAGE = 1;

    Button register;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    FirebaseDatabase database;
    DatabaseReference myRef;


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

            profilePic.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            Bitmap bitmap=BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

            imageURI = imageEncoded;

        }




    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email_str = user.getEmail();
            uid= user.getUid();


        }
      /*  else
        {
            Intent intent = new Intent(RegisterationActivity.this,LoginActivity.class);
            startActivity(intent);
        }
*/
        name = (EditText)findViewById(R.id.editTextName);
        profilePic=(ImageView)findViewById(R.id.ppView);
        gender=(Spinner) findViewById(R.id.editTextGender);
        faculty=(EditText)findViewById(R.id.editTextFaculty);
        years=(Spinner)findViewById(R.id.editTextYear);
        register=(Button)findViewById(R.id.button2);



        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(RegisterationActivity.this, galleryPermissions)) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else {
                    EasyPermissions.requestPermissions(RegisterationActivity.this, "Access for storage", 101, galleryPermissions);
                }

            }
        });

        database = FirebaseDatabase.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name_str=name.getText().toString().trim();
                //   email_str=email.getText().toString();
                gender_str=gender.getSelectedItem().toString();
                faculty_str=faculty.getText().toString();
                years_str=years.getSelectedItem().toString();

                final  DatabaseReference pRef = database.getReference("User");
                String id = pRef.push().getKey();
                User p = new User(uid,name_str,email_str,gender_str,faculty_str,years_str,imageURI);
                pRef.child(uid).setValue(p);

                Intent intent = new Intent(RegisterationActivity.this,UserProfileActivity.class);
                startActivity(intent);
                finish();



            }
        });



    }
}
