package com.example.googleanalyticsass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.ItemClickListener, CategoryAdapter.ItemClickListener2{
    private RecyclerView categoryList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView rv;
    ArrayList<Category> items;
    CategoryAdapter adapter;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    private long startTime;
    private FirebaseAnalytics mFirebaseAnalytics;

    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        startTime = System.currentTimeMillis();

        categoryList = findViewById(R.id.category_list);
        categoryList.setLayoutManager(new LinearLayoutManager(this));

        rv = findViewById(R.id.category_list);
        items = new ArrayList<Category>();
        adapter = new CategoryAdapter(this, items, this, this);
        GetAllNotes();
        screenTrack("MainActivity");
    }
    private void GetAllNotes() {

        db.collection("Category").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d("sara", "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                if (documentSnapshot.exists()) {
                                    String id = documentSnapshot.getId();
                                    String cat = documentSnapshot.getString("CategoryName");
                                    Category category = new Category(id, cat);
                                    items.add(category);
                                    rv.setLayoutManager(layoutManager);
                                    rv.setHasFixedSize(true);
                                    rv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    Log.e("LogDATA", items.toString());
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("LogDATA", "get failed with ");


            }
        });
    }

    @Override
    public void onItemClick(int position, String id) {

    }

    @Override
    public void onItemClick2(int position, String id) {
        Category clickedItem = items.get(position);
        String idc = clickedItem.getId();
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("id", idc);
        Log.e("sara02", idc);
        categEvent(idc, "Category Button", items.get(position).categoryName);
        startActivity(intent);

    }

    public void categEvent(String id, String name, String content) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, content);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }


    public void screenTrack(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Main Activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    protected void onPause() {
        Calendar calendar = Calendar.getInstance();
        int hour2 = calendar.get(Calendar.HOUR);
        int minute2 = calendar.get(Calendar.MINUTE);
        int second2 = calendar.get(Calendar.SECOND);

        int h = hour2 - hour;
        int m = minute2 - minute;
        int s = second2 - second;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String, Object> users = new HashMap<>();
        users.put("name", "saramhmd");
        users.put("hour", h);
        users.put("minute", m);
        users.put("second", s);
        users.put("screen Name", "MainActivity");
        db.collection("users").add(users)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        super.onPause();
    }
}