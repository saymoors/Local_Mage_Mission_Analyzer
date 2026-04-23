package Reports.Formats;

import Entities.Mission;
import Reports.*;
import Reports.Decorators.CurseDecorator;
import Reports.Decorators.EconomicAssessmentDecorator;
import Reports.Decorators.MissionBaseDecorator;
import Reports.Decorators.ThreatContextDecorator;
import Reports.Support.EmptyReportFormat;

public class RiskReportFormat implements IReportFormat {
    private final IReportFormat report;

    public RiskReportFormat() {
        report = new ThreatContextDecorator(
                new EconomicAssessmentDecorator(
                        new CurseDecorator(
                                new MissionBaseDecorator(new EmptyReportFormat())
                        )
                )
        );
    }

    @Override
    public String render(Mission mission) throws Exception {
        return report.render(mission);
    }
}
