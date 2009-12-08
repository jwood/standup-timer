package net.johnpwood.android.standuptimer.model;

public class Team {
    private Long id = null;
    private String name = null;

    public Team(String name) {
        this.name = name;
    }

    public Team(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
