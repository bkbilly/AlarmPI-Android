package bkbilly.alarmpi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

/**
 * Created by bkbilly on 5/3/2017.
 */

public class settingsClass extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        SharedPreferences prefs = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences("MyPrefsFile", MODE_PRIVATE).edit();

        final EditText settingsURLButton = (EditText)findViewById(R.id.settingsURL);
        final EditText settingsPortButton = (EditText)findViewById(R.id.settingsPort);
        final Switch settingsHTTPSButton = (Switch) findViewById(R.id.settingsHTTPS);
        final EditText settingsUserNameButton = (EditText)findViewById(R.id.settingsUserName);
        final EditText settingsPasswordButton = (EditText)findViewById(R.id.settingsPassword);

        settingsURLButton.setText(prefs.getString("settingsURL", null));
        settingsPortButton.setText(prefs.getString("settingsPort", null));
        settingsHTTPSButton.setChecked(prefs.getBoolean("settingsHTTPS", true));
        settingsUserNameButton.setText(prefs.getString("settingsUserName", null));
        settingsPasswordButton.setText(prefs.getString("settingsPassword", null));

        Button settingsButtonOK = (Button) findViewById(R.id.settingsOK);
        settingsButtonOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String settingsURL = settingsURLButton.getText().toString().trim();
                String settingsPort = settingsPortButton.getText().toString().trim();
                boolean settingsHTTPS = settingsHTTPSButton.isChecked();
                String settingsUserName = settingsUserNameButton.getText().toString();
                String settingsPassword = settingsPasswordButton.getText().toString();

                Log.e("settingsClass", settingsHTTPS + settingsURL + ":" + settingsPort);

                editor.putString("settingsURL", settingsURL);
                editor.putString("settingsPort", settingsPort);
                editor.putBoolean("settingsHTTPS", settingsHTTPS);
                editor.putString("settingsUserName", settingsUserName);
                editor.putString("settingsPassword", settingsPassword);
                editor.commit();
                finish();
                MainActivity.RefreshMe();
                TabLogs.RefreshMe();
                TabSensors.RefreshMe();
            }
        });

        Button settingsButtonCancel = (Button) findViewById(R.id.settingsCancel);
        settingsButtonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });




    }


}
