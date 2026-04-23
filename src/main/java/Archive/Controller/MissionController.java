package Archive.Controller;

import Archive.Dto.MissionSummaryResponse;
import Archive.Service.MissionArchiveService;
import Entities.Mission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@Tag(name = "Missions", description = "Операции с миссиями в архиве")
public class MissionController {
    private final MissionArchiveService missionArchiveService;

    public MissionController(MissionArchiveService missionArchiveService) {
        this.missionArchiveService = missionArchiveService;
    }

    @GetMapping
    @Operation(
            summary = "Получить список миссий, которые уже лежат в архиве",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список миссий получен")
            }
    )
    public List<MissionSummaryResponse> getArchive() {
        return missionArchiveService.getArchive();
    }

    @GetMapping("/{missionId}")
    @Operation(
            summary = "Получить полную миссию из архива по missionId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Миссия найдена"),
                    @ApiResponse(responseCode = "404", description = "Миссия не найдена")
            }
    )
    public Mission getMission(@PathVariable String missionId) {
        return missionArchiveService.getMission(missionId);
    }

    @GetMapping(value = "/{missionId}/report", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Получить текстовый отчет по миссии из архива",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отчет сформирован"),
                    @ApiResponse(responseCode = "400", description = "Неизвестный тип отчета"),
                    @ApiResponse(responseCode = "404", description = "Миссия не найдена")
            }
    )
    public String getMissionReport(
            @PathVariable String missionId,
            @RequestParam(value = "type", required = false) String reportType
    ) {
        return missionArchiveService.getMissionReport(missionId, reportType);
    }

    @PostMapping
    @Operation(
            summary = "Сохранить миссию в архив",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Миссия сохранена"),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации миссии")
            }
    )
    public Mission saveMission(@RequestBody Mission mission) {
        return missionArchiveService.saveMission(mission);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Импортировать миссию из файла",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Миссия импортирована и сохранена"),
                    @ApiResponse(responseCode = "400", description = "Ошибка чтения или валидации файла")
            }
    )
    public Mission importMission(@RequestParam("file") MultipartFile file) {
        return missionArchiveService.importMission(file);
    }
}
