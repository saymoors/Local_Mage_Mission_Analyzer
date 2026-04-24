package Entities;

import Entities.Enums.EscalationRisk;
import Entities.Enums.Mobility;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;

import java.util.List;

@Embeddable
@JsonPropertyOrder({
        "behaviorType",
        "targetPriority",
        "attackPatterns",
        "mobility",
        "escalationRisk",
        "countermeasuresUsed"
})
public class EnemyActivity {
    @Column(name = "enemy_behavior_type")
    private String behaviorType;

    @Column(name = "enemy_target_priority")
    private String targetPriority;

    @Enumerated(EnumType.STRING)
    @Column(name = "enemy_mobility")
    private Mobility mobility;

    @Enumerated(EnumType.STRING)
    @Column(name = "enemy_escalation_risk")
    private EscalationRisk escalationRisk;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "mission_enemy_attack_patterns", joinColumns = @JoinColumn(name = "mission_id"))
    @OrderColumn(name = "order_index")
    @Column(name = "item_value")
    private List<String> attackPatterns;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "mission_enemy_countermeasures_used", joinColumns = @JoinColumn(name = "mission_id"))
    @OrderColumn(name = "order_index")
    @Column(name = "item_value")
    private List<String> countermeasuresUsed;

    public EnemyActivity() { }

    public String getBehaviorType() {
        return behaviorType;
    }

    public void setBehaviorType(String behaviorType) {
        this.behaviorType = behaviorType;
    }

    public String getTargetPriority() {
        return targetPriority;
    }

    public void setTargetPriority(String targetPriority) {
        this.targetPriority = targetPriority;
    }

    public Mobility getMobility() {
        return mobility;
    }

    public void setMobility(Mobility mobility) {
        this.mobility = mobility;
    }

    public EscalationRisk getEscalationRisk() {
        return escalationRisk;
    }

    public void setEscalationRisk(EscalationRisk escalationRisk) {
        this.escalationRisk = escalationRisk;
    }

    public List<String> getAttackPatterns() {
        return attackPatterns;
    }

    public void setAttackPatterns(List<String> attackPatterns) {
        this.attackPatterns = attackPatterns;
    }

    public List<String> getCountermeasuresUsed() {
        return countermeasuresUsed;
    }

    public void setCountermeasuresUsed(List<String> countermeasuresUsed) {
        this.countermeasuresUsed = countermeasuresUsed;
    }
}
