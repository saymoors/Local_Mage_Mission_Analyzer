package Archive.Controller;

import Archive.Dto.MissionSummaryResponse;
import Archive.Service.MissionArchiveService;
import Entities.Mission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/missions")
@Tag(name = "Missions", description = "Операции с миссиями")
public class MissionController {
    private final MissionArchiveService missionArchiveService;

    public MissionController(MissionArchiveService missionArchiveService) {
        this.missionArchiveService = missionArchiveService;
    }

    @GetMapping
    @Operation(
            summary = "Получить список миссий",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список миссий получен")
            }
    )
    public List<MissionSummaryResponse> getArchive() {
        return missionArchiveService.getArchive();
    }

    @PostMapping
    @Operation(
            summary = "Сохранить миссию",
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

    @GetMapping("/{missionId}")
    @Operation(
            summary = "Получить миссию",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Миссия найдена"),
                    @ApiResponse(responseCode = "404", description = "Миссия не найдена")
            }
    )
    public Mission getMission(@PathVariable String missionId) {
        return missionArchiveService.getMission(missionId);
    }

    @PatchMapping("/{missionId}")
    @Operation(
            summary = "Частично обновить миссию",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\n  \"damageCost\": 1100000\n}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Миссия обновлена"),
                    @ApiResponse(responseCode = "400", description = "Ошибка частичного обновления"),
                    @ApiResponse(responseCode = "404", description = "Миссия не найдена")
            }
    )
    public Mission patchMission(@PathVariable String missionId, @RequestBody JsonNode patchData) {
        return missionArchiveService.patchMission(missionId, patchData);
    }

    @GetMapping(value = "/{missionId}/textreport", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Сформировать отчет по миссии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отчет сформирован"),
                    @ApiResponse(responseCode = "400", description = "Неизвестный тип отчета"),
                    @ApiResponse(responseCode = "404", description = "Миссия не найдена")
            }
    )
    public String getTextMissionReport(@PathVariable String missionId, @RequestParam String reportType) {
        return missionArchiveService.getMissionReport(missionId, reportType);
    }

    @GetMapping(value = "/{missionId}/filereport", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Сформировать файловый отчет по миссии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Файл отчета сформирован"),
                    @ApiResponse(responseCode = "400", description = "Неизвестный тип отчета"),
                    @ApiResponse(responseCode = "404", description = "Миссия не найдена")
            }
    )
    public ResponseEntity<byte[]> getFileMissionReport(@PathVariable String missionId, @RequestParam String reportType) {
        String report = missionArchiveService.getMissionReport(missionId, reportType);
        String fileName = "mission[" + missionId + "](" + reportType + ").txt";
        byte[] reportBytes = report.getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(new MediaType("text", "plain", StandardCharsets.UTF_8))
                .body(reportBytes);
    }
}
