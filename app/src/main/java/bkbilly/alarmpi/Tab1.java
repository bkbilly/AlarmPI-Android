package bkbilly.alarmpi;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by bkbilly on 28/2/2017.
 */

public class Tab1 extends Fragment {
    private Socket mSocket;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new getJSONf().execute("https://192.168.2.164:5003/alarmStatus.json");
        new getJSONf().execute("https://192.168.2.164:5003/alertpins.json");
        new getJSONf().execute("https://www.google.gr");
        mSocket = new ChatApplication().getSocket();
        mSocket.on("sensorsLog", onSesnorsLog);
        mSocket.on("settingsChanged", onSesnorsLog);
//        Log.e("myTag", "This is 1my message2222");


        View rootView = inflater.inflate(R.layout.tab1contacts, container, false);
        return rootView;
    }
    private Emitter.Listener onSesnorsLog = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("myTag", "This is my message");
            //JSONObject data = (JSONObject) args[0];
        }
    };

    public class getJSONf extends getJSON
    {
        @Override
        protected void onPostExecute(JSONObject response)
        {
            if(response != null) {
            Log.e("App", "Success: " + response );
            }
        }
    }

}
