package com.cloudnote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.HttpBody;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditorActivity extends AppCompatActivity {

    ImageView ivBack,ivImageEdit;
    EditText etTitle,etBody;
    ImageButton addImage;


    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ivBack = findViewById(R.id.ivBack);
        ivImageEdit = findViewById(R.id.ivImageEdit);
        addImage = findViewById(R.id.addImage);
        etTitle = findViewById(R.id.etTitle);
        etBody = findViewById(R.id.etBody);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),5);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== 5)
        {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                ivImageEdit.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(EditorActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(EditorActivity.this, "No Image Selected",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        if ( etTitle.getText().toString().isEmpty()||etBody.getText().toString().isEmpty())
        {
            Intent intent = new Intent(EditorActivity.this,MainActivity.class);
            startActivity(intent);

            Toast.makeText(this, "Empty Note Discarded", Toast.LENGTH_SHORT).show();
        }
        else
        {
            uploaddata();
            Intent intent = new Intent(EditorActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void uploaddata() {

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
