package Reports;

import Entities.Mission;

import javax.swing.*;

public class RiskReportFormat implements IReportFormat {
    @Override
    public void render(Mission mission, JPanel panel, IReportActions actions) {
        panel.add(new JLabel("missionId:"));
        panel.add(new JLabel(mission.getMissionId()));

        panel.add(new JLabel("outcome:"));
        panel.add(new JLabel(String.valueOf(mission.getOutcome())));

        panel.add(new JLabel("damageCost:"));
        panel.add(new JLabel(String.valueOf(mission.getDamageCost())));

        JButton curseButton = new JButton("Показать");
        curseButton.addActionListener(_ -> actions.showCurseDialog(mission.getCurse()));
        panel.add(new JLabel("curse:"));
        panel.add(curseButton);
    }
}
