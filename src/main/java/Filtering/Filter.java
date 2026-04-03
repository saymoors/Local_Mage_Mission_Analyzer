package Filtering;

import Entities.Mission;

public abstract class Filter implements IFilter {
    private IFilter nextFilter;

    @Override
    public IFilter setNext(IFilter nextFilter) {
        this.nextFilter = nextFilter;
        return nextFilter;
    }

    @Override
    public final void filter(Mission mission) throws Exception {
        check(mission);

        if(nextFilter != null) {
            nextFilter.filter(mission);
        }
    }

    protected abstract void check(Mission mission) throws Exception;
}
