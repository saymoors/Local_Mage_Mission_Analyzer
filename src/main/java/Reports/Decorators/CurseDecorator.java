package Reports.Decorators;

import Entities.Curse;
import Entities.Mission;
import Reports.IReportFormat;
import Reports.Support.ReportTextSupport;

public class CurseDecorator extends ReportDecorator {
    public CurseDecorator(IReportFormat innerReport) {
        super(innerReport);
    }

    @Override
    protected void appendSection(StringBuilder report, Mission mission) {
        Curse curse = mission.getCurse();
        if(curse == null) {
            return;
        }

        ReportTextSupport.appendSectionTitle(report, "Проклятие");
        ReportTextSupport.appendField(report, 1, "name", curse.getName());
        ReportTextSupport.appendField(report, 1, "threatLevel", curse.getThreatLevel());
    }
}
