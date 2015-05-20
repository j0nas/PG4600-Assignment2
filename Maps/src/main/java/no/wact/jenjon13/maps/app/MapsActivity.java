package no.wact.jenjon13.maps.app;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements TaskListener {
    private final Map<String, String> markerData = new HashMap<>();
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
        Toast.makeText(getApplicationContext(), "Fetching location, please standby..", Toast.LENGTH_SHORT).show();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                mMap.clear();

                // Marker noting current position. Not really needed since it's shown on the map anyway.
                //mMap.addMarker(new MarkerOptions().position(latLng).title("Current position").flat(true));

                mMap.setOnMyLocationChangeListener(null);
                new NearbyPublicTransportStopsFetcherTask(MapsActivity.this, latLng).execute();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ((TextView) MapsActivity.this.findViewById(R.id.mapMarkerData))
                        .setText(markerData.get(marker.getId()));
                return false;
            }
        });
    }

    private String parseJSONstopData(JSONObject json) throws JSONException {
        String[] transportationNames = new String[]
                {"Walking", "AirportBus", "Bus", "(dummy value)", "AirportTrain", "Boat", "Train", "Tram", "Metro"};

        List<String>[] transportationTypes = new List[transportationNames.length];
        for (int i = 0; i < transportationTypes.length; i++) {
            transportationTypes[i] = new ArrayList<String>();
        }

        final JSONArray lines = json.getJSONArray("Lines");
        for (int i = 0; i < lines.length(); i++) {
            final JSONObject object = lines.getJSONObject(i);
            transportationTypes[object.getInt("Transportation")].add(object.getString("Name"));
        }

        final StringBuilder builder = new StringBuilder();
        boolean contentAlreadyAdded = false;
        for (int i = 0; i < transportationTypes.length; i++) {
            if (transportationTypes[i].size() > 0) {
                if (contentAlreadyAdded) {
                    builder.append("\n");
                }

                builder.append(transportationNames[i]).append(": ");
                for (String name : transportationTypes[i]) {
                    builder.append(name).append(" ");
                }

                contentAlreadyAdded = true;
            }
        }

        return builder.toString();
    }

    @Override
    public void onTaskCompleted(String json) {
        try {
            final JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                double[] latLon = new CoordinateConversion().utm2LatLon(String.format("32 V %d %d",
                        jsonArray.getJSONObject(i).getInt("X"),
                        jsonArray.getJSONObject(i).getInt("Y")));

                final Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latLon[0], latLon[1]))
                        .title(jsonArray.getJSONObject(i).getString("Name")));

                markerData.put(marker.getId(), parseJSONstopData(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
