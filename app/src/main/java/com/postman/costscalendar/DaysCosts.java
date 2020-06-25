package com.postman.costscalendar;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DaysCosts extends AppCompatActivity {
    String v_date;
    List<TableCostsRow> list_costs;
    List<CostPiece> cost_pieces;
    int select_count;
    TextView view_sel_count;
    Button btn_edit;
    Button btn_delete;
    DatabaseHandler dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_costs);
        v_date = getIntent().getStringExtra("date");

        view_sel_count = findViewById(R.id.textView8);
        btn_edit = findViewById(R.id.btn_edit_cost);
        btn_delete = findViewById(R.id.btn_delete_cost);
        btn_edit.setEnabled(false);
        btn_delete.setEnabled(false);

        dbh = new DatabaseHandler(this);
        String predic = "dt_date = '" + v_date + "'";
        list_costs = dbh.SelectTableCosts(predic, null,null);
        //Log.d("day", String.format("%d", list_costs.size()));
        TextView date_txt = findViewById(R.id.textView7);
        date_txt.setText(v_date);
        cost_pieces = new ArrayList<>();

        for (int i =0; i < list_costs.size(); i++)
        {
            LayoutInflater lif = getLayoutInflater();
            LinearLayout linLayout = (LinearLayout) findViewById(R.id.scroll_linear);
            final View view = lif.inflate(R.layout.cost_piece, linLayout, false);
            LayoutParams lp1 = view.getLayoutParams();
            view.setId(list_costs.get(i).getId_cost());
            linLayout.addView(view);
            cost_pieces.add(new CostPiece(view.getId(),false));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < cost_pieces.size(); j++)
                    {
                        OnSelectCostView(j, view);
                    }
                }
            });

            TextView textView = view.findViewById(R.id.txt_type);
            textView.setText(list_costs.get(i).getV_type_name());

            textView = view.findViewById(R.id.txt_subtype);
            textView.setText(list_costs.get(i).getV_subtype_name());
            if (CheckIfNullString(list_costs.get(i).getV_subtype_name()))
                textView.setVisibility(View.GONE);

            textView = view.findViewById(R.id.txt_item);
            textView.setText(list_costs.get(i).getV_item_name());
            if (CheckIfNullString(list_costs.get(i).getV_item_name()))
                textView.setVisibility(View.GONE);

            textView = view.findViewById(R.id.txt_comment);
            textView.setText(list_costs.get(i).getV_comment());

            textView = view.findViewById(R.id.txt_sum);
            textView.setText(String.format("%.2f", list_costs.get(i).getF_sum()));

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
        intent.putExtra("date",v_date  );
        intent.putExtra("is_edit",false  );
        startActivity(intent);
        finish();

    }

    public  void OnClickEditCost(View view)
    {
        Intent intent = new Intent(this, AddCost.class);
        intent.putExtra("date",v_date  );
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
        AlertDialog.Builder dialog_delete = new AlertDialog.Builder(this);
        dialog_delete.setTitle(R.string.dialog_delete_cost);
        dialog_delete.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < cost_pieces.size(); i++)
                {
                    if (cost_pieces.get(i).getIs_selected()) {
                        dbh.DeletTableCosts(cost_pieces.get(i).id_view);
                    }

                }
                recreate();
            }
        });
        dialog_delete.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_delete.create().show();

    }

    public void OnSelectCostView(int j, View view)
    {
        //Log.d("daycost", "view_id: " +  String.format("%d",view.getId()));
        if (view.getId() == cost_pieces.get(j).getId_view())
        {
            cost_pieces.get(j).ChangeState();
            if (cost_pieces.get(j).getIs_selected()) {
                view.setBackground(getDrawable(R.drawable.cost_selected));
                select_count++;
            }
            else {
                view.setBackground(getDrawable(R.drawable.shadow));
                select_count--;
                if (select_count < 0)
                    select_count =0;
            }
            if (select_count == 1)
            {
                view_sel_count.setText(getString(R.string.selected_count) + String.format("%d",select_count));
                btn_edit.setEnabled(true);
                btn_delete.setEnabled(true);
            }
            else if (select_count > 1)
            {
                view_sel_count.setText(getString(R.string.selected_count) + String.format("%d",select_count));
                btn_edit.setEnabled(false);
                btn_delete.setEnabled(true);
            }
            else
            {
                view_sel_count.setText("");
                btn_edit.setEnabled(false);
                btn_delete.setEnabled(false);
            }
            //Log.d("daycost",  String.format("%d",select_count));
        }
    }

    public ArrayList<Integer> GetSelectedViewId()
    {
        ArrayList<Integer> v_id = new ArrayList<>();
        for (int i = 0; i < cost_pieces.size(); i++)
        {
            if (cost_pieces.get(i).is_selected)
            {
                v_id.add(cost_pieces.get(i).id_view);
                Log.d("daycosts", String.format("%d",i) + " " + String.format("%d",cost_pieces.get(i).id_view));
            }
        }
        return  v_id;
    }
}
