package Parsers;

import Entities.Mission;
import Factories.MissionFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ParserJSON implements IParser {
    private final ObjectMapper objectMapper;
    private final MissionFactory missionFactory;

    public ParserJSON(MissionFactory missionFactory) {
        objectMapper = new ObjectMapper();
        this.missionFactory = missionFactory;
    }

    @Override
    public Mission parse(String file) throws Exception {
        try {
            Mission mission = missionFactory.createMission();
            objectMapper.readerForUpdating(mission).readValue(new File(file));
            mission.linkEntities();
            return mission;
        } catch (IOException exception) {
            throw new Exception("Не удалось прочитать JSON-руну!");
        }
    }
}
