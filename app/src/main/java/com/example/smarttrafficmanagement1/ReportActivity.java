package com.example.smarttrafficmanagement1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarttrafficmanagement1.Class.Reports;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class ReportActivity extends AppCompatActivity {
    private CardView cvAccident, cvConstruction, cvTrafficJam, cvOthers;
    private TextView placeName;
    private CardView btn_get_Photo;
    private Reports reports;
    static String userName;
    private ImageView img_Location;
    private static final int CAMERA_REQUEST_CODE =1;
    private Uri filepath;
    private String image_name;
    private Toast toast;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask mUploadTask;
    private Uri tempUri;

    private String downloadUrl;



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReportActivity.this, MainActivity.class);
        toast.cancel();
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        cvAccident= findViewById(R.id.cvAccident);
        cvConstruction= findViewById(R.id.cvConstruction);
        cvTrafficJam = findViewById(R.id.cvTraffic);
        cvOthers = findViewById(R.id.cvOthers);
        placeName = findViewById(R.id.tvplaceName);
        btn_get_Photo = findViewById(R.id.btn_get_photo);

        img_Location = findViewById(R.id.img_Location);

        storageReference = FirebaseStorage.getInstance().getReference();


        reports = new Reports();
        requestPermisssion();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Report");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userName = user.getEmail();

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            String address = bundle.getString("address");
            placeName.setText(address);
        }
        else {
            Toast.makeText(this, "No message.", Toast.LENGTH_SHORT).show();
        }

        btn_get_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });
        cvAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                if(image_name==null){
                    Toast.makeText(ReportActivity.this, "Please Upload an Image First.", Toast.LENGTH_SHORT).show();
                    return;
                }
                reports.setLocation(placeName.getText().toString().trim());
                reports.setReport("Accident");
                reports.setUserName(userName);
                reports.setImage(downloadUrl);

                if (downloadUrl==null){
                    Toast.makeText(ReportActivity.this, "Report error please report again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                databaseReference.push().setValue(reports);
                toast=Toast.makeText(ReportActivity.this, "Accident Reported Successfully", Toast.LENGTH_SHORT);
                toast.show();
                clearImage();

            }
        });

        cvConstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                reports.setLocation(placeName.getText().toString().trim());
                reports.setReport("Construction");
                reports.setUserName(userName);
                reports.setImage(downloadUrl);

                if (downloadUrl==null){
                    Toast.makeText(ReportActivity.this, "Report error please report again.", Toast.LENGTH_SHORT).show();
                    return;
                }

                databaseReference.push().setValue(reports);
//                databaseReference.child(String.valueOf(maxId+1)).setValue("Reports");
                toast=Toast.makeText(ReportActivity.this, "Construction Reported Successfully", Toast.LENGTH_SHORT);
                toast.show();
                clearImage();

            }
        });

        cvTrafficJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                reports.setLocation(placeName.getText().toString().trim());
                reports.setReport("Traffic Jam");
                reports.setUserName(userName);
                reports.setImage(downloadUrl);

                if (downloadUrl==null){
                    Toast.makeText(ReportActivity.this, "Report error please report again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                databaseReference.push().setValue(reports);

//                databaseReference.child(String.valueOf(maxId+1)).setValue("Reports");
                toast=Toast.makeText(ReportActivity.this, "Traffic Jam Reported Successfully", Toast.LENGTH_SHORT);
                toast.show();
                clearImage();



            }
        });

        cvOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
                reports.setLocation(placeName.getText().toString().trim());
                reports.setReport("Others");
                reports.setUserName(userName);
                reports.setImage(downloadUrl);
                if (downloadUrl==null){
                    Toast.makeText(ReportActivity.this, "Report error please report again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                databaseReference.push().setValue(reports);


               toast= Toast.makeText(ReportActivity.this, "Reported Successfully", Toast.LENGTH_SHORT);
               toast.show();
                clearImage();


            }
        });
    }

    private void clearImage() {
        tempUri=null;
    }

    private void uploadImage() {
        if (tempUri != null){

            image_name = String.valueOf(System.currentTimeMillis());
            StorageReference ref = storageReference.child("images/"+image_name);
            ref.putFile(tempUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = uri.toString();
                                    Toast.makeText(ReportActivity.this, "Uploaded..", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ReportActivity.this, "Failed to Upload.."+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        else {
            Toast.makeText(this, "Please reclick image...", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermisssion() {
            Dexter.withActivity(this)
                    .withPermissions(Arrays.asList(Manifest.permission.CAMERA))
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            Toast.makeText(ReportActivity.this, "Please give access to camera.", Toast.LENGTH_SHORT).show();

                        }
                    }).check();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CAMERA_REQUEST_CODE && resultCode==RESULT_OK){

            Bitmap bitmap  = (Bitmap) data.getExtras().get("data");
            tempUri = getImageUri(getApplicationContext(), bitmap);
//            filepath = getImageUri(getApplicationContext(), bitmap);
            Picasso.get().load(tempUri).into(img_Location);

//            Toast.makeText(this, ""+getRealPathFromURI(tempUri), Toast.LENGTH_SHORT).show();

        }
    }


    private Uri getImageUri(Context applicationContext, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(applicationContext.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    }
