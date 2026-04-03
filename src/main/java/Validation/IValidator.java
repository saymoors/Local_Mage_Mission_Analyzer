package Validation;

import Entities.Mission;

public interface IValidator {
    IValidator setNext(IValidator nextRule);

    void validate(Mission mission) throws Exception;
}
