package Reports;

import Entities.Mission;
import Entities.Sorcerer;
import Entities.Technique;

import javax.swing.*;
import java.util.List;

public class StatisticsReportFormat implements IReportFormat {
    @Override
    public void render(Mission mission, JPanel panel, IReportActions actions) {
        panel.add(new JLabel("missionId:"));
        panel.add(new JLabel(mission.getMissionId()));

        List<Sorcerer> sorcerers = mission.getSorcerers();
        int sorcerersCount = sorcerers.size();
        panel.add(new JLabel("sorcerers.count:"));
        panel.add(new JLabel(String.valueOf(sorcerersCount)));

        List<Technique> techniques = mission.getTechniques();
        int techniquesCount = techniques.size();
        panel.add(new JLabel("techniques.count:"));
        panel.add(new JLabel(String.valueOf(techniquesCount)));

        int totalDamage = 0;
        for (Technique technique : techniques) {
            totalDamage += technique.getDamage();
        }

        panel.add(new JLabel("techniques.totalDamage:"));
        panel.add(new JLabel(String.valueOf(totalDamage)));

        if(techniquesCount != 0) {
            String average = String.valueOf(totalDamage / techniquesCount);
            panel.add(new JLabel("techniques.avgDamage:"));
            panel.add(new JLabel(average));
        }
    }
}
