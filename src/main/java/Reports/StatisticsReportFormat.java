package Reports;

import Entities.Mission;
import Entities.Technique;

import javax.swing.*;

public class StatisticsReportFormat implements IReportFormat {
    @Override
    public void render(Mission mission, JPanel panel, IReportActions actions) {
        panel.add(new JLabel("missionId:"));
        panel.add(new JLabel(mission.getMissionId()));

        int sorcerersCount = mission.getSorcerers().size();
        panel.add(new JLabel("sorcerers.count:"));
        panel.add(new JLabel(String.valueOf(sorcerersCount)));

        int techniquesCount = mission.getTechniques().size();
        panel.add(new JLabel("techniques.count:"));
        panel.add(new JLabel(String.valueOf(techniquesCount)));

        int totalDamage = 0;
        for (Technique technique : mission.getTechniques()) {
            totalDamage += technique.getDamage();
        }

        panel.add(new JLabel("techniques.totalDamage:"));
        panel.add(new JLabel(String.valueOf(totalDamage)));

        if (techniquesCount != 0) {
            panel.add(new JLabel("techniques.avgDamage:"));
            panel.add(new JLabel(String.valueOf(totalDamage / techniquesCount)));
        }
    }
}
