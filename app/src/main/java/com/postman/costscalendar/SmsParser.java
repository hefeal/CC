package com.postman.costscalendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SmsParser extends AppCompatActivity {

    TextView txt_from, txt_to;
    String dt_from, dt_to;
    Calendar calendar;
    int n_year_from, n_month_from, n_day_from, n_year_to, n_month_to, n_day_to;
    private final long day_in_millisec = 86399999;
    DatabaseHandler db;
    List<TableSmsTempRow> sms_temps;
    int total_sms;
    int success_sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_parcer);

        calendar = Calendar.getInstance();
        n_year_from = calendar.get(Calendar.YEAR);
        n_month_from = calendar.get(Calendar.MONTH);
        n_day_from = calendar.get(Calendar.DAY_OF_MONTH);
        n_year_to = n_year_from;
        n_month_to = n_month_from;
        n_day_to = n_day_from;
        dt_from = String.format("%04d", n_year_from)
                + "-" + String.format("%02d", n_month_from+1)
                + "-" + String.format("%02d", n_day_from);
        txt_from = findViewById(R.id.txt_from);
        txt_to = findViewById(R.id.txt_to);
        txt_from.setText(dt_from);
        dt_to = dt_from;
        txt_to.setText(dt_to);

        db = new DatabaseHandler(this);
        sms_temps = db.SelectTableSmsTemp(null);

        for (int i = 0; i < sms_temps.size(); i++)
        {
            LayoutInflater lif = getLayoutInflater();
            LinearLayout linLayout = (LinearLayout) findViewById(R.id.linear_template);
            final View view = lif.inflate(R.layout.template_piece, linLayout, false);
            view.setId(sms_temps.get(i).getId_sms_temp());
            linLayout.addView(view);

            EditText txt_phone = view.findViewById(R.id.phone);
            txt_phone.setText(sms_temps.get(i).getV_phone_num());

            EditText txt_before = view.findViewById(R.id.before);
            txt_before.setText(sms_temps.get(i).getV_str_before_sum());

            EditText txt_after = view.findViewById(R.id.after);
            txt_after.setText(sms_temps.get(i).getV_str_after_sum());

            TextView textView = view.findViewById(R.id.status);
            textView.setText(this.getResources().getString(R.string.txt_status_saved));

            Button btn_save = view.findViewById(R.id.btn_save);

            btn_save.setOnClickListener(new View.OnClickListener() {
                public void onClick(View V) {
                    OnClickSaveTemp(view);
                }
            });

            Button btn_delete = view.findViewById(R.id.btn_delete);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   OnClickDeleteTemp(view);
                }
            });
        }


    }

    void OnClickSaveTemp(View view)
    {
        int id = view.getId();
        TableSmsTempRow row = new TableSmsTempRow();
        //DatabaseHandler dbh = new DatabaseHandler(this);
        EditText txt_phone = view.findViewById(R.id.phone);
        EditText txt_before = view.findViewById(R.id.before);
        EditText txt_after = view.findViewById(R.id.after);
        row.setV_phone_num(txt_phone.getText().toString());
        row.setV_str_before_sum(txt_before.getText().toString());
        row.setV_str_after_sum(txt_after.getText().toString());
        List<TableSmsTempRow> sms_temps_to_save = db.SelectTableSmsTemp("id_sms_temp = " + id);

        if (sms_temps_to_save.size() > 0 )
        {
            //update
            row.setId_sms_temp(id);
            db.UpdateTableSmsTemp(row);
        }
        else
        {
            //insert
            db.InsertTableSmsTemp(row);
        }
        TextView txt_stat = view.findViewById(R.id.status);
        txt_stat.setText(this.getResources().getString(R.string.txt_status_saved));
    }

    void OnClickDeleteTemp(View view)
    {
        int id = view.getId();
        db.DeletTableSmsTemp(id);
        view.setVisibility(View.GONE);
    }

    public void OnClickAddBlankTemp(View view)
    {
        LayoutInflater lif = getLayoutInflater();
        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linear_template);
        final View view_new = lif.inflate(R.layout.template_piece, linLayout, false);
        linLayout.addView(view_new);
        Button btn_save = view_new.findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                OnClickSaveTemp(view_new);
            }
        });

        Button btn_delete = view_new.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickDeleteTemp(view_new);
            }
        });
    }


    public void OnClickFrom(View view)
    {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
               dt_from = String.format("%04d", year) + "-" + String.format("%02d", monthOfYear+1) + "-" + String.format("%02d", dayOfMonth);
               txt_from.setText(dt_from);
               n_year_from = year;
               n_month_from = monthOfYear;
               n_day_from = dayOfMonth;
            }
        },
                n_year_from, n_month_from, n_day_from);
        dialog.show();
    }

    public void OnClickTo(View view)
    {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                dt_to = String.format("%04d", year) + "-" + String.format("%02d", monthOfYear+1) + "-" + String.format("%02d", dayOfMonth);
                txt_to.setText(dt_to);
                n_year_to = year;
                n_month_to = monthOfYear;
                n_day_to =dayOfMonth;
            }
        },
                n_year_to, n_month_to, n_day_to);
        dialog.show();
    }

    public void OnClickSmsParse(View view) {

        total_sms =0;
        success_sms = 0;

        Date date_from = null;
        Date date_to = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date_from = formatter.parse(dt_from);
            date_to = formatter.parse(dt_to);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        DatabaseHandler db = new DatabaseHandler(this);
        List<TableSmsTempRow> sms_row = db.SelectTableSmsTemp(null);
        List<TableCostsRow> c_row = db.SelectTableCosts("dt_date >= '" + dt_from + "' and dt_date <= '"
                + dt_to + "' and n_sms_id is not null",null, null);
        List<Integer> i_list = new ArrayList<>();

        for (int i = 0; i < c_row.size(); i++ )
        {
            i_list.add(c_row.get(i).getN_sms_id());
        }

        String filter = "date >= " + date_from.getTime() + " and date <= " + String.valueOf(date_to.getTime() + day_in_millisec);
        final Uri sms_inbox = Uri.parse("content://sms/inbox");
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cursor = getContentResolver().query(sms_inbox, projection, filter, null, null);

        if (cursor.moveToFirst()) {
            int index_Address = cursor.getColumnIndex("address");
            int index_Person = cursor.getColumnIndex("person");
            int index_Body = cursor.getColumnIndex("body");
            int index_Date = cursor.getColumnIndex("date");
            int index_Type = cursor.getColumnIndex("type");
            int index_id = cursor.getColumnIndex("_id");
            do {
                String str_address = cursor.getString(index_Address);
                int int_person = cursor.getInt(index_Person);
                String str_body = cursor.getString(index_Body);
                long long_date = cursor.getLong(index_Date);
                int int_type = cursor.getInt(index_Type);
                int id = cursor.getInt(index_id);
                Log.d("sms", "Address " + str_address + ", person " + String.valueOf(int_person)
                        + ", body" + str_body + ", date " + String.valueOf(long_date) + ", type" + String.valueOf(int_type)
                        + ", id " + String.valueOf(id));
                //if sms not parsed yet
                if (!InList(id,i_list))
                {
                    for (int i = 0; i < sms_row.size(); i++)
                    {
                        String upper_body = str_body.toUpperCase();
                        String upper_before = sms_row.get(i).v_str_before_sum.toUpperCase();
                        String upper_after = sms_row.get(i).v_str_after_sum.toUpperCase();
                        if (upper_after == "")
                            upper_after = " ";
                        int pos1 = upper_body.indexOf(upper_before);
                        int after_pos = upper_body.indexOf(upper_after);
                        //compare to template
                        if (pos1 != -1
                                && (after_pos != -1 || sms_row.get(i).v_str_after_sum == "")
                                && (after_pos > pos1 || sms_row.get(i).v_str_after_sum == "") )
                        {
                            total_sms++;
                            Log.d("sms", "sms text is: " + str_body);
                            Log.d("sms", "template is " + upper_after + " "+ upper_before);
                            Log.d("sms", "first word in pos " + String.valueOf(pos1));
                            int pos2 = upper_body.indexOf(" ", pos1);
                            Log.d("sms", "first space in pos " + String.valueOf(pos2));
                            int pos3 = upper_body.indexOf(upper_after, pos2+1);
                            Log.d("sms", "end space in pos " + String.valueOf(pos3));
                            if (pos2 == pos3 || pos3 == -1)
                                pos3 = upper_body.length();
                            Log.d("sms", "end of parse in pos " + String.valueOf(pos3));
                            String raw_txt = upper_body.substring(pos2,pos3);
                            Log.d("sms", "raw sum text is: " + raw_txt);
                            raw_txt.replace(" ", "");
                            raw_txt.replace(",", ".");
                            try
                            {
                                float sum = Float.valueOf(raw_txt.trim()).floatValue();
                                Log.d("sms", "success sum " + String.valueOf(sum));

                                Date dt=new Date(long_date);
                                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                                String dt_date = df2.format(dt);
                                TableCostsRow t_cost =  new TableCostsRow();
                                t_cost.setN_sms_id(id);
                                t_cost.setId_type(-10);
                                t_cost.setF_sum(sum);
                                t_cost.setV_comment(str_address + "\n" + str_body);
                                t_cost.setV_date(dt_date);
                                InsertToCosts(t_cost);
                                success_sms++;
                                break;
                            }
                            catch (NumberFormatException nfe)
                            {
                                Log.d("sms", "can't parse sum " + raw_txt);
                            }
                        }
                    }
                }

            } while (cursor.moveToNext());

            if (!cursor.isClosed()) {
                cursor.close();
            }
            PopUpMsg();
        }
    }

    boolean InList(int compare, List<Integer> list)
    {
        boolean res = false;
        for (int i = 0; i < list.size(); i++ )
        {
            if (compare == list.get(i)) {
                res = true;
                return res;
            }

        }
        return res;
    }

    void InsertToCosts(TableCostsRow row)
    {
        db.InsertTableCosts(row);
    }

    void PopUpMsg()
    {
        String title_str = getString(R.string.txt_total_sms) + " " + String.valueOf(total_sms) + "\n"
                +  getString(R.string.txt_success_sms) + " " + String.valueOf(success_sms);
        AlertDialog.Builder dialog_sms = new AlertDialog.Builder(this);
        final TextView txt_v = new TextView(this);
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.margin_20);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.margin_20);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.margin_10);
        txt_v.setLayoutParams(params);
        container.addView(txt_v);
        txt_v.setText(title_str);
        dialog_sms.setTitle(getString(R.string.title_sms_complite));
        dialog_sms.setView(container);
        dialog_sms.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_sms.create().show();
    }

}
