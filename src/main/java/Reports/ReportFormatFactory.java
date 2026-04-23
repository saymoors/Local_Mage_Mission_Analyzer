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
        register("summary", new SummaryReportFormat());
        register("detailed", new DetailedReportFormat());
        register("risk", new RiskReportFormat());
        register("statistics", new StatisticsReportFormat());
        defaultReportType = "detailed";
    }

    public void register(String reportType, IReportFormat reportFormat) {
        reportFormats.put(reportType.toLowerCase(), reportFormat);
    }

    public String getDefaultReportType() {
        return defaultReportType;
    }

    public Map<String, IReportFormat> getReportFormats() {
        return reportFormats;
    }

    public IReportFormat createReportFormat(String reportType) throws Exception {
        IReportFormat reportFormat = reportFormats.get(reportType.toLowerCase());

        if(reportFormat == null) {
            throw new Exception("Неизвестный тип отчета: " + reportType);
        }

        return reportFormat;
    }
}
