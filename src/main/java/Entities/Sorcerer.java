package Entities;

import Entities.Enums.SorcererRank;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;

import java.util.List;

@Embeddable
@JsonPropertyOrder({"name", "rank", "techniques"})
public class Sorcerer {
    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "rank")
    private SorcererRank rank;

    @Transient
    private List<Technique> techniques;

    public Sorcerer() { }

    public Sorcerer(String name, SorcererRank rank) {
        this.name = name;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SorcererRank getRank() {
        return rank;
    }

    public void setRank(SorcererRank rank) {
        this.rank = rank;
    }

    public List<Technique> getTechniques() {
        return techniques;
    }

    public void setTechniques(List<Technique> techniques) {
        this.techniques = techniques;
    }

    public void addTechnique(Technique technique) {
        techniques.add(technique);
    }
}
