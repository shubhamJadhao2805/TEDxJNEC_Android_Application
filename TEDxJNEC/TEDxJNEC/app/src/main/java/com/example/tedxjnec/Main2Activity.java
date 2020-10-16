package com.example.tedxjnec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.util.HashMap;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    TextView nameOFUSer;
    ActionBar actionBar;
    FirebaseAuth firebaseAuth;
    ViewPager viewPager;


   VideoView videoView;
   DatabaseReference reference;
   HashMap<String,String> hashMap;
   WebView webView;
   String videoLInk;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final TextView news1 = findViewById(R.id.textView11);
        final TextView news2 = findViewById(R.id.textView12);
        final TextView news3 = findViewById(R.id.textView13);
        webView = findViewById(R.id.youtubePlayer);



        viewPager = findViewById(R.id.Slider2);
        reference = FirebaseDatabase.getInstance().getReference().child("news");
        try {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    hashMap = (HashMap<String, String>) dataSnapshot.getValue();
                    news1.setText(hashMap.get("n1"));
                    news2.setText(hashMap.get("n2"));
                    news3.setText(hashMap.get("n3"));
                    videoLInk = hashMap.get("link");

                    String frameVideo = "<iframe width=\"100%\" height=\"100%\" src=\""+videoLInk+"\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
//        String frameVideo="<div style=\"position:relative; padding-bottom:56.25%;padding-top:30px; height:0; overflow:hidden;\"><iframe width=\"auto\" height=\"auto\" src=\"https://www.youtube.com/embed/XkHV7ROmIVA\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen> </iframe> ";
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            return false;
                        }
                    });

                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    webView.loadData(frameVideo,"text/html","utf-8");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){

        }



//



        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){

            SharedPreferences prefs = getSharedPreferences("Auth", MODE_PRIVATE);
            login.name = prefs.getString("name","TEDxJNEC");
            login.score = prefs.getString("score","0");
            login.questionNo = prefs.getString("queNo","0");
            login.emailAddress = prefs.getString("email","0");


        }else{
            Intent intent = new Intent(Main2Activity.this,login.class);
            startActivity(intent);
            finish();


        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        nameOFUSer = findViewById(R.id.textView3);
        try{
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.textView3);
            navUsername.setText(login.name);        }catch (Exception e){
            Toast.makeText(Main2Activity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
//        nameOFUSer.setText("Shubham");
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E62B16")));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);






    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(Main2Activity.this, SpeakerActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_gallery) {

            Intent intent = new Intent(Main2Activity.this, ImageViewer.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

            Intent intent = new Intent(Main2Activity.this, QuizSection.class);
            startActivity(intent);

        } else if (id == R.id.nav_tools) {

            Intent intent = new Intent(Main2Activity.this,TeamActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

            Intent intent = new Intent(Main2Activity.this,LeaderBoard.class);
            startActivity(intent);

        } else if (id == R.id.nav_send) {

            firebaseAuth.signOut();
            Intent intent = new Intent(Main2Activity.this, login.class);
            startActivity(intent);
            finish();

        }else if(id == R.id.place){
            Intent intent = new Intent(Main2Activity.this,PlaceActivity.class);
            startActivity(intent);
        }else if(id == R.id.about){
            Intent intent = new Intent(Main2Activity.this,AboutUs.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
