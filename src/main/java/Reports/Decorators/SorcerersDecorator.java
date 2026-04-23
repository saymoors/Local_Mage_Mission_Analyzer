package Reports.Decorators;

import Entities.Mission;
import Entities.Sorcerer;
import Reports.IReportFormat;
import Reports.Support.ReportTextSupport;

import java.util.List;

public class SorcerersDecorator extends ReportDecorator {
    public SorcerersDecorator(IReportFormat innerReport) {
        super(innerReport);
    }

    @Override
    protected void appendSection(StringBuilder report, Mission mission) {
        List<Sorcerer> sorcerers = mission.getSorcerers();
        if(!ReportTextSupport.hasItems(sorcerers)) {
            return;
        }

        ReportTextSupport.appendSectionTitle(report, "Участники миссии");

        for (int i = 0; i < sorcerers.size(); i++) {
            Sorcerer sorcerer = sorcerers.get(i);
            if(sorcerer == null) {
                continue;
            }

            report.append("  Колдун[").append(i).append(']').append(':').append(System.lineSeparator());
            ReportTextSupport.appendField(report, 2, "name", sorcerer.getName());
            ReportTextSupport.appendField(report, 2, "rank", sorcerer.getRank());
        }
    }
}
