package Parsers;

import Entities.Mission;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParserTXT extends BaseParser {
    @Override
    public Mission parse(String file) throws Exception {
        List<String> data;
        try {
            data = TextMissionParserSupport.readNonEmptyLines(file);
        } catch (Exception exception) {
            throw new Exception("Не удалось прочитать TXT-руну: " + exception.getMessage(), exception);
        }

        if (!data.isEmpty() && data.getFirst().startsWith("[")) {
            return new ParserTXTINI().parse(file);
        }

        Map<String, Object> fields = new LinkedHashMap<>();

        for (String line : data) {
            TextMissionParserSupport.KeyValue keyValue = TextMissionParserSupport.splitKeyValue(line, ':');
            String rawKey = keyValue.key().trim();
            String value = keyValue.value();

            switch (rawKey) {
                case "missionId", "date", "location", "outcome", "damageCost", "curse.name", "curse.threatLevel" -> {
                    TextMissionParserSupport.put(fields, rawKey, value);
                    continue;
                }
                case "note" -> {
                    TextMissionParserSupport.put(fields, "comment", value);
                    continue;
                }
                default -> {
                }
            }

            TextMissionParserSupport.IndexedField sorcererField = TextMissionParserSupport.parseSorcererKey(rawKey);
            if (sorcererField != null) {
                TextMissionParserSupport.put(
                        fields,
                        "sorcerers[" + sorcererField.index() + "]." + sorcererField.field(),
                        value
                );
                continue;
            }

            TextMissionParserSupport.IndexedField techniqueField = TextMissionParserSupport.parseTechniqueKey(rawKey);
            if (techniqueField != null) {
                TextMissionParserSupport.put(
                        fields,
                        "techniques[" + techniqueField.index() + "]." + techniqueField.field(),
                        value
                );
                continue;
            }

            throw new Exception("Неизвестное TXT-поле: " + rawKey);
        }

        return buildMission(fields);
    }
}
