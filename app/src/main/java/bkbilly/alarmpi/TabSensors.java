package bkbilly.alarmpi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by bkbilly on 28/2/2017.
 */

public class TabSensors extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static View rootView;
    private static SwipeRefreshLayout mSwipeRefreshLayout;
    private static FragmentActivity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.tab_sensors, container, false);
        context = getActivity();


        // Refresh View
        rootView = inflater.inflate(R.layout.tab_sensors, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        this.RefreshMe();
        return rootView;
    }

    public static void RefreshMe(){
        mSwipeRefreshLayout.setRefreshing(true);
        new getAlarmStatus().execute(MainActivity.getCreatedURL(), "/getSensors.json", MainActivity.getUsername(), MainActivity.getPassword());
    }

    @Override
    public void onRefresh() {
        TabLogs.RefreshMe();
        TabSensors.RefreshMe();
        MainActivity.RefreshMe();
    }

    public static class getAlarmStatus extends getJSON
    {
        @Override
        protected void onPostExecute(JSONObject response)
        {
            mSwipeRefreshLayout.setRefreshing(true);
            ArrayList<String> myStringArray1 = new ArrayList<String>();
            JSONObject jsonData = new JSONObject();
            JSONArray jsonDataArray = new JSONArray();
            ListView myListView = (ListView) rootView.findViewById(R.id.sensors_list);
            myListView.setAdapter(null);
            if(response != null) {
                try {
                    Log.w("test", "Success: " + response );
                    jsonData = response.getJSONObject("sensors");
                    Log.w("TabLogs", "Success: " + jsonData );
                    Log.e("test", "--------------------------");
                    Iterator<String> keys = jsonData.keys();
//                    for(int i=0; i<keys.length(); i++){
                    while(keys.hasNext()){
                        JSONObject obj = new JSONObject();
                        String sensor = (String)keys.next();
                        Log.e("test", "Successsssssss: " + jsonData );
                        JSONObject jsonOBject = jsonData.getJSONObject(sensor);
                        myStringArray1.add(jsonOBject.getString("name"));

                        obj.put("sensor", sensor);
                        obj.put("active", jsonOBject.getBoolean("enabled"));
                        obj.put("name", jsonOBject.getString("name"));
                        obj.put("alert", jsonOBject.getBoolean("alert"));
                        obj.put("online", jsonOBject.getBoolean("online"));
                        jsonDataArray.put(obj);
                    }
                    Log.e("test", "--------------------------");
                    // Start
                    myListView = (ListView) rootView.findViewById(R.id.sensors_list);
                    CustomList adapter = new CustomList(context, myStringArray1, jsonDataArray);
                    myListView.setAdapter(adapter);
                    // END...
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public static class CustomList extends ArrayAdapter<String> {

        private final Activity context;
        private final JSONArray jsonData;
        public CustomList(Activity context, ArrayList<String> web, JSONArray jsonData) {
            super(context, R.layout.list_simple, web);
            LayoutInflater inflater = context.getLayoutInflater();
            this.context = context;
            this.jsonData = jsonData;

        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.list_simple, null, true);
            try {
                final JSONObject jsonOBject = jsonData.getJSONObject(position);
                LinearLayout li=(LinearLayout) rowView.findViewById(R.id.list_sensor_div);
                TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

                txtTitle.setText(jsonOBject.getString("name"));

                Switch myswitch = (Switch) rowView.findViewById(R.id.switch1);
                myswitch.setChecked(jsonOBject.getBoolean("active"));
                if (jsonOBject.getBoolean("online") == false){
                    li.setBackgroundResource(R.drawable.bg_sensor_offline);
                } else if (jsonOBject.getBoolean("active") == false){
                    li.setBackgroundResource(R.drawable.bg_sensor_inactive);
                } else if (jsonOBject.getBoolean("alert") == true){
                    li.setBackgroundResource(R.drawable.bg_sensor_alert);
                } else {
                    li.setBackgroundResource(R.drawable.bg_sensor_noalert);
                }

                myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        try {
                            String sensorName = jsonOBject.getString("sensor");
                            boolean active = isChecked;

                            Log.w("SWITCH", "name: " + jsonOBject.getString("name") + ", Sensor: " + sensorName + " State: " + active);
                            new getJSON().execute(MainActivity.getCreatedURL(), "/setSensorStateOnline?sensor=" + sensorName +"&enabled=" + active, MainActivity.getUsername(), MainActivity.getPassword());
                            RefreshMe();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return rowView;
        }
    }


}