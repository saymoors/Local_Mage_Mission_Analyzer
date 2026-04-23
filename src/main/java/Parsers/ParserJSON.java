package Parsers;

import Entities.Mission;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import java.io.File;
import java.io.IOException;

public class ParserJSON extends BaseParser {
    private final ObjectMapper objectMapper;

    public ParserJSON() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    @Override
    public Mission parse(String file) throws Exception {
        try {
            Mission readMission = objectMapper.readValue(new File(file), Mission.class);
            return rebuildMission(readMission);
        } catch(UnrecognizedPropertyException exception) {
            throw new Exception("Неизвестное поле JSON: " + exception.getPropertyName());
        } catch(IOException exception) {
            throw new Exception("Не удалось прочитать JSON-руну!");
        }
    }
}
