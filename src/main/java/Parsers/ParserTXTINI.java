package Parsers;

import Entities.Mission;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParserTXTINI extends BaseParser {
    @Override
    public Mission parse(String file) throws Exception {
        List<String> data;
        try {
            data = TextMissionParserSupport.readNonEmptyLines(file);
        } catch (IOException exception) {
            throw new Exception("Не удалось прочитать TXTINI-руну!");
        }

        Map<String, Object> fields = new LinkedHashMap<>();

        String currentSection = null;
        int sorcererIndex = -1;
        int techniqueIndex = -1;

        for (String line : data) {
            if(line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length() - 1).trim();

                switch (currentSection) {
                    case "SORCERER" -> sorcererIndex++;
                    case "TECHNIQUE" -> techniqueIndex++;
                    case "MISSION", "CURSE", "ENVIRONMENT" -> {
                    }
                    default -> throw new Exception("Неизвестная INI-секция: " + currentSection);
                }
                continue;
            }

            if(currentSection == null) {
                throw new Exception("Строка вне секции: " + line);
            }

            TextMissionParserSupport.KeyValue keyValue = TextMissionParserSupport.splitKeyValue(line, '=');
            String key = keyValue.key().trim();
            String value = keyValue.value();

            switch (currentSection) {
                case "MISSION" -> putMissionField(fields, key, value);
                case "CURSE" -> putCurseField(fields, key, value);
                case "SORCERER" -> putSorcererField(fields, sorcererIndex, key, value);
                case "TECHNIQUE" -> putTechniqueField(fields, techniqueIndex, key, value);
                case "ENVIRONMENT" -> putEnvironmentField(fields, key, value);
                default -> throw new Exception("Неизвестная INI-секция: " + currentSection);
            }
        }

        return buildMission(fields);
    }

    private void putMissionField(Map<String, Object> fields, String key, String value) throws Exception {
        switch (key) {
            case "missionId", "date", "location", "outcome", "damageCost" ->
                    TextMissionParserSupport.put(fields, key, value);
            default -> throw new Exception("Неизвестное поле секции MISSION: " + key);
        }
    }

    private void putCurseField(Map<String, Object> fields, String key, String value) throws Exception {
        switch (key) {
            case "name" -> TextMissionParserSupport.put(fields, "curse.name", value);
            case "threatLevel" -> TextMissionParserSupport.put(fields, "curse.threatLevel", value);
            default -> throw new Exception("Неизвестное поле секции CURSE: " + key);
        }
    }

    private void putSorcererField(Map<String, Object> fields, int index, String key, String value) throws Exception {
        switch (key) {
            case "name", "rank" -> TextMissionParserSupport.put(fields, "sorcerers[" + index + "]." + key, value);
            default -> throw new Exception("Неизвестное поле секции SORCERER: " + key);
        }
    }

    private void putTechniqueField(Map<String, Object> fields, int index, String key, String value) throws Exception {
        switch (key) {
            case "name", "type", "owner", "damage" ->
                    TextMissionParserSupport.put(fields, "techniques[" + index + "]." + key, value);
            default -> throw new Exception("Неизвестное поле секции TECHNIQUE: " + key);
        }
    }

    private void putEnvironmentField(Map<String, Object> fields, String key, String value) throws Exception {
        switch (key) {
            case "weather", "timeOfDay", "visibility", "cursedEnergyDensity" ->
                    TextMissionParserSupport.put(fields, "environmentConditions." + key, value);
            default -> throw new Exception("Неизвестное поле секции ENVIRONMENT: " + key);
        }
    }
}
