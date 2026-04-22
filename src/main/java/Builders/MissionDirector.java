package Builders;

import Entities.Curse;
import Entities.Mission;
import Entities.Sorcerer;
import Entities.Technique;
import Entities.Enums.EscalationRisk;
import Entities.Enums.Mobility;
import Entities.Enums.Outcome;
import Entities.Enums.PublicExposureRisk;
import Entities.Enums.SorcererRank;
import Entities.Enums.TechniqueType;
import Entities.Enums.ThreatLevel;
import Entities.Enums.Visibility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class MissionDirector {
    public Mission constructMission(IMissionBuilder builder, Mission source) throws Exception {
        builder.reset()
                .missionId(source.getMissionId())
                .date(source.getDate())
                .location(source.getLocation())
                .outcome(source.getOutcome())
                .damageCost(source.getDamageCost())
                .economicAssessment(source.getEconomicAssessment())
                .enemyActivity(source.getEnemyActivity())
                .environmentConditions(source.getEnvironmentConditions())
                .civilianImpact(source.getCivilianImpact())
                .operationTimeline(source.getOperationTimeline())
                .operationTags(source.getOperationTags())
                .supportUnits(source.getSupportUnits())
                .recommendations(source.getRecommendations())
                .notes(source.getNotes())
                .artifactsRecovered(source.getArtifactsRecovered())
                .evacuationZones(source.getEvacuationZones())
                .statusEffects(source.getStatusEffects())
                .comment(source.getComment());

        Curse curse = source.getCurse();
        if (curse != null) {
            builder.curse(curse.getName(), curse.getThreatLevel());
        }

        if (source.getSorcerers() != null) {
            for (Sorcerer sorcerer : source.getSorcerers()) {
                builder.addSorcerer(sorcerer.getName(), sorcerer.getRank());
            }
        }

        if (source.getTechniques() != null) {
            for (Technique technique : source.getTechniques()) {
                builder.addTechnique(
                        technique.getName(),
                        technique.getType(),
                        technique.getOwner(),
                        technique.getDamage()
                );
            }
        }

        return builder.build();
    }

    public Mission constructMission(IMissionBuilder builder, Map<String, Object> missionData) throws Exception {
        builder.reset();

        String missionId = null;
        String date = null;
        String location = null;
        Outcome outcome = null;
        Integer damageCost = null;
        String notes = null;
        String comment = null;

        CurseDraft curseDraft = new CurseDraft();
        EconomicAssessmentDraft economicAssessmentDraft = new EconomicAssessmentDraft();
        EnemyActivityDraft enemyActivityDraft = new EnemyActivityDraft();
        EnvironmentConditionsDraft environmentConditionsDraft = new EnvironmentConditionsDraft();
        CivilianImpactDraft civilianImpactDraft = new CivilianImpactDraft();

        Map<Integer, SorcererDraft> sorcerers = new TreeMap<>();
        Map<Integer, TechniqueDraft> techniques = new TreeMap<>();
        Map<Integer, TimelineDraft> timeline = new TreeMap<>();
        Map<Integer, String> operationTags = new TreeMap<>();
        Map<Integer, String> supportUnits = new TreeMap<>();
        Map<Integer, String> recommendations = new TreeMap<>();
        Map<Integer, String> artifactsRecovered = new TreeMap<>();
        Map<Integer, String> evacuationZones = new TreeMap<>();
        Map<Integer, String> statusEffects = new TreeMap<>();
        Map<Integer, String> attackPatterns = new TreeMap<>();
        Map<Integer, String> countermeasuresUsed = new TreeMap<>();

        for (Map.Entry<String, Object> entry : missionData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key == null || key.isBlank()) {
                continue;
            }

            switch (key) {
                case "missionId" -> missionId = asString(value);
                case "date" -> date = asString(value);
                case "location" -> location = asString(value);
                case "outcome" -> outcome = parseEnum(Outcome.class, value, key);
                case "damageCost" -> damageCost = asInt(value, key);
                case "notes" -> notes = asString(value);
                case "comment" -> comment = asString(value);
                default -> {
                    if (key.startsWith("curse.")) {
                        applyCurseField(key, value, curseDraft);
                        continue;
                    }

                    if (key.startsWith("economicAssessment.")) {
                        applyEconomicAssessmentField(key, value, economicAssessmentDraft);
                        continue;
                    }

                    if (key.startsWith("enemyActivity.attackPatterns[")) {
                        applyStringListField(key, value, "enemyActivity.attackPatterns", attackPatterns);
                        continue;
                    }

                    if (key.startsWith("enemyActivity.countermeasuresUsed[")) {
                        applyStringListField(key, value, "enemyActivity.countermeasuresUsed", countermeasuresUsed);
                        continue;
                    }

                    if (key.startsWith("enemyActivity.")) {
                        applyEnemyActivityField(key, value, enemyActivityDraft);
                        continue;
                    }

                    if (key.startsWith("environmentConditions.")) {
                        applyEnvironmentConditionsField(key, value, environmentConditionsDraft);
                        continue;
                    }

                    if (key.startsWith("civilianImpact.")) {
                        applyCivilianImpactField(key, value, civilianImpactDraft);
                        continue;
                    }

                    if (key.startsWith("sorcerers[")) {
                        applySorcererField(key, value, sorcerers);
                        continue;
                    }

                    if (key.startsWith("techniques[")) {
                        applyTechniqueField(key, value, techniques);
                        continue;
                    }

                    if (key.startsWith("operationTimeline[")) {
                        applyTimelineField(key, value, timeline);
                        continue;
                    }

                    if (key.startsWith("operationTags[")) {
                        applyStringListField(key, value, "operationTags", operationTags);
                        continue;
                    }

                    if (key.startsWith("supportUnits[")) {
                        applyStringListField(key, value, "supportUnits", supportUnits);
                        continue;
                    }

                    if (key.startsWith("recommendations[")) {
                        applyStringListField(key, value, "recommendations", recommendations);
                        continue;
                    }

                    if (key.startsWith("artifactsRecovered[")) {
                        applyStringListField(key, value, "artifactsRecovered", artifactsRecovered);
                        continue;
                    }

                    if (key.startsWith("evacuationZones[")) {
                        applyStringListField(key, value, "evacuationZones", evacuationZones);
                        continue;
                    }

                    if (key.startsWith("statusEffects[")) {
                        applyStringListField(key, value, "statusEffects", statusEffects);
                        continue;
                    }

                    throw new Exception("Неизвестное поле миссии: " + key);
                }
            }
        }

        builder.missionId(missionId)
                .date(date)
                .location(location)
                .outcome(outcome)
                .damageCost(damageCost == null ? 0 : damageCost)
                .notes(notes)
                .comment(comment);

        if (curseDraft.hasData()) {
            builder.curse(curseDraft.name, curseDraft.threatLevel);
        }

        if (economicAssessmentDraft.hasData()) {
            builder.economicAssessment(
                    economicAssessmentDraft.totalDamageCost,
                    economicAssessmentDraft.infrastructureDamage,
                    economicAssessmentDraft.transportDamage,
                    economicAssessmentDraft.commercialDamage,
                    economicAssessmentDraft.recoveryEstimateDays,
                    economicAssessmentDraft.insuranceCovered
            );
        }

        if (enemyActivityDraft.hasData()) {
            builder.enemyActivity(
                    enemyActivityDraft.behaviorType,
                    enemyActivityDraft.targetPriority,
                    enemyActivityDraft.mobility,
                    enemyActivityDraft.escalationRisk,
                    valuesInOrder(attackPatterns),
                    valuesInOrder(countermeasuresUsed)
            );
        }

        if (environmentConditionsDraft.hasData()) {
            builder.environmentConditions(
                    environmentConditionsDraft.weather,
                    environmentConditionsDraft.timeOfDay,
                    environmentConditionsDraft.visibility,
                    environmentConditionsDraft.cursedEnergyDensity
            );
        }

        if (civilianImpactDraft.hasData()) {
            builder.civilianImpact(
                    civilianImpactDraft.evacuated,
                    civilianImpactDraft.injured,
                    civilianImpactDraft.missing,
                    civilianImpactDraft.publicExposureRisk
            );
        }

        for (SorcererDraft sorcerer : sorcerers.values()) {
            builder.addSorcerer(sorcerer.name, sorcerer.rank);
        }

        for (TechniqueDraft technique : techniques.values()) {
            builder.addTechnique(technique.name, technique.type, technique.owner, technique.damage);
        }

        for (TimelineDraft event : timeline.values()) {
            builder.addOperationTimelineEvent(event.timestamp, event.type, event.description);
        }

        addStrings(valuesInOrder(operationTags), builder::addOperationTag);
        addStrings(valuesInOrder(supportUnits), builder::addSupportUnit);
        addStrings(valuesInOrder(recommendations), builder::addRecommendation);
        addStrings(valuesInOrder(artifactsRecovered), builder::addArtifactRecovered);
        addStrings(valuesInOrder(evacuationZones), builder::addEvacuationZone);
        addStrings(valuesInOrder(statusEffects), builder::addStatusEffect);

        return builder.build();
    }

    private void addStrings(List<String> values, Consumer<String> appender) {
        for (String value : values) {
            appender.accept(value);
        }
    }

    private void applyCurseField(String key, Object value, CurseDraft draft) throws Exception {
        String field = key.substring("curse.".length());
        switch (field) {
            case "name" -> draft.name = asString(value);
            case "threatLevel" -> draft.threatLevel = parseEnum(ThreatLevel.class, value, key);
            default -> throw new Exception("Неизвестное поле проклятия: " + key);
        }
    }

    private void applyEconomicAssessmentField(String key, Object value, EconomicAssessmentDraft draft) throws Exception {
        draft.hasData = true;
        String field = key.substring("economicAssessment.".length());

        switch (field) {
            case "totalDamageCost" -> draft.totalDamageCost = asInt(value, key);
            case "infrastructureDamage" -> draft.infrastructureDamage = asInt(value, key);
            case "transportDamage" -> draft.transportDamage = asInt(value, key);
            case "commercialDamage" -> draft.commercialDamage = asInt(value, key);
            case "recoveryEstimateDays" -> draft.recoveryEstimateDays = asInt(value, key);
            case "insuranceCovered" -> draft.insuranceCovered = asBoolean(value, key);
            default -> throw new Exception("Неизвестное поле экономической оценки: " + key);
        }
    }

    private void applyEnemyActivityField(String key, Object value, EnemyActivityDraft draft) throws Exception {
        draft.hasData = true;
        String field = key.substring("enemyActivity.".length());

        switch (field) {
            case "behaviorType" -> draft.behaviorType = asString(value);
            case "targetPriority" -> draft.targetPriority = asString(value);
            case "mobility" -> draft.mobility = parseEnum(Mobility.class, value, key);
            case "escalationRisk" -> draft.escalationRisk = parseEnum(EscalationRisk.class, value, key);
            default -> throw new Exception("Неизвестное поле активности противника: " + key);
        }
    }

    private void applyEnvironmentConditionsField(String key, Object value, EnvironmentConditionsDraft draft) throws Exception {
        draft.hasData = true;
        String field = key.substring("environmentConditions.".length());

        switch (field) {
            case "weather" -> draft.weather = asString(value);
            case "timeOfDay" -> draft.timeOfDay = asString(value);
            case "visibility" -> draft.visibility = parseEnum(Visibility.class, value, key);
            case "cursedEnergyDensity" -> draft.cursedEnergyDensity = asInt(value, key);
            default -> throw new Exception("Неизвестное поле условий среды: " + key);
        }
    }

    private void applyCivilianImpactField(String key, Object value, CivilianImpactDraft draft) throws Exception {
        draft.hasData = true;
        String field = key.substring("civilianImpact.".length());

        switch (field) {
            case "evacuated" -> draft.evacuated = asInt(value, key);
            case "injured" -> draft.injured = asInt(value, key);
            case "missing" -> draft.missing = asInt(value, key);
            case "publicExposureRisk" -> draft.publicExposureRisk = parseEnum(PublicExposureRisk.class, value, key);
            default -> throw new Exception("Неизвестное поле влияния на гражданских: " + key);
        }
    }

    private void applySorcererField(String key, Object value, Map<Integer, SorcererDraft> sorcerers) throws Exception {
        int index = parseIndex(key, "sorcerers");
        String field = parseField(key, "sorcerers");
        SorcererDraft draft = sorcerers.computeIfAbsent(index, _ -> new SorcererDraft());

        switch (field) {
            case "name" -> draft.name = asString(value);
            case "rank" -> draft.rank = parseEnum(SorcererRank.class, value, key);
            default -> throw new Exception("Неизвестное поле колдуна: " + key);
        }
    }

    private void applyTechniqueField(String key, Object value, Map<Integer, TechniqueDraft> techniques) throws Exception {
        int index = parseIndex(key, "techniques");
        String field = parseField(key, "techniques");
        TechniqueDraft draft = techniques.computeIfAbsent(index, _ -> new TechniqueDraft());

        switch (field) {
            case "name" -> draft.name = asString(value);
            case "type" -> draft.type = parseEnum(TechniqueType.class, value, key);
            case "owner" -> draft.owner = asString(value);
            case "damage" -> draft.damage = asInt(value, key);
            default -> throw new Exception("Неизвестное поле техники: " + key);
        }
    }

    private void applyTimelineField(String key, Object value, Map<Integer, TimelineDraft> timeline) throws Exception {
        int index = parseIndex(key, "operationTimeline");
        String field = parseField(key, "operationTimeline");
        TimelineDraft draft = timeline.computeIfAbsent(index, _ -> new TimelineDraft());

        switch (field) {
            case "timestamp" -> draft.timestamp = asString(value);
            case "type" -> draft.type = asString(value);
            case "description" -> draft.description = asString(value);
            default -> throw new Exception("Неизвестное поле хронологии операции: " + key);
        }
    }

    private void applyStringListField(String key, Object value, String prefix, Map<Integer, String> storage) throws Exception {
        int index = parseIndex(key, prefix);
        storage.put(index, asString(value));
    }

    private int parseIndex(String key, String prefix) throws Exception {
        int prefixStart = prefix.length();
        int openBracket = key.indexOf('[', prefixStart - prefix.length());
        int closeBracket = key.indexOf(']', openBracket);

        if (openBracket < 0 || closeBracket < 0) {
            throw new Exception("Некорректное индексированное поле: " + key);
        }

        try {
            return Integer.parseInt(key.substring(openBracket + 1, closeBracket));
        } catch (NumberFormatException exception) {
            throw new Exception("Некорректный индекс в поле: " + key);
        }
    }

    private String parseField(String key, String prefix) throws Exception {
        int closeBracket = key.indexOf(']', prefix.length());
        int dot = key.indexOf('.', closeBracket);

        if (closeBracket < 0 || dot < 0 || dot + 1 >= key.length()) {
            throw new Exception("Некорректное индексированное поле: " + key);
        }

        return key.substring(dot + 1);
    }

    private String asString(Object value) {
        if (value == null) {
            return null;
        }

        String text = value instanceof Enum<?> enumValue
                ? enumValue.name()
                : String.valueOf(value);

        return text.isBlank() ? null : text;
    }

    private int asInt(Object value, String field) throws Exception {
        if (value == null) {
            return 0;
        }

        if (value instanceof Number number) {
            return number.intValue();
        }

        try {
            return Integer.parseInt(asString(value));
        } catch (NumberFormatException exception) {
            throw new Exception("Поле " + field + " должно быть целым!");
        }
    }

    private boolean asBoolean(Object value, String field) throws Exception {
        if (value == null) {
            return false;
        }

        if (value instanceof Boolean booleanValue) {
            return booleanValue;
        }

        String text = asString(value);
        if ("true".equalsIgnoreCase(text) || "false".equalsIgnoreCase(text)) {
            return Boolean.parseBoolean(text);
        }

        throw new Exception("Поле " + field + " должно быть булевым!");
    }

    private <T extends Enum<T>> T parseEnum(Class<T> enumType, Object value, String field) throws Exception {
        if (value == null) {
            return null;
        }

        if (enumType.isInstance(value)) {
            return enumType.cast(value);
        }

        try {
            return Enum.valueOf(enumType, asString(value));
        } catch (IllegalArgumentException exception) {
            throw new Exception("Неизвестное значение " + field + ": " + value);
        }
    }

    private List<String> valuesInOrder(Map<Integer, String> storage) {
        List<String> result = new ArrayList<>();

        for (String value : storage.values()) {
            if (value != null && !value.isBlank()) {
                result.add(value);
            }
        }

        return result;
    }

    private static class CurseDraft {
        private String name;
        private ThreatLevel threatLevel;

        private boolean hasData() {
            return name != null || threatLevel != null;
        }
    }

    private static class SorcererDraft {
        private String name;
        private SorcererRank rank;
    }

    private static class TechniqueDraft {
        private String name;
        private TechniqueType type;
        private String owner;
        private int damage;
    }

    private static class TimelineDraft {
        private String timestamp;
        private String type;
        private String description;
    }

    private static class EconomicAssessmentDraft {
        private boolean hasData;
        private int totalDamageCost;
        private int infrastructureDamage;
        private int transportDamage;
        private int commercialDamage;
        private int recoveryEstimateDays;
        private boolean insuranceCovered;

        private boolean hasData() {
            return hasData;
        }
    }

    private static class EnemyActivityDraft {
        private boolean hasData;
        private String behaviorType;
        private String targetPriority;
        private Mobility mobility;
        private EscalationRisk escalationRisk;

        private boolean hasData() {
            return hasData;
        }
    }

    private static class EnvironmentConditionsDraft {
        private boolean hasData;
        private String weather;
        private String timeOfDay;
        private Visibility visibility;
        private int cursedEnergyDensity;

        private boolean hasData() {
            return hasData;
        }
    }

    private static class CivilianImpactDraft {
        private boolean hasData;
        private int evacuated;
        private int injured;
        private int missing;
        private PublicExposureRisk publicExposureRisk;

        private boolean hasData() {
            return hasData;
        }
    }
}
