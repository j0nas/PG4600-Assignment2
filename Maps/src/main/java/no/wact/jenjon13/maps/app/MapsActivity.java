package no.wact.jenjon13.maps.app;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;

public class MapsActivity extends FragmentActivity implements TaskListener {
    private GoogleMap mMap;

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
        Toast.makeText(getApplicationContext(), "Fetching locating, please standby..", Toast.LENGTH_SHORT).show();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Current position").flat(true));

                mMap.setOnMyLocationChangeListener(null);
                new NearbyPublicTransportStopsFetcherTask(MapsActivity.this, latLng).execute();
            }
        });

    }

    @Override
    public void onTaskCompleted(String json) {
        try {
            final JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.w("ASYNCTASK", jsonArray.getJSONObject(i).getString("Name") + " -- " + jsonArray.get(i).toString());

                double[] latLon = new CoordinateConversion().utm2LatLon(String.format("32 V %d %d",
                        jsonArray.getJSONObject(i).getInt("X"),
                        jsonArray.getJSONObject(i).getInt("Y")));

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latLon[0], latLon[1]))
                        .title(jsonArray.getJSONObject(i).getString("Name")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
