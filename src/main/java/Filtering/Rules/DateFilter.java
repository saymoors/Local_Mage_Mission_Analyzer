package Filtering.Rules;

import Entities.Mission;
import Filtering.Filter;

public class DateFilter extends Filter {
    private final String expectedDate;

    public DateFilter(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    @Override
    protected void check(Mission mission) throws Exception {
        if(expectedDate == null || expectedDate.isBlank()) {
            return;
        }

        if(!expectedDate.equals(mission.getDate())) {
            throw new Exception("Миссия не прошла фильтр по дате!");
        }
    }
}
