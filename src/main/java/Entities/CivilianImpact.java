package Entities;

import Entities.Enums.PublicExposureRisk;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"evacuated", "injured", "missing", "publicExposureRisk"})
public class CivilianImpact {
    private int evacuated;
    private int injured;
    private int missing;
    private PublicExposureRisk publicExposureRisk;

    public CivilianImpact() { }

    public int getEvacuated() {
        return evacuated;
    }

    public void setEvacuated(int evacuated) {
        this.evacuated = evacuated;
    }

    public int getInjured() {
        return injured;
    }

    public void setInjured(int injured) {
        this.injured = injured;
    }

    public int getMissing() {
        return missing;
    }

    public void setMissing(int missing) {
        this.missing = missing;
    }

    public PublicExposureRisk getPublicExposureRisk() {
        return publicExposureRisk;
    }

    public void setPublicExposureRisk(PublicExposureRisk publicExposureRisk) {
        this.publicExposureRisk = publicExposureRisk;
    }
}
