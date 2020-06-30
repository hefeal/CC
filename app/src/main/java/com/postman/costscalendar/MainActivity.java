package com.postman.costscalendar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int SMS_PERMISSION_CODE = 100;
    int n_day;
    int n_month;
    int n_year;
    String v_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        v_date = df.format(c);
        DatabaseHandler dbh = new DatabaseHandler(this);
        dbh.InitDbTables();
        CalendarView calender;
        calender = (CalendarView)findViewById(R.id.calendarView);
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
            n_day = dayOfMonth;
            n_month = month+1;
            n_year = year;
            v_date = String.format("%04d", year) + "-" +  String.format("%02d", month+1)
                     + "-" + String.format("%02d", dayOfMonth);
            Log.d("Calendar", v_date);
            }
        });
    }

    public void OnClickDaysCosts(View view)
    {
        Intent intent = new Intent(this, DaysCosts.class);

        intent.putExtra("day",n_day  );
        intent.putExtra("month",n_month  );
        intent.putExtra("year",n_year  );
        intent.putExtra("date",v_date  );
        startActivity(intent);
    }

    public void OnClickDict(View view)
    {
        Intent intent = new Intent(this, Dict.class);
        startActivity(intent);
    }

    public void OnClickReports(View view)
    {
        Intent intent = new Intent(this, Reports.class);
        startActivity(intent);
    }

    public void OnClickSms(View view)
    {
        if (IsSmsPermissionGranted()) {
            Intent intent = new Intent(this, SmsParser.class);
            startActivity(intent);
        }
        else
        {
            Log.d("main", "per_first");
            RequestReadAndSendSmsPermission();
        }
    }

    public boolean IsSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
            // You may display a non-blocking explanation here, read more in the documentation:
            // https://developer.android.com/training/permissions/requesting.html
        }
        Log.d("main", "per_req");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("main", "per_overide");
        Log.d("main", String.valueOf(requestCode));
        Log.d("main", String.valueOf(grantResults.length));
        Log.d("main", String.valueOf(grantResults[0]));
        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
