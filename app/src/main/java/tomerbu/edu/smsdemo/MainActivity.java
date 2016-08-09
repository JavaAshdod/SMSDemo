package tomerbu.edu.smsdemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_SEND_SMS = 10;
    private FloatingActionButton fab;
    EditText etPhone;
    EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        etMessage = (EditText) findViewById(R.id.etMessage);
        etPhone = (EditText) findViewById(R.id.etPhone);

        if (!checkSMSPermissions()) {
            requestSendSMSPermission();
        }
    }

    private boolean checkSMSPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) ==
                    PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            return false;

        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestSendSMSPermission() {
        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_SEND_SMS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_SEND_SMS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            }
        }
    }

    private void sendSMS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkSMSPermissions()) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                    Toast.makeText(MainActivity.this, "Please give us the permission", Toast.LENGTH_SHORT).show();
                    requestSendSMSPermission();
                }
                else {
                     Snackbar.make(fab, "Settings", Snackbar.LENGTH_INDEFINITE).setAction("Settings", new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             Uri uri = Uri.parse("package:tomerbu.edu.smsdemo");
                             Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);
                             startActivity(intent);
                         }
                     }).show();
                }
            }

            return;
        }
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(
                etPhone.getText().toString(),
                null, etMessage.getText().toString(),
                null,
                null
        );
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //Fab clicked
    @Override
    public void onClick(View view) {
        sendSMS();
    }
}
