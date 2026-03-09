public class Technique {
    private String name;
    private String type;
    private String owner;
    private int damage;

    public Technique(String name, String type, String owner, int damage) {
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getOwner() {
        return owner;
    }

    public int getDamage() {
        return damage;
    }
}
