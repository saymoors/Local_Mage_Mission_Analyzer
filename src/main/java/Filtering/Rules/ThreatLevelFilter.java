package Filtering.Rules;

import Entities.Curse;
import Entities.Mission;
import Filtering.Filter;

public class ThreatLevelFilter extends Filter {
    private final String expectedThreatLevel;

    public ThreatLevelFilter(String expectedThreatLevel) {
        this.expectedThreatLevel = expectedThreatLevel;
    }

    @Override
    protected void check(Mission mission) throws Exception {
        if (expectedThreatLevel == null || expectedThreatLevel.isBlank()) {
            return;
        }

        Curse curse = mission.getCurse();

        if (!expectedThreatLevel.equals(curse.getThreatLevel())) {
            throw new Exception("Миссия не прошла фильтр по уровню угрозы!");
        }
    }
}
