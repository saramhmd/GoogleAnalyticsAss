package com.example.googleanalyticsass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity implements NotesCategoryAdapter.ItemClickListener, NotesCategoryAdapter.ItemClickListener2 {
    private RecyclerView categoryNoteList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionRef = db.collection("Notes");

//    DocumentReference userRef = db.collection("Notes").document("55zWbq3z1eCFJtGlo8Vs");
    private FirebaseAnalytics mFirebaseAnalytics;
    RecyclerView rv;
    ArrayList<NotesCategory> items;
    NotesCategoryAdapter adapter;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
         mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        categoryNoteList = findViewById(R.id.noteCategory_list);
        categoryNoteList.setLayoutManager(new LinearLayoutManager(this));

        rv = findViewById(R.id.noteCategory_list);
        items = new ArrayList<NotesCategory>();
        adapter = new NotesCategoryAdapter(this, items, this, this);
        GetAllNotes();
        screenTrack("MainActivity2");





    }
    private void GetAllNotes() {
        Query query = collectionRef.whereEqualTo("categId", getIntent().getStringExtra("id"));
        query.get()
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
                                    String notecat = documentSnapshot.getString("note");
//                                    String categId = documentSnapshot.getString("categId");
                                    NotesCategory categoryNote = new NotesCategory(id, notecat);
                                    items.add(categoryNote);
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
    public void onItemClick(int position) {
//        Intent intent = new Intent(this, MainActivity2.class);
//        startActivity(intent);
    }

    @Override
    public void onItemClick2(int position) {
        NotesCategory clickedItem = items.get(position);
        String id = clickedItem.getId();
        Intent intent = new Intent(this, MainActivity3.class);
        intent.putExtra("id", id);
        Log.e("sara02", id);
        noteEvent(id, "Note Button", items.get(position).noteName);

        startActivity(intent);
    }

    public void noteEvent(String id, String name, String content) {
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
        users.put("name", "sara");
        users.put("hour", h);
        users.put("minute", m);
        users.put("second", s);
        users.put("screen Name", "MainActivity2");

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