package bkbilly.alarmpi;

import android.os.AsyncTask;
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
        HttpURLConnection c = null;
        BufferedReader bufferedReader = null;
        try
        {
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

            URL url = new URL(urls[0]);
            if(urls[0].toLowerCase().startsWith("https")){
                String encoded = Base64.encodeToString("username:password".getBytes("UTF-8"), Base64.DEFAULT);
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);

                HttpsURLConnection myConnection  = (HttpsURLConnection) url.openConnection();
                myConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                myConnection.setRequestProperty("Authorization", "Basic "+encoded);
                myConnection.setRequestProperty("Content-Type","0");
                myConnection.setRequestMethod("GET");
                myConnection.setUseCaches(false);
                myConnection.setConnectTimeout(10000);
                myConnection.setReadTimeout(10000);
                myConnection.setAllowUserInteraction(false);
                myConnection.setInstanceFollowRedirects(true);
                myConnection.connect();
                int status = myConnection.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        json = sb.toString();
                        try {
                            jObj = new JSONObject(json);
                        } catch (JSONException ex) {
                            Log.e("App", "Not JSON Data: " + url);
                        };
                }
            } else {
                HttpURLConnection myConnection  = (HttpURLConnection) url.openConnection();
                myConnection.setRequestProperty("Content-Type","0");
                myConnection.setRequestMethod("GET");
                myConnection.setUseCaches(false);
                myConnection.setConnectTimeout(10000);
                myConnection.setReadTimeout(10000);
                myConnection.setAllowUserInteraction(false);
                myConnection.setInstanceFollowRedirects(true);
                myConnection.connect();
                int status = myConnection.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        json = sb.toString();
                        try {
                            jObj = new JSONObject(json);
                        } catch (JSONException ex) {
                            Log.e("App", "Not JSON Data: " + url);
                        };
                }
            }
            return jObj;
        } catch(Exception ex) {
            Log.e("App", "getJSON", ex);
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
//            if(response != null) {
        Log.e("App", "Success: " + response );
//            }
    }
}