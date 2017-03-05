package bkbilly.alarmpi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static FloatingActionButton fab;
    private static boolean alarmStatus;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        new getAlarmStatus().execute("https://spinet.asuscomm.com:5003/alertpins.json");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("fabResources", String.valueOf(fab.getResources()));
                if (alarmStatus == false) {
                    Snackbar.make(view, "Activating Alarm", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    new getJSON().execute("https://spinet.asuscomm.com:5003/activateAlarmOnline");
                } else {
                    Snackbar.make(view, "Deactivating Alarm", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    new getJSON().execute("https://spinet.asuscomm.com:5003/deactivateAlarmOnline");
                }
                new getAlarmStatus().execute("https://spinet.asuscomm.com:5003/alertpins.json");
            }
        });

    }
    public static void RefreshMe(){
        new getAlarmStatus().execute("https://spinet.asuscomm.com:5003/alertpins.json");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(this, DisplayMessageActivity.class);
                startActivity(intent);
                return true;

            case R.id.menu_refresh:
                TabLogs.RefreshMe();
                TabSensors.RefreshMe();
                MainActivity.RefreshMe();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    TabSensors tabSensors = new TabSensors();
                    return tabSensors;
                case 1:
                    TabLogs tabLogs = new TabLogs();
                    return tabLogs;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Sensors";
                case 1:
                    return "Logs";
            }
            return null;
        }
    }
    public static class getAlarmStatus extends getJSON
    {
        @Override
        protected void onPostExecute(JSONObject response)
        {
            if(response != null) {
                Log.e("App", "Success: " + response );
                try {
                    alarmStatus = response.getBoolean("alarmArmed");
                    Log.e("getAlarmStatus", "Success: " + alarmStatus );
                    if (alarmStatus == false){
                        fab.setImageResource(R.drawable.ic_unlocked);
                    } else {
                        fab.setImageResource(R.drawable.ic_locked);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
