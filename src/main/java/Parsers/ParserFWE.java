package Parsers;

import Builders.MissionBuilder;
import Entities.Mission;

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
            builder.curse(curseParts[1], curseParts[2]);

            while (i < data.size() && data.get(i).startsWith("SORCERER_ASSIGNED|")) {
                String[] sorcererParts = splitLine(data.get(i++));
                builder.addSorcerer(sorcererParts[1], sorcererParts[2]);
            }

            while (i < data.size() && data.get(i).startsWith("TECHNIQUE_USED|")) {
                String[] techniqueParts = splitLine(data.get(i++));
                builder.addTechnique(
                        techniqueParts[1],
                        techniqueParts[2],
                        techniqueParts[3],
                        Integer.parseInt(techniqueParts[4])
                );
            }

            while (i < data.size() && data.get(i).startsWith("TIMELINE_EVENT|")) {
                String[] timelineEventParts = splitLine(data.get(i++));
                builder.addOperationTimelineEvent(
                        timelineEventParts[1],
                        timelineEventParts[2],
                        timelineEventParts[3]
                );
            }

            List<String> attackPatterns = new ArrayList<>();
            String behaviorType = null;

            while (i < data.size() && data.get(i).startsWith("ENEMY_ACTION|")) {
                String[] enemyParts = splitLine(data.get(i++));

                if (behaviorType == null) {
                    behaviorType = enemyParts[1];
                }

                if (enemyParts[2].isBlank()) {
                    attackPatterns.add(enemyParts[1]);
                } else {
                    attackPatterns.add(enemyParts[1] + ": " + enemyParts[2]);
                }
            }

            builder.enemyActivity(behaviorType, null, null, null, attackPatterns, null);

            String[] impactParts = splitLine(data.get(i++));
            int evacuated = 0;
            int injured = 0;
            int missing = 0;

            for (int j = 1; j < impactParts.length; j++) {
                if (impactParts[j].startsWith("evacuated=")) {
                    evacuated = Integer.parseInt(cuttingOf(impactParts[j]));
                }

                if (impactParts[j].startsWith("injured=")) {
                    injured = Integer.parseInt(cuttingOf(impactParts[j]));
                }

                if (impactParts[j].startsWith("missing=")) {
                    missing = Integer.parseInt(cuttingOf(impactParts[j]));
                }
            }

            builder.civilianImpact(evacuated, injured, missing, null);

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
