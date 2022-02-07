package com.postman.costscalendar.activity;

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
    List<TableTypesRow> typesRowList;
    List<TypePiece> typePieceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);

        dbh = new DatabaseHandler(this);
        typesRowList = dbh.SelectTableTypes("id_type != -10");
        typePieceList = new ArrayList<>();

        for (int i = 0; i < typesRowList.size(); i++)
        {
            LayoutInflater lif = getLayoutInflater();
            LinearLayout linLayout = (LinearLayout) findViewById(R.id.linelay_main);
            final View view = lif.inflate(R.layout.type_piece, linLayout, false);
            view.setId(typesRowList.get(i).getTypeId());
            linLayout.addView(view);
            typePieceList.add(new TypePiece(view.getId(),false));

            TextView textView = view.findViewById(R.id.txt_type);
            textView.setText(typesRowList.get(i).getTypeName());

            Button btnEdit = view.findViewById(R.id.edittype);
            final TableTypesRow typesRow = new TableTypesRow();
            typesRow.setTypeId(typesRowList.get(i).getTypeId());
            typesRow.setTypeName(typesRowList.get(i).getTypeName());
            btnEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    OnClickEditType(typesRow);
                }
            });

            Button btnDelete = view.findViewById(R.id.deletetype);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickDeleteType(typesRow);
                }
            });
        }
    }

    public void OnClickAddType(View view)
    {
        AlertDialog.Builder dialogAddType = new AlertDialog.Builder(this);
        final EditText addTypeTxt = new EditText(this);
        dialogAddType.setView(addTypeTxt);

        dialogAddType.setTitle(R.string.title_add_type);
        dialogAddType.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addTypeTxt.getText().toString() != null)
                {
                    TableTypesRow row = new TableTypesRow();
                    row.setTypeName(addTypeTxt.getText().toString());
                    dbh.InsertTableTypes(row);
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

    public void OnClickEditType(final TableTypesRow tableTypesRow)
    {
        AlertDialog.Builder dialogAddType = new AlertDialog.Builder(this);
        final EditText addTypeTxt = new EditText(this);
        addTypeTxt.setText(tableTypesRow.getTypeName());
        dialogAddType.setView(addTypeTxt);

        dialogAddType.setTitle(R.string.title_edit_type);
        dialogAddType.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (addTypeTxt.getText().toString() != null)
                {
                    TableTypesRow row = new TableTypesRow();
                    row.setTypeName(addTypeTxt.getText().toString());
                    row.setTypeId(tableTypesRow.getTypeId());
                    dbh.UpdateTableTypes(row);
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

    public void OnClickDeleteType(final TableTypesRow typesRow)
    {
        final List<TableCostsRow> costsRowList = dbh.SelectTableCosts("c.id_type = " + String.valueOf(typesRow.getTypeId()), null, null);
        final List<TableSubtypesRow> subtypesRowList =  dbh.SelectTableSubtype("id_type = " +  String.valueOf(typesRow.getTypeId()));
        String subtypeIn = "(";

        for (int i =0; i < subtypesRowList.size();i++)
        {
            if (i < subtypesRowList.size() -1)
                subtypeIn = subtypeIn + String.valueOf(subtypesRowList.get(i).getSubtypeId()) + ",";
            else
                subtypeIn = subtypeIn + String.valueOf(subtypesRowList.get(i).getSubtypeId()) + ")";
        }
        String itemPredicate;
        if (subtypeIn == "(")
            itemPredicate = "1=0"; //To.Do. сделать нормально
        else
            itemPredicate = "id_subtype in " + subtypeIn;
        final List<TableItemsRow>  itemsRowList = dbh.SelectTableItems(itemPredicate);
        String str02;
        String str03 = "";
        String str04 = "";
        String str05 = "";
        String str06;

        if (costsRowList.size() != 0)
            str03 = getString(R.string.dict_confirm03) + " " + String.valueOf(costsRowList.size()) + "\n";
        if (subtypesRowList.size() != 0)
            str04 = getString(R.string.dict_confirm04) + " " + String.valueOf(subtypesRowList.size()) + "\n";
        if (itemsRowList.size() != 0)
            str05 = getString(R.string.dict_confirm05) + " " + String.valueOf(itemsRowList.size()) + "\n";
        if (str03.equals("")  && str04.equals("") && str05.equals(""))
        {
            str02 = "";
            str06 = "";
        }
        else
        {
            str02 = getString(R.string.dict_confirm02) + " " + typesRow.getTypeName() + ":\n";
            str06 = getString(R.string.dict_confirm06);
        }
        String titleStr = str02 + str03 + str04 + str05 + str06;

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
        dialogAddType.setTitle(getString(R.string.dict_confirm01) + " " + typesRow.getTypeName()+ "?");
        dialogAddType.setView(container);
        dialogAddType.setPositiveButton(R.string.btn_sure_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < costsRowList.size(); i++)
                {
                    dbh.DeletTableCosts(costsRowList.get(i).getIdCost());
                }

                for (int i = 0; i < itemsRowList.size(); i++)
                {
                    dbh.DeletTableItems(itemsRowList.get(i).getItemId());
                }

                for (int i = 0; i < subtypesRowList.size(); i++)
                {
                    dbh.DeletTableSubtypes(subtypesRowList.get(i).getSubtypeId());
                }

                dbh.DeletTableTypes(typesRow.getTypeId());
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
}
