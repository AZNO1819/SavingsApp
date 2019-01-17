package se.kth.ht100x.sparapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * DBhelper is a database management class.
 * This is used for updating and manipulating the data in the application.
 */
public class DBhelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "Goal";
    private static final String COL1 = "ID";
    private static final String COL2 = "Name";
    private static final String COL3 = "Amount";
    private static final String COL4 = "Image";
    private static final String COL5 = "Date";
    private static final String COL6 = "SavedAmount";

    public DBhelper(Context context) {
        super(context, TABLE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(TABLE_NAME);
        sb.append("(");
        sb.append(COL1);
        sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(COL2);
        sb.append(" TEXT, ");
        sb.append(COL3);
        sb.append(" TEXT, ");
        sb.append(COL4);
        sb.append(" BLOB, ");
        sb.append(COL5);
        sb.append(" TEXT, ");
        sb.append(COL6);
        sb.append(" INTEGER");
        sb.append(")");

        String createTable = sb.toString();
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean addtoDB(String Name, String Amount, String Date, Bitmap imageURI){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,Name);
        contentValues.put(COL3,Amount);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        imageURI.compress(Bitmap.CompressFormat.JPEG, 10, out);
        byte[] buffer = out.toByteArray();
        contentValues.put(COL4, buffer);
        contentValues.put(COL5,Date);
        contentValues.put(COL6,0);

        long result =  db.insert(TABLE_NAME,null,contentValues);
        if(result < 0)
            return false;
        else
            return true;

    }

    /**
     * Retrieves data from the database.
     * @return The retrieved data.
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(SQL,null);
        return data;
    }

    /**
     * Retrieves data from the database for a specific goal.
     * @param name Name of the specific savings goal.
     * @return The retrieved data.
     */
    public Cursor getGoal(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "SELECT * FROM " + TABLE_NAME + " WHERE Name = " + "'"+ name + "'";
        Cursor data = db.rawQuery(SQL,null);
        return data;
    }

    /**
     * Updates the database with new amount saved.
     * @param saveGoal Name of the specific savings goal.
     * @param add Amount to add.
     * @param amountSaved Current amount saved.
     */
    public void updateDB(String saveGoal, int add, int amountSaved){
        SQLiteDatabase db = this.getWritableDatabase();
        int totalSaved = add + amountSaved;
        String SQL = "UPDATE " + TABLE_NAME + " SET SavedAmount =  " + "'" + totalSaved + "'" + " WHERE Name = " + "'"+ saveGoal + "'";
        db.execSQL(SQL);
    }
}