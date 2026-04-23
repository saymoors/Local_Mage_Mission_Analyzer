package Reports.Formats;

import Entities.Mission;
import Reports.Support.EmptyReportFormat;
import Reports.IReportFormat;
import Reports.Decorators.MissionBaseDecorator;

public class SummaryReportFormat implements IReportFormat {
    private final IReportFormat report;

    public SummaryReportFormat() {
        report = new MissionBaseDecorator(new EmptyReportFormat());
    }

    @Override
    public String render(Mission mission) throws Exception {
        return report.render(mission);
    }
}
