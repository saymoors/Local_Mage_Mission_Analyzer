package Reports.Decorators;

import Entities.Mission;
import Reports.IReportFormat;

public abstract class ReportDecorator implements IReportFormat {
    private final IReportFormat innerReport;

    protected ReportDecorator(IReportFormat innerReport) {
        this.innerReport = innerReport;
    }

    @Override
    public final String render(Mission mission) throws Exception {
        StringBuilder report = new StringBuilder(innerReport.render(mission));
        appendSection(report, mission);
        return report.toString();
    }

    protected abstract void appendSection(StringBuilder report, Mission mission) throws Exception;
}
