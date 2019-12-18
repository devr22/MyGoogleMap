package com.example.mygooglemap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mygooglemap.model.addressInfo;
import com.example.mygooglemap.model.placeInfo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final String fine_location = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String coarse_location = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int location_permission_request_code = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds lat_lng_Bounds = new LatLngBounds( new LatLng(-40, -168), new LatLng(71,136));

    // widgets
    private AutoCompleteTextView searchText;
    private ImageView gps_icon;
    private ImageView info_icon;

    // variables
    private Boolean location_permission_granted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    protected GeoDataClient geoDataClient;
    private placeInfo mplaceInfo;
    private Marker marker;
    private Address address;

    //Bottom sheet
    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        searchText = findViewById(R.id.input_search);
        gps_icon = findViewById(R.id.ic_gps);
        info_icon = findViewById(R.id.ic_info);

        getLocationPermission();

    }

    private void init(){

        Log.d(TAG, "init: initializing");

        geoDataClient = Places.getGeoDataClient(this,null);

        searchText.setOnItemClickListener(mAutocompleteClickListener);

        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, geoDataClient, lat_lng_Bounds, null);
        searchText.setAdapter(placeAutocompleteAdapter);

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER)
                {
                    // execute method for searching the place
                    geolocate();
                }

                return false;
            }
        });

        gps_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });

        info_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInfo();
            }
        });

        hideSoftKeyboard();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: called");
        location_permission_granted = false;

        switch (requestCode)
        {
            case location_permission_request_code:{

                if (grantResults.length > 0)
                {
                    for (int i=0; i < grantResults.length; i++)
                    {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            Log.d(TAG, "onRequestPermissionsResult: Permission failed");
                            location_permission_granted = false;
                            return;
                        }
                    }

                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                    location_permission_granted = true;
                    //initialize map
                    initMap();

                }

            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "onMapReady: map is ready");
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();

        mMap = googleMap;

        if (location_permission_granted)
        {
            getDeviceLocation();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }

    private void initMap(){

        Log.d(TAG, "initMap: initialising map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission(){

        Log.d(TAG, "getLocationPermission: getting location permission");
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), fine_location) == PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), coarse_location) == PackageManager.PERMISSION_GRANTED)

            {
                location_permission_granted = true;
                initMap();
            }
            else
            {
                ActivityCompat.requestPermissions(this, permissions, location_permission_request_code);
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this, permissions, location_permission_request_code);
        }

    }

    private void getDeviceLocation(){

        Log.d(TAG, "getDeviceLocation: getting the device current location");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {

            if (location_permission_granted)
            {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful())
                        {
                            Log.d(TAG, "onComplete: location found");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                        }
                        else
                        {
                            Log.d(TAG, "onComplete: Current Location is not found");
                            Toast.makeText(MapActivity.this, "Unable to find device's current location", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

        }
        catch (SecurityException e){
            Log.d(TAG, "getDeviceLocation: SecurityException " + e.getMessage());
        }

    }

    private void moveCamera(LatLng latLng, float zoom, placeInfo mplaceInfo){

        Log.d(TAG, "moveCamera: moving the camera to: latitude: " + latLng.latitude + ", longitude: " + latLng.longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.clear();

        if (mplaceInfo != null)
        {
            try {
                    String snippet = "Address: " + mplaceInfo.getAddress() + "\n" +
                            "Phone Number: " + mplaceInfo.getPhoneNumber() + "\n" +
                            "Website: " + mplaceInfo.getWebsitesUri() + "\n" +
                            "Price Rating: " + mplaceInfo.getRating();

                    MarkerOptions options = new MarkerOptions()
                            .position(latLng)
                            .title(mplaceInfo.getName())
                            .snippet(snippet);

                    marker = mMap.addMarker(options);

            }
            catch (NullPointerException e){
                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage());
            }
        }
        else
        {
            mMap.addMarker(new MarkerOptions().position(latLng));
        }

        hideSoftKeyboard();

    }

    private void moveCamera(LatLng latLng, float zoom, String title){

        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location"))
        {
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(markerOptions);
        }

        hideSoftKeyboard();

    }

    private void geolocate(){

        Log.d(TAG, "geolocate: geolocate method is called");
        hideSoftKeyboard();

        String searchString = searchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        }
        catch (IOException e)
        {
            Log.e(TAG, "geolocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0)
        {
            address = list.get(0);

            Log.d(TAG, "geolocate: found a location" + address.toString());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }

    }

    @SuppressLint("SetTextI18n")
    private void getInfo(){

        Log.d(TAG, "getInfo: Latitude:-" + address.getLatitude() + "Longitude:-" + address.getLongitude());

        bottomSheetDialog = new BottomSheetDialog();
        bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet");

        addressInfo maddressInfo = new addressInfo();

        maddressInfo.setLatitude(address.getLatitude());
        maddressInfo.setLongitude(address.getLongitude());
        maddressInfo.setContry(address.getCountryName() + ", " + address.getCountryCode());
        maddressInfo.setLocality(address.getLocality());
        maddressInfo.setPostalcode(address.getPostalCode());
        maddressInfo.setUrl(address.getUrl());
        maddressInfo.setPhoneNumber(address.getPhone());

    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            hideSoftKeyboard();

            final AutocompletePrediction item = (AutocompletePrediction) placeAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "AutoComplete item selected " + primaryText);

            Task<PlaceBufferResponse> placeResult = geoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);

        }
    };


    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(@NonNull Task<PlaceBufferResponse> task) {

            try {

                PlaceBufferResponse places = task.getResult();
                final Place place = places.get(0);

                mplaceInfo = new placeInfo();

                mplaceInfo.setName(place.getName().toString());
                mplaceInfo.setAddress(place.getAddress().toString());
                mplaceInfo.setPhoneNumber(place.getPhoneNumber().toString());
                mplaceInfo.setId(place.getId());
                mplaceInfo.setWebsitesUri(place.getWebsiteUri());
                mplaceInfo.setLatLng(place.getLatLng());
                mplaceInfo.setRating(place.getRating());

                Log.d(TAG, "onComplete: Place: " + mplaceInfo.toString());

                moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                        place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mplaceInfo);

                places.release();

            }
            catch (RuntimeRemoteException e){
                Log.e(TAG, "onComplete: Place query did not complete " + e.getMessage());
                return;
            }

        }
    };



}










