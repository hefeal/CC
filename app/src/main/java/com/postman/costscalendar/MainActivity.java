package com.postman.costscalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.CalendarView;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
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
}
