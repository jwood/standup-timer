package net.johnpwood.android.standuptimer.model;

import java.util.Date;
import java.util.List;

import net.johnpwood.android.standuptimer.dao.DAOFactory;
import net.johnpwood.android.standuptimer.dao.MeetingDAO;
import net.johnpwood.android.standuptimer.utils.Logger;
import android.content.Context;

public class Meeting {
    private Long id = null;
    private Team team = null;
    private Date dateTime = null;
    private int numParticipants = 0;
    private int individualStatusLength = 0;
    private int meetingLength = 0;
    private int quickestStatus = 0;
    private int longestStatus = Integer.MAX_VALUE;

    private static DAOFactory daoFactory = DAOFactory.getInstance();

    public Meeting(Team team, Date dateTime, int numParticipants, int individualStatusLength,
            int meetingLength, int quickestStatus, int longestStatus) {

        if (team == null) {
            throw new IllegalArgumentException("Meeting team must not be null");
        } else {
            this.team = new Team(team.getName());
        }

        if (dateTime == null) {
            throw new IllegalArgumentException("Meeting date/time must not be null");
        } else {
            this.dateTime = dateTime;
        }

        if (numParticipants < 2) {
            throw new IllegalArgumentException("Meeting must have at least 2 participants");
        } else {
            this.numParticipants = numParticipants;
        }
        
        this.individualStatusLength = individualStatusLength;
        this.meetingLength = meetingLength;
        this.quickestStatus = quickestStatus;
        this.longestStatus = longestStatus;
    }

    public Meeting(Long id, Team team, Date dateTime, int numParticipants, int individualStatusLength,
            int meetingLength, int quickestStatus, int longestStatus) {
        this(team, dateTime, numParticipants, individualStatusLength, meetingLength, quickestStatus, longestStatus);
        this.id = id;
    }

    public Meeting(Long id, Meeting meeting) {
        this.id = id;
        this.team = new Team(meeting.getTeam().getName());
        this.dateTime = meeting.dateTime;
        this.numParticipants = meeting.numParticipants;
        this.individualStatusLength = meeting.individualStatusLength;
        this.meetingLength = meeting.meetingLength;
        this.quickestStatus = meeting.quickestStatus;
        this.longestStatus = meeting.longestStatus;
    }

    public void delete(Context context) {
        MeetingDAO dao = null;
        try {
            dao = daoFactory.getMeetingDAO(context);
            dao.delete(this);
        } finally {
            if (dao != null) {
                dao.close();
            }
        }
    }

    public Meeting save(Context context) {
        MeetingDAO dao = null;
        Meeting meeting = null;
        try {
            dao = daoFactory.getMeetingDAO(context);
            meeting = dao.save(this);
        } catch (Exception e) {
            Logger.e(e.getMessage());
        } finally {
            dao.close();
        }

        return meeting;
    }

    public static List<Meeting> findAllByTeam(Team team, Context context) {
        MeetingDAO dao = null;
        try {
            dao = daoFactory.getMeetingDAO(context);
            return dao.findAllByTeam(team);
        } finally {
            if (dao != null) {
                dao.close();
            }
        }
    }

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public int getNumParticipants() {
        return numParticipants;
    }

    public int getIndividualStatusLength() {
        return individualStatusLength;
    }

    public int getMeetingLength() {
        return meetingLength;
    }

    public int getQuickestStatus() {
        return quickestStatus;
    }

    public int getLongestStatus() {
        return longestStatus;
    }
}
