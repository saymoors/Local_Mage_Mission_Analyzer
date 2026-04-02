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
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    data.add(line);
                }
            }
            reader.close();

            int i = 0;

            String[] missionParts = splitLine(data.get(i++));
            builder.missionId(missionParts[1]);
            builder.date(missionParts[2]);
            builder.location(missionParts[3]);

            String[] curseParts = splitLine(data.get(i++));
            Curse curse = new Curse();
            curse.setName(curseParts[1]);
            curse.setThreatLevel(curseParts[2]);
            builder.curse(curse);

            List<Sorcerer> sorcerers = new ArrayList<>();
            while (i < data.size() && data.get(i).startsWith("SORCERER_ASSIGNED|")) {
                String[] sorcererParts = splitLine(data.get(i++));
                Sorcerer sorcerer = new Sorcerer();
                sorcerer.setName(sorcererParts[1]);
                sorcerer.setRank(sorcererParts[2]);
                sorcerers.add(sorcerer);
            }
            builder.sorcerers(sorcerers);

            List<Technique> techniques = new ArrayList<>();
            while (i < data.size() && data.get(i).startsWith("TECHNIQUE_USED|")) {
                String[] techniqueParts = splitLine(data.get(i++));
                Technique technique = new Technique();
                technique.setName(techniqueParts[1]);
                technique.setType(techniqueParts[2]);
                technique.setOwner(techniqueParts[3]);
                technique.setDamage(Integer.parseInt(techniqueParts[4]));
                techniques.add(technique);
            }
            builder.techniques(techniques);

            List<OperationTimelineEvent> operationTimeline = new ArrayList<>();
            while (i < data.size() && data.get(i).startsWith("TIMELINE_EVENT|")) {
                String[] timelineEventParts = splitLine(data.get(i++));
                OperationTimelineEvent event = new OperationTimelineEvent();
                event.setTimestamp(timelineEventParts[1]);
                event.setType(timelineEventParts[2]);
                event.setDescription(timelineEventParts[3]);
                operationTimeline.add(event);
            }
            builder.operationTimeline(operationTimeline);

            EnemyActivity enemyActivity = new EnemyActivity();
            List<String> attackPatterns = new ArrayList<>();

            while (i < data.size() && data.get(i).startsWith("ENEMY_ACTION|")) {
                String[] enemyParts = splitLine(data.get(i++));

                if (enemyActivity.getBehaviorType() == null) {
                    enemyActivity.setBehaviorType(enemyParts[1]);
                }

                if (enemyParts[2].isBlank()) {
                    attackPatterns.add(enemyParts[1]);
                } else {
                    attackPatterns.add(enemyParts[1] + ": " + enemyParts[2]);
                }
            }

            enemyActivity.setAttackPatterns(attackPatterns);
            builder.enemyActivity(enemyActivity);

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
            }

            builder.civilianImpact(civilianImpact);


            String[] missionResult = splitLine(data.get(i));
            builder.outcome(missionResult[1]);
            builder.damageCost(Integer.parseInt(cuttingOf(missionResult[2])));
        } catch (Exception exception) {
            throw new Exception("Не удалось прочитать FWE-руну!");
        }

        return builder.build();
    }

    private String[] splitLine(String line) {
        return line.split("\\|", -1);
    }

    private String cuttingOf(String line) {
        int equalsIndex = line.indexOf('=');
        return line.substring(equalsIndex + 1);
    }
}
