package Factories;

import Parsers.IParser;
import Parsers.ParserJSON;
import Parsers.ParserTXT;
import Parsers.ParserXML;

public class ParserFactory {
    private final MissionFactory missionFactory;

    public ParserFactory(MissionFactory missionFactory) {
        this.missionFactory = missionFactory;
    }

    public IParser createParser(String extension) throws Exception {
        return switch (extension.toLowerCase()) {
            case "json" -> new ParserJSON(missionFactory);
            case "xml" -> new ParserXML(missionFactory);
            case "txt" -> new ParserTXT(missionFactory);
            default -> throw new Exception("Вы выбрали иную руну!");
        };
    }
}