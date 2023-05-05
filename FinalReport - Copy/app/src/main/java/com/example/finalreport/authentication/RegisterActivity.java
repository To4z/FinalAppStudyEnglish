package com.example.finalreport.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalreport.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText regName, regPass, regMail;

    Button regBtn, regLogBtn;
    ImageView userImg;
    static int REQUEST_CODE = 1;
    Uri pickedImgUri;
    private FirebaseAuth firebaseAuth;

    private ProgressBar progressBar;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regName = findViewById(R.id.reg_name);
        regPass = findViewById(R.id.reg_password);
        regMail = findViewById(R.id.reg_mail);
        regBtn = findViewById(R.id.reg_btn);
        regLogBtn = findViewById(R.id.reg_login_btn);

        userImg = findViewById(R.id.circleImageView);
        progressBar = findViewById(R.id.reg_progressBar);
        progressBar.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        reference = FirebaseDatabase.getInstance("https://skyfun-english-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Score");

        regLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkAndRequestPermission();
                openGallery();
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void checkAndRequestPermission() {
        if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(RegisterActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            openGallery();
        }
    }

    private void openGallery() {
        startActivityForResult(new Intent()
                .setAction(Intent.ACTION_GET_CONTENT)
                .setType("image/*"),REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            assert data != null;
            pickedImgUri = data.getData();
            userImg.setImageURI(pickedImgUri);
        }
    }

    private void registerUser() {
        String name, mail, pass;
        name = regName.getText().toString().trim();
        mail = regMail.getText().toString().trim();
        pass = regPass.getText().toString().trim();

        if (name.isEmpty() || mail.isEmpty() || pass.isEmpty() || pickedImgUri == null){
            Toast.makeText(this, "Please provide all fields", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(mail,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                //Toast.makeText(RegisterActivity.this, "Register is successful", Toast.LENGTH_SHORT).show();

                                updateUI(name, pickedImgUri,firebaseAuth.getCurrentUser());
                                openProfile();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void openProfile() {
        startActivity(new Intent(RegisterActivity.this, ProfileActivity.class));
        finish();
    }

    private void updateUI(final String name, Uri pickedImgUri,final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_image");

        final StorageReference imgFilePath = mStorage.child(pickedImgUri.getLastPathSegment());

        imgFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(userProfileChangeRequest);

                        HashMap map = new HashMap();
                        map.put("name", name);
                        map.put("image", uri.toString());
                        map.put("score", 0);
                        map.put("chapter",0);
                        reference.child(currentUser.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //Toast.makeText(RegisterActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }
}