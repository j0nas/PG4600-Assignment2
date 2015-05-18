package no.wact.jenjon13.maps.app;

import android.os.AsyncTask;
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

class WeatherInfoTask extends AsyncTask<String, String, String> {
    @Override
    protected void onPostExecute(String s) {
        //Log.w("ASYNCTASK", s);
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
