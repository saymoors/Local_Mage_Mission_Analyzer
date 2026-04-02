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

import java.util.ArrayList;
import java.util.List;

public class MissionBuilder {
    private final Mission mission;

    public MissionBuilder() {
        this.mission = new Mission();
    }

    public static MissionBuilder fromMission(Mission readMission) {
        return new MissionBuilder()
                .missionId(readMission.getMissionId())
                .date(readMission.getDate())
                .location(readMission.getLocation())
                .outcome(readMission.getOutcome())
                .damageCost(readMission.getDamageCost())
                .curse(readMission.getCurse())
                .sorcerers(readMission.getSorcerers())
                .techniques(readMission.getTechniques())
                .economicAssessment(readMission.getEconomicAssessment())
                .enemyActivity(readMission.getEnemyActivity())
                .environmentConditions(readMission.getEnvironmentConditions())
                .civilianImpact(readMission.getCivilianImpact())
                .operationTimeline(readMission.getOperationTimeline())
                .operationTags(readMission.getOperationTags())
                .supportUnits(readMission.getSupportUnits())
                .recommendations(readMission.getRecommendations())
                .notes(readMission.getNotes())
                .artifactsRecovered(readMission.getArtifactsRecovered())
                .evacuationZones(readMission.getEvacuationZones())
                .statusEffects(readMission.getStatusEffects())
                .comment(readMission.getComment());
    }

    public MissionBuilder missionId(String missionId) {
        mission.setMissionId(missionId);
        return this;
    }

    public MissionBuilder date(String date) {
        mission.setDate(date);
        return this;
    }

    public MissionBuilder location(String location) {
        mission.setLocation(location);
        return this;
    }

    public MissionBuilder outcome(String outcome) {
        mission.setOutcome(outcome);
        return this;
    }

    public MissionBuilder damageCost(int damageCost) {
        mission.setDamageCost(damageCost);
        return this;
    }

    public MissionBuilder curse(Curse curse) {
        mission.setCurse(curse);
        return this;
    }

    public MissionBuilder sorcerers(List<Sorcerer> sorcerers) {
        mission.setSorcerers(sorcerers);
        return this;
    }

    public void addSorcerer(Sorcerer sorcerer) {
        if (mission.getSorcerers() == null) {
            mission.setSorcerers(new ArrayList<>());
        }
        mission.getSorcerers().add(sorcerer);
    }

    public MissionBuilder techniques(List<Technique> techniques) {
        mission.setTechniques(techniques);
        return this;
    }

    public void addTechnique(Technique technique) {
        if (mission.getTechniques() == null) {
            mission.setTechniques(new ArrayList<>());
        }
        mission.getTechniques().add(technique);
    }

    public MissionBuilder economicAssessment(EconomicAssessment economicAssessment) {
        mission.setEconomicAssessment(economicAssessment);
        return this;
    }

    public MissionBuilder enemyActivity(EnemyActivity enemyActivity) {
        mission.setEnemyActivity(enemyActivity);
        return this;
    }

    public MissionBuilder environmentConditions(EnvironmentConditions environmentConditions) {
        mission.setEnvironmentConditions(environmentConditions);
        return this;
    }

    public MissionBuilder civilianImpact(CivilianImpact civilianImpact) {
        mission.setCivilianImpact(civilianImpact);
        return this;
    }

    public MissionBuilder operationTimeline(List<OperationTimelineEvent> operationTimeline) {
        mission.setOperationTimeline(operationTimeline);
        return this;
    }

    public void addOperationTimelineEvent(OperationTimelineEvent event) {
        if (mission.getOperationTimeline() == null) {
            mission.setOperationTimeline(new ArrayList<>());
        }
        mission.getOperationTimeline().add(event);
    }

    public MissionBuilder operationTags(List<String> operationTags) {
        mission.setOperationTags(operationTags);
        return this;
    }

    public void addOperationTag(String operationTag) {
        if (mission.getOperationTags() == null) {
            mission.setOperationTags(new ArrayList<>());
        }
        mission.getOperationTags().add(operationTag);
    }

    public MissionBuilder supportUnits(List<String> supportUnits) {
        mission.setSupportUnits(supportUnits);
        return this;
    }

    public void addSupportUnit(String supportUnit) {
        if (mission.getSupportUnits() == null) {
            mission.setSupportUnits(new ArrayList<>());
        }
        mission.getSupportUnits().add(supportUnit);
    }

    public MissionBuilder recommendations(List<String> recommendations) {
        mission.setRecommendations(recommendations);
        return this;
    }

    public void addRecommendation(String recommendation) {
        if (mission.getRecommendations() == null) {
            mission.setRecommendations(new ArrayList<>());
        }
        mission.getRecommendations().add(recommendation);
    }

    public MissionBuilder notes(String notes) {
        mission.setNotes(notes);
        return this;
    }

    public MissionBuilder artifactsRecovered(List<String> artifactsRecovered) {
        mission.setArtifactsRecovered(artifactsRecovered);
        return this;
    }

    public void addArtifactRecovered(String artifactRecovered) {
        if (mission.getArtifactsRecovered() == null) {
            mission.setArtifactsRecovered(new ArrayList<>());
        }
        mission.getArtifactsRecovered().add(artifactRecovered);
    }

    public MissionBuilder evacuationZones(List<String> evacuationZones) {
        mission.setEvacuationZones(evacuationZones);
        return this;
    }

    public void addEvacuationZone(String evacuationZone) {
        if (mission.getEvacuationZones() == null) {
            mission.setEvacuationZones(new ArrayList<>());
        }
        mission.getEvacuationZones().add(evacuationZone);
    }

    public MissionBuilder statusEffects(List<String> statusEffects) {
        mission.setStatusEffects(statusEffects);
        return this;
    }

    public void addStatusEffect(String statusEffect) {
        if (mission.getStatusEffects() == null) {
            mission.setStatusEffects(new ArrayList<>());
        }
        mission.getStatusEffects().add(statusEffect);
    }

    public MissionBuilder comment(String comment) {
        mission.setComment(comment);
        return this;
    }

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
