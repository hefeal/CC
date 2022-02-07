package com.postman.costscalendar.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.postman.costscalendar.dao.DatabaseHandler;
import com.postman.costscalendar.R;
import com.postman.costscalendar.dto.TableCostsRow;
import com.postman.costscalendar.dto.TableItemsRow;
import com.postman.costscalendar.dto.TableSubtypesRow;
import com.postman.costscalendar.dto.TableTypesRow;
import com.postman.costscalendar.model.StringWithTag;

import java.util.ArrayList;
import java.util.List;

public class AddItem extends AppCompatActivity {

    DatabaseHandler dbh;
    List<TableSubtypesRow> subtypesList;
    List <TableItemsRow> itemsRowList;
    Spinner spinnerType;
    Spinner spinnerSubtype;
    Integer chosenType;
    Integer chosenSubtype;
    Button btnAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        spinnerType = findViewById(R.id.spinner_type);
        spinnerSubtype = findViewById(R.id.spinner_subype);
        dbh = new DatabaseHandler(this);
        btnAddItem = findViewById(R.id.add_itm);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithTag swt = (StringWithTag) parent.getItemAtPosition(position);
                Integer typeId = (Integer) swt.tag;
                chosenType = typeId;
                Log.d("additem","type in " + String.format("%d", chosenType));
                ChooseSubType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        spinnerSubtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithTag swt = (StringWithTag) parent.getItemAtPosition(position);
                Integer subtypeId = (Integer) swt.tag;
                chosenSubtype = subtypeId;
                Log.d("additem","subtype in " + String.format("%d", chosenSubtype));
                DisplayItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                chosenSubtype = -1000;
                DisplayItems();
            }
        });

        ChooseType();

        DisplayItems();
    }

    public void DisplayItems()
    {
        //List<SubtypePiece> pieces_list = new ArrayList<>();
        Log.d("additem","subtype in " + String.format("%d", chosenSubtype));
        itemsRowList = dbh.SelectTableItems("id_subtype = " + String.valueOf(chosenSubtype));
        LayoutInflater lif = getLayoutInflater();
        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linelay_main3);
        linLayout.removeAllViews();
        for (int i = 0; i < itemsRowList.size(); i++)
        {

            final View view = lif.inflate(R.layout.item_piece, linLayout, false);
            view.setId(itemsRowList.get(i).getItemId());
            linLayout.addView(view);

            TextView textView = view.findViewById(R.id.txt_item);
            textView.setText(itemsRowList.get(i).getNameId());

            Button btnEdit = view.findViewById(R.id.edititem);
            final TableItemsRow tableItemsRow = new TableItemsRow();
            tableItemsRow.setItemId(itemsRowList.get(i).getItemId());
            tableItemsRow.setSubtypeId(itemsRowList.get(i).getSubtypeId());
            tableItemsRow.setNameId(itemsRowList.get(i).getNameId());
            btnEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    OnClickEditItem(tableItemsRow);
                }
            });

            Button btnDelete = view.findViewById(R.id.deleteitem);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickDeleteItem(tableItemsRow);
                }
            });
        }
    }

    public void ChooseType() {
        List<TableTypesRow>  typesRowList;
        DatabaseHandler db = new DatabaseHandler(this);
        typesRowList = db.SelectTableTypes(null);
        List<StringWithTag> itemList = new ArrayList<StringWithTag>();

        for (int i =0; i < typesRowList.size(); i++) {
            Integer key = typesRowList.get(i).getTypeId();
            String value = typesRowList.get(i).getTypeName();

            itemList.add(new StringWithTag(value, key));
        }

        ArrayAdapter<StringWithTag> spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(spAdapter);
    }

    public void ChooseSubType() {
        List<TableSubtypesRow>  typeList = new ArrayList<TableSubtypesRow>();
        DatabaseHandler db = new DatabaseHandler(this);
        typeList = db.SelectTableSubtype("id_type = " + String.valueOf(chosenType));
        List<StringWithTag> itemList = new ArrayList<StringWithTag>();

        for (int i =0; i < typeList.size(); i++) {
            Integer key = typeList.get(i).getSubtypeId();
            String value = typeList.get(i).getSubTypeName();

            itemList.add(new StringWithTag(value, key));
        }

        if (typeList.size() ==0)
        {
            chosenSubtype = -1000;
            DisplayItems();
        }

        ArrayAdapter<StringWithTag> spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubtype.setAdapter(spAdapter);
    }

    public void OnClickAddItem(View view)
    {
        Log.d("addsubtype","clicked_add");
        if (chosenSubtype == null || chosenSubtype == -1000)
        {
            AlertDialog.Builder dialogAddType = new AlertDialog.Builder(this);
            dialogAddType.setTitle(R.string.select_item_notification);
            dialogAddType.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DisplayItems();
                }
            });
            dialogAddType.create().show();
        }
        else {
            AlertDialog.Builder dialogAddType = new AlertDialog.Builder(this);
            final EditText add_item_txt = new EditText(this);
            dialogAddType.setView(add_item_txt);

            dialogAddType.setTitle(R.string.title_add_item);
            dialogAddType.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (add_item_txt.getText().toString() != null) {
                        TableItemsRow row = new TableItemsRow();
                        row.setNameId(add_item_txt.getText().toString());
                        row.setSubtypeId(chosenSubtype);
                        dbh.InsertTableItems(row);
                    }
                    DisplayItems();
                }
            });
            dialogAddType.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialogAddType.create().show();
        }
    }

    public void OnClickEditItem(final TableItemsRow tableItemsRow)
    {
        AlertDialog.Builder dialogAddType = new AlertDialog.Builder(this);
        final EditText addItemTxt = new EditText(this);
        addItemTxt.setText(tableItemsRow.getNameId());
        dialogAddType.setView(addItemTxt);

        dialogAddType.setTitle(R.string.title_edit_item);
        dialogAddType.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addItemTxt.getText().toString() != null)
                {
                    TableItemsRow row = new TableItemsRow();
                    row.setNameId(addItemTxt.getText().toString());
                    row.setSubtypeId(tableItemsRow.getSubtypeId());
                    row.setItemId(tableItemsRow.getItemId());
                    dbh.UpdateTableItems(row);
                }
                DisplayItems();
            }
        });
        dialogAddType.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogAddType.create().show();
    }

    public void OnClickDeleteItem(final TableItemsRow tableItemsRow)
    {
        final List<TableCostsRow> tableCostsRowList = dbh.SelectTableCosts("c.id_item = " + String.valueOf(tableItemsRow.getItemId()), null, null);

        AlertDialog.Builder dialogAddType = new AlertDialog.Builder(this);
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.margin_20);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.margin_20);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.margin_10);
        dialogAddType.setTitle(getString(R.string.dict_confirm01) + " " + tableItemsRow.getNameId()+ "?");
        dialogAddType.setView(container);
        dialogAddType.setPositiveButton(R.string.btn_sure_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < tableCostsRowList.size(); i++)
                {
                    TableCostsRow tableCostsRow = tableCostsRowList.get(i);
                    tableCostsRow.setIdItem(null);
                    dbh.UpdateTableCosts(tableCostsRow);
                }


                dbh.DeletTableItems(tableItemsRow.getItemId());
                DisplayItems();
            }
        });
        dialogAddType.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogAddType.create().show();
    }

    void Recreate()
    {
        this.recreate();
    }
}
