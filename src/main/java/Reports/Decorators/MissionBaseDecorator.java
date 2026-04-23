package Reports.Decorators;

import Entities.Mission;
import Reports.IReportFormat;
import Reports.Support.ReportTextSupport;

public class MissionBaseDecorator extends ReportDecorator {
    public MissionBaseDecorator(IReportFormat innerReport) {
        super(innerReport);
    }

    @Override
    protected void appendSection(StringBuilder report, Mission mission) {
        ReportTextSupport.appendSectionTitle(report, "Основная информация");
        ReportTextSupport.appendField(report, 1, "missionId", mission.getMissionId());
        ReportTextSupport.appendField(report, 1, "date", mission.getDate());
        ReportTextSupport.appendField(report, 1, "location", mission.getLocation());
        ReportTextSupport.appendField(report, 1, "outcome", mission.getOutcome());
        ReportTextSupport.appendField(report, 1, "damageCost", mission.getDamageCost());
    }
}
