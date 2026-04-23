package Reports.Decorators;

import Entities.Mission;
import Entities.Technique;
import Reports.IReportFormat;
import Reports.Support.ReportTextSupport;

import java.util.List;

public class TechniquesDecorator extends ReportDecorator {
    public TechniquesDecorator(IReportFormat innerReport) {
        super(innerReport);
    }

    @Override
    protected void appendSection(StringBuilder report, Mission mission) {
        List<Technique> techniques = mission.getTechniques();
        if(!ReportTextSupport.hasItems(techniques)) {
            return;
        }

        ReportTextSupport.appendSectionTitle(report, "Техники");

        for (int i = 0; i < techniques.size(); i++) {
            Technique technique = techniques.get(i);
            if(technique == null) {
                continue;
            }

            report.append("  Техника[").append(i).append(']').append(':').append(System.lineSeparator());
            ReportTextSupport.appendField(report, 2, "name", technique.getName());
            ReportTextSupport.appendField(report, 2, "type", technique.getType());
            ReportTextSupport.appendField(report, 2, "owner", technique.getOwner());
            ReportTextSupport.appendField(report, 2, "damage", technique.getDamage());
        }
    }
}
