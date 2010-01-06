package net.johnpwood.android.standuptimer.dao;

import static android.provider.BaseColumns._ID;

import java.util.Date;

import net.johnpwood.android.standuptimer.model.Meeting;
import net.johnpwood.android.standuptimer.model.Team;
import net.johnpwood.android.standuptimer.utils.Logger;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MeetingDAO extends DAOHelper {
    private static final String TABLE_NAME = "meetings";
    private static final String TEAM_NAME = "team_name";
    private static final String MEETING_TIME = "meeting_time";
    private static final String NUM_PARTICIPANTS = "num_participants";
    private static final String INDIVIDUAL_STATUS_LENGTH = "individual_status_length";
    private static final String MEETING_LENGTH = "meeting_length";
    private static final String QUICKEST_STATUS = "quickest_status";
    private static final String LONGEST_STATUS = "longest_status";
    private static final String[] ALL_COLUMS = { _ID, TEAM_NAME, MEETING_TIME, NUM_PARTICIPANTS,
        INDIVIDUAL_STATUS_LENGTH, MEETING_LENGTH, QUICKEST_STATUS, LONGEST_STATUS};

    public MeetingDAO(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TEAM_NAME + " TEXT NOT NULL, " +                
                MEETING_TIME + " INTEGER NOT NULL, " +
                NUM_PARTICIPANTS + " INTEGER NOT NULL, " +
                INDIVIDUAL_STATUS_LENGTH + " INTEGER NOT NULL, " +
                MEETING_LENGTH + " INTEGER NOT NULL, " +
                QUICKEST_STATUS + " INTEGER NOT NULL, " +
                LONGEST_STATUS + " INTEGER NOT NULL" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Meeting save(Meeting meeting) {
        SQLiteDatabase db = getWritableDatabase();
        if (meeting.getId() != null) {
            return updateExistingMeeting(db, meeting);
        } else {
            return createNewMeeting(db, meeting);
        }
    }

    public Meeting findById(Long id) {
        Cursor cursor = null;
        Meeting meeting = null;

        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(TABLE_NAME, ALL_COLUMS, _ID + " = ?", new String[]{id.toString()}, null, null, null);
            if (cursor.getCount() == 1) {
                if (cursor.moveToFirst()) {
                    String teamName = cursor.getString(1);
                    Date meetingTime = new Date(cursor.getLong(2));
                    int numParticipants = cursor.getInt(3);
                    int individualStatusLength = cursor.getInt(4);
                    int meetingLength = cursor.getInt(5);
                    int quickestStatus = cursor.getInt(6);
                    int longestStatus = cursor.getInt(7);
                    meeting = new Meeting(id, new Team(teamName), meetingTime, numParticipants, 
                            individualStatusLength, meetingLength, quickestStatus, longestStatus);
                }
            }
        } finally {
            closeCursor(cursor);
        }

        return meeting;
    }

    public void deleteAll() {
        Logger.d("Deleting all meetings");
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    private Meeting createNewMeeting(SQLiteDatabase db, Meeting meeting) {
        Logger.d("Creating new meeting for " + meeting.getTeam().getName() + " with a date/time of '" + meeting.getDateTime() + "'");
        ContentValues values = createContentValues(meeting);
        long id = db.insertOrThrow(TABLE_NAME, null, values);
        return new Meeting(id, meeting);
    }

    private Meeting updateExistingMeeting(SQLiteDatabase db, Meeting meeting) {
        Logger.d("Updating meeting for " + meeting.getTeam().getName() + " with a date/time of '" + meeting.getDateTime() + "'");
        ContentValues values = createContentValues(meeting);
        long id = db.update(TABLE_NAME, values, _ID + " = ?", new String[]{meeting.getId().toString()});
        return new Meeting(id, meeting);
    }

    private ContentValues createContentValues(Meeting meeting) {
        ContentValues values = new ContentValues();
        values.put(TEAM_NAME, meeting.getTeam().getName());
        values.put(MEETING_TIME, meeting.getDateTime().getTime());
        values.put(NUM_PARTICIPANTS, meeting.getNumParticipants());
        values.put(INDIVIDUAL_STATUS_LENGTH, meeting.getIndividualStatusLength());
        values.put(MEETING_LENGTH, meeting.getMeetingLength());
        values.put(QUICKEST_STATUS, meeting.getQuickestStatus());
        values.put(LONGEST_STATUS, meeting.getLongestStatus());
        return values;
    }
}
