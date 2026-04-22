package Entities;

import Entities.Enums.SorcererRank;

import java.util.List;

public class Sorcerer {
    private String name;
    private SorcererRank rank;
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
