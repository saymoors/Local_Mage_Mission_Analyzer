package Archive.Service;

import Archive.DTO.MissionSummaryResponse;
import Archive.Repository.MissionArchiveRepository;
import Entities.Mission;
import Parsers.IParser;
import Parsers.ParserFactory;
import Reports.IReportFormat;
import Reports.ReportFormatFactory;
import Validation.IValidator;
import Validation.ValidatorFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class MissionArchiveService {
    private final MissionArchiveRepository repository;
    private final ValidatorFactory validatorFactory = new ValidatorFactory();
    private final ParserFactory parserFactory = new ParserFactory();
    private final ReportFormatFactory reportFormatFactory = new ReportFormatFactory();
    private final ObjectMapper objectMapper = new ObjectMapper();

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

    public Mission saveMission(Mission mission) {
        validateMission(mission);
        return repository.save(mission);
    }

    public Mission importMission(MultipartFile file) {
        if(file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Файл миссии отсутствует");
        }

        String format = resolveFormat(file.getOriginalFilename());
        Path tempFile = null;

        try {
            tempFile = createTempFile(format);
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            IParser parser = parserFactory.createParser(format);
            Mission mission = parser.parse(tempFile.toString());
            return saveMission(mission);
        } catch(Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        } finally {
            deleteTempFile(tempFile);
        }
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

    public Mission patchMission(String missionId, JsonNode patchData) {
        Mission mission = repository.findByMissionId(missionId);

        if(mission == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Миссия с идентификатором \"" + missionId + "\" не найдена в архиве");
        }

        if(patchData == null || patchData.isNull() || !patchData.isObject()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Тело PATCH-запроса должно быть JSON-объектом");
        }

        try {
            Mission patchedMission = objectMapper.readerForUpdating(mission).readValue(patchData);
            patchedMission.setMissionId(missionId);
            return saveMission(patchedMission);
        } catch(Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    public String getMissionReport(String missionId, String reportType) {
        Mission mission = getMission(missionId);

        if(reportType == null || reportType.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Параметр type обязателен");
        }

        try {
            IReportFormat reportFormat = reportFormatFactory.createReportFormat(reportType.trim());
            return reportFormat.render(mission);
        } catch(Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    private void validateMission(Mission mission) {
        IValidator validator = validatorFactory.createValidationChain();

        try {
            validator.validate(mission);
            mission.linkEntities();
        } catch(Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
    }

    private String resolveFormat(String originalFilename) {
        if(originalFilename == null || originalFilename.isBlank()) {
            return "";
        }

        int dotIndex = originalFilename.lastIndexOf('.');

        if(dotIndex < 0) {
            return "";
        }

        return originalFilename.substring(dotIndex + 1);
    }

    private Path createTempFile(String format) throws IOException {
        String suffix;

        if(format.isBlank()) {
            suffix = "";
        } else {
            suffix = "." + format;
        }

        return Files.createTempFile("mission", suffix);
    }

    private void deleteTempFile(Path tempFile) {
        if(tempFile == null) {
            return;
        }

        try {
            Files.delete(tempFile);
        } catch(IOException _) {
        }
    }
}
