package Validation;

import Entities.Mission;

public abstract class Validator implements IValidator {
    private IValidator nextRule;

    @Override
    public IValidator setNext(IValidator nextRule) {
        this.nextRule = nextRule;
        return nextRule;
    }

    @Override
    public final void validate(Mission mission) throws Exception {
        check(mission);

        if(nextRule != null) {
            nextRule.validate(mission);
        }
    }

    protected abstract void check(Mission mission) throws Exception;
}
