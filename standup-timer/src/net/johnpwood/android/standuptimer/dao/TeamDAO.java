package net.johnpwood.android.standuptimer.dao;

import static android.provider.BaseColumns._ID;
import net.johnpwood.android.standuptimer.model.Team;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TeamDAO extends SQLiteOpenHelper implements DatabaseConstants {
    private static final String TABLE_NAME = "teams";
    private static final String NAME = "name";

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
        team.setId(id);

        return team;
    }
}
