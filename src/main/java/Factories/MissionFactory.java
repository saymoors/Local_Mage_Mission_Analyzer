package Factories;

import Entities.Mission;

public abstract class MissionFactory {
    public Mission create() {
        return createMission();
    }

    protected abstract Mission createMission();
}
