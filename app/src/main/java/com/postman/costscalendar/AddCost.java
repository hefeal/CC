package com.postman.costscalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AddCost extends AppCompatActivity {

    Integer choosen_type;
    Integer choosen_subtype;
    Integer choosen_item;
    String v_date;
    Boolean is_edit;
    Integer id_cost;
    DatabaseHandler db = new DatabaseHandler(this);
    Spinner spinner_type;
    Spinner spinner_subtype;
    Spinner spinner_items;
    EditText value_field;
    EditText comment_field;
    List<TableCostsRow> edit_row;
    Boolean is_add = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cost);
        v_date = getIntent().getStringExtra("date");
        is_edit = getIntent().getBooleanExtra("is_edit",false);
        value_field = findViewById(R.id.editText);
        comment_field = findViewById(R.id.editText3);
        spinner_type = findViewById(R.id.spinner);
        spinner_subtype = findViewById(R.id.spinner2);
        spinner_items = findViewById(R.id.spinner3);
        if (is_edit)
        {
            id_cost = getIntent().getIntExtra("id_edit",0);
            edit_row = db.SelectTableCosts("id_cost = " + id_cost, null, null);

        }
        ChooseType();
        ChooseSubtype();
        ChooseItem();
        if (is_edit)
        {
            ChooseSum();
            ChooseComment();
        }

        //Actions for spinner for types

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithTag swt = (StringWithTag) parent.getItemAtPosition(position);
                Integer id_type = (Integer) swt.tag;
                choosen_type = id_type;
                Log.d("addcost","type " + String.format("%d",choosen_type));
                ChooseSubtype();
                ChooseItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        //Actions for spinner for sub types

        spinner_subtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithTag swt = (StringWithTag) parent.getItemAtPosition(position);
                Integer id_subtype = (Integer) swt.tag;
                choosen_subtype = id_subtype;
            //    Log.d("addcost","subtype " + String.format("%d",choosen_subtype));
                ChooseItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        //Actions for spinner for items

        spinner_items.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithTag swt = (StringWithTag) parent.getItemAtPosition(position);
                Integer id_item = (Integer) swt.tag;
                choosen_item = id_item;
            //    Log.d("addcost","item " + String.format("%d",choosen_item));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        ReturnToDayCost();
    }

    public void ChooseType() {
        List<TableTypesRow>  type_list = new ArrayList<TableTypesRow>();
        DatabaseHandler db = new DatabaseHandler(this);
        type_list = db.SelectTableTypes(null);
        List<StringWithTag> item_list = new ArrayList<StringWithTag>();

        /* Iterate through your original collection, in this case defined with an Integer key and String value. */
        for (int i =0; i < type_list.size(); i++) {
            Integer key = type_list.get(i).getId_type();
            String value = type_list.get(i).getV_name();

            /* Build the StringWithTag List using these keys and values. */
            item_list.add(new StringWithTag(value, key));
        }

        ArrayAdapter<StringWithTag> sp_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, item_list);
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(sp_adapter);
        if (is_edit)
        {
            for (int i = 0; i < sp_adapter.getCount(); i++) {
                StringWithTag st = (StringWithTag)sp_adapter.getItem(i);
                if ((Integer)st.tag ==
                        edit_row.get(0).getId_type()) {
                    spinner_type.setSelection(sp_adapter.getPosition(st));
                }
            }
        }

        if (is_add)
        {
            for (int i = 0; i < sp_adapter.getCount(); i++) {
                StringWithTag st = (StringWithTag)sp_adapter.getItem(i);
                if ((Integer)st.tag == choosen_type)
                {
                    spinner_type.setSelection(sp_adapter.getPosition(st));
                }
            }
            is_add = false;
        }
     }

    public void ChooseSubtype() {
        List<TableSubtypesRow>  subtype_list = new ArrayList<TableSubtypesRow>();

        String predic = "id_type = " + String.format("%d",choosen_type);
        subtype_list = db.SelectTableSubtype(predic);

        List<StringWithTag> item_list = new ArrayList<StringWithTag>();
        item_list.add(new StringWithTag(this.getResources().getString(R.string.optional_subtype) , -1000));
        /* Iterate through your original collection, in this case defined with an Integer key and String value. */
        for (int i =0; i < subtype_list.size(); i++) {
            Integer key = subtype_list.get(i).getId_subtype();
            String value = subtype_list.get(i).getV_name();

            /* Build the StringWithTag List using these keys and values. */
            item_list.add(new StringWithTag(value, key));
        }

        ArrayAdapter<StringWithTag> sp_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, item_list);
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_subtype.setAdapter(sp_adapter);
        if (is_edit)
        {
            for (int i = 0; i < sp_adapter.getCount(); i++) {
                StringWithTag st = (StringWithTag)sp_adapter.getItem(i);
                if ((Integer)st.tag ==
                        edit_row.get(0).getId_subtype()) {
                    spinner_subtype.setSelection(sp_adapter.getPosition(st));
                }
            }
        }

        if (is_add)
        {
            for (int i = 0; i < sp_adapter.getCount(); i++) {
                StringWithTag st = (StringWithTag)sp_adapter.getItem(i);
                if ((Integer)st.tag == choosen_subtype)
                {
                    spinner_subtype.setSelection(sp_adapter.getPosition(st));
                }
            }
            is_add = false;
        }


    }

    public void ChooseItem() {
        List<TableItemsRow>  items_list = new ArrayList<TableItemsRow>();
        Log.d("addcost","subtype " + String.format("%d",choosen_subtype));
        String predic = "id_subtype = " + String.format("%d",choosen_subtype);
        items_list = db.SelectTableItems(predic);

        List<StringWithTag> item_list = new ArrayList<StringWithTag>();
        item_list.add(new StringWithTag(this.getResources().getString(R.string.optional_item) , -1000));
        /* Iterate through your original collection, in this case defined with an Integer key and String value. */
        for (int i =0; i < items_list.size(); i++) {
            Integer key = items_list.get(i).getId_item();
            String value = items_list.get(i).getV_name();

            /* Build the StringWithTag List using these keys and values. */
            item_list.add(new StringWithTag(value, key));
        }

        ArrayAdapter<StringWithTag> sp_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, item_list);
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_items.setAdapter(sp_adapter);
        if (is_edit)
        {
            for (int i = 0; i < sp_adapter.getCount(); i++) {
                StringWithTag st = (StringWithTag)sp_adapter.getItem(i);
                if ((Integer)st.tag ==
                        edit_row.get(0).getId_item()) {
                    spinner_items.setSelection(sp_adapter.getPosition(st));
                }
            }
        }

        if (is_add)
        {
            for (int i = 0; i < sp_adapter.getCount(); i++) {
                StringWithTag st = (StringWithTag)sp_adapter.getItem(i);
                if ((Integer)st.tag == choosen_item)
                {
                    spinner_items.setSelection(sp_adapter.getPosition(st));
                }
            }
            is_add = false;
        }

    }

    public void ChooseSum()
    {
        value_field.setText(String.format(Locale.ROOT,"%.2f", edit_row.get(0).getF_sum()));
    }

    public void ChooseComment()
    {
        comment_field.setText( edit_row.get(0).getV_comment());
    }

    public void OnClickSaveCost(View view)
    {
        OnClickSaveCostFinal(false);
    }

    public void OnClickSaveCostFinal(boolean is_exit)
    {
        TableCostsRow row = new TableCostsRow();
        Float f_sum;
        value_field = findViewById(R.id.editText);
        comment_field = findViewById(R.id.editText3);
        if (TextUtils.isEmpty( value_field.getText().toString() ))
            f_sum = 0f;
        else
            f_sum =Float.parseFloat(value_field.getText().toString());
        row.setF_sum(f_sum);
        row.setV_date(v_date);

        String new_cmnt = comment_field.getText().toString().replace("\n", "").replace("\r", "").replace(" ", "");
        if (new_cmnt.length() == 0)
            row.setV_comment("");
        else
            row.setV_comment(comment_field.getText().toString());
        row.setId_item(choosen_item);
        if (choosen_subtype != -1000)
            row.setId_subtype(choosen_subtype);
        if(choosen_item != -1000)
            row.setId_item(choosen_item);
        row.setId_type(choosen_type);

        if (!is_edit)
            db.InsertTableCosts(row);
        else
        {
            row.setId_cost(id_cost);
            db.UpdateTableCosts(row);
        }
        Toast.makeText(this,R.string.cost_added,Toast.LENGTH_SHORT).show();
        if(!is_exit)
            ResetValues();
    }

    public void OnClickSaveCostExit(View view)
    {
        OnClickSaveCostFinal(true);
        ReturnToDayCost();
    }


    public void OnClickReturn(View view)
    {
        ReturnToDayCost();
    }

    public void ReturnToDayCost()
    {
        Intent intent =  new Intent(this, DaysCosts.class);
        intent.putExtra("date",v_date  );
        startActivity(intent);
        finish();
    }

    public void OnClickAddType(View view)
    {
        AlertDialog.Builder dialog_add_type = new AlertDialog.Builder(this);
        final EditText add_type_txt = new EditText(this);
        dialog_add_type.setView(add_type_txt);

        dialog_add_type.setTitle(R.string.title_add_type);
        dialog_add_type.setPositiveButton(R.string.dialog_create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add_type_txt.getText().toString() != null)
                {
                    TableTypesRow row = new TableTypesRow();
                    row.setV_name(add_type_txt.getText().toString());
                    long l_id = db.InsertTableTypesGetId(row);
                    row.setId_type((int)l_id);
                    choosen_type = row.getId_type();
                    is_add = true;
                    ChooseType();
                }
                recreate();
            }
        });
        dialog_add_type.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_add_type.create().show();
    }

    public void OnClickAddSubtype(View view)
    {
        TableTypesRow t_row = new TableTypesRow();
        if (choosen_type != -1000)
        {
            t_row = db.SelectTableTypes("id_type = " + String.valueOf(choosen_type)).get(0);
        }
        else
        {
            return;
        }
        AlertDialog.Builder dialog_add_subtype = new AlertDialog.Builder(this);
        final EditText add_subtype_txt = new EditText(this);
        dialog_add_subtype.setView(add_subtype_txt);
        dialog_add_subtype.setTitle(this.getResources().getString(R.string.dialog_new_subtype) + " " + t_row.getV_name());
        dialog_add_subtype.setPositiveButton(R.string.dialog_create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add_subtype_txt.getText().toString() != null)
                {
                    TableSubtypesRow row = new TableSubtypesRow();
                    row.setV_name(add_subtype_txt.getText().toString());
                    row.setId_type(choosen_type);
                    long l_id = db.InsertTableSubtypesGetId(row);
                    row.setId_subtype((int)l_id);
                    choosen_subtype = row.getId_subtype();
                    is_add = true;
                    ChooseSubtype();
                    //StringWithTag st = new StringWithTag(row.getV_name(), row.getId_subtype());
                    //int index = GetSpinnerIndex(spinner_subtype, st);
                    //spinner_subtype.setSelection(index);
                }
                //recreate();
            }
        });
        dialog_add_subtype.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_add_subtype.create().show();
    }

    public void OnClickAddItem(View view)
    {
        TableSubtypesRow t_row = new TableSubtypesRow();
        if (choosen_subtype != -1000)
        {
            t_row = db.SelectTableSubtype("id_subtype = " + String.valueOf(choosen_subtype)).get(0);
            AlertDialog.Builder dialog_add_item = new AlertDialog.Builder(this);
            final EditText add_item_txt = new EditText(this);
            dialog_add_item.setView(add_item_txt);
            dialog_add_item.setTitle(this.getResources().getString(R.string.dialog_new_item) + " " + t_row.getV_name());
            dialog_add_item.setPositiveButton(R.string.dialog_create, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (add_item_txt.getText().toString() != null)
                    {
                        TableItemsRow row = new TableItemsRow();
                        row.setV_name(add_item_txt.getText().toString());
                        row.setId_subtype(choosen_subtype);
                        long l_id = db.InsertTableItemsGetId(row);
                        row.setId_item((int)l_id);
                        choosen_item =  row.getId_item();
                        is_add = true;
                        ChooseItem();
                     //   StringWithTag st = new StringWithTag(row.getV_name(), row.getId_item());
                    //    int index = GetSpinnerIndex(spinner_items, st);
                     //   spinner_items.setSelection(index);
                    }
                    //recreate();
                }
            });
            dialog_add_item.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog_add_item.create().show();
        }
        else
        {
            AlertDialog.Builder dialog_notify = new AlertDialog.Builder(this);
            dialog_notify.setTitle(this.getResources().getString(R.string.select_item_notification));
            dialog_notify.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }

            });
            dialog_notify.create().show();
        }

    }

    public void ResetValues()
    {
        spinner_type.setSelection(0);
        spinner_subtype.setSelection(0);
        spinner_items.setSelection(0);
        value_field.setText("");
        comment_field.setText("");

    }

    private int GetSpinnerIndex(Spinner spinner, StringWithTag st){
        for (int i=0;i<spinner.getCount();i++){
            StringWithTag st_sp = (StringWithTag)spinner.getItemAtPosition(i);
            if (st.tag == st_sp.tag)
            {
                return (int)st.tag;
            }
        }

        return 0;
    }
}
