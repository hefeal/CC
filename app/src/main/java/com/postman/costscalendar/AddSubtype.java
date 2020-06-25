package com.postman.costscalendar;

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

import java.util.ArrayList;
import java.util.List;

public class AddSubtype extends AppCompatActivity {

    DatabaseHandler dbh;
    List<TableSubtypesRow> subtypes_list;

    Spinner spinner_type;
    Integer choosen_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subtype);

        spinner_type = findViewById(R.id.spinner4);
        dbh = new DatabaseHandler(this);


        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithTag swt = (StringWithTag) parent.getItemAtPosition(position);
                Integer id_type = (Integer) swt.tag;
                choosen_type = id_type;
                Log.d("addsubtype","type in " + String.format("%d",choosen_type));
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
        Log.d("addsubtype", "choosen_type "+ String.valueOf(choosen_type));
        subtypes_list = dbh.SelectTableSubtype("id_type = " + String.valueOf(choosen_type));
        LayoutInflater lif = getLayoutInflater();
        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linelay_main2);
        linLayout.removeAllViews();
        for (int i = 0; i < subtypes_list.size(); i++)
        {
            Log.d("addsubtype", "cycle run " + String.valueOf(i) + " of list size " + String.valueOf(subtypes_list.size()));

            final View view = lif.inflate(R.layout.subtype_piece, linLayout, false);
            view.setId(subtypes_list.get(i).getId_subtype());
            linLayout.addView(view);
            pieces_list.add(new SubtypePiece(view.getId(),false));

            TextView textView = view.findViewById(R.id.txt_subtype);
            textView.setText(subtypes_list.get(i).getV_name());

            Button btn_edit = view.findViewById(R.id.editsubtype);
            final TableSubtypesRow t_row = new TableSubtypesRow();
            t_row.setId_type(subtypes_list.get(i).getId_type());
            t_row.setId_subtype(subtypes_list.get(i).getId_subtype());
            t_row.setV_name(subtypes_list.get(i).getV_name());
            btn_edit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    OnClickEditSubType(t_row);
                }
            });

            Button btn_delete = view.findViewById(R.id.deletesubtype);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickDeleteSubType(t_row);
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
            Integer key = type_list.get(i).getId_type();
            String value = type_list.get(i).getV_name();

            item_list.add(new StringWithTag(value, key));
        }

        ArrayAdapter<StringWithTag> sp_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, item_list);
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(sp_adapter);
    }

    public void OnClickAddSubType(View view)
    {
        Log.d("addsubtype","clicked_add");
        AlertDialog.Builder dialog_add_type = new AlertDialog.Builder(this);
        final EditText add_subtype_txt = new EditText(this);
        dialog_add_type.setView(add_subtype_txt);

        dialog_add_type.setTitle(R.string.title_add_subtype);
        dialog_add_type.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add_subtype_txt.getText().toString() != null)
                {
                    TableSubtypesRow row = new TableSubtypesRow();
                    row.setV_name(add_subtype_txt.getText().toString());
                    row.setId_type(choosen_type);
                    dbh.InsertTableSubtypes(row);
                }
                Recreate();
            }
        });
        dialog_add_type.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_add_type.create().show();
    }

    public void OnClickEditSubType(final TableSubtypesRow t_row)
    {
        AlertDialog.Builder dialog_add_type = new AlertDialog.Builder(this);
        final EditText add_subtype_txt = new EditText(this);
        add_subtype_txt.setText(t_row.getV_name());
        dialog_add_type.setView(add_subtype_txt);

        dialog_add_type.setTitle(R.string.title_edit_subtype);
        dialog_add_type.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add_subtype_txt.getText().toString() != null)
                {
                    TableSubtypesRow row = new TableSubtypesRow();
                    row.setV_name(add_subtype_txt.getText().toString());
                    row.setId_type(t_row.getId_type());
                    row.setId_subtype(t_row.getId_subtype());
                    dbh.UpdateTableSubypes(row);
                }
                Recreate();
            }
        });
        dialog_add_type.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_add_type.create().show();
    }

    public void OnClickDeleteSubType(final TableSubtypesRow t_row)
    {
        final List<TableCostsRow> tc_rows = dbh.SelectTableCosts("c.id_subtype = " + String.valueOf(t_row.getId_subtype()), null, null);
        final List<TableItemsRow> ti_rows = dbh.SelectTableItems("id_subtype = " + String.valueOf(t_row.getId_subtype()));
        String str02;
        String str05 = "";
        String str06;

        if (ti_rows.size() != 0)
            str05 = getString(R.string.dict_confirm05) + " " + String.valueOf(ti_rows.size()) + "\n";
        if (str05.equals(""))
        {
            str02 = "";
            str06 = "";
        }
        else
        {
            str02 = getString(R.string.dict_confirm02) + " " + t_row.getV_name() + ":\n";
            str06 = getString(R.string.dict_confirm06);
        }
        String title_str = str02 + str05 + str06;

        AlertDialog.Builder dialog_add_type = new AlertDialog.Builder(this);
        final TextView txt_v = new TextView(this);
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.margin_20);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.margin_20);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.margin_10);
        txt_v.setLayoutParams(params);
        container.addView(txt_v);
        txt_v.setText(title_str);
        dialog_add_type.setTitle(getString(R.string.dict_confirm01) + " " + t_row.getV_name()+ "?");
        dialog_add_type.setView(container);
        dialog_add_type.setPositiveButton(R.string.btn_sure_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < tc_rows.size(); i++)
                {
                    TableCostsRow tc_row_v = tc_rows.get(i);
                    tc_row_v.setId_subtype(null);
                    tc_row_v.setId_item(null);
                    dbh.UpdateTableCosts(tc_row_v);
                }

                for (int i = 0; i < ti_rows.size(); i++)
                {
                    dbh.DeletTableItems(ti_rows.get(i).getId_item());
                }

                dbh.DeletTableSubtypes(t_row.getId_subtype());

                Recreate();
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
