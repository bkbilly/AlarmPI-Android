package bkbilly.alarmpi;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by bkbilly on 5/3/2017.
 */

public class DisplayMessageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

//        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("MYLABEL", "myStringToSave").commit();
//        PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound");

    }


}
