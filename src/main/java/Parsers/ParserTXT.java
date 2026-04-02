package Parsers;

import Builders.MissionBuilder;
import Entities.Mission;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ParserTXT implements IParser {
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

            if (!data.isEmpty() && data.getFirst().startsWith("[")) {
                return new ParserTXTINI().parse(file);
            }

            int i = 0;

            builder.missionId(cuttingOf(data.get(i++)));
            builder.date(cuttingOf(data.get(i++)));
            builder.location(cuttingOf(data.get(i++)));
            builder.outcome(cuttingOf(data.get(i++)));
            builder.damageCost(Integer.parseInt(cuttingOf(data.get(i++))));

            builder.curse(cuttingOf(data.get(i++)), cuttingOf(data.get(i++)));

            while (i < data.size() && data.get(i).startsWith("sorcerer[")) {
                builder.addSorcerer(cuttingOf(data.get(i++)), cuttingOf(data.get(i++)));
            }

            while (i < data.size() && data.get(i).startsWith("technique[")) {
                builder.addTechnique(
                        cuttingOf(data.get(i++)),
                        cuttingOf(data.get(i++)),
                        cuttingOf(data.get(i++)),
                        Integer.parseInt(cuttingOf(data.get(i++)))
                );
            }

            if (i < data.size() && data.get(i).startsWith("note:")) {
                builder.comment(cuttingOf(data.get(i)));
            }
        } catch (Exception exception) {
            throw new Exception("Не удалось прочитать TXT-руну!");
        }

        return builder.build();
    }

    private String cuttingOf(String line) {
        int colonIndex = line.indexOf(':');
        return line.substring(colonIndex + 2);
    }
}
