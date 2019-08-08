package com.cloudnote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.HttpBody;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditorActivity extends AppCompatActivity {

    ImageView ivBack,ivImageEdit;
    EditText etTitle,etBody;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ivBack = findViewById(R.id.ivBack);
        ivImageEdit = findViewById(R.id.ivImageEdit);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED)
        {
           // uploaddata();
        }
    }

    @Override
    public void onBackPressed() {
        uploaddata();

        Intent intent = new Intent(EditorActivity.this,MainActivity.class);
        startActivity(intent);
    }

    private void uploaddata() {
        etTitle = findViewById(R.id.etTitle);
        etBody = findViewById(R.id.etBody);
        String title = etTitle.getText().toString();
        String body = etBody.getText().toString();

        Map<String ,String> Notes = new HashMap<>();

        Notes.put("Title", title);
        Notes.put("Body", body);

        firestore.collection("Notes").add(Notes).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(EditorActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditorActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
