package com.example.tedxjnec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizSection extends AppCompatActivity {


    ImageView imageView;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    ArrayList<RadioButton> radioButtons;
    int answSelected = 10;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    DatabaseReference reference2;
    HashMap<String,String> map;
    ProgressDialog progressDialog;
    Button submitButton;
    DatabaseReference reference3;
    DatabaseReference reference4;
    int newScore;
    String newQuizNo;
    View view14;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_section);
        firebaseAuth = FirebaseAuth.getInstance();
        imageView = findViewById(R.id.quizImageView);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        radioButtons = new ArrayList<>();
        radioButtons.add(radioButton1);
        radioButtons.add(radioButton2);
        radioButtons.add(radioButton3);
        radioButtons.add(radioButton4);
        view14 = findViewById(R.id.view14);
        submitButton = findViewById(R.id.submitButton);
        progressDialog = new ProgressDialog(QuizSection.this,R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching image!...");
        reference = FirebaseDatabase.getInstance().getReference().child("Auth").child(firebaseAuth.getCurrentUser().getUid()).child("score");
        reference2 = FirebaseDatabase.getInstance().getReference().child("Quiz");
        reference3 = FirebaseDatabase.getInstance().getReference().child("Auth").child(firebaseAuth.getCurrentUser().getUid()).child("queNo");

        Button button = findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        submitButton.setEnabled(false);
        progressDialog.show();
        try {
            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    map = (HashMap<String, String>) dataSnapshot.getValue();
                    radioButton1.setText(map.get("opt1"));
                    radioButton2.setText(map.get("opt2"));
                    radioButton3.setText(map.get("opt3"));

                    radioButton4.setText(map.get("opt4"));
                    newQuizNo = map.get("newQuiz");
                    Bitmap bitmap = StringToBitMap(map.get("imageData"));
                    imageView.setImageBitmap(bitmap);
                    progressDialog.dismiss();
                    if(Integer.parseInt(newQuizNo) <= Integer.parseInt(login.questionNo)){

                        view14.setVisibility(View.VISIBLE);
                        submitButton.setEnabled(false);
                        Toast.makeText(QuizSection.this,"Response already Submitted,Please try again!",Toast.LENGTH_SHORT).show();

                    }else{

                        view14.setVisibility(View.INVISIBLE);
                        submitButton.setEnabled(true);


                    }
                                    }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Toast.makeText(QuizSection.this,"Please check your connection!",Toast.LENGTH_SHORT).show();

            progressDialog.dismiss();
        }






    }



    public void submit(View view){


        if(answSelected != 10){

            progressDialog.setMessage("Submitting response!...");
            progressDialog.show();

            try{
                if(answSelected == Integer.parseInt(map.get("ans"))){



                    newScore = Integer.parseInt(login.score) + 1;


                    reference.setValue(String.valueOf(newScore)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                SharedPreferences.Editor editor = getSharedPreferences("Auth", MODE_PRIVATE).edit();

                                reference3.setValue(newQuizNo);
                                editor.putString("score",String.valueOf(newScore));
                                editor.putString("queNo",newQuizNo);
                                login.score = String.valueOf(newScore);
                                login.questionNo = newQuizNo;
                                editor.commit();
                                Toast.makeText(QuizSection.this,"Answer Send!",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(QuizSection.this,Main2Activity.class);
                                startActivity(intent);
                                finish();

                            }
                        }
                    });

                }else{

                    SharedPreferences.Editor editor = getSharedPreferences("Auth", MODE_PRIVATE).edit();
                    reference3.setValue(newQuizNo);
                    editor.putString("queNo",newQuizNo);
                    editor.commit();
                    Toast.makeText(QuizSection.this,"Answer Send!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Intent intent = new Intent(QuizSection.this,Main2Activity.class);
                    startActivity(intent);
                    finish();
                }

            }catch (Exception e){

                Toast.makeText(QuizSection.this,"Pleaase check Internet!",Toast.LENGTH_SHORT).show();


            }




        }else{

            Animation animation = AnimationUtils.loadAnimation(QuizSection.this,R.anim.fade_in);
            view.startAnimation(animation);
            Toast.makeText(QuizSection.this,"Please Select option!",Toast.LENGTH_SHORT).show();


        }



    }

    public void select(View view){

        answSelected = Integer.parseInt(view.getTag().toString());
        uncheckRadio(Integer.parseInt(view.getTag().toString()) - 1);


    }

    public void uncheckRadio(int id){
        int i;
        for(i=0;i<4;i++){
            if(i != id){
                radioButtons.get(i).setChecked(false);
            }
        }

    }


    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
