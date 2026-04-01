package Factories;

import Parsers.IParser;
import Parsers.ParserJSON;
import Parsers.ParserTXT;
import Parsers.ParserXML;

public class ParserFactory {
    public IParser createParser(String extension) throws Exception {
        return switch (extension.toLowerCase()) {
            case "json" -> new ParserJSON();
            case "xml" -> new ParserXML();
            case "txt" -> new ParserTXT();
            default -> throw new Exception("Вы выбрали иную руну!");
        };
    }
}
