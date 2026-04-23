package Archive.Dto;

public record MissionSummaryResponse(
        String missionId,
        String date,
        String location,
        String outcome
) {
}
