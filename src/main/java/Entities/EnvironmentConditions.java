package Entities;

import Entities.Enums.Visibility;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"weather", "timeOfDay", "visibility", "cursedEnergyDensity"})
public class EnvironmentConditions {
    private String weather;
    private String timeOfDay;
    private Visibility visibility;
    private int cursedEnergyDensity;

    public EnvironmentConditions() { }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public int getCursedEnergyDensity() {
        return cursedEnergyDensity;
    }

    public void setCursedEnergyDensity(int cursedEnergyDensity) {
        this.cursedEnergyDensity = cursedEnergyDensity;
    }
}
