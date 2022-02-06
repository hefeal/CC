package com.postman.costscalendar.activity;

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

import com.postman.costscalendar.R;
import com.postman.costscalendar.dao.DatabaseHandler;
import com.postman.costscalendar.dto.TableCostsRow;
import com.postman.costscalendar.dto.TableSmsTemplateRow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SmsParser extends AppCompatActivity {

    TextView txtFrom, txtTo;
    String dateFrom, dateTo;
    Calendar calendar;
    int yearFrom, monthFrom, dayFrom, yearTo, monthTo, dayTo;
    private final long dayInMillisec = 86399999;
    DatabaseHandler db;
    List<TableSmsTemplateRow> smsTemplateRowList;
    int totalSms;
    int successSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_parcer);

        calendar = Calendar.getInstance();
        yearFrom = calendar.get(Calendar.YEAR);
        monthFrom = calendar.get(Calendar.MONTH);
        dayFrom = calendar.get(Calendar.DAY_OF_MONTH);
        yearTo = yearFrom;
        monthTo = monthFrom;
        dayTo = dayFrom;
        dateFrom = String.format("%04d", yearFrom)
                + "-" + String.format("%02d", monthFrom +1)
                + "-" + String.format("%02d", dayFrom);
        txtFrom = findViewById(R.id.txt_from);
        txtTo = findViewById(R.id.txt_to);
        txtFrom.setText(dateFrom);
        dateTo = dateFrom;
        txtTo.setText(dateTo);

        db = new DatabaseHandler(this);
        smsTemplateRowList = db.SelectTableSmsTemp(null);

        for (int i = 0; i < smsTemplateRowList.size(); i++)
        {
            LayoutInflater lif = getLayoutInflater();
            LinearLayout linLayout = (LinearLayout) findViewById(R.id.linear_template);
            final View view = lif.inflate(R.layout.template_piece, linLayout, false);
            view.setId(smsTemplateRowList.get(i).getSmsTempId());
            linLayout.addView(view);

            EditText txtPhone = view.findViewById(R.id.phone);
            txtPhone.setText(smsTemplateRowList.get(i).getPhoneNumber());

            EditText txtBefore = view.findViewById(R.id.before);
            txtBefore.setText(smsTemplateRowList.get(i).getStringBeforeSum());

            EditText txtAfter = view.findViewById(R.id.after);
            txtAfter.setText(smsTemplateRowList.get(i).getStringAfterSum());

            TextView textView = view.findViewById(R.id.status);
            textView.setText(this.getResources().getString(R.string.txt_status_saved));

            Button btnSave = view.findViewById(R.id.btn_save);

            btnSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View V) {
                    OnClickSaveTemp(view);
                }
            });

            Button btnDelete = view.findViewById(R.id.btn_delete);
            btnDelete.setOnClickListener(new View.OnClickListener() {
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
        TableSmsTemplateRow row = new TableSmsTemplateRow();
        //DatabaseHandler dbh = new DatabaseHandler(this);
        EditText txtPhone = view.findViewById(R.id.phone);
        EditText txtBefore = view.findViewById(R.id.before);
        EditText txtAfter = view.findViewById(R.id.after);
        row.setPhoneNumber(txtPhone.getText().toString());
        row.setStringBeforeSum(txtBefore.getText().toString());
        row.setStringAfterSum(txtAfter.getText().toString());
        List<TableSmsTemplateRow> smsTemplateRowList = db.SelectTableSmsTemp("id_sms_temp = " + id);

        if (smsTemplateRowList.size() > 0 )
        {
            //update
            row.setSmsTempId(id);
            db.UpdateTableSmsTemp(row);
        }
        else
        {
            //insert
            db.InsertTableSmsTemp(row);
        }
        TextView txtStatus = view.findViewById(R.id.status);
        txtStatus.setText(this.getResources().getString(R.string.txt_status_saved));
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
        final View viewNew = lif.inflate(R.layout.template_piece, linLayout, false);
        linLayout.addView(viewNew);
        Button btnSave = viewNew.findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                OnClickSaveTemp(viewNew);
            }
        });

        Button btnDelete = viewNew.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickDeleteTemp(viewNew);
            }
        });
    }


    public void OnClickFrom(View view)
    {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
               dateFrom = String.format("%04d", year) + "-" + String.format("%02d", monthOfYear+1) + "-" + String.format("%02d", dayOfMonth);
               txtFrom.setText(dateFrom);
               yearFrom = year;
               monthFrom = monthOfYear;
               dayFrom = dayOfMonth;
            }
        },
                yearFrom, monthFrom, dayFrom);
        dialog.show();
    }

    public void OnClickTo(View view)
    {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                dateTo = String.format("%04d", year) + "-" + String.format("%02d", monthOfYear+1) + "-" + String.format("%02d", dayOfMonth);
                txtTo.setText(dateTo);
                yearTo = year;
                monthTo = monthOfYear;
                dayTo =dayOfMonth;
            }
        },
                yearTo, monthTo, dayTo);
        dialog.show();
    }

    public void OnClickSmsParse(View view) {

        totalSms =0;
        successSms = 0;

        Date dateFrom = null;
        Date dateTo = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateFrom = formatter.parse(this.dateFrom);
            dateTo = formatter.parse(this.dateTo);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        DatabaseHandler db = new DatabaseHandler(this);
        List<TableSmsTemplateRow> smsTemplateRowList = db.SelectTableSmsTemp(null);
        List<TableCostsRow> tableCostsRowList = db.SelectTableCosts("dt_date >= '" + this.dateFrom + "' and dt_date <= '"
                + this.dateTo + "' and n_sms_id is not null",null, null);
        List<Integer> idList = new ArrayList<>();

        for (int i = 0; i < tableCostsRowList.size(); i++ )
        {
            idList.add(tableCostsRowList.get(i).getSmsId());
        }

        String filter = "date >= " + dateFrom.getTime() + " and date <= " + String.valueOf(dateTo.getTime() + dayInMillisec);
        final Uri smsInbox = Uri.parse("content://sms/inbox");
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cursor = getContentResolver().query(smsInbox, projection, filter, null, null);

        if (cursor.moveToFirst()) {
            int indexAddress = cursor.getColumnIndex("address");
            int indexPerson = cursor.getColumnIndex("person");
            int indexBody = cursor.getColumnIndex("body");
            int indexDate = cursor.getColumnIndex("date");
            int indexType = cursor.getColumnIndex("type");
            int indexId = cursor.getColumnIndex("_id");
            do {
                String stringAddress = cursor.getString(indexAddress);
                int intPerson = cursor.getInt(indexPerson);
                String stringBody = cursor.getString(indexBody);
                long longDate = cursor.getLong(indexDate);
                int intType = cursor.getInt(indexType);
                int id = cursor.getInt(indexId);
                Log.d("sms", "Address " + stringAddress + ", person " + String.valueOf(intPerson)
                        + ", body" + stringBody + ", date " + String.valueOf(longDate) + ", type" + String.valueOf(intType)
                        + ", id " + String.valueOf(id));
                //if sms not parsed yet
                if (!InList(id,idList))
                {
                    for (int i = 0; i < smsTemplateRowList.size(); i++)
                    {
                        String upperBody = stringBody.toUpperCase();
                        String upperBefore = smsTemplateRowList.get(i).getStringBeforeSum().toUpperCase();
                        String upperAfter = smsTemplateRowList.get(i).getStringAfterSum().toUpperCase();
                        if (upperAfter == "")
                            upperAfter = " ";
                        int pos1 = upperBody.indexOf(upperBefore);
                        int afterPos = upperBody.indexOf(upperAfter);
                        //compare to template
                        if (pos1 != -1
                                && (afterPos != -1 || smsTemplateRowList.get(i).getStringAfterSum() == "")
                                && (afterPos > pos1 || smsTemplateRowList.get(i).getStringAfterSum() == "") )
                        {
                            totalSms++;
                            Log.d("sms", "sms text is: " + stringBody);
                            Log.d("sms", "template is " + upperAfter + " "+ upperBefore);
                            Log.d("sms", "first word in pos " + String.valueOf(pos1));
                            int pos2 = upperBody.indexOf(" ", pos1);
                            Log.d("sms", "first space in pos " + String.valueOf(pos2));
                            int pos3 = upperBody.indexOf(upperAfter, pos2+1);
                            Log.d("sms", "end space in pos " + String.valueOf(pos3));
                            if (pos2 == pos3 || pos3 == -1)
                                pos3 = upperBody.length();
                            Log.d("sms", "end of parse in pos " + String.valueOf(pos3));
                            String rawTxt = upperBody.substring(pos2,pos3);
                            Log.d("sms", "raw sum text is: " + rawTxt);
                            rawTxt.replace(" ", "");
                            rawTxt.replace(",", ".");
                            try
                            {
                                float sum = Float.valueOf(rawTxt.trim()).floatValue();
                                Log.d("sms", "success sum " + String.valueOf(sum));

                                Date dt=new Date(longDate);
                                SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                                String dateCost = df2.format(dt);
                                TableCostsRow tableCostsRow =  new TableCostsRow();
                                tableCostsRow.setSmsId(id);
                                tableCostsRow.setIdType(-10);
                                tableCostsRow.setCostSum(sum);
                                tableCostsRow.setCostComment(stringAddress + "\n" + stringBody);
                                tableCostsRow.setStringDate(dateCost);
                                InsertToCosts(tableCostsRow);
                                successSms++;
                                break;
                            }
                            catch (NumberFormatException nfe)
                            {
                                Log.d("sms", "can't parse sum " + rawTxt);
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
        String stringTitle = getString(R.string.txt_total_sms) + " " + String.valueOf(totalSms) + "\n"
                +  getString(R.string.txt_success_sms) + " " + String.valueOf(successSms);
        AlertDialog.Builder dialogSms = new AlertDialog.Builder(this);
        final TextView textView = new TextView(this);
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.margin_20);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.margin_20);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.margin_10);
        textView.setLayoutParams(params);
        container.addView(textView);
        textView.setText(stringTitle);
        dialogSms.setTitle(getString(R.string.title_sms_complite));
        dialogSms.setView(container);
        dialogSms.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogSms.create().show();
    }

}
