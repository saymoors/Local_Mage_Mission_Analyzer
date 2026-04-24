package Entities;

import Entities.Enums.ThreatLevel;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
@JsonPropertyOrder({"name", "threatLevel"})
public class Curse {
    @Column(name = "curse_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "curse_threat_level")
    private ThreatLevel threatLevel;

    public Curse() { }

    public Curse(String name, ThreatLevel threatLevel) {
        this.name = name;
        this.threatLevel = threatLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ThreatLevel getThreatLevel() {
        return threatLevel;
    }

    public void setThreatLevel(ThreatLevel threatLevel) {
        this.threatLevel = threatLevel;
    }
}
