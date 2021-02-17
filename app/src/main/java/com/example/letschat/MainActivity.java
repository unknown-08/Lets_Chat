package com.example.letschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {



    static FirebaseAuth firebaseAuth;
    private TextInputEditText emaileditText,passwordeditText;
    private TextView signUpTextView;
    private Button loginButton;
    private FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference reference;
    private TextView forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        emaileditText=findViewById(R.id.emailinputedittetxt);
        passwordeditText=findViewById(R.id.passwordinputedittext);
        loginButton=findViewById(R.id.loginButton);
        signUpTextView=findViewById(R.id.signuptextview);
        forgot_password=findViewById(R.id.forgot_password);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ResetPasswordActivity.class));
            }
        });

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()!=null){

                    Intent intent=new Intent(MainActivity.this,UsefeedActivity.class);
                    startActivity(intent);
                }
            }
        };


        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emaileditText.getText().toString();
                String password = passwordeditText.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

                    Toast.makeText(MainActivity.this, "Enter Email And Password", Toast.LENGTH_LONG).show();
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {

                                Toast.makeText(MainActivity.this, "Successfully Login", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
            }
        });

    }

    protected void onStart() {

        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}