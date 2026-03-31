package Parsers;

import Entities.Mission;
import Factories.MissionFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

public class ParserXML implements IParser {
    private final XmlMapper xmlMapper;
    private final MissionFactory missionFactory;

    public ParserXML(MissionFactory missionFactory) {
        xmlMapper = new XmlMapper();
        this.missionFactory = missionFactory;
    }

    @Override
    public Mission parse(String file) throws Exception {
        try {
            Mission mission = missionFactory.createMission();
            xmlMapper.readerForUpdating(mission).readValue(new File(file));
            mission.linkEntities();
            return mission;
        } catch (IOException exception) {
            throw new Exception("Не удалось прочитать XML-руну!");
        }
    }
}
