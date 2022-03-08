package com.example.yogabiofeedback;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

    public class SignIn extends AppCompatActivity {
        String[] tips={"Hello ","How are you", "Happy"};
        TextView tip;
        int i=0;
        int l=tip.length();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_in);
        }
}
