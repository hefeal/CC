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
import com.postman.costscalendar.model.SubtypePiece;

import java.util.ArrayList;
import java.util.List;

public class AddSubtype extends AppCompatActivity {

    DatabaseHandler dbh;
    List<TableSubtypesRow> subtypesList;

    Spinner spinnerType;
    Integer chosenType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subtype);

        spinnerType = findViewById(R.id.spinner4);
        dbh = new DatabaseHandler(this);


        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithTag swt = (StringWithTag) parent.getItemAtPosition(position);
                Integer id_type = (Integer) swt.tag;
                chosenType = id_type;
                Log.d("addsubtype","type in " + String.format("%d", chosenType));
                DisplaySubtypes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });
        ChooseType();
        DisplaySubtypes();
    }

    public void DisplaySubtypes()
    {
        List<SubtypePiece> pieces_list = new ArrayList<>();
        Log.d("addsubtype", "choosen_type "+ String.valueOf(chosenType));
        subtypesList = dbh.SelectTableSubtype("id_type = " + String.valueOf(chosenType));
        LayoutInflater lif = getLayoutInflater();
        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linelay_main2);
        linLayout.removeAllViews();
        for (int i = 0; i < subtypesList.size(); i++)
        {
            Log.d("addsubtype", "cycle run " + String.valueOf(i) + " of list size " + String.valueOf(subtypesList.size()));

            final View view = lif.inflate(R.layout.subtype_piece, linLayout, false);
            view.setId(subtypesList.get(i).getSubtypeId());
            linLayout.addView(view);
            pieces_list.add(new SubtypePiece(view.getId(),false));

            TextView textView = view.findViewById(R.id.txt_subtype);
            textView.setText(subtypesList.get(i).getSubTypeName());

            Button btn_edit = view.findViewById(R.id.editsubtype);
            final TableSubtypesRow tableSubtypesRow = new TableSubtypesRow();
            tableSubtypesRow.setTypeId(subtypesList.get(i).getTypeId());
            tableSubtypesRow.setSubtypeId(subtypesList.get(i).getSubtypeId());
            tableSubtypesRow.setSubTypeName(subtypesList.get(i).getSubTypeName());
            btn_edit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    OnClickEditSubType(tableSubtypesRow);
                }
            });

            Button btnDelete = view.findViewById(R.id.deletesubtype);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickDeleteSubType(tableSubtypesRow);
                }
            });
        }
    }

    public void ChooseType() {
        List<TableTypesRow>  typesRowList = new ArrayList<TableTypesRow>();
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

    public void OnClickAddSubType(View view)
    {
        Log.d("addsubtype","clicked_add");
        AlertDialog.Builder dialogAddType = new AlertDialog.Builder(this);
        final EditText addSubtypeTxt = new EditText(this);
        dialogAddType.setView(addSubtypeTxt);

        dialogAddType.setTitle(R.string.title_add_subtype);
        dialogAddType.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addSubtypeTxt.getText().toString() != null)
                {
                    TableSubtypesRow row = new TableSubtypesRow();
                    row.setSubTypeName(addSubtypeTxt.getText().toString());
                    row.setTypeId(chosenType);
                    dbh.InsertTableSubtypes(row);
                }
                Recreate();
            }
        });
        dialogAddType.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogAddType.create().show();
    }

    public void OnClickEditSubType(final TableSubtypesRow subtypesRow)
    {
        AlertDialog.Builder dialogAddType = new AlertDialog.Builder(this);
        final EditText addSubtypeTxt = new EditText(this);
        addSubtypeTxt.setText(subtypesRow.getSubTypeName());
        dialogAddType.setView(addSubtypeTxt);

        dialogAddType.setTitle(R.string.title_edit_subtype);
        dialogAddType.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addSubtypeTxt.getText().toString() != null)
                {
                    TableSubtypesRow row = new TableSubtypesRow();
                    row.setSubTypeName(addSubtypeTxt.getText().toString());
                    row.setTypeId(subtypesRow.getTypeId());
                    row.setSubtypeId(subtypesRow.getSubtypeId());
                    dbh.UpdateTableSubypes(row);
                }
                Recreate();
            }
        });
        dialogAddType.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogAddType.create().show();
    }

    public void OnClickDeleteSubType(final TableSubtypesRow subtypesRow)
    {
        final List<TableCostsRow> costsRowList = dbh.SelectTableCosts("c.id_subtype = " + String.valueOf(subtypesRow.getSubtypeId()), null, null);
        final List<TableItemsRow> itemsRowList = dbh.SelectTableItems("id_subtype = " + String.valueOf(subtypesRow.getSubtypeId()));
        String str02;
        String str05 = "";
        String str06;

        if (itemsRowList.size() != 0)
            str05 = getString(R.string.dict_confirm05) + " " + String.valueOf(itemsRowList.size()) + "\n";
        if (str05.equals(""))
        {
            str02 = "";
            str06 = "";
        }
        else
        {
            str02 = getString(R.string.dict_confirm02) + " " + subtypesRow.getSubTypeName() + ":\n";
            str06 = getString(R.string.dict_confirm06);
        }
        String titleStr = str02 + str05 + str06;

        AlertDialog.Builder dialogAddType = new AlertDialog.Builder(this);
        final TextView textView = new TextView(this);
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.margin_20);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.margin_20);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.margin_10);
        textView.setLayoutParams(params);
        container.addView(textView);
        textView.setText(titleStr);
        dialogAddType.setTitle(getString(R.string.dict_confirm01) + " " + subtypesRow.getSubTypeName()+ "?");
        dialogAddType.setView(container);
        dialogAddType.setPositiveButton(R.string.btn_sure_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < costsRowList.size(); i++)
                {
                    TableCostsRow costsRow = costsRowList.get(i);
                    costsRow.setIdSubtype(null);
                    costsRow.setIdItem(null);
                    dbh.UpdateTableCosts(costsRow);
                }

                for (int i = 0; i < itemsRowList.size(); i++)
                {
                    dbh.DeletTableItems(itemsRowList.get(i).getItemId());
                }

                dbh.DeletTableSubtypes(subtypesRow.getSubtypeId());

                Recreate();
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
