package Reports.Decorators;

import Entities.Mission;
import Entities.OperationTimelineEvent;
import Reports.IReportFormat;
import Reports.Support.ReportTextSupport;

import java.util.List;

public class TimelineDecorator extends ReportDecorator {
    public TimelineDecorator(IReportFormat innerReport) {
        super(innerReport);
    }

    @Override
    protected void appendSection(StringBuilder report, Mission mission) {
        List<OperationTimelineEvent> timeline = mission.getOperationTimeline();
        boolean hasTimeline = ReportTextSupport.hasItems(timeline);
        boolean hasOperationTags = ReportTextSupport.hasItems(mission.getOperationTags());
        boolean hasSupportUnits = ReportTextSupport.hasItems(mission.getSupportUnits());

        if(!hasTimeline && !hasOperationTags && !hasSupportUnits) {
            return;
        }

        ReportTextSupport.appendSectionTitle(report, "Хронология и сопровождение");

        if(hasTimeline) {
            for (int i = 0; i < timeline.size(); i++) {
                OperationTimelineEvent event = timeline.get(i);
                if(event == null) {
                    continue;
                }

                report.append("  Событие[").append(i).append(']').append(':').append(System.lineSeparator());
                ReportTextSupport.appendField(report, 2, "timestamp", event.getTimestamp());
                ReportTextSupport.appendField(report, 2, "type", event.getType());
                ReportTextSupport.appendField(report, 2, "description", event.getDescription());
            }
        }

        ReportTextSupport.appendList(report, 1, "operationTags", mission.getOperationTags());
        ReportTextSupport.appendList(report, 1, "supportUnits", mission.getSupportUnits());
    }
}
