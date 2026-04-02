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
                String trimmedLine = line.trim();
                if (!trimmedLine.isEmpty() && !trimmedLine.startsWith(";") && !trimmedLine.startsWith("#")) {
                    data.add(trimmedLine);
                }
            }
            reader.close();

            int i = 0;

            while (i < data.size()) {
                switch (data.get(i)) {
                    case "[MISSION]" -> {
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

                        continue;
                    }
                    case "[CURSE]" -> {
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
                        continue;
                    }
                    case "[SORCERER]" -> {
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

                        builder.addSorcerer(sorcerer);
                        continue;
                    }
                    case "[TECHNIQUE]" -> {
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

                        builder.addTechnique(technique);
                        continue;
                    }
                    case "[ENVIRONMENT]" -> {
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
                        continue;
                    }
                }
            }
        } catch (Exception exception) {
            throw new Exception("Не удалось прочитать TXTINI-руну!");
        }

        Mission createdMission = builder.build();
        createdMission.linkEntities();
        return createdMission;
    }

    private String cuttingOf(String line) {
        int equalsIndex = line.indexOf('=');
        return line.substring(equalsIndex + 1);
    }
}
