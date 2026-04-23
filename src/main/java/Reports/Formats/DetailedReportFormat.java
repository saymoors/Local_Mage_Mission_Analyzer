package Reports.Formats;

import Entities.Mission;
import Reports.*;
import Reports.Decorators.*;
import Reports.Support.EmptyReportFormat;

public class DetailedReportFormat implements IReportFormat {
    private final IReportFormat report;

    public DetailedReportFormat() {
        report = new AftermathDecorator(
                new TimelineDecorator(
                        new ThreatContextDecorator(
                                new EconomicAssessmentDecorator(
                                        new TechniquesDecorator(
                                                new SorcerersDecorator(
                                                        new CurseDecorator(
                                                                new MissionBaseDecorator(new EmptyReportFormat())
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    @Override
    public String render(Mission mission) throws Exception {
        return report.render(mission);
    }
}
