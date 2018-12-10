package com.dam.asfaltame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginRegisterActivity extends AppCompatActivity {
    final int OPTION_LOGIN = 1;
    final int OPTION_REGISTER = 2;

    private Button loginButton;
    private TextView loginText1;
    private TextView loginText2;
    private TextView emailText;
    private EditText emailInput;

    private int option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        getViews();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        option = extras.getInt("LoginRegisterOption");
        switch(option){
            case OPTION_LOGIN:
                loginButton.setText(R.string.loginButton);
                loginText1.setText(R.string.registerText1);
                loginText2.setText(R.string.registerText2);
                emailText.setVisibility(View.GONE);
                emailInput.setVisibility(View.GONE);
                break;
            case OPTION_REGISTER:
                loginButton.setText(R.string.registerButton);
                loginText1.setText(R.string.loginText1);
                loginText2.setText(R.string.loginText2);
                break;
        }

        loginText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginRegisterActivity.this, LoginRegisterActivity.class);
                switch(option){
                    case OPTION_LOGIN:
                        i.putExtra("LoginRegisterOption", OPTION_REGISTER);
                        break;
                    case OPTION_REGISTER:
                        i.putExtra("LoginRegisterOption", OPTION_LOGIN);
                        break;
                }
                startActivity(i);
                finish();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginRegisterActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void getViews(){
        loginButton = findViewById(R.id.login_button);
        loginText1 = findViewById(R.id.login_text_1);
        loginText2 = findViewById(R.id.login_text_2);
        emailText = findViewById(R.id.login_email);
        emailInput = findViewById(R.id.login_email_input);
    }
}
