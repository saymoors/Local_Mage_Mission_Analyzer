package Reports.Formats;

import Entities.Mission;
import Reports.Support.EmptyReportFormat;
import Reports.IReportFormat;
import Reports.Decorators.MissionBaseDecorator;
import Reports.Decorators.StatisticsDecorator;

public class StatisticsReportFormat implements IReportFormat {
    private final IReportFormat report;

    public StatisticsReportFormat() {
        report = new StatisticsDecorator(
                new MissionBaseDecorator(new EmptyReportFormat())
        );
    }

    @Override
    public String render(Mission mission) throws Exception {
        return report.render(mission);
    }
}
