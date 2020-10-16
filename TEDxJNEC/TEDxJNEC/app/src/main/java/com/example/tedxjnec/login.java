package com.example.tedxjnec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.zip.Inflater;

public class login extends AppCompatActivity {



    EditText email;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    EditText pass;
    Animation shakeAnimation;

    static String name = " ";
    static String mobNo = " ";
    static String questionNo = "0";
    static String score = "0";
    Animation animation;
    Button logIn;
    ProgressDialog progressDialog;
    static String emailAddress = " ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logIn = findViewById(R.id.login);


        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        shakeAnimation = AnimationUtils.loadAnimation(login.this,R.anim.shake);

        animation = AnimationUtils.loadAnimation(login.this,R.anim.blink);
        progressDialog = new ProgressDialog(login.this,R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
//        if(firebaseAuth.getCurrentUser() != null){
//
//            SharedPreferences prefs = getSharedPreferences("Auth", MODE_PRIVATE);
//            name = prefs.getString("name","TEDxJNEC");
//            score = prefs.getString("score","0");
//            Toast.makeText(login.this,score,Toast.LENGTH_SHORT).show();
//
////            DatabaseReference reference2 = reference.child("Auth").child(firebaseAuth.getCurrentUser().getUid());
////            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
////                @Override
////                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                    try {
////                        HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();
////                        name = map.get("name");
////                        mobNo = map.get("mobNo");
////                        score = map.get("score");
////
////                    }catch (Exception e){
////                        Toast.makeText(login.this, "Unknown error!", Toast.LENGTH_SHORT).show();
////                    }
////                }
////
////                @Override
////                public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                }
////            });
//            Intent intent  = new Intent(login.this,Main2Activity.class);
//            startActivity(intent);
//
//        }


    }




    public void logIn(final View view)
    {


        logIn.startAnimation(animation);

        if(TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(pass.getText())){


            if(TextUtils.isEmpty(email.getText())){
                email.startAnimation(shakeAnimation);

            }
            if(TextUtils.isEmpty(pass.getText())) {

                pass.startAnimation(shakeAnimation);
            }



        }else {
            progressDialog.show();
            logIn.setEnabled(false);
            firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        logIn.setEnabled(true);
                        DatabaseReference reference2 = reference.child("Auth").child(firebaseAuth.getCurrentUser().getUid());
                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();
                                    name = map.get("name");
                                    mobNo = map.get("mobNo");
                                    score = map.get("score");
                                    questionNo = map.get("queNo");
                                    SharedPreferences.Editor editor = getSharedPreferences("Auth", MODE_PRIVATE).edit();
                                    editor.putString("name",name);
                                    editor.putString("mobilNo",mobNo);
                                    editor.putString("score",score);
                                    editor.putString("queNo",questionNo);
                                    editor.putString("email",email.getText().toString());
                                    editor.commit();
                                    Intent intent  = new Intent(login.this,Main2Activity.class);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                    finish();



                                }catch (Exception e){

                                    new CustomToast().Show_Toast(login.this,view,"Failed to logIn!");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    } else {
//                        progressBar1.setVisibility(View.INVISIBLE);
//                        logInVIew.setVisibility(View.INVISIBLE);
                        progressDialog.dismiss();
                        logIn.setEnabled(true);
//                        Toast.makeText(login.this, "Failed", Toast.LENGTH_SHORT).show();
                        new CustomToast().Show_Toast(login.this,view,"Failed to logIn!");



                    }


                }
            });

        }


    }


    public void signUp(View view){

        Intent intent = new Intent(login.this,Registration.class);
        startActivity(intent);

    }



}
