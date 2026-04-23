package Archive.Repository;

import Entities.Mission;

import java.util.List;
import java.util.Optional;

public interface MissionArchiveRepository {
    Mission save(Mission mission);

    Optional<Mission> findByMissionId(String missionId);

    List<Mission> findAll();
}
