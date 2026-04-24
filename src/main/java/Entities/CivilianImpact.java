package Entities;

import Entities.Enums.PublicExposureRisk;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
@JsonPropertyOrder({"evacuated", "injured", "missing", "publicExposureRisk"})
public class CivilianImpact {
    @Column(name = "civilian_evacuated")
    private int evacuated;

    @Column(name = "civilian_injured")
    private int injured;

    @Column(name = "civilian_missing")
    private int missing;

    @Enumerated(EnumType.STRING)
    @Column(name = "civilian_public_exposure_risk")
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
