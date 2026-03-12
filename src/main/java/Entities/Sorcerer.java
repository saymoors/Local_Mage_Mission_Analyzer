package Entities;

import java.util.List;

public class Sorcerer {
    private String name;
    private String rank;
    private List<Technique> techniques;

    public Sorcerer() { }

    public Sorcerer(String name, String rank) {
        this.name = name;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
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
