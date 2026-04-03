package Filtering.Rules;

import Entities.Mission;
import Filtering.Filter;

public class OutcomeFilter extends Filter {
    private final String expectedOutcome;

    public OutcomeFilter(String expectedOutcome) {
        this.expectedOutcome = expectedOutcome;
    }

    @Override
    protected void check(Mission mission) throws Exception {
        if(expectedOutcome == null || expectedOutcome.isBlank()) {
            return;
        }

        if(!expectedOutcome.equals(mission.getOutcome())) {
            throw new Exception("Миссия не прошла фильтр по результату!");
        }
    }
}
