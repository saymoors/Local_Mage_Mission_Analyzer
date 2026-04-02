package Parsers;

import Builders.MissionBuilder;
import Entities.Mission;

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
                String curseName = null;
                String threatLevel = null;

                while (i < data.size() && !data.get(i).startsWith("[")) {
                    if (data.get(i).startsWith("name=")) {
                        curseName = cuttingOf(data.get(i++));
                        continue;
                    }

                    if (data.get(i).startsWith("threatLevel=")) {
                        threatLevel = cuttingOf(data.get(i++));
                        continue;
                    }

                    i++;
                }

                builder.curse(curseName, threatLevel);
            }

            while (i < data.size() && data.get(i).equals("[SORCERER]")) {
                i++;
                String name = null;
                String rank = null;

                while (i < data.size() && !data.get(i).startsWith("[")) {
                    if (data.get(i).startsWith("name=")) {
                        name = cuttingOf(data.get(i++));
                        continue;
                    }

                    if (data.get(i).startsWith("rank=")) {
                        rank = cuttingOf(data.get(i++));
                        continue;
                    }

                    i++;
                }

                builder.addSorcerer(name, rank);
            }

            while (i < data.size() && data.get(i).equals("[TECHNIQUE]")) {
                i++;
                String name = null;
                String type = null;
                String owner = null;
                int damage = 0;

                while (i < data.size() && !data.get(i).startsWith("[")) {
                    if (data.get(i).startsWith("name=")) {
                        name = cuttingOf(data.get(i++));
                        continue;
                    }

                    if (data.get(i).startsWith("type=")) {
                        type = cuttingOf(data.get(i++));
                        continue;
                    }

                    if (data.get(i).startsWith("owner=")) {
                        owner = cuttingOf(data.get(i++));
                        continue;
                    }

                    if (data.get(i).startsWith("damage=")) {
                        damage = Integer.parseInt(cuttingOf(data.get(i++)));
                        continue;
                    }

                    i++;
                }

                builder.addTechnique(name, type, owner, damage);
            }

            if (i < data.size() && data.get(i).equals("[ENVIRONMENT]")) {
                i++;
                String weather = null;
                String timeOfDay = null;
                String visibility = null;
                int cursedEnergyDensity = 0;

                while (i < data.size() && !data.get(i).startsWith("[")) {
                    if (data.get(i).startsWith("weather=")) {
                        weather = cuttingOf(data.get(i++));
                        continue;
                    }

                    if (data.get(i).startsWith("timeOfDay=")) {
                        timeOfDay = cuttingOf(data.get(i++));
                        continue;
                    }

                    if (data.get(i).startsWith("visibility=")) {
                        visibility = cuttingOf(data.get(i++));
                        continue;
                    }

                    if (data.get(i).startsWith("cursedEnergyDensity=")) {
                        cursedEnergyDensity = Integer.parseInt(cuttingOf(data.get(i++)));
                        continue;
                    }

                    i++;
                }

                builder.environmentConditions(weather, timeOfDay, visibility, cursedEnergyDensity);
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
