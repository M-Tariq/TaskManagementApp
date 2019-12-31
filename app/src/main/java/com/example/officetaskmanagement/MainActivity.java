package com.example.officetaskmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText edtEmail, edtPass;
    String email=null, pass=null;

    FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtEmail=findViewById(R.id.edt_email_login_id);
        edtPass=findViewById(R.id.edt_password_login_id);
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(this, HomeActivity.class));
        }
        progressDialog=new ProgressDialog(this);
    }



    public void onClick(View view) {

        if(view.getId()==R.id.tv_forgotpass){
            Toast.makeText(this, "Forgot Password Clicked.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ForgotPasswordActivity.class));
            finish();
        }
        if(view.getId()==R.id.btn_login){
            Toast.makeText(this, "Login Clicked", Toast.LENGTH_SHORT).show();

            email=edtEmail.getText().toString().trim();
            pass=edtPass.getText().toString().trim();
            if(TextUtils.isEmpty(email)){
                edtEmail.setError("Email Reuired.");
                return;
            }
            if(TextUtils.isEmpty(pass)){
                edtPass.setError("Password Reuired.");
                return;
            }

            else{
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                           //String userName=mAuth.getCurrentUser().getEmail();
                            Toast.makeText(MainActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            finish();
                        }
                        else{

                            progressDialog.dismiss();
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                edtPass.setError("Invalid Password.");
                                Toast.makeText(MainActivity.this, "Invalid Password.", Toast.LENGTH_SHORT).show();
                            }
                            else if(task.getException() instanceof FirebaseAuthInvalidUserException){
                                edtEmail.setError("This Email address is not Register.");
                                Toast.makeText(MainActivity.this, "This Email address is not Register.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
            }
        }
        if(view.getId()==R.id.btn_signup){
            Toast.makeText(this, "Signup Clicked", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        }
        if(view.getId()==R.id.remember_me){
            //sharedpref code here
            CheckBox rememberMe=findViewById(R.id.remember_me);
            if(rememberMe.isChecked())
            Toast.makeText(this, "Remember me checked.", Toast.LENGTH_SHORT).show();
        }
    }
}
