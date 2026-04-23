package Reports.Support;

import java.util.List;

public class ReportTextSupport {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private ReportTextSupport() {
    }

    public static void appendSectionTitle(StringBuilder report, String title) {
        if(report.length() > 0) {
            report.append(LINE_SEPARATOR);
        }

        report.append(title).append(':').append(LINE_SEPARATOR);
    }

    public static void appendField(StringBuilder report, int indentLevel, String label, Object value) {
        if(value == null) {
            return;
        }

        if(value instanceof String text && text.isBlank()) {
            return;
        }

        report.append(indent(indentLevel))
                .append(label)
                .append(": ")
                .append(value)
                .append(LINE_SEPARATOR);
    }

    public static void appendList(StringBuilder report, int indentLevel, String label, List<String> values) {
        List<String> filteredValues = values == null
                ? List.of()
                : values.stream()
                .filter(value -> value != null && !value.isBlank())
                .toList();

        if(filteredValues.isEmpty()) {
            return;
        }

        report.append(indent(indentLevel))
                .append(label)
                .append(':')
                .append(LINE_SEPARATOR);

        for (String value : filteredValues) {
            report.append(indent(indentLevel + 1))
                    .append("- ")
                    .append(value)
                    .append(LINE_SEPARATOR);
        }
    }

    public static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    public static boolean hasItems(List<?> values) {
        return values != null && !values.isEmpty();
    }

    private static String indent(int indentLevel) {
        return "  ".repeat(Math.max(indentLevel, 0));
    }
}
