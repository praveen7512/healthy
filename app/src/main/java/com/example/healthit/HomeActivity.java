package com.example.healthit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    private static final String[] disease = new String[]{
      "Insomnia", "Migraine", "Diabetes", "Alzheimer"
    };

    private StorageReference mStorageReference;
    private ImageView imageview;
    private ListView listView;
    private EditText searchField;

    private FirebaseUser user;
    private String userId;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                            //Toast.makeText(HomeActivity.this,"Image Retrieved",Toast.LENGTH_LONG).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ((ImageView)findViewById(R.id.imageview)).setImageBitmap(bitmap);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HomeActivity.this,"Error in retrieving image",Toast.LENGTH_LONG).show();
                }
            });
        }  catch (IOException e){
            e.printStackTrace();
        }



        AutoCompleteTextView editText = findViewById(R.id.search_edit);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, disease);
        editText.setAdapter(adapter);

        editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String str = editText.getText().toString();

                if(editText.getText().toString().equals("Insomnia")) {
                    Intent intent = new Intent(HomeActivity.this,InsomniaActivity.class);
                    startActivity(intent);
                }

                else{
                    Toast.makeText(HomeActivity.this,"Noooooo",Toast.LENGTH_LONG).show();
                }

//                if(str.equals("Migraine")) {
//                    Toast.makeText(HomeActivity.this,"Nooooooo!!!!",Toast.LENGTH_LONG).show();
//                }
            }
        });

        imageview = findViewById(R.id.imageview);

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,UserProfile.class);
                startActivity(intent);
            }
        });
    }
}