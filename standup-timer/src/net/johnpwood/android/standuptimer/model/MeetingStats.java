package net.johnpwood.android.standuptimer.model;

public class MeetingStats {
    private int numParticipants = 0;
    private int individualStatusLength = 0;
    private int meetingLength = 0;
    private int quickestStatus = 0;
    private int longestStatus = Integer.MAX_VALUE;

    public MeetingStats(int numParticipants, int individualStatusLength, int meetingLength, int quickestStatus, int longestStatus) {
        this.numParticipants = numParticipants;
        this.individualStatusLength = individualStatusLength;
        this.meetingLength = meetingLength;
        this.quickestStatus = quickestStatus;
        this.longestStatus = longestStatus;
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
