package org.rescue.command.center.emergencycallsystem.enums;

public enum EmergencyCallStateEnum {
    CREATED("CREATED"),
    DISPATCHED("DISPATCHED"),
    RUNNING("RUNNING"),
    COMPLETED("COMPLETED"),
    FINISHED("FINISHED");

    private final String state;

    EmergencyCallStateEnum(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return state;
    }
}