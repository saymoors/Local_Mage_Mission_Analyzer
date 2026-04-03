package Analysis;

import Entities.Mission;

public interface IAnalyzer {
    IAnalyzer setNext(IAnalyzer nextAnalyzer);

    void analyze(Mission mission, AnalysisResult analysisResult) throws Exception;
}
