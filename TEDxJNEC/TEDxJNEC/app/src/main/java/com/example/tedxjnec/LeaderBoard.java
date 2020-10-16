package com.example.tedxjnec;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class LeaderBoard extends AppCompatActivity {

    ListView listView;
    List<String> leaderboardList;
    ProgressDialog progressDialog;
    String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
         leaderboardList = new ArrayList<>();

        Button button = findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
         progressDialog = new ProgressDialog(this);
         progressDialog.setMessage("Fetching List...");
        progressDialog.show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Auth");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                System.out.print(dataSnapshot.getValue());
try {


    JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
    JSONArray jsonArray = jsonObject.names();

    HashMap<String,Integer> map = new HashMap<>();

    int i;
    for(i=0;i<jsonArray.length();i++){

         String uid = jsonArray.getString(i);
         String email = jsonObject.getJSONObject(uid).getString("email");
         String score = jsonObject.getJSONObject(uid).getString("score");
         map.put(email,Integer.parseInt(score));


    }

    System.out.print("Mapping is");
    System.out.print(map);
    Set<Map.Entry<String,Integer>> set = map.entrySet();
    List<Map.Entry<String,Integer>> list = new ArrayList<>(set);
    Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
        @Override
        public int compare(Map.Entry<String, Integer> stringIntegerEntry, Map.Entry<String, Integer> t1) {
            return (t1.getValue()).compareTo(stringIntegerEntry.getValue());
        }
    });

    for(i=0;i<list.size();i++){
        int srNo = i + 1;

        if(list.get(i).getKey().equals(login.emailAddress)){
            currentUser = srNo + ". " + list.get(i).getKey() + "   Score = " + list.get(i).getValue();
        }
        String totalString = srNo + ". " + list.get(i).getKey() + "   Score = " + list.get(i).getValue();
        leaderboardList.add(totalString);


    }
    progressDialog.dismiss();

    listView = findViewById(R.id.listView);

    ArrayAdapter<String> adapter = new ArrayAdapter<String>(LeaderBoard.this,
            R.layout.listviewcustom, R.id.list_content, leaderboardList);

listView.setAdapter(adapter);
//    HashMap<String,String> map =




}catch (Exception e){

    Toast.makeText(LeaderBoard.this,"Failed to get!",Toast.LENGTH_SHORT).show();

    Log.i("ErrorMess",e.toString());

}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LeaderBoard.this,"Failed to get!",Toast.LENGTH_SHORT).show();

            }
        });





    }
}
