package Parsers;

import Builders.MissionBuilder;
import Entities.Mission;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

public class ParserYAML implements IParser {
    private final YAMLMapper yamlMapper;

    public ParserYAML() {
        yamlMapper = new YAMLMapper();
    }

    @Override
    public Mission parse(String file) throws Exception {
        try {
            Mission readMission = yamlMapper.readValue(new File(file), Mission.class);
            return MissionBuilder.fromMission(readMission).build();
        } catch (IOException exception) {
            throw new Exception("Не удалось прочитать YAML-руну!");
        }
    }
}
