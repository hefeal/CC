package com.postman.costscalendar.service;

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

import com.postman.costscalendar.dao.DatabaseHandler;
import com.postman.costscalendar.R;
import com.postman.costscalendar.dto.TableCostsRow;
import com.postman.costscalendar.dto.TableItemsRow;
import com.postman.costscalendar.dto.TableSubtypesRow;
import com.postman.costscalendar.dto.TableTypesRow;
import com.postman.costscalendar.model.StringWithTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AddCost extends AppCompatActivity {

    Integer chosenType;
    Integer chosenSubtype;
    Integer chosenItem;
    String vDate;
    Boolean isEdit;
    Integer idCost;
    DatabaseHandler db = new DatabaseHandler(this);
    Spinner spinnerType;
    Spinner spinnerSubtype;
    Spinner spinnerItems;
    EditText valueField;
    EditText commentField;
    List<TableCostsRow> editRow;
    Boolean isAdd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cost);
        vDate = getIntent().getStringExtra("date");
        isEdit = getIntent().getBooleanExtra("is_edit",false);
        valueField = findViewById(R.id.editText);
        commentField = findViewById(R.id.editText3);
        spinnerType = findViewById(R.id.spinner);
        spinnerSubtype = findViewById(R.id.spinner2);
        spinnerItems = findViewById(R.id.spinner3);
        if (isEdit)
        {
            idCost = getIntent().getIntExtra("id_edit",0);
            editRow = db.SelectTableCosts("id_cost = " + idCost, null, null);

        }
        ChooseType();
        ChooseSubtype();
        ChooseItem();
        if (isEdit)
        {
            ChooseSum();
            ChooseComment();
        }

        //Actions for spinner for types

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithTag swt = (StringWithTag) parent.getItemAtPosition(position);
                Integer idType = (Integer) swt.tag;
                chosenType = idType;
                Log.d("addcost","type " + String.format("%d", chosenType));
                ChooseSubtype();
                ChooseItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        //Actions for spinner for sub types

        spinnerSubtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithTag swt = (StringWithTag) parent.getItemAtPosition(position);
                Integer idSubtype = (Integer) swt.tag;
                chosenSubtype = idSubtype;
            //    Log.d("addcost","subtype " + String.format("%d",choosen_subtype));
                ChooseItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        //Actions for spinner for items

        spinnerItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithTag swt = (StringWithTag) parent.getItemAtPosition(position);
                Integer idItem = (Integer) swt.tag;
                chosenItem = idItem;
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
        List<TableTypesRow>  typeList;
        DatabaseHandler db = new DatabaseHandler(this);
        typeList = db.SelectTableTypes(null);
        List<StringWithTag> item_list = new ArrayList<StringWithTag>();

        /* Iterate through your original collection, in this case defined with an Integer key and String value. */
        for (int i =0; i < typeList.size(); i++) {
            Integer key = typeList.get(i).getTypeId();
            String value = typeList.get(i).getTypeName();

            /* Build the StringWithTag List using these keys and values. */
            item_list.add(new StringWithTag(value, key));
        }

        ArrayAdapter<StringWithTag> spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, item_list);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(spAdapter);
        if (isEdit)
        {
            for (int i = 0; i < spAdapter.getCount(); i++) {
                StringWithTag st = (StringWithTag)spAdapter.getItem(i);
                if ((Integer)st.tag ==
                        editRow.get(0).getIdType()) {
                    spinnerType.setSelection(spAdapter.getPosition(st));
                }
            }
        }

        if (isAdd)
        {
            for (int i = 0; i < spAdapter.getCount(); i++) {
                StringWithTag st = (StringWithTag)spAdapter.getItem(i);
                if ((Integer)st.tag == chosenType)
                {
                    spinnerType.setSelection(spAdapter.getPosition(st));
                }
            }
            isAdd = false;
        }
     }

    public void ChooseSubtype() {
        List<TableSubtypesRow>  subtypeList;

        String predic = "id_type = " + String.format("%d", chosenType);
        subtypeList = db.SelectTableSubtype(predic);

        List<StringWithTag> itemList = new ArrayList<StringWithTag>();
        itemList.add(new StringWithTag(this.getResources().getString(R.string.optional_subtype) , -1000));
        /* Iterate through your original collection, in this case defined with an Integer key and String value. */
        for (int i =0; i < subtypeList.size(); i++) {
            Integer key = subtypeList.get(i).getSubtypeId();
            String value = subtypeList.get(i).getSubTypeName();

            /* Build the StringWithTag List using these keys and values. */
            itemList.add(new StringWithTag(value, key));
        }

        ArrayAdapter<StringWithTag> spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubtype.setAdapter(spAdapter);
        if (isEdit)
        {
            for (int i = 0; i < spAdapter.getCount(); i++) {
                StringWithTag st = (StringWithTag)spAdapter.getItem(i);
                if ((Integer)st.tag ==
                        editRow.get(0).getIdSubtype()) {
                    spinnerSubtype.setSelection(spAdapter.getPosition(st));
                }
            }
        }

        if (isAdd)
        {
            for (int i = 0; i < spAdapter.getCount(); i++) {
                StringWithTag st = (StringWithTag)spAdapter.getItem(i);
                if ((Integer)st.tag == chosenSubtype)
                {
                    spinnerSubtype.setSelection(spAdapter.getPosition(st));
                }
            }
            isAdd = false;
        }


    }

    public void ChooseItem() {
        List<TableItemsRow>  itemsList;
        Log.d("addcost","subtype " + String.format("%d", chosenSubtype));
        String predic = "id_subtype = " + String.format("%d", chosenSubtype);
        itemsList = db.SelectTableItems(predic);

        List<StringWithTag> itemList = new ArrayList<StringWithTag>();
        itemList.add(new StringWithTag(this.getResources().getString(R.string.optional_item) , -1000));
        /* Iterate through your original collection, in this case defined with an Integer key and String value. */
        for (int i =0; i < itemsList.size(); i++) {
            Integer key = itemsList.get(i).getItemId();
            String value = itemsList.get(i).getNameId();

            /* Build the StringWithTag List using these keys and values. */
            itemList.add(new StringWithTag(value, key));
        }

        ArrayAdapter<StringWithTag> spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItems.setAdapter(spAdapter);
        if (isEdit)
        {
            for (int i = 0; i < spAdapter.getCount(); i++) {
                StringWithTag st = (StringWithTag)spAdapter.getItem(i);
                if ((Integer)st.tag ==
                        editRow.get(0).getIdItem()) {
                    spinnerItems.setSelection(spAdapter.getPosition(st));
                }
            }
        }

        if (isAdd)
        {
            for (int i = 0; i < spAdapter.getCount(); i++) {
                StringWithTag st = (StringWithTag)spAdapter.getItem(i);
                if ((Integer)st.tag == chosenItem)
                {
                    spinnerItems.setSelection(spAdapter.getPosition(st));
                }
            }
            isAdd = false;
        }

    }

    public void ChooseSum()
    {
        valueField.setText(String.format(Locale.ROOT,"%.2f", editRow.get(0).getCostSum()));
    }

    public void ChooseComment()
    {
        commentField.setText( editRow.get(0).getCostComment());
    }

    public void OnClickSaveCost(View view)
    {
        OnClickSaveCostFinal(false);
    }

    public void OnClickSaveCostFinal(boolean isExit)
    {
        TableCostsRow row = new TableCostsRow();
        Float fSum;
        valueField = findViewById(R.id.editText);
        commentField = findViewById(R.id.editText3);
        if (TextUtils.isEmpty( valueField.getText().toString() ))
            fSum = 0f;
        else
            fSum =Float.parseFloat(valueField.getText().toString());
        row.setCostSum(fSum);
        row.setStringDate(vDate);

        String newComment = commentField.getText().toString().replace("\n", "").replace("\r", "").replace(" ", "");
        if (newComment.length() == 0)
            row.setCostComment("");
        else
            row.setCostComment(commentField.getText().toString());
        row.setIdItem(chosenItem);
        if (chosenSubtype != -1000)
            row.setIdSubtype(chosenSubtype);
        if(chosenItem != -1000)
            row.setIdItem(chosenItem);
        row.setIdType(chosenType);

        if (!isEdit)
            db.InsertTableCosts(row);
        else
        {
            row.setIdCost(idCost);
            db.UpdateTableCosts(row);
        }
        Toast.makeText(this,R.string.cost_added,Toast.LENGTH_SHORT).show();
        if(!isExit)
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
        intent.putExtra("date", vDate);
        startActivity(intent);
        finish();
    }

    public void OnClickAddType(View view)
    {
        AlertDialog.Builder dialogAddType = new AlertDialog.Builder(this);
        final EditText addTypeTxt = new EditText(this);
        dialogAddType.setView(addTypeTxt);

        dialogAddType.setTitle(R.string.title_add_type);
        dialogAddType.setPositiveButton(R.string.dialog_create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addTypeTxt.getText().toString() != null)
                {
                    TableTypesRow row = new TableTypesRow();
                    row.setTypeName(addTypeTxt.getText().toString());
                    long typesId = db.InsertTableTypesGetId(row);
                    row.setTypeId((int)typesId);
                    chosenType = row.getTypeId();
                    isAdd = true;
                    ChooseType();
                }
                recreate();
            }
        });
        dialogAddType.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogAddType.create().show();
    }

    public void OnClickAddSubtype(View view)
    {
        TableTypesRow typesRow;
        if (chosenType != -1000)
        {
            typesRow = db.SelectTableTypes("id_type = " + String.valueOf(chosenType)).get(0);
        }
        else
        {
            return;
        }
        AlertDialog.Builder dialogAddSubtype = new AlertDialog.Builder(this);
        final EditText addSubtypeTxt = new EditText(this);
        dialogAddSubtype.setView(addSubtypeTxt);
        dialogAddSubtype.setTitle(this.getResources().getString(R.string.dialog_new_subtype) + " " + typesRow.getTypeName());
        dialogAddSubtype.setPositiveButton(R.string.dialog_create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addSubtypeTxt.getText().toString() != null)
                {
                    TableSubtypesRow row = new TableSubtypesRow();
                    row.setSubTypeName(addSubtypeTxt.getText().toString());
                    row.setTypeId(chosenType);
                    long subTypeId = db.InsertTableSubtypesGetId(row);
                    row.setSubtypeId((int)subTypeId);
                    chosenSubtype = row.getSubtypeId();
                    isAdd = true;
                    ChooseSubtype();
                }
            }
        });
        dialogAddSubtype.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogAddSubtype.create().show();
    }

    public void OnClickAddItem(View view)
    {
        TableSubtypesRow subtypesRow;
        if (chosenSubtype != -1000)
        {
            subtypesRow = db.SelectTableSubtype("id_subtype = " + String.valueOf(chosenSubtype)).get(0);
            AlertDialog.Builder dialogAddItem = new AlertDialog.Builder(this);
            final EditText addItemTxt = new EditText(this);
            dialogAddItem.setView(addItemTxt);
            dialogAddItem.setTitle(this.getResources().getString(R.string.dialog_new_item) + " " + subtypesRow.getSubTypeName());
            dialogAddItem.setPositiveButton(R.string.dialog_create, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (addItemTxt.getText().toString() != null)
                    {
                        TableItemsRow row = new TableItemsRow();
                        row.setNameId(addItemTxt.getText().toString());
                        row.setSubtypeId(chosenSubtype);
                        long itemId = db.InsertTableItemsGetId(row);
                        row.setItemId((int)itemId);
                        chosenItem =  row.getItemId();
                        isAdd = true;
                        ChooseItem();
                    }
                }
            });
            dialogAddItem.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialogAddItem.create().show();
        }
        else
        {
            AlertDialog.Builder dialogNotify = new AlertDialog.Builder(this);
            dialogNotify.setTitle(this.getResources().getString(R.string.select_item_notification));
            dialogNotify.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }

            });
            dialogNotify.create().show();
        }

    }

    public void ResetValues()
    {
        spinnerType.setSelection(0);
        spinnerSubtype.setSelection(0);
        spinnerItems.setSelection(0);
        valueField.setText("");
        commentField.setText("");

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
