package Entities;

import java.util.ArrayList;
import java.util.List;

public class Mission {
    private String missionId;
    private String date;
    private String location;
    private String outcome;
    private int damageCost;
    private Curse curse;
    private List<Sorcerer> sorcerers;
    private List<Technique> techniques;
    private String comment;

    public Mission() { }

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public int getDamageCost() {
        return damageCost;
    }

    public void setDamageCost(int damageCost) {
        this.damageCost = damageCost;
    }

    public Curse getCurse() {
        return curse;
    }

    public void setCurse(Curse curse) {
        this.curse = curse;
    }

    public List<Sorcerer> getSorcerers() {
        return sorcerers;
    }

    public void setSorcerers(List<Sorcerer> sorcerers) {
        this.sorcerers = sorcerers;
    }

    public List<Technique> getTechniques() {
        return techniques;
    }

    public void setTechniques(List<Technique> techniques) {
        this.techniques = techniques;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void linkEntities() throws Exception {
        if(sorcerers == null || techniques == null) {
            throw new Exception("Данные упали в бездну. Найдите их!");
        }

        for (Sorcerer sorcerer : this.sorcerers) {
            sorcerer.setTechniques(new ArrayList<>());
        }

        for (Technique technique : this.techniques) {
            boolean isFound = false;
            String ownerName = technique.getOwner();

            for (Sorcerer sorcerer : this.sorcerers) {
                if (sorcerer.getName().equals(ownerName)) {
                    sorcerer.addTechnique(technique);
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                throw new Exception("Техника \"" + technique.getName() + "\" не имеет колдуна");
            }
        }
    }
}
