package net.johnpwood.android.standuptimer.dao;

import static android.provider.BaseColumns._ID;
import net.johnpwood.android.standuptimer.model.Team;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TeamDAO extends SQLiteOpenHelper implements DatabaseConstants {
    private static final String TABLE_NAME = "teams";
    private static final String NAME = "name";
    private static final String[] ALL_COLUMS = { _ID, NAME };

    public TeamDAO(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT NOT NULL" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Team save(Team team) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        if (team.getId() != null) {
            values.put(_ID, team.getId().longValue());
        }

        values.put(NAME, team.getName());
        long id = db.insertOrThrow(TABLE_NAME, null, values);

        return new Team(id, team.getName());
    }

    public Team findById(Long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMS, _ID + " = " + id, null, null, null, null);
        if (cursor.getCount() == 1) {
            if (cursor.moveToFirst()) {
                String name = cursor.getString(1);
                return new Team(id, name);
            }
        }
        return null;
    }
}
