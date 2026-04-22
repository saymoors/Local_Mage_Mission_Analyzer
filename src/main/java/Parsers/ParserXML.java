package Parsers;

import Entities.Mission;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

public class ParserXML extends BaseParser {
    private final XmlMapper xmlMapper;

    public ParserXML() {
        xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    @Override
    public Mission parse(String file) throws Exception {
        try {
            Mission readMission = xmlMapper.readValue(new File(file), Mission.class);
            return rebuildMission(readMission);
        } catch (UnrecognizedPropertyException exception) {
            throw new Exception("Неизвестное поле XML: " + exception.getPropertyName());
        } catch (IOException exception) {
            throw new Exception("Не удалось прочитать XML-руну!");
        }
    }
}
