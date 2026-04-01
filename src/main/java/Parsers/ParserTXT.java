package Parsers;

import Builders.MissionBuilder;
import Entities.Curse;
import Entities.Mission;
import Entities.Sorcerer;
import Entities.Technique;

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

            int i = 0;

            builder.missionId(cuttingOf(data.get(i++)));
            builder.date(cuttingOf(data.get(i++)));
            builder.location(cuttingOf(data.get(i++)));
            builder.outcome(cuttingOf(data.get(i++)));
            builder.damageCost(Integer.parseInt(cuttingOf(data.get(i++))));

            Curse curse = new Curse();
            curse.setName(cuttingOf(data.get(i++)));
            curse.setThreatLevel(cuttingOf(data.get(i++)));
            builder.curse(curse);

            List<Sorcerer> sorcerers = new ArrayList<>();
            while (i < data.size() && data.get(i).startsWith("sorcerer[")) {
                Sorcerer sorcerer = new Sorcerer();
                sorcerer.setName(cuttingOf(data.get(i++)));
                sorcerer.setRank(cuttingOf(data.get(i++)));
                sorcerers.add(sorcerer);
            }
            builder.sorcerers(sorcerers);

            List<Technique> techniques = new ArrayList<>();
            while (i < data.size() && data.get(i).startsWith("technique[")) {
                Technique technique = new Technique();
                technique.setName(cuttingOf(data.get(i++)));
                technique.setType(cuttingOf(data.get(i++)));
                technique.setOwner(cuttingOf(data.get(i++)));
                technique.setDamage(Integer.parseInt(cuttingOf(data.get(i++))));
                techniques.add(technique);
            }
            builder.techniques(techniques);

            if (i < data.size() && data.get(i).startsWith("note:")) {
                builder.comment(cuttingOf(data.get(i)));
            }
        } catch (Exception exception) {
            throw new Exception("Не удалось прочитать TXT-руну!");
        }

        Mission createdMission = builder.build();
        createdMission.linkEntities();
        return createdMission;
    }

    private String cuttingOf(String line) {
        int colonIndex = line.indexOf(':');
        return line.substring(colonIndex + 2);
    }
}
