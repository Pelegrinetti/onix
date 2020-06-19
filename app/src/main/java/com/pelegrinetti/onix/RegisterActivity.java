package com.pelegrinetti.onix;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    EditText txtName;
    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadWidgets();

        setUsername();
    }

    private void loadWidgets() {
        txtName = (EditText) findViewById(R.id.username);
        btnContinue = (Button) findViewById(R.id.btn_register);
    }

    @SuppressLint("CommitPrefEdits")
    private void setUsername () {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", MODE_PRIVATE).edit();

                editor.putString("username", txtName.getText().toString());

                editor.apply();

                setResult(RESULT_OK);
                finish();
            }
        });
    }
}