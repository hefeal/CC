package com.postman.costscalendar.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.postman.costscalendar.R;
import com.postman.costscalendar.dao.DatabaseHandler;
import com.postman.costscalendar.dto.TableCostsRow;
import com.postman.costscalendar.model.CostPiece;
import com.postman.costscalendar.activity.AddCost;

public class DaysCosts extends AppCompatActivity {
    String stringDate;
    List<TableCostsRow> costsRowList;
    List<CostPiece> costPieceList;
    int selectCount;
    TextView viewSelectCount;
    Button btnEdit;
    Button btnDelete;
    DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_costs);
        stringDate = getIntent().getStringExtra("date");

        viewSelectCount = findViewById(R.id.textView8);
        btnEdit = findViewById(R.id.btn_edit_cost);
        btnDelete = findViewById(R.id.btn_delete_cost);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);

        databaseHandler = new DatabaseHandler(this);
        String predic = "dt_date = '" + stringDate + "'";
        costsRowList = databaseHandler.SelectTableCosts(predic, null,null);
        TextView dateTxt = findViewById(R.id.textView7);
        dateTxt.setText(stringDate);
        costPieceList = new ArrayList<>();

        for (int i = 0; i < costsRowList.size(); i++)
        {
            LayoutInflater lif = getLayoutInflater();
            LinearLayout linLayout = (LinearLayout) findViewById(R.id.scroll_linear);
            final View view = lif.inflate(R.layout.cost_piece, linLayout, false);
            LayoutParams lp1 = view.getLayoutParams();
            view.setId(costsRowList.get(i).getIdCost());
            linLayout.addView(view);
            costPieceList.add(new CostPiece(view.getId(),false));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < costPieceList.size(); j++)
                    {
                        OnSelectCostView(j, view);
                    }
                }
            });

            TextView textView = view.findViewById(R.id.txt_type);
            textView.setText(costsRowList.get(i).getTypeName());

            textView = view.findViewById(R.id.txt_subtype);
            textView.setText(costsRowList.get(i).getSubtypeName());
            if (CheckIfNullString(costsRowList.get(i).getSubtypeName()))
                textView.setVisibility(View.GONE);

            textView = view.findViewById(R.id.txt_item);
            textView.setText(costsRowList.get(i).getItemName());
            if (CheckIfNullString(costsRowList.get(i).getItemName()))
                textView.setVisibility(View.GONE);

            textView = view.findViewById(R.id.txt_comment);
            textView.setText(costsRowList.get(i).getCostComment());

            textView = view.findViewById(R.id.txt_sum);
            textView.setText(String.format("%.2f", costsRowList.get(i).getCostSum()));

        }
    }

    boolean CheckIfNullString(String str)
    {
        if (str == null || str == "null" || str == "")
            return  true;
        else
            return  false;
    }

    public  void OnClickAddCost(View view)
    {
        Intent intent = new Intent(this, AddCost.class);
        intent.putExtra("date", stringDate);
        intent.putExtra("is_edit",false  );
        startActivity(intent);
        finish();

    }

    public  void OnClickEditCost(View view)
    {
        Intent intent = new Intent(this, AddCost.class);
        intent.putExtra("date", stringDate);
        intent.putExtra("is_edit",true  );
        ArrayList<Integer> v_id = GetSelectedViewId();
        Log.d("daycost",String.format("%d",v_id.get(0)));
        intent.putExtra("id_edit",v_id.get(0));
        startActivity(intent);
        finish();
    }

    public  void OnClickDeleteCost(View view)
    {
        Log.d("daycost", "rabotaet");
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this);
        dialogDelete.setTitle(R.string.dialog_delete_cost);
        dialogDelete.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < costPieceList.size(); i++)
                {
                    if (costPieceList.get(i).getSelected()) {
                        databaseHandler.DeletTableCosts(costPieceList.get(i).getViewId());
                    }

                }
                recreate();
            }
        });
        dialogDelete.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogDelete.create().show();

    }

    public void OnSelectCostView(int j, View view)
    {
        //Log.d("daycost", "view_id: " +  String.format("%d",view.getId()));
        if (view.getId() == costPieceList.get(j).getViewId())
        {
            costPieceList.get(j).ChangeState();
            if (costPieceList.get(j).getSelected()) {
                view.setBackground(getDrawable(R.drawable.cost_selected));
                selectCount++;
            }
            else {
                view.setBackground(getDrawable(R.drawable.shadow));
                selectCount--;
                if (selectCount < 0)
                    selectCount =0;
            }
            if (selectCount == 1)
            {
                viewSelectCount.setText(getString(R.string.selected_count) + String.format("%d", selectCount));
                btnEdit.setEnabled(true);
                btnDelete.setEnabled(true);
            }
            else if (selectCount > 1)
            {
                viewSelectCount.setText(getString(R.string.selected_count) + String.format("%d", selectCount));
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(true);
            }
            else
            {
                viewSelectCount.setText("");
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
            }
            //Log.d("daycost",  String.format("%d",select_count));
        }
    }

    public ArrayList<Integer> GetSelectedViewId()
    {
        ArrayList<Integer> selectedIdList = new ArrayList<>();
        for (int i = 0; i < costPieceList.size(); i++)
        {
            if (costPieceList.get(i).getSelected())
            {
                selectedIdList.add(costPieceList.get(i).getViewId());
                Log.d("daycosts", String.format("%d",i) + " "
                        + String.format("%d", costPieceList.get(i).getViewId()));
            }
        }
        return  selectedIdList;
    }
}
