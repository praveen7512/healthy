package com.example.healthit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity {

    private Button updateBtn, logoutBtn;
    private EditText nameEdit, passEdit;
    private EditText ageSpin, gendSpin;
    private FirebaseAuth mAuth;
    private TextView emailEdit;
    private ProgressBar progressBar;
    private StorageReference mStorageReference;
    private ImageView imageview;

    private FirebaseUser user;
    private String userId;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //updateBtn = findViewById(R.id.update_btn);
        logoutBtn = findViewById(R.id.logout_btn);
        mAuth = FirebaseAuth.getInstance();
        nameEdit = findViewById(R.id.edit_name);
        ageSpin = findViewById(R.id.age_spin);
        gendSpin = findViewById(R.id.gender_spin);
        emailEdit = findViewById(R.id.edit_email);
        passEdit = findViewById(R.id.editText2);
        progressBar = findViewById(R.id.progressBar);


        // to retrieve profile image

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        mStorageReference = FirebaseStorage.getInstance().getReference().child(userId).child("profile.png");

        try {
            final File localFile = File.createTempFile("image", "png");
            mStorageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(UserProfile.this,"Image Retrieved",Toast.LENGTH_LONG).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ((ImageView)findViewById(R.id.imageview)).setImageBitmap(bitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserProfile.this,"Error in retrieving image",Toast.LENGTH_LONG).show();
                }
            });
        }  catch (IOException e){
            e.printStackTrace();
        }



        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserProfile.this,MainActivity.class));
            }
        });

//        updateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateProfile();
//            }
//        });


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String fullName = userProfile.name;
                    String age = userProfile.age;
                    String gender = userProfile.gender;
                    String email = userProfile.email;

                    nameEdit.setText(fullName);
                    ageSpin.setText(age);
                    gendSpin.setText(gender);
                    emailEdit.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this, "Something went wrong! Don't know what", Toast.LENGTH_SHORT).show();

            }
        });


    }
}