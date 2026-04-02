package Parsers;

import Builders.MissionBuilder;
import Entities.Curse;
import Entities.EnvironmentConditions;
import Entities.Mission;
import Entities.Sorcerer;
import Entities.Technique;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ParserTXTINI implements IParser {
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

            if (data.get(i).equals("[MISSION]")) {
                i++;
                while (i < data.size() && !data.get(i).startsWith("[")) {
                    if (data.get(i).startsWith("missionId=")) {
                        builder.missionId(cuttingOf(data.get(i++)));
                        continue;
                    }

                    if (data.get(i).startsWith("date=")) {
                        builder.date(cuttingOf(data.get(i++)));
                        continue;
                    }

                    if (data.get(i).startsWith("location=")) {
                        builder.location(cuttingOf(data.get(i++)));
                        continue;
                    }

                    if (data.get(i).startsWith("outcome=")) {
                        builder.outcome(cuttingOf(data.get(i++)));
                        continue;
                    }

                    if (data.get(i).startsWith("damageCost=")) {
                        builder.damageCost(Integer.parseInt(cuttingOf(data.get(i++))));
                        continue;
                    }

                    i++;
                }
            }

            if (i < data.size() && data.get(i).equals("[CURSE]")) {
                i++;
                Curse curse = new Curse();

                while (i < data.size() && !data.get(i).startsWith("[")) {
                    if (data.get(i).startsWith("name=")) {
                        curse.setName(cuttingOf(data.get(i++)));
                        continue;
                    }

                    if (data.get(i).startsWith("threatLevel=")) {
                        curse.setThreatLevel(cuttingOf(data.get(i++)));
                        continue;
                    }

                    i++;
                }

                builder.curse(curse);
            }

            List<Sorcerer> sorcerers = new ArrayList<>();
            while (i < data.size() && data.get(i).equals("[SORCERER]")) {
                i++;
                Sorcerer sorcerer = new Sorcerer();

                while (i < data.size() && !data.get(i).startsWith("[")) {
                    if (data.get(i).startsWith("name=")) {
                        sorcerer.setName(cuttingOf(data.get(i++)));
                        continue;
                    }

                    if (data.get(i).startsWith("rank=")) {
                        sorcerer.setRank(cuttingOf(data.get(i++)));
                        continue;
                    }

                    i++;
                }

                sorcerers.add(sorcerer);
            }
            builder.sorcerers(sorcerers);

            List<Technique> techniques = new ArrayList<>();
            while (i < data.size() && data.get(i).equals("[TECHNIQUE]")) {
                i++;
                Technique technique = new Technique();

                while (i < data.size() && !data.get(i).startsWith("[")) {
                    if (data.get(i).startsWith("name=")) {
                        technique.setName(cuttingOf(data.get(i++)));
                        continue;
                    }

                    if (data.get(i).startsWith("type=")) {
                        technique.setType(cuttingOf(data.get(i++)));
                        continue;
                    }

                    if (data.get(i).startsWith("owner=")) {
                        technique.setOwner(cuttingOf(data.get(i++)));
                        continue;
                    }

                    if (data.get(i).startsWith("damage=")) {
                        technique.setDamage(Integer.parseInt(cuttingOf(data.get(i++))));
                        continue;
                    }

                    i++;
                }

                techniques.add(technique);
            }
            builder.techniques(techniques);

            if (i < data.size() && data.get(i).equals("[ENVIRONMENT]")) {
                i++;
                EnvironmentConditions environment = new EnvironmentConditions();

                while (i < data.size() && !data.get(i).startsWith("[")) {
                    if (data.get(i).startsWith("weather=")) {
                        environment.setWeather(cuttingOf(data.get(i++)));
                        continue;
                    }

                    if (data.get(i).startsWith("timeOfDay=")) {
                        environment.setTimeOfDay(cuttingOf(data.get(i++)));
                        continue;
                    }

                    if (data.get(i).startsWith("visibility=")) {
                        environment.setVisibility(cuttingOf(data.get(i++)));
                        continue;
                    }

                    if (data.get(i).startsWith("cursedEnergyDensity=")) {
                        environment.setCursedEnergyDensity(Integer.parseInt(cuttingOf(data.get(i++))));
                        continue;
                    }

                    i++;
                }

                builder.environmentConditions(environment);
            }
        } catch (Exception exception) {
            throw new Exception("Не удалось прочитать TXTINI-руну!");
        }

        return builder.build();
    }

    private String cuttingOf(String line) {
        int equalsIndex = line.indexOf('=');
        return line.substring(equalsIndex + 1);
    }
}
