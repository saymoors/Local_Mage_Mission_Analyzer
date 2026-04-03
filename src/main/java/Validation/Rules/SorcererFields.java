package Validation.Rules;

import Entities.Mission;
import Entities.Sorcerer;
import Validation.Validator;

import java.util.List;

public class SorcererFields extends Validator {
    @Override
    protected void check(Mission mission) throws Exception {
        List<Sorcerer> sorcerers = mission.getSorcerers();

        if(sorcerers == null) {
            return;
        }

        for(int i = 0; i < sorcerers.size(); i++) {
            Sorcerer sorcerer = sorcerers.get(i);
            String prefix = "Колдун[" + i + "]";

            if(sorcerer == null) {
                throw new Exception(prefix + " отсутствует!");
            }

            if(sorcerer.getName() == null || sorcerer.getName().isBlank()) {
                throw new Exception(prefix + ": не заполнено поле name!");
            }

            if(sorcerer.getRank() == null || sorcerer.getRank().isBlank()) {
                throw new Exception(prefix + ": не заполнено поле rank!");
            }
        }
    }
}
