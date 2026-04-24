package Entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@JsonPropertyOrder({
        "totalDamageCost",
        "infrastructureDamage",
        "transportDamage",
        "commercialDamage",
        "recoveryEstimateDays",
        "insuranceCovered"
})
public class EconomicAssessment {
    @Column(name = "economic_total_damage_cost")
    private int totalDamageCost;

    @Column(name = "economic_infrastructure_damage")
    private int infrastructureDamage;

    @Column(name = "economic_transport_damage")
    private int transportDamage;

    @Column(name = "economic_commercial_damage")
    private int commercialDamage;

    @Column(name = "economic_recovery_estimate_days")
    private int recoveryEstimateDays;

    @Column(name = "economic_insurance_covered")
    private boolean insuranceCovered;

    public EconomicAssessment() { }

    public int getTotalDamageCost() {
        return totalDamageCost;
    }

    public void setTotalDamageCost(int totalDamageCost) {
        this.totalDamageCost = totalDamageCost;
    }

    public int getInfrastructureDamage() {
        return infrastructureDamage;
    }

    public void setInfrastructureDamage(int infrastructureDamage) {
        this.infrastructureDamage = infrastructureDamage;
    }

    public int getTransportDamage() {
        return transportDamage;
    }

    public void setTransportDamage(int transportDamage) {
        this.transportDamage = transportDamage;
    }

    public int getCommercialDamage() {
        return commercialDamage;
    }

    public void setCommercialDamage(int commercialDamage) {
        this.commercialDamage = commercialDamage;
    }

    public int getRecoveryEstimateDays() {
        return recoveryEstimateDays;
    }

    public void setRecoveryEstimateDays(int recoveryEstimateDays) {
        this.recoveryEstimateDays = recoveryEstimateDays;
    }

    public boolean getInsuranceCovered() {
        return insuranceCovered;
    }

    public void setInsuranceCovered(boolean insuranceCovered) {
        this.insuranceCovered = insuranceCovered;
    }
}
