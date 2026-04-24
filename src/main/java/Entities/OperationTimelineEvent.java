package Entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@JsonPropertyOrder({"timestamp", "type", "description"})
public class OperationTimelineEvent {
    @Column(name = "event_timestamp")
    private String timestamp;

    @Column(name = "event_type")
    private String type;

    @Column(name = "description")
    private String description;

    public OperationTimelineEvent() { }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
