package com.example.smarttrafficmanagement1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlaceActivity extends AppCompatActivity {

    PlacesClient placesClient;
    List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS);
    TextView edt_Address, tvDetails;
    ImageView img_Location;
    Button btn_find_current_place, btn_get_Photo, btn_confirm;
    private String placeID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        requestPermission();

        btn_find_current_place = findViewById(R.id.btn_get_current_place);
        edt_Address = findViewById(R.id.edt_Address);
        tvDetails = findViewById(R.id.tv_details);
        btn_get_Photo = findViewById(R.id.btn_get_photo);
        img_Location = findViewById(R.id.img_Location);
        btn_confirm = findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edt_Address.getText())) {
                    Toast.makeText(PlaceActivity.this, "You need to select your location first.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(PlaceActivity.this, "" + edt_Address.getText(), Toast.LENGTH_SHORT).show();

                Intent reportIntent = new Intent(PlaceActivity.this, ReportActivity.class);
                reportIntent.putExtra("address", edt_Address.getText().toString());
                startActivity(reportIntent);
            }
        });

        btn_find_current_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentPlace();
            }
        });

        btn_get_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(placeID)) {
                    Toast.makeText(PlaceActivity.this, "Place Id cannot be null.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    getPhotoAndDetails(placeID);
                }
            }
        });
        initPlaces();
    }

    private void getPhotoAndDetails(final String placeID) {
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeID,Arrays.asList(Place.Field.PHOTO_METADATAS)).build();
        placesClient.fetchPlace(request)
                .addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        final Place place = fetchPlaceResponse.getPlace();
                        PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
                        FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata).build();
                        placesClient.fetchPhoto(photoRequest)
                                .addOnSuccessListener(new OnSuccessListener<FetchPhotoResponse>() {
                                    @Override
                                    public void onSuccess(FetchPhotoResponse fetchPhotoResponse) {
                                        Bitmap bitmap = fetchPhotoResponse.getBitmap();
                                        img_Location.setImageBitmap(bitmap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PlaceActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        FetchPlaceRequest request1 = FetchPlaceRequest.builder(placeID,Arrays.asList(Place.Field.LAT_LNG)).build();
                        placesClient.fetchPlace(request1)
                                .addOnCompleteListener(new OnCompleteListener<FetchPlaceResponse>() {
                                    @Override
                                    public void onComplete(@NonNull Task<FetchPlaceResponse> task) {
                                        if (task.isSuccessful()){
                                            Place place1 = task.getResult().getPlace();
                                            tvDetails.setText(new StringBuilder(String.valueOf(place1.getLatLng().latitude)).append("/")
                                                    .append(place1.getLatLng().longitude));
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                        public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PlaceActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PlaceActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void requestPermission(){
        Dexter.withActivity(this)
                .withPermissions(Arrays.asList(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION))
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(PlaceActivity.this, "Please enable the location.", Toast.LENGTH_SHORT).show();

                    }
                }).check();
    }

    private void getCurrentPlace() {
        FindCurrentPlaceRequest request =FindCurrentPlaceRequest.builder(placeFields).build();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            return;
        }
        Task<FindCurrentPlaceResponse> placeResponseTask = placesClient
                .findCurrentPlace(request);
        placeResponseTask.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
            @Override
            public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                if (task.isSuccessful()) {

                    FindCurrentPlaceResponse response = task.getResult();

//                    Collections.sort(response.getPlaceLikelihoods(), new Comparator<PlaceLikelihood>() {
//                        @Override
//                        public int compare(PlaceLikelihood o1, PlaceLikelihood o2) {
//                            return new Double(o1.getLikelihood()).compareTo(o2.getLikelihood());
//                        }
//                    });
//
//                    Collections.reverse(response.getPlaceLikelihoods());

                    placeID = response.getPlaceLikelihoods().get(0).getPlace().getId();

                    edt_Address.setText(new StringBuilder(response.getPlaceLikelihoods()
                            .get(0)
                            .getPlace()
                            .getAddress()));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PlaceActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPlaces() {
        Places.initialize(this,getString(R.string.places_api_key));
        placesClient = Places.createClient(this);
    }
    public void onBackPressed() {
        Intent intent = new Intent(PlaceActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



}