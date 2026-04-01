package Builders;

import Entities.Curse;
import Entities.Mission;
import Entities.Sorcerer;
import Entities.Technique;

import java.util.List;

public class MissionBuilder {
    private final Mission mission;

    public MissionBuilder() {
        this.mission = new Mission();
    }

    public static MissionBuilder fromMission(Mission source) {
        return new MissionBuilder()
                .missionId(source.getMissionId())
                .date(source.getDate())
                .location(source.getLocation())
                .outcome(source.getOutcome())
                .damageCost(source.getDamageCost())
                .curse(source.getCurse())
                .sorcerers(source.getSorcerers())
                .techniques(source.getTechniques())
                .comment(source.getComment());
    }

    public MissionBuilder missionId(String missionId) {
        mission.setMissionId(missionId);
        return this;
    }

    public MissionBuilder date(String date) {
        mission.setDate(date);
        return this;
    }

    public MissionBuilder location(String location) {
        mission.setLocation(location);
        return this;
    }

    public MissionBuilder outcome(String outcome) {
        mission.setOutcome(outcome);
        return this;
    }

    public MissionBuilder damageCost(int damageCost) {
        mission.setDamageCost(damageCost);
        return this;
    }

    public MissionBuilder curse(Curse curse) {
        mission.setCurse(curse);
        return this;
    }

    public MissionBuilder sorcerers(List<Sorcerer> sorcerers) {
        mission.setSorcerers(sorcerers);
        return this;
    }

    public MissionBuilder techniques(List<Technique> techniques) {
        mission.setTechniques(techniques);
        return this;
    }

    public MissionBuilder comment(String comment) {
        mission.setComment(comment);
        return this;
    }

    public Mission build() {
        return mission;
    }
}
