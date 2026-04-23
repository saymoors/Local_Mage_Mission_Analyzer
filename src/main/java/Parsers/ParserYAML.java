package Parsers;

import Entities.Mission;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

public class ParserYAML extends BaseParser {
    private final YAMLMapper yamlMapper;

    public ParserYAML() {
        yamlMapper = new YAMLMapper();
        yamlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    @Override
    public Mission parse(String file) throws Exception {
        try {
            Mission readMission = yamlMapper.readValue(new File(file), Mission.class);
            return rebuildMission(readMission);
        } catch(UnrecognizedPropertyException exception) {
            throw new Exception("Неизвестное поле YAML: " + exception.getPropertyName());
        } catch(IOException exception) {
            throw new Exception("Не удалось прочитать YAML-руну!");
        }
    }
}
