package Parsers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextMissionParserSupport {
    private static final Pattern SORCERER_KEY = Pattern.compile("^sorcerer\\[(\\d+)]\\.(name|rank)$");
    private static final Pattern TECHNIQUE_KEY = Pattern.compile("^technique\\[(\\d+)]\\.(name|type|owner|damage)$");

    private TextMissionParserSupport() {
    }

    public static List<String> readNonEmptyLines(String file) throws IOException {
        List<String> lines = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
        )) {
            String line;
            while((line = reader.readLine()) != null) {
                if(!line.isBlank()) {
                    lines.add(line.trim());
                }
            }
        }

        return lines;
    }

    public static KeyValue splitKeyValue(String line, char delimiter) throws Exception {
        int delimiterIndex = line.indexOf(delimiter);

        if(delimiterIndex < 0) {
            throw new Exception("Неподдерживаемый формат строки: " + line);
        }

        String key = line.substring(0, delimiterIndex).trim();
        String value = line.substring(delimiterIndex + 1).trim();
        return new KeyValue(key, value);
    }

    public static IndexedField parseSorcererKey(String rawKey) {
        return parseIndexedField(rawKey, SORCERER_KEY);
    }

    public static IndexedField parseTechniqueKey(String rawKey) {
        return parseIndexedField(rawKey, TECHNIQUE_KEY);
    }

    public static void put(Map<String, Object> fields, String key, Object value) {
        if(key == null || value == null) {
            return;
        }

        if(value instanceof String text && text.isBlank()) {
            return;
        }

        fields.put(key, value);
    }

    private static IndexedField parseIndexedField(String rawKey, Pattern pattern) {
        Matcher matcher = pattern.matcher(rawKey.trim());
        if(!matcher.matches()) {
            return null;
        }

        return new IndexedField(Integer.parseInt(matcher.group(1)), matcher.group(2));
    }

    public record KeyValue(String key, String value) {
    }

    public static final class IndexedField {
        private final int index;
        private final String field;

        private IndexedField(int index, String field) {
            this.index = index;
            this.field = field;
        }

        public int index() {
            return index;
        }

        public String field() {
            return field;
        }
    }
}
