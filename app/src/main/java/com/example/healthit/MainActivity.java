package com.example.healthit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView txt;
    private Button logInBtn;
    private EditText emailEdit, passEdit;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        txt = findViewById(R.id.textView3);
        logInBtn = findViewById(R.id.login_btn);
        emailEdit = findViewById(R.id.editText);
        passEdit = findViewById(R.id.editText2);
        progressBar = findViewById(R.id.progressBar2);

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            // User is signed in
//            // Start home activity
//            startActivity(new Intent(MainActivity.this, HomeActivity.class));
//        } else {
//            // No user is signed in
//            // start login activity
//            startActivity(new Intent(MainActivity.this, NewUserActivity.class));
//        }

        // close splash activity
        //finish();

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
                startActivity(intent);
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin(){
        String email = emailEdit.getText().toString().trim();
        String password = passEdit.getText().toString().trim();

        if(email.isEmpty()){
            emailEdit.setError("Email is required!");
            emailEdit.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdit.setError("Please enter a valid email!");
            emailEdit.requestFocus();
            return;
        }

        if (password.isEmpty()){
            passEdit.setError("Password is required!");
            passEdit.requestFocus();
            return;
        }

        if (password.length() < 6){
            passEdit.setError("Minimum password length should be 6 characters");
            passEdit.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    // redirect to user profile
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this,"Welcome to Healthit !", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);

                }
                else{
                    Toast.makeText(MainActivity.this,"Failed to login! Please check your credentiala", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
