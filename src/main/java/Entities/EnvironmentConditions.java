package Entities;

import Entities.Enums.Visibility;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
@JsonPropertyOrder({"weather", "timeOfDay", "visibility", "cursedEnergyDensity"})
public class EnvironmentConditions {
    @Column(name = "environment_weather")
    private String weather;

    @Column(name = "environment_time_of_day")
    private String timeOfDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "environment_visibility")
    private Visibility visibility;

    @Column(name = "environment_cursed_energy_density")
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
