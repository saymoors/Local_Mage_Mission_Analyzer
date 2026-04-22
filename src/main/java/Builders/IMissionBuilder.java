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

import java.util.List;

public interface IMissionBuilder {
    IMissionBuilder reset();

    IMissionBuilder missionId(String missionId);

    IMissionBuilder date(String date);

    IMissionBuilder location(String location);

    IMissionBuilder outcome(Outcome outcome);

    IMissionBuilder damageCost(int damageCost);

    IMissionBuilder curse(Curse curse);

    IMissionBuilder curse(String name, ThreatLevel threatLevel);

    IMissionBuilder sorcerers(List<Sorcerer> sorcerers);

    IMissionBuilder addSorcerer(Sorcerer sorcerer);

    IMissionBuilder addSorcerer(String name, SorcererRank rank);

    IMissionBuilder techniques(List<Technique> techniques);

    IMissionBuilder addTechnique(Technique technique);

    IMissionBuilder addTechnique(String name, TechniqueType type, String owner, int damage);

    IMissionBuilder economicAssessment(EconomicAssessment economicAssessment);

    IMissionBuilder economicAssessment(
            int totalDamageCost,
            int infrastructureDamage,
            int transportDamage,
            int commercialDamage,
            int recoveryEstimateDays,
            boolean insuranceCovered
    );

    IMissionBuilder enemyActivity(EnemyActivity enemyActivity);

    IMissionBuilder enemyActivity(
            String behaviorType,
            String targetPriority,
            Mobility mobility,
            EscalationRisk escalationRisk,
            List<String> attackPatterns,
            List<String> countermeasuresUsed
    );

    IMissionBuilder environmentConditions(EnvironmentConditions environmentConditions);

    IMissionBuilder environmentConditions(
            String weather,
            String timeOfDay,
            Visibility visibility,
            int cursedEnergyDensity
    );

    IMissionBuilder civilianImpact(CivilianImpact civilianImpact);

    IMissionBuilder civilianImpact(
            int evacuated,
            int injured,
            int missing,
            PublicExposureRisk publicExposureRisk
    );

    IMissionBuilder operationTimeline(List<OperationTimelineEvent> operationTimeline);

    IMissionBuilder addOperationTimelineEvent(OperationTimelineEvent event);

    IMissionBuilder addOperationTimelineEvent(String timestamp, String type, String description);

    IMissionBuilder operationTags(List<String> operationTags);

    IMissionBuilder addOperationTag(String operationTag);

    IMissionBuilder supportUnits(List<String> supportUnits);

    IMissionBuilder addSupportUnit(String supportUnit);

    IMissionBuilder recommendations(List<String> recommendations);

    IMissionBuilder addRecommendation(String recommendation);

    IMissionBuilder notes(String notes);

    IMissionBuilder artifactsRecovered(List<String> artifactsRecovered);

    IMissionBuilder addArtifactRecovered(String artifactRecovered);

    IMissionBuilder evacuationZones(List<String> evacuationZones);

    IMissionBuilder addEvacuationZone(String evacuationZone);

    IMissionBuilder statusEffects(List<String> statusEffects);

    IMissionBuilder addStatusEffect(String statusEffect);

    IMissionBuilder comment(String comment);

    Mission build() throws Exception;
}
