package Reports.Decorators;

import Entities.Mission;
import Reports.IReportFormat;
import Reports.Support.ReportTextSupport;

public class AftermathDecorator extends ReportDecorator {
    public AftermathDecorator(IReportFormat innerReport) {
        super(innerReport);
    }

    @Override
    protected void appendSection(StringBuilder report, Mission mission) {
        boolean hasNotes = ReportTextSupport.hasText(mission.getNotes());
        boolean hasComment = ReportTextSupport.hasText(mission.getComment());
        boolean hasRecommendations = ReportTextSupport.hasItems(mission.getRecommendations());
        boolean hasArtifacts = ReportTextSupport.hasItems(mission.getArtifactsRecovered());
        boolean hasEvacuationZones = ReportTextSupport.hasItems(mission.getEvacuationZones());
        boolean hasStatusEffects = ReportTextSupport.hasItems(mission.getStatusEffects());

        if(!hasNotes && !hasComment && !hasRecommendations && !hasArtifacts && !hasEvacuationZones && !hasStatusEffects) {
            return;
        }

        ReportTextSupport.appendSectionTitle(report, "Итоги миссии");
        ReportTextSupport.appendList(report, 1, "recommendations", mission.getRecommendations());
        ReportTextSupport.appendField(report, 1, "notes", mission.getNotes());
        ReportTextSupport.appendList(report, 1, "artifactsRecovered", mission.getArtifactsRecovered());
        ReportTextSupport.appendList(report, 1, "evacuationZones", mission.getEvacuationZones());
        ReportTextSupport.appendList(report, 1, "statusEffects", mission.getStatusEffects());
        ReportTextSupport.appendField(report, 1, "comment", mission.getComment());
    }
}
