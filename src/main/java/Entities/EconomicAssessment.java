package Entities;

public class EconomicAssessment {
    private int totalDamageCost;
    private int infrastructureDamage;
    private int transportDamage;
    private int commercialDamage;
    private int recoveryEstimateDays;
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
