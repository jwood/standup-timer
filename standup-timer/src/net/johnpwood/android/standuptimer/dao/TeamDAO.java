package net.johnpwood.android.standuptimer.dao;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;
import java.util.List;

import net.johnpwood.android.standuptimer.model.Team;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TeamDAO extends DAOHelper {
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
        Cursor cursor = null;
        Team team = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(TABLE_NAME, ALL_COLUMS, _ID + " = " + id, null, null, null, null);
            if (cursor.getCount() == 1) {
                if (cursor.moveToFirst()) {
                    String name = cursor.getString(1);
                    team = new Team(id, name);
                }
            }
        } finally {
            closeCursor(cursor);
        }

        return team;
    }

    public List<Team> findAll() {
        List<Team> teamList = new ArrayList<Team>();
        Cursor cursor = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(TABLE_NAME, ALL_COLUMS, null, null, null, null, NAME);
            while (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String name = cursor.getString(1);
                teamList.add(new Team(id, name));
            }
        } finally {
            closeCursor(cursor);
        }

        return teamList;
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}
