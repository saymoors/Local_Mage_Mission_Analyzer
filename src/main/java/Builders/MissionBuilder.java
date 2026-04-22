package Builders;

import Entities.CivilianImpact;
import Entities.Curse;
import Entities.EconomicAssessment;
import Entities.EnemyActivity;
import Entities.EnvironmentConditions;
import Entities.Mission;
import Entities.OperationTimelineEvent;
import Entities.Sorcerer;
import Entities.Technique;
import Entities.Enums.EscalationRisk;
import Entities.Enums.Mobility;
import Entities.Enums.Outcome;
import Entities.Enums.PublicExposureRisk;
import Entities.Enums.SorcererRank;
import Entities.Enums.TechniqueType;
import Entities.Enums.ThreatLevel;
import Entities.Enums.Visibility;

import java.util.ArrayList;
import java.util.List;

public class MissionBuilder implements IMissionBuilder {
    private Mission mission;

    public MissionBuilder() {
        reset();
    }

    @Override
    public MissionBuilder reset() {
        mission = new Mission();
        return this;
    }

    @Override
    public MissionBuilder missionId(String missionId) {
        mission.setMissionId(missionId);
        return this;
    }

    @Override
    public MissionBuilder date(String date) {
        mission.setDate(date);
        return this;
    }

    @Override
    public MissionBuilder location(String location) {
        mission.setLocation(location);
        return this;
    }

    @Override
    public MissionBuilder outcome(Outcome outcome) {
        mission.setOutcome(outcome);
        return this;
    }

    @Override
    public MissionBuilder damageCost(int damageCost) {
        mission.setDamageCost(damageCost);
        return this;
    }

    @Override
    public MissionBuilder curse(Curse curse) {
        mission.setCurse(curse);
        return this;
    }

    @Override
    public MissionBuilder curse(String name, ThreatLevel threatLevel) {
        return curse(new Curse(name, threatLevel));
    }

    @Override
    public MissionBuilder sorcerers(List<Sorcerer> sorcerers) {
        mission.setSorcerers(sorcerers);
        return this;
    }

    @Override
    public MissionBuilder addSorcerer(Sorcerer sorcerer) {
        if (mission.getSorcerers() == null) {
            mission.setSorcerers(new ArrayList<>());
        }
        mission.getSorcerers().add(sorcerer);
        return this;
    }

    @Override
    public MissionBuilder addSorcerer(String name, SorcererRank rank) {
        return addSorcerer(new Sorcerer(name, rank));
    }

    @Override
    public MissionBuilder techniques(List<Technique> techniques) {
        mission.setTechniques(techniques);
        return this;
    }

    @Override
    public MissionBuilder addTechnique(Technique technique) {
        if (mission.getTechniques() == null) {
            mission.setTechniques(new ArrayList<>());
        }
        mission.getTechniques().add(technique);
        return this;
    }

    @Override
    public MissionBuilder addTechnique(String name, TechniqueType type, String owner, int damage) {
        return addTechnique(new Technique(name, type, owner, damage));
    }

    @Override
    public MissionBuilder economicAssessment(EconomicAssessment economicAssessment) {
        mission.setEconomicAssessment(economicAssessment);
        return this;
    }

    @Override
    public MissionBuilder economicAssessment(
            int totalDamageCost,
            int infrastructureDamage,
            int transportDamage,
            int commercialDamage,
            int recoveryEstimateDays,
            boolean insuranceCovered
    ) {
        EconomicAssessment economicAssessment = new EconomicAssessment();
        economicAssessment.setTotalDamageCost(totalDamageCost);
        economicAssessment.setInfrastructureDamage(infrastructureDamage);
        economicAssessment.setTransportDamage(transportDamage);
        economicAssessment.setCommercialDamage(commercialDamage);
        economicAssessment.setRecoveryEstimateDays(recoveryEstimateDays);
        economicAssessment.setInsuranceCovered(insuranceCovered);
        return economicAssessment(economicAssessment);
    }

    @Override
    public MissionBuilder enemyActivity(EnemyActivity enemyActivity) {
        mission.setEnemyActivity(enemyActivity);
        return this;
    }

    @Override
    public MissionBuilder enemyActivity(
            String behaviorType,
            String targetPriority,
            Mobility mobility,
            EscalationRisk escalationRisk,
            List<String> attackPatterns,
            List<String> countermeasuresUsed
    ) {
        EnemyActivity enemyActivity = new EnemyActivity();
        enemyActivity.setBehaviorType(behaviorType);
        enemyActivity.setTargetPriority(targetPriority);
        enemyActivity.setMobility(mobility);
        enemyActivity.setEscalationRisk(escalationRisk);
        enemyActivity.setAttackPatterns(attackPatterns);
        enemyActivity.setCountermeasuresUsed(countermeasuresUsed);
        return enemyActivity(enemyActivity);
    }

    @Override
    public MissionBuilder environmentConditions(EnvironmentConditions environmentConditions) {
        mission.setEnvironmentConditions(environmentConditions);
        return this;
    }

    @Override
    public MissionBuilder environmentConditions(
            String weather,
            String timeOfDay,
            Visibility visibility,
            int cursedEnergyDensity
    ) {
        EnvironmentConditions environmentConditions = new EnvironmentConditions();
        environmentConditions.setWeather(weather);
        environmentConditions.setTimeOfDay(timeOfDay);
        environmentConditions.setVisibility(visibility);
        environmentConditions.setCursedEnergyDensity(cursedEnergyDensity);
        return environmentConditions(environmentConditions);
    }

    @Override
    public MissionBuilder civilianImpact(CivilianImpact civilianImpact) {
        mission.setCivilianImpact(civilianImpact);
        return this;
    }

    @Override
    public MissionBuilder civilianImpact(
            int evacuated,
            int injured,
            int missing,
            PublicExposureRisk publicExposureRisk
    ) {
        CivilianImpact civilianImpact = new CivilianImpact();
        civilianImpact.setEvacuated(evacuated);
        civilianImpact.setInjured(injured);
        civilianImpact.setMissing(missing);
        civilianImpact.setPublicExposureRisk(publicExposureRisk);
        return civilianImpact(civilianImpact);
    }

    @Override
    public MissionBuilder operationTimeline(List<OperationTimelineEvent> operationTimeline) {
        mission.setOperationTimeline(operationTimeline);
        return this;
    }

    @Override
    public MissionBuilder addOperationTimelineEvent(OperationTimelineEvent event) {
        if (mission.getOperationTimeline() == null) {
            mission.setOperationTimeline(new ArrayList<>());
        }
        mission.getOperationTimeline().add(event);
        return this;
    }

    @Override
    public MissionBuilder addOperationTimelineEvent(String timestamp, String type, String description) {
        OperationTimelineEvent event = new OperationTimelineEvent();
        event.setTimestamp(timestamp);
        event.setType(type);
        event.setDescription(description);
        return addOperationTimelineEvent(event);
    }

    @Override
    public MissionBuilder operationTags(List<String> operationTags) {
        mission.setOperationTags(operationTags);
        return this;
    }

    @Override
    public MissionBuilder addOperationTag(String operationTag) {
        if (mission.getOperationTags() == null) {
            mission.setOperationTags(new ArrayList<>());
        }
        mission.getOperationTags().add(operationTag);
        return this;
    }

    @Override
    public MissionBuilder supportUnits(List<String> supportUnits) {
        mission.setSupportUnits(supportUnits);
        return this;
    }

    @Override
    public MissionBuilder addSupportUnit(String supportUnit) {
        if (mission.getSupportUnits() == null) {
            mission.setSupportUnits(new ArrayList<>());
        }
        mission.getSupportUnits().add(supportUnit);
        return this;
    }

    @Override
    public MissionBuilder recommendations(List<String> recommendations) {
        mission.setRecommendations(recommendations);
        return this;
    }

    @Override
    public MissionBuilder addRecommendation(String recommendation) {
        if (mission.getRecommendations() == null) {
            mission.setRecommendations(new ArrayList<>());
        }
        mission.getRecommendations().add(recommendation);
        return this;
    }

    @Override
    public MissionBuilder notes(String notes) {
        mission.setNotes(notes);
        return this;
    }

    @Override
    public MissionBuilder artifactsRecovered(List<String> artifactsRecovered) {
        mission.setArtifactsRecovered(artifactsRecovered);
        return this;
    }

    @Override
    public MissionBuilder addArtifactRecovered(String artifactRecovered) {
        if (mission.getArtifactsRecovered() == null) {
            mission.setArtifactsRecovered(new ArrayList<>());
        }
        mission.getArtifactsRecovered().add(artifactRecovered);
        return this;
    }

    @Override
    public MissionBuilder evacuationZones(List<String> evacuationZones) {
        mission.setEvacuationZones(evacuationZones);
        return this;
    }

    @Override
    public MissionBuilder addEvacuationZone(String evacuationZone) {
        if (mission.getEvacuationZones() == null) {
            mission.setEvacuationZones(new ArrayList<>());
        }
        mission.getEvacuationZones().add(evacuationZone);
        return this;
    }

    @Override
    public MissionBuilder statusEffects(List<String> statusEffects) {
        mission.setStatusEffects(statusEffects);
        return this;
    }

    @Override
    public MissionBuilder addStatusEffect(String statusEffect) {
        if (mission.getStatusEffects() == null) {
            mission.setStatusEffects(new ArrayList<>());
        }
        mission.getStatusEffects().add(statusEffect);
        return this;
    }

    @Override
    public MissionBuilder comment(String comment) {
        mission.setComment(comment);
        return this;
    }

    @Override
    public Mission build() throws Exception {
        if (mission.getSorcerers() == null) {
            mission.setSorcerers(new ArrayList<>());
        }

        if (mission.getTechniques() == null) {
            mission.setTechniques(new ArrayList<>());
        }

        if (mission.getOperationTimeline() == null) {
            mission.setOperationTimeline(new ArrayList<>());
        }

        if (mission.getOperationTags() == null) {
            mission.setOperationTags(new ArrayList<>());
        }

        if (mission.getSupportUnits() == null) {
            mission.setSupportUnits(new ArrayList<>());
        }

        if (mission.getRecommendations() == null) {
            mission.setRecommendations(new ArrayList<>());
        }

        if (mission.getArtifactsRecovered() == null) {
            mission.setArtifactsRecovered(new ArrayList<>());
        }

        if (mission.getEvacuationZones() == null) {
            mission.setEvacuationZones(new ArrayList<>());
        }

        if (mission.getStatusEffects() == null) {
            mission.setStatusEffects(new ArrayList<>());
        }

        mission.linkEntities();
        return mission;
    }
}
