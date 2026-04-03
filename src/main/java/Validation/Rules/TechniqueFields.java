package Validation.Rules;

import Entities.Mission;
import Entities.Technique;
import Validation.Validator;

import java.util.List;

public class TechniqueFields extends Validator {
    @Override
    protected void check(Mission mission) throws Exception {
        List<Technique> techniques = mission.getTechniques();

        if (techniques == null) {
            return;
        }

        for (int i = 0; i < techniques.size(); i++) {
            Technique technique = techniques.get(i);
            String prefix = "Техника[" + i + "]";

            if (technique == null) {
                throw new Exception(prefix + " отсутствует!");
            }

            if (technique.getName() == null || technique.getName().isBlank()) {
                throw new Exception(prefix + ": не заполнено поле name!");
            }

            if (technique.getType() == null || technique.getType().isBlank()) {
                throw new Exception(prefix + ": не заполнено поле type!");
            }

            if (technique.getOwner() == null || technique.getOwner().isBlank()) {
                throw new Exception(prefix + ": не заполнено поле owner!");
            }

            if (technique.getDamage() < 0) {
                throw new Exception(prefix + ": damage не может быть отрицательным!");
            }
        }
    }
}
