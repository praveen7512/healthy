package com.example.healthit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            // User is signed in
//            // Start home activity
//            startActivity(new Intent(SplashScreen.this, HomeActivity.class));
//        } else {
//            // No user is signed in
//            // start login activity
//            startActivity(new Intent(SplashScreen.this, MainActivity.class));
//        }
//
////         //close splash activity
////        finish();

        Window window = getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        Thread splashTread = new Thread(){

            @Override
            public void run() {
                try {

                    sleep(3000);
//                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                    finish();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        // Start home activity
                        startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                    } else {
                        // No user is signed in
                        // start login activity
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    }

         //close splash activity
        finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }

        };
        splashTread.start();
    }
}