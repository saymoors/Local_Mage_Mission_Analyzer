package Archive.Service;

import Entities.Mission;
import Validation.IValidator;
import Validation.ValidatorFactory;
import Archive.Dto.MissionSummaryResponse;
import Archive.Repository.MissionArchiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MissionArchiveService {
    private final MissionArchiveRepository repository;
    private final ValidatorFactory validatorFactory = new ValidatorFactory();

    public MissionArchiveService(MissionArchiveRepository repository) {
        this.repository = repository;
    }

    public List<MissionSummaryResponse> getArchive() {
        List<Mission> missions = repository.findAll();
        List<MissionSummaryResponse> archive = new ArrayList<>();

        for(Mission mission : missions) {
            archive.add(MissionSummaryResponse.fromMission(mission));
        }

        return archive;
    }

    public Mission getMission(String missionId) {
        Mission mission = repository.findByMissionId(missionId);

        if(mission == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Миссия с идентификатором \"" + missionId + "\" не найдена в архиве"
            );
        }

        return mission;
    }

    public Mission saveMission(Mission mission) {
        validateMission(mission);

        return repository.save(mission);
    }

    private void validateMission(Mission mission) {
        if(mission == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Тело запроса с миссией отсутствует");
        }

        IValidator validator = validatorFactory.createValidationChain();

        try {
            validator.validate(mission);
            mission.linkEntities();
        } catch(Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }
}
