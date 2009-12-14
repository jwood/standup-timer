package net.johnpwood.android.standuptimer.dao;

public class InvalidTeamNameException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidTeamNameException(String message) {
        super(message);
    }
}
