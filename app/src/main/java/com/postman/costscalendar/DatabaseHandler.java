package com.postman.costscalendar;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class DatabaseHandler extends SQLiteOpenHelper {
    //private static final String date_format = "dd/MM/yyyy";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CostsCalendar_db";
    private static final String TABLE_COSTS = "COSTS";
    private static final String COSTS_ID_COST = "ID_COST";
    private static final String COSTS_DT_DATE = "DT_DATE";
    private static final String COSTS_ID_TYPE = "ID_TYPE";
    private static final String COSTS_ID_SUBTYPE = "ID_SUBTYPE";
    private static final String COSTS_ID_ITEM = "ID_ITEM";
    private static final String COSTS_F_SUM = "F_SUM";
    private static final String COSTS_V_COMMENT = "V_COMMENT";
    private static final String TABLE_TYPES = "TYPES";
    private static final String TYPES_ID_TYPE = "ID_TYPE";
    private static final String TYPES_V_NAME = "V_NAME";
    private static final String TABLE_SUBTYPES = "SUBTYPES";
    private static final String SUBTYPES_ID_SUBTYPE = "ID_SUBTYPE";
    private static final String SUBTYPES_ID_TYPE = "ID_TYPE";
    private static final String SUBTYPES_V_NAME = "V_NAME";
    private static final String TABLE_ITEMS = "ITEMS";
    private static final String ITEMS_ID_ITEM = "ID_ITEM";
    private static final String ITEMS_ID_SUBTYPE = "ID_SUBTYPE";
    private static final String ITEMS_V_NAME = "V_NAME";
    private static final String TABLE_INIT = "T_INIT";
    private static final String INIT_IS_INIT = "B_IS_INIT";

    private final Context contxt;
    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        contxt = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //this.this_db = db;
        db.execSQL("PRAGMA foreign_keys = ON;");

        String CREATE_INIT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_INIT + "("
                + INIT_IS_INIT + " INTEGER )";
        db.execSQL(CREATE_INIT_TABLE);

        String CREATE_TYPES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TYPES + "("
                + TYPES_ID_TYPE + " INTEGER PRIMARY KEY,"
                + TYPES_V_NAME + " TEXT"  + ")";
        db.execSQL(CREATE_TYPES_TABLE);

        String CREATE_SUBTYPES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SUBTYPES + "("
                + SUBTYPES_ID_SUBTYPE + " INTEGER PRIMARY KEY,"
                + SUBTYPES_ID_TYPE + " INTEGER,"
                + SUBTYPES_V_NAME + " TEXT,"
                + "FOREIGN KEY (" + SUBTYPES_ID_TYPE + ") REFERENCES " + TABLE_TYPES + "(" + TYPES_ID_TYPE + ")"
                + ")";
        db.execSQL(CREATE_SUBTYPES_TABLE);

        String CREATE_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + "("
                + ITEMS_ID_ITEM + " INTEGER PRIMARY KEY,"
                + ITEMS_ID_SUBTYPE + " INTEGER,"
                + ITEMS_V_NAME + " TEXT,"
                + "FOREIGN KEY (" + ITEMS_ID_SUBTYPE + ") REFERENCES " + TABLE_SUBTYPES + "(" + SUBTYPES_ID_SUBTYPE + ")"
                + ")";
        db.execSQL(CREATE_ITEMS_TABLE);

        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_COSTS);

        String CREATE_COSTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_COSTS + "("
                + COSTS_ID_COST + " INTEGER PRIMARY KEY,"
                + COSTS_DT_DATE + " DATE,"
                + COSTS_ID_TYPE + " INTEGER,"
                + COSTS_ID_SUBTYPE + " INTEGER,"
                + COSTS_ID_ITEM + " INTEGER,"
                + COSTS_F_SUM + " REAL,"
                + COSTS_V_COMMENT + " TEXT,"
                + "FOREIGN KEY (" + COSTS_ID_TYPE + ") REFERENCES " + TABLE_TYPES + "(" + TYPES_ID_TYPE + "),"
                + "FOREIGN KEY (" + COSTS_ID_SUBTYPE + ") REFERENCES " + TABLE_SUBTYPES + "(" + SUBTYPES_ID_SUBTYPE + "),"
                + "FOREIGN KEY (" + COSTS_ID_ITEM + ") REFERENCES " + TABLE_ITEMS + "(" + ITEMS_ID_ITEM + ")"
                + ")";
        db.execSQL(CREATE_COSTS_TABLE);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //    db.execSQL("DROP TABLE IF EXISTS " + TABLE_COSTS);
    //    onCreate(db);
    }

    ////////////INSERT SECTION/////////////

    public void InsertTableCosts(TableCostsRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COSTS_DT_DATE, row.getV_date());
        values.put(COSTS_ID_TYPE, row.getId_type());
        values.put(COSTS_ID_SUBTYPE, row.getId_subtype());
        values.put(COSTS_ID_ITEM, row.getId_item());
        values.put(COSTS_F_SUM, row.getF_sum());
        values.put(COSTS_V_COMMENT, row.getV_comment());
        db.insert(TABLE_COSTS, null, values);
        //db.close();
    }

    public void InsertTableTypes(TableTypesRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TYPES_ID_TYPE, row.getId_type());
        values.put(TYPES_V_NAME, row.getV_name());
        db.insert(TABLE_TYPES, null, values);
       // db.close();
    }

    public long InsertTableTypesGetId(TableTypesRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TYPES_ID_TYPE, row.getId_type());
        values.put(TYPES_V_NAME, row.getV_name());
        long id = db.insert(TABLE_TYPES, null, values);
        return id;
        // db.close();
    }

    public void InsertTableSubtypes(TableSubtypesRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SUBTYPES_ID_SUBTYPE, row.getId_subtype());
        values.put(SUBTYPES_ID_TYPE, row.getId_type());
        values.put(SUBTYPES_V_NAME, row.getV_name());
        db.insert(TABLE_SUBTYPES, null, values);
       // db.close();
    }

    public long InsertTableSubtypesGetId(TableSubtypesRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SUBTYPES_ID_SUBTYPE, row.getId_subtype());
        values.put(SUBTYPES_ID_TYPE, row.getId_type());
        values.put(SUBTYPES_V_NAME, row.getV_name());
        long id = db.insert(TABLE_SUBTYPES, null, values);
        return id;
        // db.close();
    }

    public void InsertTableItems(TableItemsRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ITEMS_ID_SUBTYPE, row.getId_subtype());
        values.put(ITEMS_ID_ITEM, row.getId_item());
        values.put(ITEMS_V_NAME, row.getV_name());
        db.insert(TABLE_ITEMS, null, values);
        // db.close();
    }

    public long InsertTableItemsGetId(TableItemsRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ITEMS_ID_SUBTYPE, row.getId_subtype());
        values.put(ITEMS_ID_ITEM, row.getId_item());
        values.put(ITEMS_V_NAME, row.getV_name());
        long id = db.insert(TABLE_ITEMS, null, values);
        return id;
        // db.close();
    }

    ////////////UPDATE SECTION/////////////


    public void UpdateTableCosts(TableCostsRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COSTS_DT_DATE, row.getV_date());
        values.put(COSTS_ID_TYPE, row.getId_type());
        values.put(COSTS_ID_SUBTYPE, row.getId_subtype());
        values.put(COSTS_ID_ITEM, row.getId_item());
        values.put(COSTS_F_SUM, row.getF_sum());
        values.put(COSTS_V_COMMENT, row.getV_comment());
        db.update(TABLE_COSTS, values, COSTS_ID_COST+"=?", new String[]{String.valueOf(row.getId_cost())});
        //db.close();
    }

    public void UpdateTableTypes(TableTypesRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TYPES_ID_TYPE, row.getId_type());
        values.put(TYPES_V_NAME, row.getV_name());
        db.update(TABLE_TYPES, values, TYPES_ID_TYPE+"=?", new String[]{String.valueOf(row.getId_type())});
        // db.close();
    }

    public void UpdateTableSubypes(TableSubtypesRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SUBTYPES_ID_SUBTYPE, row.getId_subtype());
        values.put(SUBTYPES_ID_TYPE, row.getId_type());
        values.put(SUBTYPES_V_NAME, row.getV_name());
        db.update(TABLE_SUBTYPES, values, SUBTYPES_ID_SUBTYPE+"=?", new String[]{String.valueOf(row.getId_subtype())});
        // db.close();
    }
    public void UpdateTableItems(TableItemsRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ITEMS_ID_SUBTYPE, row.getId_subtype());
        values.put(ITEMS_ID_ITEM, row.getId_item());
        values.put(ITEMS_V_NAME, row.getV_name());
        db.update(TABLE_ITEMS, values, ITEMS_ID_ITEM+"=?", new String[]{String.valueOf(row.getId_item())});
        // db.close();
    }

    ////////////DELETE SECTION/////////////

    public void DeletTableCosts(Integer id_row){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_COSTS, COSTS_ID_COST+"=?", new String[]{String.valueOf(id_row)});
        //db.close();
    }

    public void DeletTableTypes(Integer id_row){
        SQLiteDatabase db=this.getWritableDatabase();
        List<TableSubtypesRow> sb_rows = SelectTableSubtype(SUBTYPES_ID_TYPE + "=" + String.valueOf(id_row));
        for (int i=0; i < sb_rows.size();i++)
        {
            db.delete(TABLE_ITEMS, ITEMS_ID_SUBTYPE+"=?", new String[]{String.valueOf(sb_rows.get(i).getId_subtype())});
        }
        db.delete(TABLE_SUBTYPES, SUBTYPES_ID_TYPE+"=?", new String[]{String.valueOf(id_row)});
        db.delete(TABLE_TYPES, TYPES_ID_TYPE+"=?", new String[]{String.valueOf(id_row)});

        //db.close();
    }

    public void DeletTableSubtypes(Integer id_row){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_ITEMS, ITEMS_ID_SUBTYPE+"=?", new String[]{String.valueOf(id_row)});
        db.delete(TABLE_SUBTYPES, SUBTYPES_ID_SUBTYPE+"=?", new String[]{String.valueOf(id_row)});
        //db.close();
    }

    public void DeletTableItems(Integer id_row){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_ITEMS, ITEMS_ID_ITEM+"=?", new String[]{String.valueOf(id_row)});
        //db.close();
    }

    ////////////SELECT SECTION/////////////

    public List<TableCostsRow> SelectTableCosts(String predict, String group, String group_select ){
        List<TableCostsRow> list_costs  = new ArrayList<>();

        //building sql query
        String where;
        String group_by;
        String select;
        String order_by;

        if (predict == null)
            where = "";
        else
        {
            where = "where " + predict;
        }

        if (group == null)
        {
            group_by = "";
            select = "c.*, t.v_name as V_TYPE_NAME, st.v_name as V_SUBTYPE_NAME, it.v_name as V_ITEM_NAME";
            order_by = " order by id_cost desc ";
        }

        else
        {
            group_by = "group by " + group;
            select = "sum(" + COSTS_F_SUM + ") as " + COSTS_F_SUM + ", " + group_select;
            order_by = "";
        }

        //final query
        String query="SELECT " + select + "  FROM "+TABLE_COSTS
                     + " c left join " + TABLE_TYPES + " t on c.id_type = t.id_type"
                     + " left join " + TABLE_SUBTYPES + " st on st.id_subtype = c.id_subtype "
                     + " left join " + TABLE_ITEMS + " it on it.id_item = c.id_item "
                     + where + " "
                     + group_by
                     + order_by;
        Log.d("db_handler",query);
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query, null);

        //fetch cursor to TableCostsRow
        if(cursor.moveToFirst()){
            do{
                TableCostsRow row=new TableCostsRow();
                if (cursor.getColumnIndex(COSTS_ID_COST) == -1 || cursor.isNull(cursor.getColumnIndex(COSTS_ID_COST)))
                    row.setId_cost(null);
                else
                    row.setId_cost(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(COSTS_ID_COST))));

                if (cursor.getColumnIndex(COSTS_ID_TYPE) == -1 || cursor.isNull(cursor.getColumnIndex(COSTS_ID_TYPE)))
                    row.setId_type(null);
                else
                    row.setId_type(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(COSTS_ID_TYPE))));

                if (cursor.getColumnIndex(COSTS_ID_SUBTYPE) == -1  || cursor.isNull(cursor.getColumnIndex(COSTS_ID_SUBTYPE)))
                    row.setId_subtype(null);
                else
                    row.setId_subtype(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(COSTS_ID_SUBTYPE))));

                if (cursor.getColumnIndex(COSTS_F_SUM) == -1  || cursor.isNull(cursor.getColumnIndex(COSTS_F_SUM)))
                    row.setF_sum(0);
                else
                    row.setF_sum(Float.valueOf(cursor.getFloat(cursor.getColumnIndex(COSTS_F_SUM))));

                if (cursor.getColumnIndex(COSTS_ID_ITEM) == -1  || cursor.isNull(cursor.getColumnIndex(COSTS_ID_ITEM)))
                    row.setId_item(null);
                else
                    row.setId_item(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(COSTS_ID_ITEM))));

                if (cursor.getColumnIndex(COSTS_V_COMMENT) == -1)
                    row.setV_comment(null);
                else
                    row.setV_comment(String.valueOf(cursor.getString(cursor.getColumnIndex(COSTS_V_COMMENT))));

                if (cursor.getColumnIndex(COSTS_DT_DATE) == -1)
                    row.setV_date(null);
                else
                    row.setV_date(String.valueOf(cursor.getString(cursor.getColumnIndex(COSTS_DT_DATE))));

                if (cursor.getColumnIndex("V_TYPE_NAME") == -1)
                    row.setV_type_name(null);
                else
                    row.setV_type_name(String.valueOf(cursor.getString(cursor.getColumnIndex("V_TYPE_NAME"))));

                if (cursor.getColumnIndex("V_SUBTYPE_NAME") == -1)
                    row.setV_subtype_name(null);
                else
                    row.setV_subtype_name(String.valueOf(cursor.getString(cursor.getColumnIndex("V_SUBTYPE_NAME"))));

                if (cursor.getColumnIndex("V_ITEM_NAME") == -1)
                    row.setV_item_name(null);
                else
                    row.setV_item_name(String.valueOf(cursor.getString(cursor.getColumnIndex("V_ITEM_NAME"))));

                list_costs.add(row);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list_costs;
    }

    public List<TableTypesRow> SelectTableTypes(String predict ) {
        List<TableTypesRow> list_types = new ArrayList<>();

        //building sql query
        String where;

        if (predict == null)
            where = "";
        else
        {
            where = " where " + predict;
        }

        String query = "select * from " + TABLE_TYPES + where + " order by " + TYPES_V_NAME;

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query, null);

        //fetch cursor to TableCostsRow
        if(cursor.moveToFirst()){
            do{
                TableTypesRow row = new TableTypesRow();
                if (cursor.getColumnIndex(TYPES_ID_TYPE) == -1 || cursor.isNull(cursor.getColumnIndex(TYPES_ID_TYPE)))
                    row.setId_type(null);
                else
                    row.setId_type(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(TYPES_ID_TYPE))));

                if (cursor.getColumnIndex(TYPES_V_NAME) == -1)
                    row.setV_name(null);
                else
                    row.setV_name(String.valueOf(cursor.getString(cursor.getColumnIndex(TYPES_V_NAME))));

                list_types.add(row);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return  list_types;
    }

    public List<TableSubtypesRow> SelectTableSubtype(String predict ) {
        List<TableSubtypesRow> list_types = new ArrayList<>();

        //building sql query
        String where;

        if (predict == null)
            where = "";
        else
        {
            where = " where " + predict;
        }

        String query = "select * from " + TABLE_SUBTYPES + where + " order by " + SUBTYPES_V_NAME;
        Log.d("dbhandler","subtype query:" + query);
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query, null);

        //fetch cursor to TableCostsRow
        if(cursor.moveToFirst()){
            do{
                TableSubtypesRow row = new TableSubtypesRow();
                if (cursor.getColumnIndex(SUBTYPES_ID_SUBTYPE) == -1 || cursor.isNull(cursor.getColumnIndex(SUBTYPES_ID_SUBTYPE)))
                    row.setId_subtype(null);
                else
                    row.setId_subtype(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(SUBTYPES_ID_SUBTYPE))));

                if (cursor.getColumnIndex(SUBTYPES_ID_TYPE) == -1 || cursor.isNull(cursor.getColumnIndex(SUBTYPES_ID_TYPE)))
                    row.setId_type(null);
                else
                    row.setId_type(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(SUBTYPES_ID_TYPE))));

                if (cursor.getColumnIndex(SUBTYPES_V_NAME) == -1)
                    row.setV_name(null);
                else
                    row.setV_name(String.valueOf(cursor.getString(cursor.getColumnIndex(SUBTYPES_V_NAME))));

                list_types.add(row);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return  list_types;
    }

    public List<TableItemsRow> SelectTableItems(String predict ) {
        List<TableItemsRow> list_items = new ArrayList<>();

        //building sql query
        String where;

        if (predict == null)
            where = "";
        else
        {
            where = " where " + predict;
        }

        String query = "select * from " + TABLE_ITEMS + where + " order by " + ITEMS_V_NAME;
        Log.d("dbhandler","items query:" + query);
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query, null);

        //fetch cursor to TableCostsRow
        if(cursor.moveToFirst()){
            do{
                TableItemsRow row = new TableItemsRow();
                if (cursor.getColumnIndex(ITEMS_ID_SUBTYPE) == -1 || cursor.isNull(cursor.getColumnIndex(ITEMS_ID_SUBTYPE)))
                    row.setId_subtype(null);
                else
                    row.setId_subtype(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(ITEMS_ID_SUBTYPE))));

                if (cursor.getColumnIndex(ITEMS_ID_ITEM) == -1 || cursor.isNull(cursor.getColumnIndex(ITEMS_ID_ITEM)))
                    row.setId_item(null);
                else
                    row.setId_item(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(ITEMS_ID_ITEM))));

                if (cursor.getColumnIndex(ITEMS_V_NAME) == -1)
                    row.setV_name(null);
                else
                    row.setV_name(String.valueOf(cursor.getString(cursor.getColumnIndex(ITEMS_V_NAME))));

                list_items.add(row);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return  list_items;
    }

    void InitDbTables()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //if table T_INIT is empty then fill DB by default values (and fill T_INIT table)
        //else do nothing
        Cursor cursor=db.rawQuery("Select * from " + TABLE_INIT + ";", null);
        ContentValues value_init=new ContentValues();
        if(cursor!=null && cursor.getCount()>0)
        {
            Log.d("db_handler", "already init");
            return;
        }
        else
        {
            Log.d("db_handler", "no rows in init table");
            value_init.put(INIT_IS_INIT, 1);
            db.insert(TABLE_INIT, null, value_init);
            TableTypesRow t_row = new TableTypesRow();
            TableSubtypesRow ts_row = new TableSubtypesRow();

            Resources res = contxt.getResources();

            XmlPullParser xml_pars = res.getXml(R.xml.db_init_data);
            try {
            int eventType = -1;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                    // Ищем теги record
                if ((eventType == XmlResourceParser.START_TAG)
                            && (xml_pars.getName().equals("type"))) {
                    String id = xml_pars.getAttributeValue(null,"ID_TYPE");

                        t_row.setId_type(Integer.parseInt(xml_pars.getAttributeValue(null,"ID_TYPE")));
                        t_row.setV_name(xml_pars.getAttributeValue(null, "V_NAME"));
                    Log.d("dbhandler", "id " + String.valueOf(t_row.getId_type()));
                    Log.d("dbhandler", "name " + t_row.getV_name());
                        InsertTableTypes(t_row);
                }

                if ((eventType == XmlResourceParser.START_TAG)
                        && (xml_pars.getName().equals("subtype"))) {

                    ts_row.setId_type(Integer.parseInt(xml_pars.getAttributeValue(null,"ID_TYPE")));
                    ts_row.setId_subtype(Integer.parseInt(xml_pars.getAttributeValue(null, "ID_SUBTYPE")));
                    ts_row.setV_name(xml_pars.getAttributeValue(null, "V_NAME"));

                    InsertTableSubtypes(ts_row);
                }

                 eventType = xml_pars.next();
            }
            }
            // Catch errors
            catch (XmlPullParserException e) {
                Log.e("Test", e.getMessage(), e);
            } catch (IOException e) {
                Log.e("Test", e.getMessage(), e);

            } finally {
                // Close the xml file
                ((XmlResourceParser) xml_pars).close();
            }

        }



    }
}