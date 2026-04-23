package Archive.Controller;

import Entities.Mission;
import Archive.Dto.MissionSummaryResponse;
import Archive.Service.MissionArchiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@Tag(name = "Missions")
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
}
