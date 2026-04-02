package Factories;

import Parsers.IParser;
import Parsers.ParserFWE;
import Parsers.ParserJSON;
import Parsers.ParserTXT;
import Parsers.ParserXML;
import Parsers.ParserYAML;

public class ParserFactory {
    public IParser createParser(String extension) throws Exception {
        return switch (extension.toLowerCase()) {
            case "json" -> new ParserJSON();
            case "xml" -> new ParserXML();
            case "yaml" -> new ParserYAML();
            case "txt" -> new ParserTXT();
            case "" -> new ParserFWE();
            default -> throw new Exception("Вы выбрали иную руну!");
        };
    }
}
