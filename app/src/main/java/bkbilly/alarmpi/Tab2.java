package bkbilly.alarmpi;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import static android.R.id.list;

/**
 * Created by bkbilly on 28/2/2017.
 */

public class Tab2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public ArrayList<String> myStringArray1;
    public ArrayAdapter<String> adapter;
    SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2contacts, container, false);


        // Refresh View
        View view = inflater.inflate(R.layout.tab2contacts, container, false);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);

        // START******
        new getAlarmStatus().execute("https://192.168.2.164:5003/alertpins.json");
        ListView myListView = (ListView) rootView.findViewById(list);
        myStringArray1 = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, myStringArray1);
        myListView.setAdapter(adapter);
        // END********

        return rootView;
    }

    @Override
    public void onRefresh() {
        Log.e("refresh: ", "onRefresh called from SwipeRefreshLayout");
    }


    public class getAlarmStatus extends getJSON
    {
        @Override
        protected void onPostExecute(JSONObject response)
        {
            if(response != null) {
                Log.e("App", "Success: " + response );
                try {
                    JSONArray jsonData = response.getJSONArray("sensors");
                    Log.e("App", "Success: " + jsonData );
                    for(int i=0; i<jsonData.length(); i++){
                        JSONObject jsonOBject = jsonData.getJSONObject(i);
                        myStringArray1.add(jsonOBject.getString("name"));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}