package net.johnpwood.android.standuptimer.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public abstract class DAOHelper extends SQLiteOpenHelper implements DatabaseConstants {

    public DAOHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
}
