package Reports.Decorators;

import Entities.EconomicAssessment;
import Entities.Mission;
import Reports.IReportFormat;
import Reports.Support.ReportTextSupport;

public class EconomicAssessmentDecorator extends ReportDecorator {
    public EconomicAssessmentDecorator(IReportFormat innerReport) {
        super(innerReport);
    }

    @Override
    protected void appendSection(StringBuilder report, Mission mission) {
        EconomicAssessment economicAssessment = mission.getEconomicAssessment();
        if(economicAssessment == null) {
            return;
        }

        ReportTextSupport.appendSectionTitle(report, "Экономическая оценка");
        ReportTextSupport.appendField(report, 1, "totalDamageCost", economicAssessment.getTotalDamageCost());
        ReportTextSupport.appendField(report, 1, "infrastructureDamage", economicAssessment.getInfrastructureDamage());
        ReportTextSupport.appendField(report, 1, "transportDamage", economicAssessment.getTransportDamage());
        ReportTextSupport.appendField(report, 1, "commercialDamage", economicAssessment.getCommercialDamage());
        ReportTextSupport.appendField(report, 1, "recoveryEstimateDays", economicAssessment.getRecoveryEstimateDays());
        ReportTextSupport.appendField(report, 1, "insuranceCovered", economicAssessment.getInsuranceCovered());
    }
}
