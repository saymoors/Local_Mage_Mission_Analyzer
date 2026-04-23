package Archive.Repository;

import Entities.Mission;

import java.util.List;

public interface MissionArchiveRepository {
    Mission save(Mission mission);

    Mission findByMissionId(String missionId);

    List<Mission> findAll();
}
