package no.wact.jenjon13.maps.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        if (mMap == null && (mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap()) != null) {
            setUpMap();
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        final LatLng position = new LatLng(59.951458, 10.7426095);
        final MarkerOptions home = new MarkerOptions().position(position).title("Home");
        mMap.addMarker(home);

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(home.getPosition(), 17, 0, 0)));
        new WeatherInfoTask().execute();
    }

    private class WeatherInfoTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            Log.w("ASYNCTASK", s);
        }

        @Override
        protected String doInBackground(String... params) {
            URI uri;
            try {
                String addr = "http://api.yr.no/weatherapi/locationforecast/1.9/"; //?lat=59.951458;lon=10.7426095
                List<NameValuePair> uriParams = new ArrayList<>();
                uriParams.add(new BasicNameValuePair("lat", "59.951458"));
                uriParams.add(new BasicNameValuePair("lon", "10.7426095"));
                uri = new URI(addr + "?" + URLEncodedUtils.format(uriParams, "utf-8"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null;
            }

            try {
                final ResponseHandler<String> responseHandler = new BasicResponseHandler();
                return new DefaultHttpClient().execute(new HttpGet(uri), responseHandler).toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
