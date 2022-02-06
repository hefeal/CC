package com.postman.costscalendar.service;

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
    List<TableSubtypesRow> subtypes_list;
    List <TableItemsRow> items_list;
    Spinner spinner_type;
    Spinner spinner_subtype;
    Integer choosen_type;
    Integer choosen_subtype;
    Button add_itm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        spinner_type = findViewById(R.id.spinner_type);
        spinner_subtype = findViewById(R.id.spinner_subype);
        dbh = new DatabaseHandler(this);
        add_itm = findViewById(R.id.add_itm);

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithTag swt = (StringWithTag) parent.getItemAtPosition(position);
                Integer id_type = (Integer) swt.tag;
                choosen_type = id_type;
                Log.d("additem","type in " + String.format("%d",choosen_type));
                ChooseSubType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        spinner_subtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithTag swt = (StringWithTag) parent.getItemAtPosition(position);
                Integer id_subtype = (Integer) swt.tag;
                choosen_subtype = id_subtype;
                Log.d("additem","subtype in " + String.format("%d",choosen_subtype));
                DisplayItems();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                choosen_subtype = -1000;
                DisplayItems();
            }
        });

        ChooseType();

        DisplayItems();
    }

    public void DisplayItems()
    {
        //List<SubtypePiece> pieces_list = new ArrayList<>();
        Log.d("additem","subtype in " + String.format("%d",choosen_subtype));
        items_list = dbh.SelectTableItems("id_subtype = " + String.valueOf(choosen_subtype));
        LayoutInflater lif = getLayoutInflater();
        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linelay_main3);
        linLayout.removeAllViews();
        for (int i = 0; i < items_list.size(); i++)
        {

            final View view = lif.inflate(R.layout.item_piece, linLayout, false);
            view.setId(items_list.get(i).getItemId());
            linLayout.addView(view);

            TextView textView = view.findViewById(R.id.txt_item);
            textView.setText(items_list.get(i).getNameId());

            Button btn_edit = view.findViewById(R.id.edititem);
            final TableItemsRow t_row = new TableItemsRow();
            t_row.setItemId(items_list.get(i).getItemId());
            t_row.setSubtypeId(items_list.get(i).getSubtypeId());
            t_row.setNameId(items_list.get(i).getNameId());
            btn_edit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    OnClickEditItem(t_row);
                }
            });

            Button btn_delete = view.findViewById(R.id.deleteitem);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickDeleteItem(t_row);
                }
            });
        }
    }

    public void ChooseType() {
        List<TableTypesRow>  type_list = new ArrayList<TableTypesRow>();
        DatabaseHandler db = new DatabaseHandler(this);
        type_list = db.SelectTableTypes(null);
        List<StringWithTag> item_list = new ArrayList<StringWithTag>();

        for (int i =0; i < type_list.size(); i++) {
            Integer key = type_list.get(i).getTypeId();
            String value = type_list.get(i).getTypeName();

            item_list.add(new StringWithTag(value, key));
        }

        ArrayAdapter<StringWithTag> sp_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, item_list);
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(sp_adapter);
    }

    public void ChooseSubType() {
        List<TableSubtypesRow>  type_list = new ArrayList<TableSubtypesRow>();
        DatabaseHandler db = new DatabaseHandler(this);
        type_list = db.SelectTableSubtype("id_type = " + String.valueOf(choosen_type));
        List<StringWithTag> item_list = new ArrayList<StringWithTag>();

        for (int i =0; i < type_list.size(); i++) {
            Integer key = type_list.get(i).getSubtypeId();
            String value = type_list.get(i).getSubTypeName();

            item_list.add(new StringWithTag(value, key));
        }

        if (type_list.size() ==0)
        {
            choosen_subtype = -1000;
            DisplayItems();
        }

        ArrayAdapter<StringWithTag> sp_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, item_list);
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_subtype.setAdapter(sp_adapter);
    }

    public void OnClickAddItem(View view)
    {
        Log.d("addsubtype","clicked_add");
        if (choosen_subtype == null || choosen_subtype == -1000)
        {
            AlertDialog.Builder dialog_add_type = new AlertDialog.Builder(this);
            dialog_add_type.setTitle(R.string.select_item_notification);
            dialog_add_type.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DisplayItems();
                }
            });
            dialog_add_type.create().show();
        }
        else {
            AlertDialog.Builder dialog_add_type = new AlertDialog.Builder(this);
            final EditText add_item_txt = new EditText(this);
            dialog_add_type.setView(add_item_txt);

            dialog_add_type.setTitle(R.string.title_add_item);
            dialog_add_type.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (add_item_txt.getText().toString() != null) {
                        TableItemsRow row = new TableItemsRow();
                        row.setNameId(add_item_txt.getText().toString());
                        row.setSubtypeId(choosen_subtype);
                        dbh.InsertTableItems(row);
                    }
                    DisplayItems();
                }
            });
            dialog_add_type.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog_add_type.create().show();
        }
    }

    public void OnClickEditItem(final TableItemsRow t_row)
    {
        AlertDialog.Builder dialog_add_type = new AlertDialog.Builder(this);
        final EditText add_item_txt = new EditText(this);
        add_item_txt.setText(t_row.getNameId());
        dialog_add_type.setView(add_item_txt);

        dialog_add_type.setTitle(R.string.title_edit_item);
        dialog_add_type.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add_item_txt.getText().toString() != null)
                {
                    TableItemsRow row = new TableItemsRow();
                    row.setNameId(add_item_txt.getText().toString());
                    row.setSubtypeId(t_row.getSubtypeId());
                    row.setItemId(t_row.getItemId());
                    dbh.UpdateTableItems(row);
                }
                DisplayItems();
            }
        });
        dialog_add_type.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_add_type.create().show();
    }

    public void OnClickDeleteItem(final TableItemsRow t_row)
    {
        final List<TableCostsRow> tc_rows = dbh.SelectTableCosts("c.id_item = " + String.valueOf(t_row.getItemId()), null, null);

        AlertDialog.Builder dialog_add_type = new AlertDialog.Builder(this);
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.margin_20);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.margin_20);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.margin_10);
        dialog_add_type.setTitle(getString(R.string.dict_confirm01) + " " + t_row.getNameId()+ "?");
        dialog_add_type.setView(container);
        dialog_add_type.setPositiveButton(R.string.btn_sure_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < tc_rows.size(); i++)
                {
                    TableCostsRow tc_row_v = tc_rows.get(i);
                    tc_row_v.setIdItem(null);
                    dbh.UpdateTableCosts(tc_row_v);
                }


                dbh.DeletTableItems(t_row.getItemId());
                DisplayItems();
            }
        });
        dialog_add_type.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_add_type.create().show();
    }

    void Recreate()
    {
        this.recreate();
    }
}
