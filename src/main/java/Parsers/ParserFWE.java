package Parsers;

import Entities.Mission;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParserFWE extends BaseParser {
    @Override
    public Mission parse(String file) throws Exception {
        List<String> data;
        try {
            data = TextMissionParserSupport.readNonEmptyLines(file);
        } catch (IOException exception) {
            throw new Exception("Не удалось прочитать FWE-руну!");
        }

        Map<String, Object> fields = new LinkedHashMap<>();

        int sorcererIndex = 0;
        int techniqueIndex = 0;
        int timelineIndex = 0;
        int attackPatternIndex = 0;

        for (String line : data) {
            String[] parts = line.split("\\|", -1);
            String recordType = parts[0].trim();

            switch (recordType) {
                case "MISSION_CREATED" -> {
                    putPart(fields, "missionId", parts, 1);
                    putPart(fields, "date", parts, 2);
                    putPart(fields, "location", parts, 3);
                }
                case "CURSE_DETECTED" -> {
                    putPart(fields, "curse.name", parts, 1);
                    putPart(fields, "curse.threatLevel", parts, 2);
                }
                case "SORCERER_ASSIGNED" -> {
                    putPart(fields, "sorcerers[" + sorcererIndex + "].name", parts, 1);
                    putPart(fields, "sorcerers[" + sorcererIndex + "].rank", parts, 2);
                    sorcererIndex++;
                }
                case "TECHNIQUE_USED" -> {
                    putPart(fields, "techniques[" + techniqueIndex + "].name", parts, 1);
                    putPart(fields, "techniques[" + techniqueIndex + "].type", parts, 2);
                    putPart(fields, "techniques[" + techniqueIndex + "].owner", parts, 3);
                    putPart(fields, "techniques[" + techniqueIndex + "].damage", parts, 4);
                    techniqueIndex++;
                }
                case "TIMELINE_EVENT" -> {
                    putPart(fields, "operationTimeline[" + timelineIndex + "].timestamp", parts, 1);
                    putPart(fields, "operationTimeline[" + timelineIndex + "].type", parts, 2);
                    putPart(fields, "operationTimeline[" + timelineIndex + "].description", parts, 3);
                    timelineIndex++;
                }
                case "ENEMY_ACTION" -> {
                    putPartIfAbsent(fields, "enemyActivity.behaviorType", parts, 1);

                    if(parts.length > 2) {
                        String attackPattern = parts[2].isBlank() ? parts[1] : parts[1] + ": " + parts[2];
                        TextMissionParserSupport.put(fields, "enemyActivity.attackPatterns[" + attackPatternIndex++ + "]", attackPattern);
                    }
                }
                case "CIVILIAN_IMPACT" -> applyKeyValuePairs(fields, parts, 1, "civilianImpact.");
                case "MISSION_RESULT" -> {
                    putPart(fields, "outcome", parts, 1);
                    applyKeyValuePairs(fields, parts, 2, "");
                }
                default -> throw new Exception("Неизвестный FWE-блок: " + recordType);
            }
        }

        return buildMission(fields);
    }

    private void putPart(Map<String, Object> fields, String key, String[] parts, int index) {
        if(index < parts.length) {
            TextMissionParserSupport.put(fields, key, parts[index]);
        }
    }

    private void putPartIfAbsent(Map<String, Object> fields, String key, String[] parts, int index) {
        if(!fields.containsKey(key)) {
            putPart(fields, key, parts, index);
        }
    }

    private void applyKeyValuePairs(Map<String, Object> fields, String[] parts, int startIndex, String prefix) throws Exception {
        for (int i = startIndex; i < parts.length; i++) {
            if(!parts[i].contains("=")) {
                continue;
            }

            int equalsIndex = parts[i].indexOf('=');
            String key = parts[i].substring(0, equalsIndex).trim();
            String value = parts[i].substring(equalsIndex + 1);

            switch (key) {
                case "damageCost", "evacuated", "injured", "missing" -> TextMissionParserSupport.put(fields, prefix + key, value);
                default -> throw new Exception("Неизвестное FWE-блок: " + key);
            }
        }
    }
}
