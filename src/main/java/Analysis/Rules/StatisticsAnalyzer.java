package Analysis.Rules;

import Analysis.AnalysisResult;
import Analysis.Analyzer;
import Entities.Mission;
import Entities.Technique;

public class StatisticsAnalyzer extends Analyzer {
    @Override
    protected void check(Mission mission, AnalysisResult analysisResult) {
        int sorcerersCount = mission.getSorcerers() == null ? 0 : mission.getSorcerers().size();
        int techniquesCount = mission.getTechniques() == null ? 0 : mission.getTechniques().size();
        int totalDamage = 0;

        if (mission.getTechniques() != null) {
            for (Technique technique : mission.getTechniques()) {
                totalDamage += technique.getDamage();
            }
        }

        analysisResult.putMetric("sorcerers.count", sorcerersCount);
        analysisResult.putMetric("techniques.count", techniquesCount);
        analysisResult.putMetric("techniques.totalDamage", totalDamage);

        if (techniquesCount > 0) {
            analysisResult.putMetric("techniques.avgDamage", totalDamage / techniquesCount);
        }
    }
}
