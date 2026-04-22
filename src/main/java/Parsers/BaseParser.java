package Parsers;

import Builders.MissionBuilder;
import Builders.MissionDirector;
import Entities.Mission;

import java.util.Map;

public abstract class BaseParser implements IParser {
    private final MissionDirector missionDirector = new MissionDirector();

    protected Mission buildMission(Map<String, Object> missionData) throws Exception {
        return missionDirector.constructMission(new MissionBuilder(), missionData);
    }

    protected Mission rebuildMission(Mission mission) throws Exception {
        return missionDirector.constructMission(new MissionBuilder(), mission);
    }
}
