package Filtering.Rules;

import Entities.Mission;
import Entities.Enums.Outcome;
import Filtering.Filter;

public class OutcomeFilter extends Filter {
    private final Outcome expectedOutcome;

    public OutcomeFilter(Outcome expectedOutcome) {
        this.expectedOutcome = expectedOutcome;
    }

    @Override
    protected void check(Mission mission) throws Exception {
        if (expectedOutcome != mission.getOutcome()) {
            throw new Exception("Миссия не прошла фильтр по результату!");
        }
    }
}
