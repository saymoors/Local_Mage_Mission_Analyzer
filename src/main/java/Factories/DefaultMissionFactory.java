package Factories;

import Entities.Mission;

public class DefaultMissionFactory extends MissionFactory {
    @Override
    protected Mission createMission() {
        return new Mission();
    }
}
