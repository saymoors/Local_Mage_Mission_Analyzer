package Analysis;

import java.util.LinkedHashMap;
import java.util.Map;

public class AnalysisResult {
    private final Map<String, Object> metrics = new LinkedHashMap<>();

    public void putMetric(String key, Object value) {
        metrics.put(key, value);
    }

    public Object getMetric(String key) {
        return metrics.get(key);
    }

    public Map<String, Object> getMetrics() {
        return metrics;
    }
}
