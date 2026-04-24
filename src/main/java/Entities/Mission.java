package Entities;

import Entities.Enums.Outcome;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "missions")
@JsonPropertyOrder({
        "missionId",
        "date",
        "location",
        "outcome",
        "damageCost",
        "curse",
        "sorcerers",
        "techniques",
        "economicAssessment",
        "enemyActivity",
        "environmentConditions",
        "civilianImpact",
        "operationTimeline",
        "operationTags",
        "supportUnits",
        "recommendations",
        "notes",
        "artifactsRecovered",
        "evacuationZones",
        "statusEffects",
        "comment"
})
public class Mission {
    @Id
    @Column(name = "mission_id", nullable = false, length = 100)
    private String missionId;

    @Column(name = "mission_date")
    private String date;

    private String location;

    @Enumerated(EnumType.STRING)
    private Outcome outcome;

    @Column(name = "damage_cost")
    private int damageCost;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "curse_name")),
            @AttributeOverride(name = "threatLevel", column = @Column(name = "curse_threat_level"))
    })
    private Curse curse;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "mission_sorcerers", joinColumns = @JoinColumn(name = "mission_id"))
    @OrderColumn(name = "order_index")
    private List<Sorcerer> sorcerers;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "mission_techniques", joinColumns = @JoinColumn(name = "mission_id"))
    @OrderColumn(name = "order_index")
    private List<Technique> techniques;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "totalDamageCost", column = @Column(name = "economic_total_damage_cost")),
            @AttributeOverride(name = "infrastructureDamage", column = @Column(name = "economic_infrastructure_damage")),
            @AttributeOverride(name = "transportDamage", column = @Column(name = "economic_transport_damage")),
            @AttributeOverride(name = "commercialDamage", column = @Column(name = "economic_commercial_damage")),
            @AttributeOverride(name = "recoveryEstimateDays", column = @Column(name = "economic_recovery_estimate_days")),
            @AttributeOverride(name = "insuranceCovered", column = @Column(name = "economic_insurance_covered"))
    })
    private EconomicAssessment economicAssessment;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "behaviorType", column = @Column(name = "enemy_behavior_type")),
            @AttributeOverride(name = "targetPriority", column = @Column(name = "enemy_target_priority")),
            @AttributeOverride(name = "mobility", column = @Column(name = "enemy_mobility")),
            @AttributeOverride(name = "escalationRisk", column = @Column(name = "enemy_escalation_risk"))
    })
    private EnemyActivity enemyActivity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "weather", column = @Column(name = "environment_weather")),
            @AttributeOverride(name = "timeOfDay", column = @Column(name = "environment_time_of_day")),
            @AttributeOverride(name = "visibility", column = @Column(name = "environment_visibility")),
            @AttributeOverride(name = "cursedEnergyDensity", column = @Column(name = "environment_cursed_energy_density"))
    })
    private EnvironmentConditions environmentConditions;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "evacuated", column = @Column(name = "civilian_evacuated")),
            @AttributeOverride(name = "injured", column = @Column(name = "civilian_injured")),
            @AttributeOverride(name = "missing", column = @Column(name = "civilian_missing")),
            @AttributeOverride(name = "publicExposureRisk", column = @Column(name = "civilian_public_exposure_risk"))
    })
    private CivilianImpact civilianImpact;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "mission_timeline_events", joinColumns = @JoinColumn(name = "mission_id"))
    @OrderColumn(name = "order_index")
    private List<OperationTimelineEvent> operationTimeline;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "mission_operation_tags", joinColumns = @JoinColumn(name = "mission_id"))
    @OrderColumn(name = "order_index")
    @Column(name = "item_value")
    private List<String> operationTags;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "mission_support_units", joinColumns = @JoinColumn(name = "mission_id"))
    @OrderColumn(name = "order_index")
    @Column(name = "item_value")
    private List<String> supportUnits;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "mission_recommendations", joinColumns = @JoinColumn(name = "mission_id"))
    @OrderColumn(name = "order_index")
    @Column(name = "item_value")
    private List<String> recommendations;

    private String notes;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "mission_artifacts_recovered", joinColumns = @JoinColumn(name = "mission_id"))
    @OrderColumn(name = "order_index")
    @Column(name = "item_value")
    private List<String> artifactsRecovered;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "mission_evacuation_zones", joinColumns = @JoinColumn(name = "mission_id"))
    @OrderColumn(name = "order_index")
    @Column(name = "item_value")
    private List<String> evacuationZones;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "mission_status_effects", joinColumns = @JoinColumn(name = "mission_id"))
    @OrderColumn(name = "order_index")
    @Column(name = "item_value")
    private List<String> statusEffects;

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

    public Outcome getOutcome() {
        return outcome;
    }

    public void setOutcome(Outcome outcome) {
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

    public EconomicAssessment getEconomicAssessment() {
        return economicAssessment;
    }

    public void setEconomicAssessment(EconomicAssessment economicAssessment) {
        this.economicAssessment = economicAssessment;
    }

    public EnemyActivity getEnemyActivity() {
        return enemyActivity;
    }

    public void setEnemyActivity(EnemyActivity enemyActivity) {
        this.enemyActivity = enemyActivity;
    }

    public EnvironmentConditions getEnvironmentConditions() {
        return environmentConditions;
    }

    public void setEnvironmentConditions(EnvironmentConditions environmentConditions) {
        this.environmentConditions = environmentConditions;
    }

    public CivilianImpact getCivilianImpact() {
        return civilianImpact;
    }

    public void setCivilianImpact(CivilianImpact civilianImpact) {
        this.civilianImpact = civilianImpact;
    }

    public List<OperationTimelineEvent> getOperationTimeline() {
        return operationTimeline;
    }

    public void setOperationTimeline(List<OperationTimelineEvent> operationTimeline) {
        this.operationTimeline = operationTimeline;
    }

    public List<String> getOperationTags() {
        return operationTags;
    }

    public void setOperationTags(List<String> operationTags) {
        this.operationTags = operationTags;
    }

    public List<String> getSupportUnits() {
        return supportUnits;
    }

    public void setSupportUnits(List<String> supportUnits) {
        this.supportUnits = supportUnits;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getArtifactsRecovered() {
        return artifactsRecovered;
    }

    public void setArtifactsRecovered(List<String> artifactsRecovered) {
        this.artifactsRecovered = artifactsRecovered;
    }

    public List<String> getEvacuationZones() {
        return evacuationZones;
    }

    public void setEvacuationZones(List<String> evacuationZones) {
        this.evacuationZones = evacuationZones;
    }

    public List<String> getStatusEffects() {
        return statusEffects;
    }

    public void setStatusEffects(List<String> statusEffects) {
        this.statusEffects = statusEffects;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void linkEntities() throws Exception {
        if(sorcerers == null) {
            sorcerers = new ArrayList<>();
        }

        if(techniques == null) {
            techniques = new ArrayList<>();
        }

        for(Sorcerer sorcerer : sorcerers) {
            sorcerer.setTechniques(new ArrayList<>());
        }

        for(Technique technique : techniques) {
            boolean isFound = false;
            String ownerName = technique.getOwner();

            for(Sorcerer sorcerer : sorcerers) {
                if(sorcerer.getName().equals(ownerName)) {
                    sorcerer.addTechnique(technique);
                    isFound = true;
                    break;
                }
            }

            if(!isFound) {
                throw new Exception("Техника \"" + technique.getName() + "\" не имеет колдуна");
            }
        }
    }
}
