package com.postman.costscalendar.service;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.postman.costscalendar.dao.DatabaseHandler;
import com.postman.costscalendar.R;
import com.postman.costscalendar.dto.TableCostsRow;
import com.postman.costscalendar.dto.TableItemsRow;
import com.postman.costscalendar.dto.TableSubtypesRow;
import com.postman.costscalendar.dto.TableTypesRow;
import com.postman.costscalendar.model.TypePiece;

import java.util.ArrayList;
import java.util.List;

public class AddType extends AppCompatActivity {

    DatabaseHandler dbh;
    List<TableTypesRow> types_list;
    List<TypePiece> pieces_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);

        dbh = new DatabaseHandler(this);
        types_list = dbh.SelectTableTypes("id_type != -10");
        pieces_list = new ArrayList<>();

        for (int i = 0; i < types_list.size(); i++)
        {
            LayoutInflater lif = getLayoutInflater();
            LinearLayout linLayout = (LinearLayout) findViewById(R.id.linelay_main);
            final View view = lif.inflate(R.layout.type_piece, linLayout, false);
            view.setId(types_list.get(i).getTypeId());
            linLayout.addView(view);
            pieces_list.add(new TypePiece(view.getId(),false));

            TextView textView = view.findViewById(R.id.txt_type);
            textView.setText(types_list.get(i).getTypeName());

            Button btn_edit = view.findViewById(R.id.edittype);
            final TableTypesRow t_row = new TableTypesRow();
            t_row.setTypeId(types_list.get(i).getTypeId());
            t_row.setTypeName(types_list.get(i).getTypeName());
            btn_edit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    OnClickEditType(t_row);
                }
            });

            Button btn_delete = view.findViewById(R.id.deletetype);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickDeleteType(t_row);
                }
            });
        }
    }

    public void OnClickAddType(View view)
    {
        AlertDialog.Builder dialog_add_type = new AlertDialog.Builder(this);
        final EditText add_type_txt = new EditText(this);
        dialog_add_type.setView(add_type_txt);

        dialog_add_type.setTitle(R.string.title_add_type);
        dialog_add_type.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add_type_txt.getText().toString() != null)
                {
                    TableTypesRow row = new TableTypesRow();
                    row.setTypeName(add_type_txt.getText().toString());
                    dbh.InsertTableTypes(row);
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

    public void OnClickEditType(final TableTypesRow t_row)
    {
        AlertDialog.Builder dialog_add_type = new AlertDialog.Builder(this);
        final EditText add_type_txt = new EditText(this);
        add_type_txt.setText(t_row.getTypeName());
        dialog_add_type.setView(add_type_txt);

        dialog_add_type.setTitle(R.string.title_edit_type);
        dialog_add_type.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add_type_txt.getText().toString() != null)
                {
                    TableTypesRow row = new TableTypesRow();
                    row.setTypeName(add_type_txt.getText().toString());
                    row.setTypeId(t_row.getTypeId());
                    dbh.UpdateTableTypes(row);
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

    public void OnClickDeleteType(final TableTypesRow t_row)
    {
        final List<TableCostsRow> tc_rows = dbh.SelectTableCosts("c.id_type = " + String.valueOf(t_row.getTypeId()), null, null);
        final List<TableSubtypesRow> ts_rows =  dbh.SelectTableSubtype("id_type = " +  String.valueOf(t_row.getTypeId()));
        String subtype_in = "(";

        for (int i =0; i < ts_rows.size();i++)
        {
            if (i < ts_rows.size() -1)
                subtype_in = subtype_in + String.valueOf(ts_rows.get(i).getSubtypeId()) + ",";
            else
                subtype_in = subtype_in + String.valueOf(ts_rows.get(i).getSubtypeId()) + ")";
        }
        String item_pred;
        if (subtype_in == "(")
            item_pred = "1=0"; //To.Do. сделать нормально
        else
            item_pred = "id_subtype in " + subtype_in;
        final List<TableItemsRow>  ti_rows = dbh.SelectTableItems(item_pred);
        String str02;
        String str03 = "";
        String str04 = "";
        String str05 = "";
        String str06;

        if (tc_rows.size() != 0)
            str03 = getString(R.string.dict_confirm03) + " " + String.valueOf(tc_rows.size()) + "\n";
        if (ts_rows.size() != 0)
            str04 = getString(R.string.dict_confirm04) + " " + String.valueOf(ts_rows.size()) + "\n";
        if (ti_rows.size() != 0)
            str05 = getString(R.string.dict_confirm05) + " " + String.valueOf(ti_rows.size()) + "\n";
        if (str03.equals("")  && str04.equals("") && str05.equals(""))
        {
            str02 = "";
            str06 = "";
        }
        else
        {
            str02 = getString(R.string.dict_confirm02) + " " + t_row.getTypeName() + ":\n";
            str06 = getString(R.string.dict_confirm06);
        }
        String title_str = str02 + str03 + str04 + str05 + str06;

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
        dialog_add_type.setTitle(getString(R.string.dict_confirm01) + " " + t_row.getTypeName()+ "?");
        dialog_add_type.setView(container);
        dialog_add_type.setPositiveButton(R.string.btn_sure_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < tc_rows.size(); i++)
                {
                    dbh.DeletTableCosts(tc_rows.get(i).getIdCost());
                }

                for (int i = 0; i < ti_rows.size(); i++)
                {
                    dbh.DeletTableItems(ti_rows.get(i).getItemId());
                }

                for (int i = 0; i < ts_rows.size(); i++)
                {
                    dbh.DeletTableSubtypes(ts_rows.get(i).getSubtypeId());
                }

                dbh.DeletTableTypes(t_row.getTypeId());
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
}
