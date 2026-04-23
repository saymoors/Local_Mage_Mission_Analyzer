package Reports.Support;

import java.util.List;

public final class ReportTextSupport {
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

        if(value instanceof String text && !hasText(text)) {
            return;
        }

        report.append(indent(indentLevel))
                .append(label)
                .append(": ")
                .append(value)
                .append(LINE_SEPARATOR);
    }

    public static void appendList(StringBuilder report, int indentLevel, String label, List<String> values) {
        if(!hasTextItems(values)) {
            return;
        }

        report.append(indent(indentLevel))
                .append(label)
                .append(':')
                .append(LINE_SEPARATOR);

        for(String value : values) {
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

    private static boolean hasTextItems(List<String> values) {
        if(values == null) {
            return false;
        }

        for(String value : values) {
            if(hasText(value)) {
                return true;
            }
        }

        return false;
    }

    private static String indent(int indentLevel) {
        return "  ".repeat(Math.max(indentLevel, 0));
    }
}
