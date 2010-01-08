package net.johnpwood.android.standuptimer.test.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.johnpwood.android.standuptimer.model.MeetingStats;

public class MeetingStatsTest extends TestCase {

    public void test_average_stats() {
        List<MeetingStats> meetingStats = new ArrayList<MeetingStats>();
        meetingStats.add(new MeetingStats(5, 301, 343, 30, 65));
        meetingStats.add(new MeetingStats(8, 534, 550, 32, 120));
        meetingStats.add(new MeetingStats(2, 234, 300, 23, 122));
        meetingStats.add(new MeetingStats(3, 765, 765, 15, 78));
        meetingStats.add(new MeetingStats(9, 444, 445, 10, 93));

        MeetingStats averageStats = MeetingStats.getAverageStats(meetingStats);
        assertEquals(5.4f, averageStats.getNumParticipants());
        assertEquals(455.6f, averageStats.getIndividualStatusLength());
        assertEquals(480.6f, averageStats.getMeetingLength());
        assertEquals(22f, averageStats.getQuickestStatus());
        assertEquals(95.6f, averageStats.getLongestStatus());
    }
}
