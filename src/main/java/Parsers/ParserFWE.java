package Parsers;

import Builders.MissionBuilder;
import Entities.CivilianImpact;
import Entities.Curse;
import Entities.EnemyActivity;
import Entities.Mission;
import Entities.OperationTimelineEvent;
import Entities.Sorcerer;
import Entities.Technique;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ParserFWE implements IParser {
    @Override
    public Mission parse(String file) throws Exception {
        MissionBuilder builder = new MissionBuilder();
        List<String> data = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
            );
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    data.add(line);
                }
            }
            reader.close();

            int i = 0;

            String[] missionCreated = splitLine(data.get(i++));
            builder.missionId(missionCreated[1]);
            builder.date(missionCreated[2]);
            builder.location(missionCreated[3]);

            String[] curseDetected = splitLine(data.get(i++));
            Curse curse = new Curse();
            curse.setName(curseDetected[1]);
            curse.setThreatLevel(curseDetected[2]);
            builder.curse(curse);

            List<Sorcerer> sorcerers = new ArrayList<>();
            while (i < data.size() && data.get(i).startsWith("SORCERER_ASSIGNED|")) {
                String[] sorcererAssigned = splitLine(data.get(i++));
                Sorcerer sorcerer = new Sorcerer();
                sorcerer.setName(sorcererAssigned[1]);
                sorcerer.setRank(sorcererAssigned[2]);
                sorcerers.add(sorcerer);
            }
            builder.sorcerers(sorcerers);

            List<Technique> techniques = new ArrayList<>();
            while (i < data.size() && data.get(i).startsWith("TECHNIQUE_USED|")) {
                String[] techniqueUsed = splitLine(data.get(i++));
                Technique technique = new Technique();
                technique.setName(techniqueUsed[1]);
                technique.setType(techniqueUsed[2]);
                technique.setOwner(techniqueUsed[3]);
                technique.setDamage(Integer.parseInt(techniqueUsed[4]));
                techniques.add(technique);
            }
            builder.techniques(techniques);

            List<OperationTimelineEvent> operationTimeline = new ArrayList<>();
            while (i < data.size() && data.get(i).startsWith("TIMELINE_EVENT|")) {
                String[] timelineEvent = splitLine(data.get(i++));
                OperationTimelineEvent event = new OperationTimelineEvent();
                event.setTimestamp(timelineEvent[1]);
                event.setType(timelineEvent[2]);
                event.setDescription(timelineEvent[3]);
                operationTimeline.add(event);
            }
            builder.operationTimeline(operationTimeline);

            if (i < data.size() && data.get(i).startsWith("ENEMY_ACTION|")) {
                EnemyActivity enemyActivity = new EnemyActivity();
                List<String> attackPatterns = new ArrayList<>();

                while (i < data.size() && data.get(i).startsWith("ENEMY_ACTION|")) {
                    String[] enemyAction = splitLine(data.get(i++));

                    if (enemyActivity.getBehaviorType() == null) {
                        enemyActivity.setBehaviorType(enemyAction[1]);
                    }

                    if (enemyAction[2].isBlank()) {
                        attackPatterns.add(enemyAction[1]);
                    } else {
                        attackPatterns.add(enemyAction[1] + ": " + enemyAction[2]);
                    }
                }

                enemyActivity.setAttackPatterns(attackPatterns);
                builder.enemyActivity(enemyActivity);
            }

            if (i < data.size() && data.get(i).startsWith("CIVILIAN_IMPACT|")) {
                String[] impactParts = splitLine(data.get(i++));
                CivilianImpact civilianImpact = new CivilianImpact();

                for (int j = 1; j < impactParts.length; j++) {
                    if (impactParts[j].startsWith("evacuated=")) {
                        civilianImpact.setEvacuated(Integer.parseInt(cuttingOf(impactParts[j])));
                    }

                    if (impactParts[j].startsWith("injured=")) {
                        civilianImpact.setInjured(Integer.parseInt(cuttingOf(impactParts[j])));
                    }

                    if (impactParts[j].startsWith("missing=")) {
                        civilianImpact.setMissing(Integer.parseInt(cuttingOf(impactParts[j])));
                    }

                    if (impactParts[j].startsWith("publicExposureRisk=")) {
                        civilianImpact.setPublicExposureRisk(cuttingOf(impactParts[j]));
                    }
                }

                builder.civilianImpact(civilianImpact);
            }

            if (i < data.size() && data.get(i).startsWith("MISSION_RESULT|")) {
                String[] missionResult = splitLine(data.get(i++));
                builder.outcome(missionResult[1]);

                for (int j = 2; j < missionResult.length; j++) {
                    if (missionResult[j].startsWith("damageCost=")) {
                        builder.damageCost(Integer.parseInt(cuttingOf(missionResult[j])));
                    }
                }
            }

            while (i < data.size()) {
                String[] parts = splitLine(data.get(i++));

                switch (parts[0]) {
                    case "NOTE" -> builder.notes(parts[1]);
                    case "TAG" -> builder.addOperationTag(parts[1]);
                    case "SUPPORT_UNIT" -> builder.addSupportUnit(parts[1]);
                    case "RECOMMENDATION" -> builder.addRecommendation(parts[1]);
                    case "ARTIFACT_RECOVERED" -> builder.addArtifactRecovered(parts[1]);
                    case "EVACUATION_ZONE" -> builder.addEvacuationZone(parts[1]);
                    case "STATUS_EFFECT" -> builder.addStatusEffect(parts[1]);
                    default -> {
                    }
                }
            }
        } catch (Exception exception) {
            throw new Exception("Не удалось прочитать FWE-руну!");
        }

        Mission createdMission = builder.build();
        createdMission.linkEntities();
        return createdMission;
    }

    private String[] splitLine(String line) {
        return line.split("\\|", -1);
    }

    private String cuttingOf(String line) {
        int equalsIndex = line.indexOf('=');
        return line.substring(equalsIndex + 1);
    }
}
