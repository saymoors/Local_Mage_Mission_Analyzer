package Parsers;

import Entities.Mission;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

public class ParserXML implements IParser {
    private final XmlMapper xmlMapper;

    public ParserXML() {
        xmlMapper = new XmlMapper();
    }

    @Override
    public Mission parse(String file) throws Exception {
        try {
            Mission mission = xmlMapper.readValue(new File(file), Mission.class);
            mission.linkEntities();
            return mission;
        } catch (IOException exception) {
            throw new Exception("Не удалось прочитать XML-руну!");
        }
    }
}
