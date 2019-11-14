package com.example.sensoresAlhambra.ui.navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;

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
    private ImageView image;

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

        image = (ImageView) root.findViewById(R.id.imageNavigation);
        image.setVisibility(View.INVISIBLE);

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


        PolygonOptions recOption = new PolygonOptions()
                .add(new LatLng(37.1775, -3.5911),
                        new LatLng(37.1774, -3.5901),
                        new LatLng(37.1772, -3.5901),
                        new LatLng(37.1772, -3.5907),
                        new LatLng(37.1767, -3.5908),
                        new LatLng(37.1768, -3.5911))
                .strokeColor(Color.RED).fillColor(0x8585DAFD)
                .strokeWidth(5)
                .geodesic(true);



        Polygon polygon = nMap.addPolygon(recOption);
        polygon.setTag("poligono1");
        polygon.setClickable(true);

        PolygonOptions recOption2 = new PolygonOptions()
                .add(new LatLng(37.1763, -3.5910),
                        new LatLng(37.1765, -3.5911),
                        new LatLng(37.1767, -3.5914),
                        new LatLng(37.1767, -3.5921),
                        new LatLng(37.1767, -3.5921),
                        new LatLng(37.1767, -3.5923),
                        new LatLng(37.1768, -3.5923),
                        new LatLng(37.1768, -3.5928),
                        new LatLng(37.1768, -3.5930),
                        new LatLng(37.1769, -3.5929),
                        new LatLng(37.1769, -3.5931),
                        new LatLng(37.1770, -3.5931),
                        new LatLng(37.1771, -3.5930),
                        new LatLng(37.1771, -3.5929),
                        new LatLng(37.1772, -3.5925),
                        new LatLng(37.1772, -3.5925),
                        new LatLng(37.1772, -3.5924),
                        new LatLng(37.1773, -3.5924),
                        new LatLng(37.1773, -3.5921),
                        new LatLng(37.1774, -3.5921),
                        new LatLng(37.1774, -3.5920),
                        new LatLng(37.1773, -3.5920),
                        new LatLng(37.1773, -3.5919),
                        new LatLng(37.1775, -3.5913),
                        new LatLng(37.1775, -3.5914),
                        new LatLng(37.1775, -3.5912),
                        new LatLng(37.1775, -3.5911),
                        new LatLng(37.1768, -3.5912),
                        new LatLng(37.1766, -3.5909),
                        new LatLng(37.1764, -3.5909),
                        new LatLng(37.1763, -3.5909))
                .strokeColor(Color.RED).fillColor(0x85DFAD24)
                .strokeWidth(5)
                .geodesic(true);



        Polygon polygon2 = nMap.addPolygon(recOption2);
        polygon2.setTag("poligono2");
        polygon2.setClickable(true);

        nMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                String cases = (String)polygon.getTag();
                switch((String)polygon.getTag()){
                    case "poligono1":
                        try {
                            image.setImageDrawable(Drawable.createFromStream( getActivity().getAssets().open("fuente.jpg"),null) );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (image.getVisibility() == View.INVISIBLE)
                            image.setVisibility(View.VISIBLE);
                        else
                            image.setVisibility(View.INVISIBLE);
                        break;

                    case "poligono2":
                        try {
                            image.setImageDrawable(Drawable.createFromStream( getActivity().getAssets().open("palacio.jpg"),null) );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (image.getVisibility() == View.INVISIBLE)
                            image.setVisibility(View.VISIBLE);
                        else
                            image.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        Toast.makeText(getContext(), "Zona no registrada", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });

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

        nMap.setMinZoomPreference(nMap.getCameraPosition().zoom+2);

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
        //nMap.addMarker(new MarkerOptions().position(sydney).title("Estoy mamadisimo hdtpm").icon(BitmapDescriptorFactory.defaultMarker(HUE_VIOLET)));
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

            marker = nMap.addMarker(new MarkerOptions().position(sydney).title("Soy kraken").icon(BitmapDescriptorFactory.defaultMarker(HUE_VIOLET)));
            float zoomlevel = nMap.getCameraPosition().zoom;
            //nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomlevel));
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