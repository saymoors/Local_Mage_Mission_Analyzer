package Filtering.Rules;

import Entities.Mission;
import Entities.Enums.ThreatLevel;
import Filtering.Filter;

public class ThreatLevelFilter extends Filter {
    private final ThreatLevel expectedThreatLevel;

    public ThreatLevelFilter(ThreatLevel expectedThreatLevel) {
        this.expectedThreatLevel = expectedThreatLevel;
    }

    @Override
    protected void check(Mission mission) throws Exception {
        if (expectedThreatLevel != mission.getCurse().getThreatLevel()) {
            throw new Exception("Миссия не прошла фильтр по уровню угрозы!");
        }
    }
}
