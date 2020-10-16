package com.example.tedxjnec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registration extends AppCompatActivity {


    EditText email;
    EditText name;
    EditText mobNo;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    EditText pass;
    HashMap<String,String> map;
    Animation shakeAnimation;
    ProgressBar progressBar3;
    ProgressDialog progressDialog;
    TextView repassword;
    View view;
   static boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        repassword = findViewById(R.id.repassword);
        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Auth");
        map = new HashMap<>();
        email = findViewById(R.id.txtEmail);
        name = findViewById(R.id.txtName2);
        pass = findViewById(R.id.txtPassword);
        mobNo = findViewById(R.id.txtMob2);
        shakeAnimation = AnimationUtils.loadAnimation(Registration.this,R.anim.shake);
        progressDialog = new ProgressDialog(Registration.this,R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating account...");
    }




    public void registration(final View view) {


        checkInternet();
        if (TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(pass.getText()) || TextUtils.isEmpty(mobNo.getText()) || TextUtils.isEmpty(name.getText()) || !pass.getText().toString().equals(repassword.getText().toString())) {


            if (TextUtils.isEmpty(email.getText())) {
                email.startAnimation(shakeAnimation);

            }
            if (TextUtils.isEmpty(pass.getText())) {

                pass.startAnimation(shakeAnimation);
                repassword.startAnimation(shakeAnimation);
            }
            if (TextUtils.isEmpty(name.getText())) {

                name.startAnimation(shakeAnimation);
            }
            if (TextUtils.isEmpty(mobNo.getText())) {
                mobNo.startAnimation(shakeAnimation);
            }
            if(!pass.getText().toString().equals(repassword.getText().toString())){

                repassword.startAnimation(shakeAnimation);
                new CustomToast().Show_Toast(Registration.this,view,"Please conform your password!");
            }
           if(!connected){
               new CustomToast().Show_Toast(this,view,"Please check internet!");

           }


        } else {

try{
    progressDialog.show();
    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        progressDialog.dismiss();
                        map.put("name", name.getText().toString());
                        map.put("mobNo", mobNo.getText().toString());
                        map.put("score","0");
                        map.put("queNo","0");
                        map.put("email",email.getText().toString());
                        DatabaseReference reference2 = reference.child(firebaseAuth.getCurrentUser().getUid());
                        reference2.setValue(map);
                        Intent intent = new Intent(Registration.this,Main2Activity.class);
                        startActivity(intent);
                        finish();



                    } else {

                        progressDialog.dismiss();


                        new CustomToast().Show_Toast(Registration.this,view,"Failed To Create Account!");

                    }


                }
            });

        }catch (Exception e){

    new CustomToast().Show_Toast(Registration.this,view,"Failed To Create Account!");


        }
    }

    }

    public void checkInternet() {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                //we are connected to a network
                connected = true;
            } else
                connected = false;


        }catch (Exception e){


            System.out.print("ExceptionTHROEW");

        }
    }

}
