package com.postman.costscalendar.dao;

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

import com.postman.costscalendar.R;
import com.postman.costscalendar.dto.TableCostsRow;
import com.postman.costscalendar.dto.TableItemsRow;
import com.postman.costscalendar.dto.TableSmsTemplateRow;
import com.postman.costscalendar.dto.TableSubtypesRow;
import com.postman.costscalendar.dto.TableTypesRow;

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
    private static final String COSTS_N_SMS_ID = "N_SMS_ID";
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
    private static final String TABLE_SMS_TEMPLATES = "SMS_TEMPLATES";
    private static final String SMS_TEMPLATES_ID_SMS_TEMP = "ID_SMS_TEMP";
    private static final String SMS_TEMPLATES_V_PHONE_NUM = "V_PHONE_NUM";
    private static final String SMS_TEMPLATES_V_STR_BEFORE_SUM = "V_STR_BEFORE_SUM";
    private static final String SMS_TEMPLATES_V_STR_AFTER_SUM = "V_STR_AFTER_SUM";
    private static final String SMS_TEMPLATES_V_TEMP_NAME = "V_TEMP_NAME";

    private final Context contxt;
    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        contxt = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

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
                + COSTS_N_SMS_ID + " INTEGER,"
                + "FOREIGN KEY (" + COSTS_ID_TYPE + ") REFERENCES " + TABLE_TYPES + "(" + TYPES_ID_TYPE + "),"
                + "FOREIGN KEY (" + COSTS_ID_SUBTYPE + ") REFERENCES " + TABLE_SUBTYPES + "(" + SUBTYPES_ID_SUBTYPE + "),"
                + "FOREIGN KEY (" + COSTS_ID_ITEM + ") REFERENCES " + TABLE_ITEMS + "(" + ITEMS_ID_ITEM + ")"
                + ")";
        db.execSQL(CREATE_COSTS_TABLE);

        String CREATE_SMS = "CREATE TABLE IF NOT EXISTS " + TABLE_SMS_TEMPLATES + "("
                + SMS_TEMPLATES_ID_SMS_TEMP + " INTEGER PRIMARY KEY,"
                + SMS_TEMPLATES_V_PHONE_NUM + " TEXT,"
                + SMS_TEMPLATES_V_STR_BEFORE_SUM + " TEXT,"
                + SMS_TEMPLATES_V_STR_AFTER_SUM + " TEXT,"
                + SMS_TEMPLATES_V_TEMP_NAME + " TEXT"
                + ")";
        Log.d("dbhandler", CREATE_SMS);
        db.execSQL(CREATE_SMS);



    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    ////////////INSERT SECTION/////////////

    public void InsertTableCosts(TableCostsRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COSTS_DT_DATE, row.getStringDate());
        values.put(COSTS_ID_TYPE, row.getIdType());
        values.put(COSTS_ID_SUBTYPE, row.getIdSubtype());
        values.put(COSTS_ID_ITEM, row.getIdItem());
        values.put(COSTS_F_SUM, row.getCostSum());
        values.put(COSTS_V_COMMENT, row.getCostComment());
        values.put(COSTS_N_SMS_ID,row.getSmsId());
        db.insert(TABLE_COSTS, null, values);
        //db.close();
    }

    public void InsertTableTypes(TableTypesRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TYPES_ID_TYPE, row.getTypeId());
        values.put(TYPES_V_NAME, row.getTypeName());
        db.insert(TABLE_TYPES, null, values);
       // db.close();
    }

    public long InsertTableTypesGetId(TableTypesRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TYPES_ID_TYPE, row.getTypeId());
        values.put(TYPES_V_NAME, row.getTypeName());
        long id = db.insert(TABLE_TYPES, null, values);
        return id;
        // db.close();
    }

    public void InsertTableSubtypes(TableSubtypesRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SUBTYPES_ID_SUBTYPE, row.getSubtypeId());
        values.put(SUBTYPES_ID_TYPE, row.getTypeId());
        values.put(SUBTYPES_V_NAME, row.getSubTypeName());
        db.insert(TABLE_SUBTYPES, null, values);
       // db.close();
    }

    public long InsertTableSubtypesGetId(TableSubtypesRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SUBTYPES_ID_SUBTYPE, row.getSubtypeId());
        values.put(SUBTYPES_ID_TYPE, row.getTypeId());
        values.put(SUBTYPES_V_NAME, row.getSubTypeName());
        long id = db.insert(TABLE_SUBTYPES, null, values);
        return id;
        // db.close();
    }

    public void InsertTableItems(TableItemsRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ITEMS_ID_SUBTYPE, row.getSubtypeId());
        values.put(ITEMS_ID_ITEM, row.getItemId());
        values.put(ITEMS_V_NAME, row.getNameId());
        db.insert(TABLE_ITEMS, null, values);
        // db.close();
    }

    public long InsertTableItemsGetId(TableItemsRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ITEMS_ID_SUBTYPE, row.getSubtypeId());
        values.put(ITEMS_ID_ITEM, row.getItemId());
        values.put(ITEMS_V_NAME, row.getNameId());
        long id = db.insert(TABLE_ITEMS, null, values);
        return id;
        // db.close();
    }

    public void InsertTableSmsTemp (TableSmsTemplateRow row) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SMS_TEMPLATES_V_PHONE_NUM, row.getPhoneNumber());
        values.put(SMS_TEMPLATES_V_STR_AFTER_SUM, row.getStringAfterSum());
        values.put(SMS_TEMPLATES_V_STR_BEFORE_SUM, row.getStringBeforeSum());
        values.put(SMS_TEMPLATES_V_TEMP_NAME, row.getTemplateName());
        db.insert(TABLE_SMS_TEMPLATES, null, values);
        // db.close();
    }
    ////////////UPDATE SECTION/////////////


    public void UpdateTableCosts(TableCostsRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COSTS_DT_DATE, row.getStringDate());
        values.put(COSTS_ID_TYPE, row.getIdType());
        values.put(COSTS_ID_SUBTYPE, row.getIdSubtype());
        values.put(COSTS_ID_ITEM, row.getIdItem());
        values.put(COSTS_F_SUM, row.getCostSum());
        values.put(COSTS_V_COMMENT, row.getCostComment());
        db.update(TABLE_COSTS, values, COSTS_ID_COST+"=?", new String[]{String.valueOf(row.getIdCost())});
        //db.close();
    }

    public void UpdateTableTypes(TableTypesRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(TYPES_ID_TYPE, row.getTypeId());
        values.put(TYPES_V_NAME, row.getTypeName());
        db.update(TABLE_TYPES, values, TYPES_ID_TYPE+"=?", new String[]{String.valueOf(row.getTypeId())});
        // db.close();
    }

    public void UpdateTableSubypes(TableSubtypesRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SUBTYPES_ID_SUBTYPE, row.getSubtypeId());
        values.put(SUBTYPES_ID_TYPE, row.getTypeId());
        values.put(SUBTYPES_V_NAME, row.getSubTypeName());
        db.update(TABLE_SUBTYPES, values, SUBTYPES_ID_SUBTYPE+"=?", new String[]{String.valueOf(row.getSubtypeId())});
        // db.close();
    }
    public void UpdateTableItems(TableItemsRow row){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(ITEMS_ID_SUBTYPE, row.getSubtypeId());
        values.put(ITEMS_ID_ITEM, row.getItemId());
        values.put(ITEMS_V_NAME, row.getNameId());
        db.update(TABLE_ITEMS, values, ITEMS_ID_ITEM+"=?", new String[]{String.valueOf(row.getItemId())});
        // db.close();
    }

    public void UpdateTableSmsTemp (TableSmsTemplateRow row) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SMS_TEMPLATES_V_PHONE_NUM, row.getPhoneNumber());
        values.put(SMS_TEMPLATES_V_STR_AFTER_SUM, row.getStringAfterSum());
        values.put(SMS_TEMPLATES_V_STR_BEFORE_SUM, row.getStringBeforeSum());
        values.put(SMS_TEMPLATES_V_TEMP_NAME, row.getTemplateName());
        db.update(TABLE_SMS_TEMPLATES, values, SMS_TEMPLATES_ID_SMS_TEMP+"=?", new String[]{String.valueOf(row.getSmsTempId())});
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
        List<TableSubtypesRow> subtypesRows = SelectTableSubtype(SUBTYPES_ID_TYPE + "=" + String.valueOf(id_row));
        for (int i=0; i < subtypesRows.size();i++)
        {
            db.delete(TABLE_ITEMS, ITEMS_ID_SUBTYPE+"=?", new String[]{String.valueOf(subtypesRows.get(i).getSubtypeId())});
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

    public void DeletTableSmsTemp(Integer id_row){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_SMS_TEMPLATES, SMS_TEMPLATES_ID_SMS_TEMP+"=?", new String[]{String.valueOf(id_row)});
        //db.close();
    }

    ////////////SELECT SECTION/////////////

    public List<TableCostsRow> SelectTableCosts(String predict, String group, String group_select ){
        List<TableCostsRow> tableCostsRows  = new ArrayList<>();

        //building sql query
        String where;
        String groupBy;
        String select;
        String orderBy;

        if (predict == null)
            where = "";
        else
        {
            where = "where " + predict;
        }

        if (group == null)
        {
            groupBy = "";
            select = "c.*, t.v_name as V_TYPE_NAME, st.v_name as V_SUBTYPE_NAME, it.v_name as V_ITEM_NAME";
            orderBy = " order by id_cost desc ";
        }

        else
        {
            groupBy = "group by " + group;
            select = "sum(" + COSTS_F_SUM + ") as " + COSTS_F_SUM + ", " + group_select;
            orderBy = "";
        }

        //final query
        String query="SELECT " + select + "  FROM "+TABLE_COSTS
                     + " c left join " + TABLE_TYPES + " t on c.id_type = t.id_type"
                     + " left join " + TABLE_SUBTYPES + " st on st.id_subtype = c.id_subtype "
                     + " left join " + TABLE_ITEMS + " it on it.id_item = c.id_item "
                     + where + " "
                     + groupBy
                     + orderBy;
        Log.d("db_handler",query);
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query, null);

        //fetch cursor to TableCostsRow
        if(cursor.moveToFirst()){
            do{
                TableCostsRow row=new TableCostsRow();
                if (cursor.getColumnIndex(COSTS_ID_COST) == -1 || cursor.isNull(cursor.getColumnIndex(COSTS_ID_COST)))
                    row.setIdCost(null);
                else
                    row.setIdCost(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(COSTS_ID_COST))));

                if (cursor.getColumnIndex(COSTS_ID_TYPE) == -1 || cursor.isNull(cursor.getColumnIndex(COSTS_ID_TYPE)))
                    row.setIdType(null);
                else
                    row.setIdType(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(COSTS_ID_TYPE))));

                if (cursor.getColumnIndex(COSTS_ID_SUBTYPE) == -1  || cursor.isNull(cursor.getColumnIndex(COSTS_ID_SUBTYPE)))
                    row.setIdSubtype(null);
                else
                    row.setIdSubtype(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(COSTS_ID_SUBTYPE))));

                if (cursor.getColumnIndex(COSTS_F_SUM) == -1  || cursor.isNull(cursor.getColumnIndex(COSTS_F_SUM)))
                    row.setCostSum(0);
                else
                    row.setCostSum(Float.valueOf(cursor.getFloat(cursor.getColumnIndex(COSTS_F_SUM))));

                if (cursor.getColumnIndex(COSTS_ID_ITEM) == -1  || cursor.isNull(cursor.getColumnIndex(COSTS_ID_ITEM)))
                    row.setIdItem(null);
                else
                    row.setIdItem(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(COSTS_ID_ITEM))));

                if (cursor.getColumnIndex(COSTS_V_COMMENT) == -1)
                    row.setCostComment(null);
                else
                    row.setCostComment(String.valueOf(cursor.getString(cursor.getColumnIndex(COSTS_V_COMMENT))));

                if (cursor.getColumnIndex(COSTS_DT_DATE) == -1)
                    row.setStringDate(null);
                else
                    row.setStringDate(String.valueOf(cursor.getString(cursor.getColumnIndex(COSTS_DT_DATE))));

                if (cursor.getColumnIndex(COSTS_N_SMS_ID) == -1  || cursor.isNull(cursor.getColumnIndex(COSTS_N_SMS_ID)))
                    row.setSmsId(null);
                else
                    row.setSmsId(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(COSTS_N_SMS_ID))));

                if (cursor.getColumnIndex("V_TYPE_NAME") == -1)
                    row.setTypeName(null);
                else
                    row.setTypeName(String.valueOf(cursor.getString(cursor.getColumnIndex("V_TYPE_NAME"))));

                if (cursor.getColumnIndex("V_SUBTYPE_NAME") == -1)
                    row.setSubtypeName(null);
                else
                    row.setSubtypeName(String.valueOf(cursor.getString(cursor.getColumnIndex("V_SUBTYPE_NAME"))));

                if (cursor.getColumnIndex("V_ITEM_NAME") == -1)
                    row.setItemName(null);
                else
                    row.setItemName(String.valueOf(cursor.getString(cursor.getColumnIndex("V_ITEM_NAME"))));

                tableCostsRows.add(row);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return tableCostsRows;
    }

    public List<TableTypesRow> SelectTableTypes(String predict ) {
        List<TableTypesRow> tableTypesRowList = new ArrayList<>();

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
                    row.setTypeId(null);
                else
                    row.setTypeId(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(TYPES_ID_TYPE))));

                if (cursor.getColumnIndex(TYPES_V_NAME) == -1)
                    row.setTypeName(null);
                else
                    row.setTypeName(String.valueOf(cursor.getString(cursor.getColumnIndex(TYPES_V_NAME))));

                tableTypesRowList.add(row);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return  tableTypesRowList;
    }

    public List<TableSubtypesRow> SelectTableSubtype(String predict ) {
        List<TableSubtypesRow> tableSubtypesRowList = new ArrayList<>();

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
                    row.setSubtypeId(null);
                else
                    row.setSubtypeId(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(SUBTYPES_ID_SUBTYPE))));

                if (cursor.getColumnIndex(SUBTYPES_ID_TYPE) == -1 || cursor.isNull(cursor.getColumnIndex(SUBTYPES_ID_TYPE)))
                    row.setTypeId(null);
                else
                    row.setTypeId(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(SUBTYPES_ID_TYPE))));

                if (cursor.getColumnIndex(SUBTYPES_V_NAME) == -1)
                    row.setSubTypeName(null);
                else
                    row.setSubTypeName(String.valueOf(cursor.getString(cursor.getColumnIndex(SUBTYPES_V_NAME))));

                tableSubtypesRowList.add(row);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return  tableSubtypesRowList;
    }

    public List<TableItemsRow> SelectTableItems(String predict ) {
        List<TableItemsRow> itemsRowList = new ArrayList<>();

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
                    row.setSubtypeId(null);
                else
                    row.setSubtypeId(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(ITEMS_ID_SUBTYPE))));

                if (cursor.getColumnIndex(ITEMS_ID_ITEM) == -1 || cursor.isNull(cursor.getColumnIndex(ITEMS_ID_ITEM)))
                    row.setItemId(null);
                else
                    row.setItemId(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(ITEMS_ID_ITEM))));

                if (cursor.getColumnIndex(ITEMS_V_NAME) == -1)
                    row.setNameId(null);
                else
                    row.setNameId(String.valueOf(cursor.getString(cursor.getColumnIndex(ITEMS_V_NAME))));

                itemsRowList.add(row);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return  itemsRowList;
    }

    public List<TableSmsTemplateRow> SelectTableSmsTemp(String predict ) {
        List<TableSmsTemplateRow> smsTemplateRowList = new ArrayList<>();

        //building sql query
        String where;

        if (predict == null)
            where = "";
        else
        {
            where = " where " + predict;
        }

        String query = "select * from " + TABLE_SMS_TEMPLATES + where;
        //Log.d("dbhandler","items query:" + query);
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query, null);

        //fetch cursor to TableCostsRow
        if(cursor.moveToFirst()){
            do{
                TableSmsTemplateRow row = new TableSmsTemplateRow();
                if (cursor.getColumnIndex(SMS_TEMPLATES_ID_SMS_TEMP) == -1 || cursor.isNull(cursor.getColumnIndex(SMS_TEMPLATES_ID_SMS_TEMP)))
                    row.setSmsTempId(null);
                else
                    row.setSmsTempId(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(SMS_TEMPLATES_ID_SMS_TEMP))));

                if (cursor.getColumnIndex(SMS_TEMPLATES_V_PHONE_NUM) == -1)
                    row.setPhoneNumber(null);
                else
                    row.setPhoneNumber(String.valueOf(cursor.getString(cursor.getColumnIndex(SMS_TEMPLATES_V_PHONE_NUM))));

                if (cursor.getColumnIndex(SMS_TEMPLATES_V_STR_BEFORE_SUM) == -1)
                    row.setStringBeforeSum(null);
                else
                    row.setStringBeforeSum(String.valueOf(cursor.getString(cursor.getColumnIndex(SMS_TEMPLATES_V_STR_BEFORE_SUM))));

                if (cursor.getColumnIndex(SMS_TEMPLATES_V_STR_AFTER_SUM) == -1)
                    row.setStringAfterSum(null);
                else
                    row.setStringAfterSum(String.valueOf(cursor.getString(cursor.getColumnIndex(SMS_TEMPLATES_V_STR_AFTER_SUM))));

                if (cursor.getColumnIndex(SMS_TEMPLATES_V_TEMP_NAME) == -1)
                    row.setTemplateName(null);
                else
                    row.setTemplateName(String.valueOf(cursor.getString(cursor.getColumnIndex(SMS_TEMPLATES_V_TEMP_NAME))));

                smsTemplateRowList.add(row);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return  smsTemplateRowList;
    }


    /////////INIT SECTION/////////////

    public void InitDbTables()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_SMS = "CREATE TABLE IF NOT EXISTS " + TABLE_SMS_TEMPLATES + "("
                + SMS_TEMPLATES_ID_SMS_TEMP + " INTEGER PRIMARY KEY,"
                + SMS_TEMPLATES_V_PHONE_NUM + " TEXT,"
                + SMS_TEMPLATES_V_STR_BEFORE_SUM + " TEXT,"
                + SMS_TEMPLATES_V_STR_AFTER_SUM + " TEXT,"
                + SMS_TEMPLATES_V_TEMP_NAME + " TEXT"
                + ")";
        Log.d("dbhandler", CREATE_SMS);
        db.execSQL(CREATE_SMS);

        //ALTER TABLES
        Cursor cursorAlter = db.rawQuery("SELECT * FROM " + TABLE_COSTS, null);
        int deleteStateColumnIndex = cursorAlter.getColumnIndex(COSTS_N_SMS_ID);
        Log.d("db", String.valueOf(deleteStateColumnIndex));
        if (deleteStateColumnIndex < 0) {
            db.execSQL("ALTER TABLE " + TABLE_COSTS + " ADD COLUMN " + COSTS_N_SMS_ID + " INTEGER;");
        }

        //if table T_INIT is empty then fill DB by default values (and fill T_INIT table)
        //else do nothing
        Cursor cursor=db.rawQuery("Select * from " + TABLE_INIT + ";", null);
        ContentValues valueInit = new ContentValues();
        if(cursor!=null && cursor.getCount()>0)
        {
            Log.d("db_handler", "already init");
            return;
        }
        else
        {
            Log.d("db_handler", "no rows in init table");
            valueInit.put(INIT_IS_INIT, 1);
            db.insert(TABLE_INIT, null, valueInit);
            TableTypesRow typesRow = new TableTypesRow();
            TableSubtypesRow subtypesRow = new TableSubtypesRow();

            Resources res = contxt.getResources();

            XmlPullParser xmlPullParser = res.getXml(R.xml.db_init_data);
            try {
            int eventType = -1;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                    // Ищем теги record
                if ((eventType == XmlResourceParser.START_TAG)
                            && (xmlPullParser.getName().equals("type"))) {
                    String id = xmlPullParser.getAttributeValue(null,"ID_TYPE");

                        typesRow.setTypeId(Integer.parseInt(xmlPullParser.getAttributeValue(null,"ID_TYPE")));
                        typesRow.setTypeName(xmlPullParser.getAttributeValue(null, "V_NAME"));
                    Log.d("dbhandler", "id " + String.valueOf(typesRow.getTypeId()));
                    Log.d("dbhandler", "name " + typesRow.getTypeName());
                        InsertTableTypes(typesRow);
                }

                if ((eventType == XmlResourceParser.START_TAG)
                        && (xmlPullParser.getName().equals("subtype"))) {

                    subtypesRow.setTypeId(Integer.parseInt(xmlPullParser.getAttributeValue(null,"ID_TYPE")));
                    subtypesRow.setSubtypeId(Integer.parseInt(xmlPullParser.getAttributeValue(null, "ID_SUBTYPE")));
                    subtypesRow.setSubTypeName(xmlPullParser.getAttributeValue(null, "V_NAME"));

                    InsertTableSubtypes(subtypesRow);
                }

                 eventType = xmlPullParser.next();
            }
            }
            // Catch errors
            catch (XmlPullParserException e) {
                Log.e("Test", e.getMessage(), e);
            } catch (IOException e) {
                Log.e("Test", e.getMessage(), e);

            } finally {
                // Close the xml file
                ((XmlResourceParser) xmlPullParser).close();
            }

        }



    }
}