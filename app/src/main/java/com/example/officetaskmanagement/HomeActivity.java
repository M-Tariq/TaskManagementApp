package com.example.officetaskmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.officetaskmanagement.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.zip.Inflater;


public class HomeActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    Button saveBtn;
    FloatingActionButton fabBtn;
    private Toolbar toolbar;
    private EditText titleInput, noteInput;

    String uId;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser firebaseUser;

    private RecyclerView recyclerView;


    //update input fields
    private EditText titleUPD;
    private EditText noteUPD;
    private Button btnUpdateUPD;
    private Button btnDeleteUPD;
    private String postKey, title, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAuth=FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();
        uId=firebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("TasksNote").child(uId);
        mDatabase.keepSynced(true);

        toolbar=(Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Task App");

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "tariq");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        //Recyclerview...
        recyclerView=findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        fabBtn=findViewById(R.id.floatingActionButton);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Floatin Button Clicked.", Toast.LENGTH_SHORT).show();


                AlertDialog.Builder mDialogBuilder=new AlertDialog.Builder(HomeActivity.this);
                LayoutInflater layoutInflater=LayoutInflater.from(HomeActivity.this);
                View myview= layoutInflater.inflate(R.layout.custom_input_field, null);

                mDialogBuilder.setView(myview);
                final AlertDialog alertDialog=mDialogBuilder.create();
                saveBtn=myview.findViewById(R.id.save_btn);
                titleInput=myview.findViewById(R.id.edt_title);
                noteInput=myview.findViewById(R.id.edt_note);


                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title, note;
                        title=titleInput.getText().toString();
                        note=noteInput.getText().toString();
                        if (TextUtils.isEmpty(title)){
                            titleInput.setError("Title can't be empty.");
                            return;
                        }
                        if(TextUtils.isEmpty(note)){
                            noteInput.setError("Note can't be empty.");
                            return;
                        }


                String id=mDatabase.push().getKey();
                String _date= DateFormat.getDateInstance().format(new Date());
                Data data=new Data(title, note,id , _date);
                        mDatabase.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Data Inserted Successfully.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Error Occured.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        Log.d("title:", title);
                        Log.d("note:", note);

                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout){
            mAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Data, MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>(
                Data.class,
                R.layout.item_data,
                MyViewHolder.class,
                mDatabase
        ){

            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder, final Data data, final int i) {
                myViewHolder.setTitle(data.getTitle());
                myViewHolder.setNote(data.getNote());
                myViewHolder.setDate(data.getDate());
                myViewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postKey=getRef(i).getKey();
                        title=data.getTitle();
                        note=data.getNote();
                        updateData();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

        }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView=itemView;
        }
        public void setTitle(String title){
            TextView mTitle=myView.findViewById(R.id.title);
            mTitle.setText(title);
        }
        public void setNote(String note){
            TextView mNote=myView.findViewById(R.id.note);
            mNote.setText(note);
        }
        public void setDate(String date){
            TextView mDate=myView.findViewById(R.id.date);
            mDate.setText(date);
        }
    }

    public void updateData(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater layoutInflater=LayoutInflater.from(HomeActivity.this);
        View myView=layoutInflater.inflate(R.layout.update_input_field, null);
        builder.setView(myView);
        final AlertDialog alertDialog=builder.create();

        titleUPD=myView.findViewById(R.id.edt_title_update);
        noteUPD=myView.findViewById(R.id.edt_note_update);
        btnUpdateUPD=myView.findViewById(R.id.upd_btn);
        btnDeleteUPD=myView.findViewById(R.id.del_btn);
        titleUPD.setText(title);
        titleUPD.setSelection(title.length());
        noteUPD.setText(note);
        noteUPD.setSelection(note.length());

        btnDeleteUPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(postKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(HomeActivity.this, "Data Deletd succussfully.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(HomeActivity.this, "Data Not Deletd.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertDialog.dismiss();
            }
        });
        btnUpdateUPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String title=titleUPD.getText().toString().trim();
               String note=noteUPD.getText().toString().trim();
                String _date= DateFormat.getDateInstance().format(new Date());
                Data data=new Data(title, note,postKey , _date);
                mDatabase.child(postKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(HomeActivity.this, "Data Updated Successfully.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(HomeActivity.this, "Data not updated.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

               alertDialog.dismiss();

            }
        });

        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onStart();
    }
}
