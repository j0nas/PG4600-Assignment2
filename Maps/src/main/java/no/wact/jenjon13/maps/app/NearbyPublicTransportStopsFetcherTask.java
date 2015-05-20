package no.wact.jenjon13.maps.app;

import android.os.AsyncTask;
import com.google.android.gms.maps.model.LatLng;
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

class NearbyPublicTransportStopsFetcherTask extends AsyncTask<String, String, String> {
    private final TaskListener listener;
    private final LatLng latLng;

    public NearbyPublicTransportStopsFetcherTask(TaskListener listener, LatLng latLng) {
        this.listener = listener;
        this.latLng = latLng;
    }

    @Override
    protected void onPostExecute(String json) {
        listener.onTaskCompleted(json);
    }

    @Override
    protected String doInBackground(String... params) {
        String[] split = new CoordinateConversion().latLon2UTM(latLng.latitude, latLng.longitude).split(" ");
        String x = split[2];
        String y = split[3];

        URI uri;
        try {
            String addr = "http://reisapi.ruter.no/Place/GetClosestStops";
            List<NameValuePair> uriParams = new ArrayList<>();
            uriParams.add(new BasicNameValuePair("proposals", "5"));
            uriParams.add(new BasicNameValuePair("Coordinates", String.format("(x=%s,y=%s)", x, y)));
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
