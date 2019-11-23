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
    private Button btn_get_Photo;
    Reports reports;
    DatabaseReference databaseReference;
    static String userName;
    long maxId =0;
    ImageView img_Location;
    private static final int CAMERA_REQUEST_CODE =1;
    private StorageReference storageReference;
    private ProgressDialog mProgressDialog;
    private Uri filepath;
    private String image_name;


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReportActivity.this, MainActivity.class);
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

        mProgressDialog =new ProgressDialog(this);

        storageReference = FirebaseStorage.getInstance().getReference();


        reports = new Reports();
        requestPermisssion();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Report");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists())
//                    maxId= (dataSnapshot.getChildrenCount());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userName = user.getEmail();

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            String address = bundle.getString("address");
            placeName.setText(address);
//            Toast.makeText(this, "Address : "+address, Toast.LENGTH_SHORT).show();

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
                reports.setLocation(placeName.getText().toString().trim());
                reports.setReport("Accident");
                reports.setUserName(userName);
                reports.setImage(image_name);
                databaseReference.push().setValue(reports);
//                databaseReference.child(String.valueOf(maxId+1)).setValue("Reports");
                Toast.makeText(ReportActivity.this, "Accident Reported Successfully", Toast.LENGTH_SHORT).show();
//                Toast.makeText(ReportActivity.this, ""+filepath, Toast.LENGTH_SHORT).show();


            }
        });

        cvConstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                reports.setLocation(placeName.getText().toString().trim());
                reports.setReport("Construction");
                reports.setUserName(userName);
                databaseReference.push().setValue(reports);
                reports.setImage(image_name);
//                databaseReference.child(String.valueOf(maxId+1)).setValue("Reports");
                Toast.makeText(ReportActivity.this, "Construction Reported Successfully", Toast.LENGTH_SHORT).show();


            }
        });

        cvTrafficJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                reports.setLocation(placeName.getText().toString().trim());
                reports.setReport("Traffic Jam");
                reports.setUserName(userName);
                databaseReference.push().setValue(reports);
                reports.setImage(image_name);

//                databaseReference.child(String.valueOf(maxId+1)).setValue("Reports");
                Toast.makeText(ReportActivity.this, "Traffic Jam Reported Successfully", Toast.LENGTH_SHORT).show();


            }
        });

        cvOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                reports.setLocation(placeName.getText().toString().trim());
                reports.setReport("Others");
                reports.setUserName(userName);
                databaseReference.push().setValue(reports);
                reports.setImage(image_name);

//                databaseReference.child(String.valueOf(maxId+1)).setValue("Reports");
                Toast.makeText(ReportActivity.this, "Reported Successfully", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void uploadImage() {
        if (filepath != null){
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();

            image_name = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/"+image_name);
            ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    progressDialog.dismiss();
                    Toast.makeText(ReportActivity.this, "Uploaded..", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    progressDialog.dismiss();
                    Toast.makeText(ReportActivity.this, "Failed to Upload.."+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
//                    progressDialog.setMessage("Uploaded "+ (int)progress+"%");
//                }
//            });
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
            img_Location.setImageBitmap(bitmap);

            Uri tempUri = getImageUri(getApplicationContext(), bitmap);
            filepath = getImageUri(getApplicationContext(), bitmap);

            Toast.makeText(this, ""+getRealPathFromURI(tempUri), Toast.LENGTH_SHORT).show();

//            System.out.println(getRealPathFromURI(tempUri));

//            mProgressDialog.setMessage("Uploading Image....");
//            mProgressDialog.show();
//            final Uri uri = data.getData();
//            final StorageReference filepath = storageReference.child("Photos").child("firstimage.jpg");
//
//
//
//            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    mProgressDialog.dismiss();
//                    Task<Uri> urlTask = filepath.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                        @Override
//                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                            if (!task.isSuccessful()) {
//
//                                throw task.getException();
//                            }
//
//                             Continue with the task to get the download URL
//                            return filepath.getDownloadUrl();

//                        }
//                    });
//                    Picasso.get().load(String.valueOf(urlTask)).into(img_Location);

//                    Toast.makeText(ReportActivity.this, "Uploaded Successfully!!!", Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }

    private Uri getImageUri(Context applicationContext, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(applicationContext.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    }
