package no.wact.jenjon13.maps.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null && (mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap()) != null) {
            setUpMap();
        }
    }

    private void setUpMap() {
        final LatLng position = new LatLng(59.951458, 10.7426095);
        final MarkerOptions home = new MarkerOptions().position(position).title("Home");
        mMap.addMarker(home);

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(home.getPosition(), 17, 0, 0)));
        new WeatherInfoTask().execute();
    }
}
