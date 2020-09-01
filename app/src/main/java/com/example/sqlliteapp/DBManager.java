package com.example.sqlliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

public class DBManager {
    private SQLiteDatabase sqlDB;
    static final String DBname="Keep_Notes";
    static final String TableName="Notes";
    static final String ColDateTime="DateTime";
    static final String ColTitle="Title";
    static final String ColDescription="Description";
    static final String ColRemTime="Time";
    static final String ColRemDate="Date";
    static final String ColID="ID";
    static final int DBVersion=1;
    static final String CreateTable="Create table IF NOT EXISTS "+TableName+ "(ID integer primary key autoincrement,"+ColDateTime+
            " text,"+ColTitle+" text,"+ColDescription+" text,"+ColRemTime+" text,"+ColRemDate+" text);";

    static class DatabaseHelperUser extends SQLiteOpenHelper{

        Context context;

        DatabaseHelperUser(Context context){
            super(context,DBname,null,DBVersion);
            this.context=context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Toast.makeText(context, "Keep Notes", Toast.LENGTH_LONG).show();
            db.execSQL(CreateTable);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop table IF EXISTS "+TableName);
            onCreate(db);
        }
    }

    DBManager(Context context){
        DatabaseHelperUser db=new DatabaseHelperUser(context);
        sqlDB=db.getWritableDatabase();
    }

    public long Insert(ContentValues values){
        long ID=sqlDB.insert(TableName,"",values);
        return ID;
    }

    // In sql data in the form of table is handled using Cursor
    // Projection no. means no. of columns, Selection is selecting a specific column
    public Cursor query(String[] Projection, String Selection, String[] SelectionArgs, String SortOrder){
        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();
        qb.setTables(TableName);

        Cursor cursor=qb.query(sqlDB,Projection,Selection,SelectionArgs,null,null,SortOrder);
        return cursor;
    }

    public int Delete(String Selection, String[] SelectionArgs){
        int count=sqlDB.delete(TableName,Selection,SelectionArgs);
        return count;
    }
    public int Update(ContentValues values, String Selection, String[] SelectionArgs){
        int count=sqlDB.update(TableName,values,Selection,SelectionArgs);
        return count;
    }

    public long RowCount(){
        long count= DatabaseUtils.queryNumEntries(sqlDB,TableName);
        return count;
    }
}
