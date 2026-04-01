package Reports;

import Entities.Curse;
import Entities.Mission;

import javax.swing.*;

public class RiskReportFormat implements IReportFormat {
    @Override
    public void render(Mission mission, JPanel panel, IReportActions actions) {
        panel.add(new JLabel("missionId:"));
        panel.add(new JLabel(mission.getMissionId()));

        panel.add(new JLabel("outcome:"));
        panel.add(new JLabel(mission.getOutcome()));

        panel.add(new JLabel("damageCost:"));
        panel.add(new JLabel(String.valueOf(mission.getDamageCost())));

        Curse curse = mission.getCurse();
        if (curse != null) {
            panel.add(new JLabel("curse.name:"));
            panel.add(new JLabel(curse.getName()));

            panel.add(new JLabel("curse.threatLevel:"));
            panel.add(new JLabel(curse.getThreatLevel()));
        }
    }
}
