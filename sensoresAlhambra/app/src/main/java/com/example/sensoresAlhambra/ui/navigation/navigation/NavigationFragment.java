package com.example.sensoresAlhambra.ui.navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.sensoresAlhambra.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_VIOLET;

public class NavigationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private NavigationViewModel dashboardViewModel;
    private static final int LOCATION_PERMISSION_LOCATION = 0x000000;
    private GoogleApiClient googleApiClient;
    private static final long MIN_CAMBIO_DISTANCIA_PARA_UPDATES = 10; // 10 metros
    //Minimo tiempo para updates en Milisegundos
    private static final long MIN_TIEMPO_ENTRE_UPDATES = 1000 * 5 * 1; //1 minuto
    private GoogleMap nMap;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(NavigationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_navigation, container, false);
        googleApiClient = new GoogleApiClient.Builder(getContext(), this, this).addApi(LocationServices.API).build();

        if (ContextCompat.checkSelfPermission(getContext()
                ,
                Manifest
                        .permission
                        .ACCESS_FINE_LOCATION)
                != PackageManager
                .PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity()
                    ,
                    Manifest
                            .permission
                            .ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity()
                        ,
                        new String[]{Manifest.permission
                                .ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_LOCATION);

                // CAMERA_PERMISSION_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext());

        if (status == ConnectionResult.SUCCESS) {

            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.fragment_dashboard);
            mapFragment.getMapAsync(this);

        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getActivity().getApplicationContext(), 10);
            dialog.show();
        }


        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;
        //Set style
        try {
            boolean success = nMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }
        UiSettings uiSettings = nMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);





        //Limits for maps
        LatLng one = new LatLng(37.1783, -3.5924);
        LatLng two = new LatLng(37.1738, -3.5847);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        //add them to builder
        builder.include(one);
        builder.include(two);

        LatLngBounds bounds = builder.build();
        //get width and height to current display screen
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        // 20% padding
        int padding = (int) (width * 0.20);

        //set latlong bounds
        nMap.setLatLngBoundsForCameraTarget(bounds);

        //move camera to fill the bound to screen
        nMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

        //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds
        nMap.setMinZoomPreference(nMap.getCameraPosition().zoom);

        //Location lastLocation = LocationServices.FusedLolastLocationcationApi.getLastLocation(googleApiClient);

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIEMPO_ENTRE_UPDATES, MIN_CAMBIO_DISTANCIA_PARA_UPDATES, locListener, Looper.getMainLooper());
        /*if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }*/
        double latitude = 5;
        double longitude = 5;
        @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!= null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        //Add a marker in Syndey
        LatLng sydney = new LatLng(latitude, longitude);
        //LatLng sydney = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        //System.out.println(lastLocation.getLatitude());
        nMap.addMarker(new MarkerOptions().position(sydney).title("Estoy mamadisimo hdtpm").icon(BitmapDescriptorFactory.defaultMarker(HUE_VIOLET)));
        float zoomlevel = 16;
        //nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomlevel));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public LocationListener locListener = new LocationListener() {
        Marker marker;
        public void onLocationChanged(Location location) {
            if(marker != null)
                marker.remove();
            Log.i(TAG, "Lat " + location.getLatitude() + " Long " + location.getLongitude());
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            //Add a marker in Syndey
            LatLng sydney = new LatLng(latitude, longitude);
            //LatLng sydney = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            //System.out.println(lastLocation.getLatitude());
            //marker = nMap.addMarker(new MarkerOptions().position(sydney).title("Soy kraken").icon(BitmapDescriptorFactory.defaultMarker(HUE_VIOLET)));
            float zoomlevel = 16;
            nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomlevel));
        }

        public void onProviderDisabled(String provider) {
            Log.i(TAG, "onProviderDisabled()");
        }

        public void onProviderEnabled(String provider) {
            Log.i(TAG, "onProviderEnabled()");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged()");
        }
    };


}