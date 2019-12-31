package com.example.officetaskmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegistrationActivity extends AppCompatActivity {
    EditText edtEmailReg, edtPassReg;
    String email=null, pass=null;

    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtEmailReg =findViewById(R.id.edt_email_signup_id);
        edtPassReg=findViewById(R.id.edt_password_signup_id);
        progressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
    }

    public void onClick(View view) {
        if(view.getId()==R.id.btn_register_signupActivity){
            Toast.makeText(this, "Signup from signup Clicked", Toast.LENGTH_SHORT).show();
            email= edtEmailReg.getText().toString().trim();
            pass=edtPassReg.getText().toString().trim();
            if(TextUtils.isEmpty(email)){
                edtEmailReg.setError("Email Reuired.");
            }
            if(TextUtils.isEmpty(pass)){
                edtPassReg.setError("Password Reuired.");
            }
            else{
                progressDialog.setMessage("Processing...");
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){

                            Toast.makeText(getApplicationContext(), "Registration Successfull", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(RegistrationActivity.this, "Some Error Occured.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            edtEmailReg.setError("Email already registered.");
                            Toast.makeText(RegistrationActivity.this, "This email address is already registered.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else if(view.getId()==R.id.btn_login_signupActivity){
            Toast.makeText(this, "Login from signup Clicked", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
