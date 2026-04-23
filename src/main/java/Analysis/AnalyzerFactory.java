package Analysis;

import Analysis.Rules.StatisticsAnalyzer;

import java.util.LinkedHashMap;
import java.util.Map;

public class AnalyzerFactory {
    private final Map<String, IAnalyzer> analyzers = new LinkedHashMap<>();

    public AnalyzerFactory() {
        register("StatisticsAnalyzer", new StatisticsAnalyzer());
    }

    public void register(String analyzerName, IAnalyzer analyzer) {
        analyzers.put(analyzerName, analyzer);
    }

    public Map<String, IAnalyzer> getAnalyzers() {
        return analyzers;
    }

    public IAnalyzer createAnalysisChain() {
        IAnalyzer firstAnalyzer = null;
        IAnalyzer currentAnalyzer = null;

        for (IAnalyzer analyzer : analyzers.values()) {
            if(firstAnalyzer == null) {
                firstAnalyzer = analyzer;
                currentAnalyzer = analyzer;
            } else {
                currentAnalyzer = currentAnalyzer.setNext(analyzer);
            }
        }

        return firstAnalyzer;
    }
}
