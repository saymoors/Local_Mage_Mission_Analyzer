package Factories;

import Entities.Mission;

public class MissionFactory {
    public Mission createMission() {
        return new Mission();
    }
}
