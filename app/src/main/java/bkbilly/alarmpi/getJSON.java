package bkbilly.alarmpi;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;

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

/**
 * Created by bkbilly on 3/3/2017.
 */


public class getJSON extends AsyncTask<String, Void, JSONObject>
{
    @Override
    protected JSONObject doInBackground(String... urls)
    {
        JSONObject jObj = null;
        String json = "";
        int status = 0;
        BufferedReader br = null;
        HttpURLConnection c = null;
        BufferedReader bufferedReader = null;
        try {
            HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            Log.w("getJSON URL", urls[0] + urls[1]);
            if (urls[0] != null) {
                URL url = new URL(urls[0] + urls[1]);
                String userpass = urls[2] + ":" + urls[3];
                Log.w("getJSON", "UserPass: " + userpass);
                if (urls[0].toLowerCase().startsWith("https")) {
                    try {
                        HttpsURLConnection myConnection = (HttpsURLConnection) url.openConnection();
                        SSLContext sc = SSLContext.getInstance("SSL");
                        sc.init(null, trustAllCerts, new java.security.SecureRandom());
                        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                        HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);
                        myConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                        String encoded = Base64.encodeToString(userpass.getBytes("UTF-8"), Base64.DEFAULT);
                        myConnection.setRequestProperty("Authorization", "Basic " + encoded);
                        myConnection.setRequestProperty("Content-Type", "0");
                        myConnection.setRequestMethod("GET");
                        myConnection.setUseCaches(false);
                        myConnection.setConnectTimeout(5000);
                        myConnection.setReadTimeout(5000);
                        myConnection.setAllowUserInteraction(false);
                        myConnection.setInstanceFollowRedirects(true);

                        myConnection.connect();
                        status = myConnection.getResponseCode();
                        switch (status) {
                            case 200:
                            case 201:
                                br = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                        }
                    } catch (java.net.SocketTimeoutException e) {
                        Log.e("GetJSON", "Timeout");
                    }
                } else {
                    try {
                        HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();

                        String encoded = Base64.encodeToString(userpass.getBytes("UTF-8"), Base64.DEFAULT);
                        myConnection.setRequestProperty("Authorization", "Basic " + encoded);
                        myConnection.setRequestProperty("Content-Type", "0");
                        myConnection.setRequestMethod("GET");
                        myConnection.setUseCaches(false);
                        myConnection.setConnectTimeout(5000);
                        myConnection.setReadTimeout(5000);
                        myConnection.setAllowUserInteraction(false);
                        myConnection.setInstanceFollowRedirects(true);

                        myConnection.connect();
                        status = myConnection.getResponseCode();
                        switch (status) {
                            case 200:
                            case 201:
                                br = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                        }
                    } catch (java.net.SocketTimeoutException e) {
                        Log.e("GetJSON", "Timeout");
                    }
                }
                Log.w("getJSON", "Status: " + String.valueOf(status));
                switch (status) {
                    case 201:
                    case 200:
                        StringBuilder sb = new StringBuilder();
                        String line;
                        if (br != null) {
                            while ((line = br.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                            br.close();
                        }
                        json = sb.toString();
                        try {
                            jObj = new JSONObject(json);
                        } catch (JSONException ex) {
                            Log.e("getJSON", "Not JSON Data: " + url);
                        }
                    case 401:
                        Log.e("getJSON", "Wrong Authentication");
                }
            } else {
                Log.e("getJSON", "Empty URL");
            }
            return jObj;
        } catch(Exception ex) {
            Log.e("getJSON", "Wrong URL");
            return null;
        } finally {
            if(bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(JSONObject response)
    {
        Log.w("getJSON", "Success: " + response );
    }
}