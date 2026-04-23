package Archive.Service;

import Archive.Dto.MissionSummaryResponse;
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

    public String getMissionReport(String missionId, String reportType) {
        Mission mission = getMission(missionId);
        String resolvedReportType = resolveReportType(reportType);

        try {
            IReportFormat reportFormat = reportFormatFactory.createReportFormat(resolvedReportType);
            return reportFormat.render(mission);
        } catch(Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
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

    private String resolveReportType(String reportType) {
        if(reportType == null || reportType.isBlank()) {
            return reportFormatFactory.getDefaultReportType();
        }

        return reportType;
    }

    private Path createTempFile(String format) throws IOException {
        String suffix = format.isBlank() ? "" : "." + format;
        return Files.createTempFile("mission-import-", suffix);
    }

    private void deleteTempFile(Path tempFile) {
        if(tempFile == null) {
            return;
        }

        try {
            Files.deleteIfExists(tempFile);
        } catch(IOException ignored) {
        }
    }
}
