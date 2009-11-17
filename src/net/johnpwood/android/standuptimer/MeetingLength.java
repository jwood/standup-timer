package net.johnpwood.android.standuptimer;

public enum MeetingLength {
    FIVE_MINUTES(5),
    TEN_MINUTES(10),
    FIFTEEN_MINUTES(15),
    TWENTY_MINUTES(20);
    
    private int minutes;
    
    private MeetingLength(int minutes) {
        this.minutes = minutes;
    }
    
    public int getLength() {
        return minutes;        
    }
    
    public static MeetingLength findByPosition(int position) {
        switch (position) {
        case 0:
            return FIVE_MINUTES;
        case 1:
            return TEN_MINUTES;
        case 2:
            return FIFTEEN_MINUTES;
        case 3:
            return TWENTY_MINUTES;
        default:
            return null;
        }            
    }
}
