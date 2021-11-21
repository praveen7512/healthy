package com.example.healthit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class NewUserActivity extends AppCompatActivity {

    private Button nextBtn, backBtn;
    private EditText nameEdit, emailEdit, passEdit;
    private Spinner ageSpin, gendSpin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        nextBtn = findViewById(R.id.next_btn);
        mAuth = FirebaseAuth.getInstance();
        nameEdit = findViewById(R.id.edit_name);
        ageSpin = findViewById(R.id.age_spin);
        gendSpin = findViewById(R.id.gender_spin);
        emailEdit = findViewById(R.id.edit_email);
        passEdit = findViewById(R.id.editText2);
        progressBar = findViewById(R.id.progressBar);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });



        // Gender Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        gendSpin.setAdapter(adapter);

        // Age spinner
        List age = new ArrayList<Integer>();
        for (int i = 18; i <= 100; i++) {
            age.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                NewUserActivity.this, android.R.layout.simple_spinner_item, age);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        Spinner spinner = (Spinner)findViewById(R.id.age_spin);
        spinner.setAdapter(spinnerArrayAdapter);

    }


    private void registerUser() {
        String name = nameEdit.getText().toString().trim();
        String age = ageSpin.getSelectedItem().toString();
        String gender = gendSpin.getSelectedItem().toString();
        String email = emailEdit.getText().toString().trim();
        String pass = passEdit.getText().toString().trim();

        if (name.isEmpty()) {
            nameEdit.setError("Full name is required!");
            nameEdit.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailEdit.setError("Email is required!");
            emailEdit.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdit.setError("Please provide valid email!");
            emailEdit.requestFocus();
            return;
        }

        if (pass.isEmpty()){
            passEdit.setError("Password is required!");
            passEdit.requestFocus();
            return;
        }

        if (pass.length() < 6){
            passEdit.setError("Minimum password length should be 6 characters");
            passEdit.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name, email, age, gender);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(NewUserActivity.this,"User is registerd",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }
                            });

                        }
                        else{
                            Toast.makeText(NewUserActivity.this, "Failed to Register", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}