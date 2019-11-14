package com.example.sensoresAlhambra.ui.navigation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

import java.io.IOException;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_VIOLET;

public class NavigationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private GoogleApiClient googleApiClient;

    /**
     * Distancia entre updates en metros
     */
    private static final long MIN_CAMBIO_DISTANCIA_PARA_UPDATES = 10; // 10 metros

    /**
     * Tiempo entre updates en milisegundos
     */
    private static final long MIN_TIEMPO_ENTRE_UPDATES = 1000 * 5 * 1; //1 minuto
    private GoogleMap nMap;
    private ImageView image;


    /**
     * Creamos la vista y establecemos conexión a través de la API de Google
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_navigation, container, false);
        googleApiClient = new GoogleApiClient.Builder(getContext(), this, this).addApi(LocationServices.API).build();



        // Establecemos conexión

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext());

        if (status == ConnectionResult.SUCCESS) {

            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.fragment_dashboard);
            mapFragment.getMapAsync(this);

        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getActivity().getApplicationContext(), 10);
            dialog.show();
        }


        // Especificamos que las imágenes extras sean invisibles por ahora
        image = (ImageView) root.findViewById(R.id.imageNavigation);
        image.setVisibility(View.INVISIBLE);

        return root;
    }

    /**
     * Establecemos el estilo del mapa,
     * las zonas resaltadas,
     * click-listener para las zonas,
     * límites en el mapa (desplazamiento y zoom) y
     * los intervalos entre las actualizaciones de posición
     * @param googleMap base del mapa
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        nMap = googleMap;


        //Establece el estilo del mapa desde el JSON, creado a través de GoogleStyleWizard
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


        //Creamos las zonas resaltadas a través de puntos

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

        // Añadimos al mapa y establecemos como clickeable

        Polygon polygon = nMap.addPolygon(recOption);
        polygon.setTag("poligono1");
        polygon.setClickable(true);


        // Repetimos el proceso con la segunda zona

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


        // Creamos el listener para los click de las zonas resaltadas

        nMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                // Dependiendo de la zona resaltada, identificada por su tag, enseñamos la imagen correspondiente o ocultamos la actual si es la misma
                switch((String)polygon.getTag()){
                    case "poligono1":
                        try {
                            image.setImageDrawable(Drawable.createFromStream( getActivity().getAssets().open("machuca.png"),null) );
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
                            image.setImageDrawable(Drawable.createFromStream( getActivity().getAssets().open("alcazaba.png"),null) );
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



        // Establecemos unos límites en el mapa para que el usuario no se desplace fuera de la Alhambra

        LatLng one = new LatLng(37.1783, -3.5924);
        LatLng two = new LatLng(37.1738, -3.5847);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        // Añadimos los límites al constructor de límites
        builder.include(one);
        builder.include(two);

        LatLngBounds bounds = builder.build();


        // Obtenemos el tamaño de la pantalla
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        // Añadimos un 20% de márgen
        int padding = (int) (width * 0.20);

        // Establecemos limites en el mapa
        nMap.setLatLngBoundsForCameraTarget(bounds);

        // Movemos la cámara el lugar predeterminado
        nMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

        // También establecemos un límite en el zoom para que el usuario no sea capaz de alejarse demasiado
        nMap.setMinZoomPreference(nMap.getCameraPosition().zoom+2);



        // Ahora vamos a establecer las actualizaciones de posición en el mapa

        //  Primero creamos un manager para la localización
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //  Hacemos una petición de actualización con el listener y el intervalo específicado
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIEMPO_ENTRE_UPDATES, MIN_CAMBIO_DISTANCIA_PARA_UPDATES, locListener, Looper.getMainLooper());

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

    /**
     * Listener para la localización
     * Actualiza la posición y el marker que indica donde está el usuario
     */

    public LocationListener locListener = new LocationListener() {
        /**
         * Marker con la posición exacta del usuario
         */
        Marker marker;

        /**
         * Update de localización. Actulizamos el marker.
         * @param location nueva posición del usuario
         */
        public void onLocationChanged(Location location) {


            // Si existe un marker anterior lo borramos
            if(marker != null)
                marker.remove();

            // Obtenemos nueva posición
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            LatLng new_pos = new LatLng(latitude, longitude);


            // Añadimos marker en la nueva posición
            marker = nMap.addMarker(new MarkerOptions().position(new_pos).title("Yo").icon(BitmapDescriptorFactory.defaultMarker(HUE_VIOLET)));

            //Descomentar si queremos que la cámara se mueva con la posición
                //float zoomlevel = nMap.getCameraPosition().zoom;
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