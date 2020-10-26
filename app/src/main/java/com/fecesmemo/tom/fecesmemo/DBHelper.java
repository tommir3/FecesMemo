package com.fecesmemo.tom.fecesmemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper  extends SQLiteOpenHelper {
    private final static int _dbVersion = 1;
    private final static String _fecesTableName = "FacesInfo";

    public DBHelper(Context context)
    {
        super(context,"FecesMemoDatabase",null,_dbVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        CreateFecesInfoTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(!IsExistTable(db, _fecesTableName))
        {
            CreateFecesInfoTable(db);
        }
    }

    public boolean IsExistTable(SQLiteDatabase db, String tableName)
    {
        boolean result = false;
        try
        {
            String sqlTest = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + tableName + "';";
            Cursor bakData = db.rawQuery(sqlTest,null);
            if(bakData != null)
            {
                bakData.moveToFirst();
                result = (bakData.getInt(0) == 0) ? false : true;
            }
        }
        catch(Exception err)
        {
            result = false;
            System.out.printf(err.getMessage());
        }
        return result;
    }

    //创建便便信息表
    private void CreateFecesInfoTable(SQLiteDatabase db)
    {
        try
        {
            /*"
            create table AAA(
            id int primary key AUTOINCREMENT,
            date text not null,
            remark text null,
            imgPath text null
            )"
             */
            StringBuilder sql = new StringBuilder();
            sql.append("create table IF NOT EXISTS ");
            sql.append(_fecesTableName);
            sql.append("(");
            sql.append("id Integer primary key AUTOINCREMENT,");
            sql.append("date text not null,");
            sql.append("remark text null,");
            sql.append("imgPath text null");
            sql.append(")");
            db.execSQL(sql.toString());
        }
        catch(Exception err)
        {
            System.out.printf(err.getMessage());
        }
    }

    protected int AddFecesInfo(FecesInfo info){
        int result;
        try
        {
            ContentValues values = new ContentValues();
            values.put("date",info.dateStr);
            values.put("remark",info.remark);
            values.put("imgPath",info.imgPath);
            SQLiteDatabase db = getWritableDatabase();
            long newID = db.insert(_fecesTableName, null, values);
            result = (int)newID;
        }
        catch(Exception err)
        {
            result = -1;
            System.out.printf(err.getMessage());
        }
        return result;
    }

    protected boolean DelFecesInfo(int id){
        boolean result;
        try
        {
            SQLiteDatabase db = getWritableDatabase();
            int bakVal = db.delete(_fecesTableName,"id=?",new String[]{ Integer.toString(id) });
            result = (bakVal == 1) ? true : false;
        }
        catch(Exception err)
        {
            result = false;
            System.out.printf(err.getMessage());
        }
        return result;
    }

    protected boolean UpdateFecesInfo(FecesInfo info){
        boolean result;
        try
        {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("date",info.dateStr);
            values.put("remark",info.remark);
            values.put("imgPath",info.imgPath);
            int bakVal = db.update(_fecesTableName,values,"id=?",new String[]{ Integer.toString(info.flowId) });
            result = (bakVal == 1) ? true : false;
        }
        catch(Exception err)
        {
            result = false;
            System.out.printf(err.getMessage());
        }
        return result;
    }

    protected FecesInfo GetFecesInfo(int id){
        FecesInfo result = null;
        try
        {
            SQLiteDatabase db = getReadableDatabase();
            Cursor bakData = db.query(_fecesTableName,null,"id=?",new String[]{ Integer.toString(id) },"","","","");
            if(bakData != null)
            {
                FecesInfo[] toVals = CursorToFecesInfos(bakData);
                if(toVals != null && toVals.length == 1)
                {
                    result = toVals[0];
                }
            }
        }
        catch(Exception err)
        {
            result = null;
        }
        return result;
    }

    protected FecesInfo[] GetFecesInfos(){
        FecesInfo[] result = null;
        try
        {
            SQLiteDatabase db = getReadableDatabase();
            Cursor bakData = db.query(_fecesTableName,null,null,null,"","",null,null);
            if(bakData != null)
            {
                result = CursorToFecesInfos(bakData);
            }
        }
        catch(Exception err)
        {
            result = null;
            System.out.printf(err.getMessage());
        }
        return result;
    }

    protected FecesInfo[] GetFecesInfosByDate(Date low, Date high){
        FecesInfo[] result = null;
        try{
            SQLiteDatabase db = getReadableDatabase();
            if(low == null){
                low = DateHelper.Now();
            }
            if(high == null){
                high = new Date();
            }
            String selection = "date > datetime(?) and date < datetime(?)";
            String[] selectionArgs = new String[2];
            selectionArgs[0] = DateHelper.DateToString(low);
            selectionArgs[1] = DateHelper.DateToString(high);
            Cursor bakData = db.query(_fecesTableName,null,selection,selectionArgs,"","","date",null);
            if(bakData != null)
            {
                result = CursorToFecesInfos(bakData);
            }
        }catch(Exception err){
            result = null;
            System.out.printf(err.getMessage());
        }
        return result;
    }

    //从游标中读取用餐信息
    private FecesInfo[] CursorToFecesInfos(Cursor ptr)
    {
        FecesInfo[] result = null;
        try
        {
            if(ptr != null)
            {
                List<FecesInfo> list = new ArrayList<FecesInfo>();
                ptr.moveToFirst();
                while(!ptr.isAfterLast())
                {
                    FecesInfo info = new FecesInfo();
                    info.flowId = ptr.getInt(ptr.getColumnIndex("id"));
                    info.dateStr = ptr.getString(ptr.getColumnIndex("date"));
                    info.remark = ptr.getString(ptr.getColumnIndex("remark"));
                    info.imgPath = ptr.getString(ptr.getColumnIndex("imgPath"));
                    list.add(info);
                    ptr.moveToNext();
                }
                if(list.size() > 0)
                {
                    result = new FecesInfo[list.size()];
                    list.toArray(result);
                }
            }
        }
        catch(Exception err)
        {
            result = null;
            System.out.printf(err.getMessage());
        }
        return result;
    }
}
