package Reports;

import Reports.Formats.DetailedReportFormat;
import Reports.Formats.RiskReportFormat;
import Reports.Formats.StatisticsReportFormat;
import Reports.Formats.SummaryReportFormat;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReportFormatFactory {
    private final Map<String, IReportFormat> reportFormats = new LinkedHashMap<>();
    private final String defaultReportType;

    public ReportFormatFactory() {
        register( "Краткое резюме", new SummaryReportFormat());
        register("Детализированный отчет", new DetailedReportFormat());
        register("Отчет по рискам", new RiskReportFormat());
        register( "Статистический отчет", new StatisticsReportFormat());
        defaultReportType = "Детализированный отчет";
    }

    public void register(String reportType, IReportFormat reportFormat) {
        reportFormats.put(reportType, reportFormat);
    }

    public String getDefaultReportType() {
        return defaultReportType;
    }

    public Map<String, IReportFormat> getReportFormats() {
        return reportFormats;
    }

    public IReportFormat createReportFormat(String reportType) throws Exception {
        IReportFormat reportFormat = reportFormats.get(reportType);

        if(reportFormat == null) {
            throw new Exception("Вы не выбрали тип отчета!");
        }

        return reportFormat;
    }
}
