package Parsers;

import Builders.MissionBuilder;
import Entities.Mission;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ParserJSON implements IParser {
    private final ObjectMapper objectMapper;

    public ParserJSON() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public Mission parse(String file) throws Exception {
        try {
            Mission readMission = objectMapper.readValue(new File(file), Mission.class);
            Mission createdMission = MissionBuilder.fromMission(readMission).build();
            createdMission.linkEntities();
            return createdMission;
        } catch (IOException exception) {
            throw new Exception("Не удалось прочитать JSON-руну!");
        }
    }
}
