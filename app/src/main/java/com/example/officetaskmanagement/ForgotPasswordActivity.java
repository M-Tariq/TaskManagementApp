package com.example.officetaskmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText edtEmail;
    String email;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        edtEmail=findViewById(R.id.edt_email_forgot_id);
        mAuth=FirebaseAuth.getInstance();
    }

    public void sendLink(View view) {
        email=edtEmail.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            edtEmail.setError("Email can't be empty.");
            return;
        }
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Check your email address.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                    Toast.makeText(ForgotPasswordActivity.this, "This email address is not registered.", Toast.LENGTH_SHORT).show();
                    edtEmail.setError("Invalid email.");
                }
            }
        });
    }
    public void goToMain(View view){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
