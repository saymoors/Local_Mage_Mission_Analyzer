package Validation.Rules;

import Entities.Curse;
import Entities.Mission;
import Validation.Validator;

public class MissionFields extends Validator {
    @Override
    protected void check(Mission mission) throws Exception {
        require(mission.getMissionId(), "Не заполнено обязательное поле missionId.");
        require(mission.getDate(), "Не заполнено обязательное поле date.");
        require(mission.getLocation(), "Не заполнено обязательное поле location.");
        require(mission.getOutcome(), "Не заполнено обязательное поле outcome.");

        Curse curse = mission.getCurse();

        if (curse == null) {
            throw new Exception("Не заполнен обязательный блок curse.");
        }

        require(curse.getName(), "Не заполнено обязательное поле curse.name.");
        require(curse.getThreatLevel(), "Не заполнено обязательное поле curse.threatLevel.");
    }

    private void require(String value, String message) throws Exception {
        if (value == null || value.isBlank()) {
            throw new Exception(message);
        }
    }
}
