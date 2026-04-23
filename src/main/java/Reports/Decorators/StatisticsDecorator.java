package Reports.Decorators;

import Analysis.AnalysisResult;
import Analysis.AnalyzerFactory;
import Analysis.IAnalyzer;
import Entities.Mission;
import Reports.IReportFormat;
import Reports.Support.ReportTextSupport;

public class StatisticsDecorator extends ReportDecorator {
    public StatisticsDecorator(IReportFormat innerReport) {
        super(innerReport);
    }

    @Override
    protected void appendSection(StringBuilder report, Mission mission) throws Exception {
        AnalyzerFactory analyzerFactory = new AnalyzerFactory();
        IAnalyzer analyzer = analyzerFactory.createAnalysisChain();
        AnalysisResult analysisResult = new AnalysisResult();

        if(analyzer != null) {
            analyzer.analyze(mission, analysisResult);
        }

        ReportTextSupport.appendSectionTitle(report, "Статистика");
        ReportTextSupport.appendField(report, 1, "sorcerers.count", analysisResult.getMetric("sorcerers.count"));
        ReportTextSupport.appendField(report, 1, "techniques.count", analysisResult.getMetric("techniques.count"));
        ReportTextSupport.appendField(report, 1, "techniques.totalDamage", analysisResult.getMetric("techniques.totalDamage"));
        ReportTextSupport.appendField(report, 1, "techniques.avgDamage", analysisResult.getMetric("techniques.avgDamage"));
    }
}
