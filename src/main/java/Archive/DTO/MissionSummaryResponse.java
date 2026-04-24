package Archive.DTO;

import Entities.Mission;

public record MissionSummaryResponse(
        String missionId,
        String date,
        String location,
        String outcome
) {
    public static MissionSummaryResponse fromMission(Mission mission) {
        return new MissionSummaryResponse(
                mission.getMissionId(),
                mission.getDate(),
                mission.getLocation(),
                mission.getOutcome().name()
        );
    }
}
