package Reports;

import Entities.Mission;

import javax.swing.*;

public class SummaryReportFormat implements IReportFormat {
    @Override
    public void render(Mission mission, JPanel panel, IReportActions actions) {
        panel.add(new JLabel("missionId:"));
        panel.add(new JLabel(mission.getMissionId()));

        panel.add(new JLabel("date:"));
        panel.add(new JLabel(mission.getDate()));

        panel.add(new JLabel("location:"));
        panel.add(new JLabel(mission.getLocation()));

        panel.add(new JLabel("outcome:"));
        panel.add(new JLabel(String.valueOf(mission.getOutcome())));
    }
}
