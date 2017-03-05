package bkbilly.alarmpi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by bkbilly on 28/2/2017.
 */

public class TabLogs extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static View rootView;
    private static SwipeRefreshLayout mSwipeRefreshLayout;
    public static ArrayList<String> myStringArray1;
    public static ArrayAdapter<String> adapter;
    private static FragmentActivity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_logs, container, false);
        context = getActivity();

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        this.RefreshMe();

        return rootView;
    }
    public static void RefreshMe(){
        mSwipeRefreshLayout.setRefreshing(true);
        new getLogs().execute("https://spinet.asuscomm.com:5003/sensorsLog.json?limit=100");
    }

    @Override
    public void onRefresh() {
        TabLogs.RefreshMe();
        TabSensors.RefreshMe();
        MainActivity.RefreshMe();
    }

    public static class getLogs extends getJSON
    {
        @Override
        protected void onPostExecute(JSONObject response)
        {
            mSwipeRefreshLayout.setRefreshing(true);
            myStringArray1 = new ArrayList<String>();
            ListView myListView = (ListView) rootView.findViewById(R.id.logs_list);
            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, myStringArray1);
            myListView.setAdapter(adapter);

            JSONArray jsonData = new JSONArray();
            if(response != null) {
                Log.e("App", "Success: " + response );
                try {
                    jsonData = response.getJSONArray("log");
                    for(int i=0; i<jsonData.length(); i++){
                        Log.e("App", "Succesds: " + jsonData.getString(i) );
                        myStringArray1.add(jsonData.getString(i));
                        adapter.notifyDataSetChanged();
                    }
                    Collections.reverse(myStringArray1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

}