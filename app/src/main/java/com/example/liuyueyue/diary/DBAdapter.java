package com.example.liuyueyue.diary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by liuyueyue on 2017/6/9.
 */
public class DBAdapter
{
    public static final String DB_ACTION="db_action";//LogCat

    private static final String DB_NAME="people.db";//数据库名
    private static final String DB_TABLE="peopleinfo";//数据库表名
    private static final int    DB_VERSION=1;//数据库版本号

    public static final String KEY_ID = "_id";  //表属性ID
    public static final String KEY_NAME = "name";//表属性name
    public static final String KEY_AGE  = "age";//表属性age
    public static final String KEY_HEIGHT = "height";//表属性height

    private SQLiteDatabase db ;
    private Context xContext ;
    private DBOpenHelper dbOpenHelper ;
    public DBAdapter(Context context)
    {
        xContext = context ;
    }
//空间不够存储的时候设为只读
    public void open() throws SQLiteException
    {
        dbOpenHelper = new DBOpenHelper(xContext, DB_NAME, null,DB_VERSION);
        try
        {
            db = dbOpenHelper.getWritableDatabase();
        }
        catch (SQLiteException e)
        {
            db = dbOpenHelper.getReadableDatabase();
        }
    }

    public void close()
    {
        if(db != null)
        {
            db.close();
            db = null;
        }
    }

      //向表中添加一条数据
    public long insert(People people)
    {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAME, people.Diary);
        newValues.put(KEY_AGE, people.Time);
        return db.insert(DB_TABLE, null, newValues);
    }

    //删除一条数据
    public long deleteOneData(long id)
    {
        return db.delete(DB_TABLE, KEY_ID+"="+id, null );
    }
    //删除所有数据
    public long deleteAllData()
    {
        return db.delete(DB_TABLE, null, null);
    }
     //根据id查询数据的代码
    public People[] queryOneData(long id)
    {
        Cursor result = db.query(DB_TABLE, new String[] {KEY_ID,KEY_NAME,KEY_AGE,KEY_HEIGHT},
                KEY_ID+"="+id, null, null, null, null);
        return ConvertToPeople(result) ;
    }
    // 查询全部数据的代码
    public People[] queryAllData()
    {
        Cursor result = db.query(DB_TABLE, new String[] {KEY_ID,KEY_NAME,KEY_AGE,KEY_HEIGHT},
                null, null, null, null, null);
        return ConvertToPeople(result);
    }
   // 更新数据
    public long updateOneData(long id ,People people)
    {
        ContentValues newValues = new ContentValues();

        newValues.put(KEY_NAME, people.Diary);
        newValues.put(KEY_AGE, people.Time);

        return db.update(DB_TABLE, newValues, KEY_ID+"="+id, null);
    }

    private People[] ConvertToPeople(Cursor cursor)
    {
        int resultCounts = cursor.getCount();
        if(resultCounts == 0 || !cursor.moveToFirst())
        {
            return null ;
        }
        People[] peoples = new People[resultCounts];
        Log.i(DB_ACTION, "PeoPle len:"+peoples.length);
        for (int i = 0; i < resultCounts; i++)
        {
            peoples[i] = new People();
            peoples[i].ID = cursor.getInt(0);
            peoples[i].Diary = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            peoples[i].Time  = cursor.getString(cursor.getColumnIndex(KEY_AGE));
            Log.i(DB_ACTION, "people "+i+"info :"+peoples[i].toString());
            cursor.moveToNext();
        }
        return peoples;
    }


   //静态Helper类，用于建立、更新和打开数据库
    private static class DBOpenHelper extends SQLiteOpenHelper
    {
        private static final String DB_CREATE=
                "CREATE TABLE "+DB_TABLE
                        +" ("+KEY_ID+" integer primary key autoincrement, "
                        +KEY_NAME+" text not null, "
                        +KEY_AGE+" integer,"+
                        KEY_HEIGHT+" float);";
        public DBOpenHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version)
        {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DB_CREATE);
            Log.i(DB_ACTION, "onCreate");
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion)
        {
            _db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE);
            onCreate(_db);
            Log.i(DB_ACTION, "Upgrade");
        }

    }
}