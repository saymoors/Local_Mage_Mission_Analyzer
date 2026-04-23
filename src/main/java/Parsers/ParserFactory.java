package Parsers;

import java.util.LinkedHashMap;
import java.util.Map;

public class ParserFactory {
    private final Map<String, IParser> parsers = new LinkedHashMap<>();

    public ParserFactory() {
        register("json", new ParserJSON());
        register("xml", new ParserXML());
        register("yaml", new ParserYAML());
        register("yml", new ParserYAML());
        register("txt", new ParserTXT());
        register("", new ParserFWE());
    }

    public void register(String extension, IParser parser) {
        parsers.put(extension.toLowerCase(), parser);
    }

    public Map<String, IParser> getParsers() {
        return parsers;
    }

    public IParser createParser(String extension) throws Exception {
        IParser parser = parsers.get(extension.toLowerCase());

        if(parser == null) {
            throw new Exception("Вы выбрали иную руну!");
        }

        return parser;
    }
}
