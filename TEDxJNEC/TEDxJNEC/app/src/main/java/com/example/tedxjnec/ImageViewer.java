package com.example.tedxjnec;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class ImageViewer extends AppCompatActivity {


    GridView simpleGrid;
    ImageView imageView;
    View imageView1;
int logos[] = {R.drawable.gone,R.drawable.gthree,R.drawable.gfour,R.drawable.gtwo,R.drawable.gfive,R.drawable.gnine,R.drawable.gseven,R.drawable.geight,R.drawable.gten,R.drawable.geleven,R.drawable.gtwel,R.drawable.gfourteen,R.drawable.gfifteen,R.drawable.gsixteen,R.drawable.gseventieen,R.drawable.gtwentyfour,R.drawable.gtwentyfive,R.drawable.gtwentysix,R.drawable.gfourteen,R.drawable.geighteen};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);


        imageView1 = findViewById(R.id.view3);
        imageView = findViewById(R.id.imageView);
        simpleGrid = findViewById(R.id.simpleGridView);

        CustomAdapter customAdapter = new CustomAdapter(ImageViewer.this,logos);
        simpleGrid.setAdapter(customAdapter);
//
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                createImageView(i);
//                imageView.setImageResource(logos[i]);
//                imageView.setVisibility(View.VISIBLE);
//                imageView1.setVisibility(View.VISIBLE);
//                Toast.makeText(ImageViewer.this,"You Clicked",Toast.LENGTH_SHORT).show();
//                makeDialog(i);

            }
        });



        Button button = findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


    }


    public void createImageView(int imageID){
        ImageView image = new ImageView(this);


        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setClickable(true);
        image.setImageResource(logos[imageID]);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this).
                        setView(image);
        final AlertDialog show = builder.show();
        builder.setCancelable(true);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.dismiss();
            }
        });




    }



}
