package Reports.Decorators;

import Entities.CivilianImpact;
import Entities.EnemyActivity;
import Entities.EnvironmentConditions;
import Entities.Mission;
import Reports.IReportFormat;
import Reports.Support.ReportTextSupport;

public class ThreatContextDecorator extends ReportDecorator {
    public ThreatContextDecorator(IReportFormat innerReport) {
        super(innerReport);
    }

    @Override
    protected void appendSection(StringBuilder report, Mission mission) {
        EnemyActivity enemyActivity = mission.getEnemyActivity();
        EnvironmentConditions environmentConditions = mission.getEnvironmentConditions();
        CivilianImpact civilianImpact = mission.getCivilianImpact();
        boolean hasEnemyActivityData = hasEnemyActivityData(enemyActivity);

        if(!hasEnemyActivityData && environmentConditions == null && civilianImpact == null) {
            return;
        }

        ReportTextSupport.appendSectionTitle(report, "Контекст угрозы");

        if(hasEnemyActivityData) {
            report.append("  Активность противника:").append(System.lineSeparator());
            ReportTextSupport.appendField(report, 2, "behaviorType", enemyActivity.getBehaviorType());
            ReportTextSupport.appendField(report, 2, "targetPriority", enemyActivity.getTargetPriority());
            ReportTextSupport.appendField(report, 2, "mobility", enemyActivity.getMobility());
            ReportTextSupport.appendField(report, 2, "escalationRisk", enemyActivity.getEscalationRisk());
            ReportTextSupport.appendList(report, 2, "attackPatterns", enemyActivity.getAttackPatterns());
            ReportTextSupport.appendList(report, 2, "countermeasuresUsed", enemyActivity.getCountermeasuresUsed());
        }

        if(environmentConditions != null) {
            report.append("  Условия среды:").append(System.lineSeparator());
            ReportTextSupport.appendField(report, 2, "weather", environmentConditions.getWeather());
            ReportTextSupport.appendField(report, 2, "timeOfDay", environmentConditions.getTimeOfDay());
            ReportTextSupport.appendField(report, 2, "visibility", environmentConditions.getVisibility());
            ReportTextSupport.appendField(report, 2, "cursedEnergyDensity", environmentConditions.getCursedEnergyDensity());
        }

        if(civilianImpact != null) {
            report.append("  Влияние на гражданских:").append(System.lineSeparator());
            ReportTextSupport.appendField(report, 2, "evacuated", civilianImpact.getEvacuated());
            ReportTextSupport.appendField(report, 2, "injured", civilianImpact.getInjured());
            ReportTextSupport.appendField(report, 2, "missing", civilianImpact.getMissing());
            ReportTextSupport.appendField(report, 2, "publicExposureRisk", civilianImpact.getPublicExposureRisk());
        }
    }

    private boolean hasEnemyActivityData(EnemyActivity enemyActivity) {
        if(enemyActivity == null) {
            return false;
        }

        return ReportTextSupport.hasText(enemyActivity.getBehaviorType())
                || ReportTextSupport.hasText(enemyActivity.getTargetPriority())
                || enemyActivity.getMobility() != null
                || enemyActivity.getEscalationRisk() != null
                || ReportTextSupport.hasItems(enemyActivity.getAttackPatterns())
                || ReportTextSupport.hasItems(enemyActivity.getCountermeasuresUsed());
    }
}
