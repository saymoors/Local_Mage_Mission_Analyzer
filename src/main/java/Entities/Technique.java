package Entities;

import Entities.Enums.TechniqueType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
@JsonPropertyOrder({"name", "type", "owner", "damage"})
public class Technique {
    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TechniqueType type;

    @Column(name = "owner")
    private String owner;

    @Column(name = "damage")
    private int damage;

    public Technique() { }

    public Technique(String name, TechniqueType type, String owner, int damage) {
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TechniqueType getType() {
        return type;
    }

    public void setType(TechniqueType type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
