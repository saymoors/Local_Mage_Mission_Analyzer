public class Curse {
    private String name;
    private String threatLevel;

    public Curse(String name, String threatLevel) {
        this.name = name;
        this.threatLevel = threatLevel;
    }

    public String getName() {
        return name;
    }

    public String getThreatLevel() {
        return threatLevel;
    }
}
