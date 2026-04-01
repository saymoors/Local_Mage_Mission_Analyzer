package Factories;

import Reports.DetailedReportFormat;
import Reports.IReportFormat;
import Reports.RiskReportFormat;
import Reports.StatisticsReportFormat;
import Reports.SummaryReportFormat;

public class ReportFormatFactory {
    public IReportFormat createReportFormat(String reportType) throws Exception {
        return switch (reportType) {
            case "summary" -> new SummaryReportFormat();
            case "detailed" -> new DetailedReportFormat();
            case "risk" -> new RiskReportFormat();
            case "statistics" -> new StatisticsReportFormat();
            default -> throw new Exception("Вы не выбрали тип отчета!");
        };
    }
}
