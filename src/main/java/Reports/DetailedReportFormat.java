package Reports;

import Entities.Mission;

import javax.swing.*;

public class DetailedReportFormat implements IReportFormat {
    @Override
    public void render(Mission mission, JPanel panel, IReportActions actions) {
        panel.add(new JLabel("missionId:"));
        panel.add(new JLabel(mission.getMissionId()));

        panel.add(new JLabel("date:"));
        panel.add(new JLabel(mission.getDate()));

        panel.add(new JLabel("location:"));
        panel.add(new JLabel(mission.getLocation()));

        panel.add(new JLabel("outcome:"));
        panel.add(new JLabel(mission.getOutcome()));

        panel.add(new JLabel("damageCost:"));
        panel.add(new JLabel(String.valueOf(mission.getDamageCost())));

        JButton curserButton = new JButton("Показать");
        curserButton.addActionListener(_ -> actions.showCurseDialog(mission.getCurse()));
        panel.add(new JLabel("curse:"));
        panel.add(curserButton);

        JButton sorcerersButton = new JButton("Показать");
        sorcerersButton.addActionListener(_ -> actions.showSorcerersDialog(mission.getSorcerers()));
        panel.add(new JLabel("sorcerers:"));
        panel.add(sorcerersButton);

        JButton techniquesButton = new JButton("Показать");
        techniquesButton.addActionListener(_ -> actions.showTechniquesDialog(mission.getTechniques()));
        panel.add(new JLabel("techniques:"));
        panel.add(techniquesButton);

        if (mission.getComment() != null) {
            panel.add(new JLabel("comment:"));
            panel.add(new JLabel(mission.getComment()));
        }
    }
}
