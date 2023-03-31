package com.example.googleanalyticsass;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity3 extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAnalytics mFirebaseAnalytics;
    TextView noteTtext;
    TextView noteDtext;
    ArrayList<NotesCategory> items;
    ImageView noteImage;
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        noteImage = findViewById(R.id.imageView);
         mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        GetNote();
        screenTrack("MainActivity3");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("Untitled.png");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("saraaaa", "hhhhhhhhhhhhhhhhh");
                Glide.with(getBaseContext()).load(uri.toString()).into(noteImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("saraaaa", "mmmmmmmmmmmmmmmmm");
            }
        });

    }

    private void GetNote() {

        db.collection("Notes").document(getIntent().getStringExtra("id")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d("TAG", "58888888888888");

                    if (document.exists()) {
                        String namenote = document.getString("note");
                        String notedescription = document.getString("noteDescription");

                        noteTtext = findViewById(R.id.textView3);
                        noteDtext = findViewById(R.id.textView4);

                        noteTtext.setText(namenote);
                        noteDtext.setText(notedescription);
                        Log.d("TAG", namenote);

                    } else {
                        Log.d("TAG", "No such document555555555555555555555555555555");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
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
        users.put("screen Name", "MainActivity3");

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