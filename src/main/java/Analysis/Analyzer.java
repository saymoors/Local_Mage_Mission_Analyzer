package Analysis;

import Entities.Mission;

public abstract class Analyzer implements IAnalyzer {
    private IAnalyzer nextAnalyzer;

    @Override
    public IAnalyzer setNext(IAnalyzer nextAnalyzer) {
        this.nextAnalyzer = nextAnalyzer;
        return nextAnalyzer;
    }

    @Override
    public final void analyze(Mission mission, AnalysisResult analysisResult) throws Exception {
        check(mission, analysisResult);

        if(nextAnalyzer != null) {
            nextAnalyzer.analyze(mission, analysisResult);
        }
    }

    protected abstract void check(Mission mission, AnalysisResult analysisResult) throws Exception;
}
