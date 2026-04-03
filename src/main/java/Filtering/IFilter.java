package Filtering;

import Entities.Mission;

public interface IFilter {
    IFilter setNext(IFilter nextFilter);

    void filter(Mission mission) throws Exception;
}
