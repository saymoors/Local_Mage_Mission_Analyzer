package Validation;

import Validation.Rules.MissionFields;
import Validation.Rules.SorcererFields;
import Validation.Rules.TechniqueFields;

import java.util.LinkedHashMap;
import java.util.Map;

public class ValidatorFactory {
    private final Map<String, IValidator> rules = new LinkedHashMap<>();

    public ValidatorFactory() {
        register("requiredMissionFields", new MissionFields());
        register("sorcererFields", new SorcererFields());
        register("techniqueFields", new TechniqueFields());
    }

    public void register(String ruleName, IValidator rule) {
        rules.put(ruleName, rule);
    }

    public Map<String, IValidator> getRules() {
        return rules;
    }

    public IValidator createValidationChain() {
        IValidator firstRule = null;
        IValidator currentRule = null;

        for (IValidator rule : rules.values()) {
            if (firstRule == null) {
                firstRule = rule;
                currentRule = rule;
            } else {
                currentRule = currentRule.setNext(rule);
            }
        }

        return firstRule;
    }
}
