package Archive.Repository;

import Entities.CivilianImpact;
import Entities.Curse;
import Entities.EconomicAssessment;
import Entities.EnemyActivity;
import Entities.EnvironmentConditions;
import Entities.Mission;
import Entities.OperationTimelineEvent;
import Entities.Sorcerer;
import Entities.Technique;
import Entities.Enums.EscalationRisk;
import Entities.Enums.Mobility;
import Entities.Enums.Outcome;
import Entities.Enums.PublicExposureRisk;
import Entities.Enums.SorcererRank;
import Entities.Enums.TechniqueType;
import Entities.Enums.ThreatLevel;
import Entities.Enums.Visibility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMissionArchiveRepository implements MissionArchiveRepository {
    private final String jdbcUrl;
    private final String jdbcUsername;
    private final String jdbcPassword;

    public JdbcMissionArchiveRepository(
            @Value("${archive.jdbc.url}") String jdbcUrl,
            @Value("${archive.jdbc.username}") String jdbcUsername,
            @Value("${archive.jdbc.password}") String jdbcPassword
    ) {
        this.jdbcUrl = jdbcUrl;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    @Override
    public Mission save(Mission mission) {
        String missionId = mission.getMissionId();

        try(Connection connection = openConnection()) {
            connection.setAutoCommit(false);

            try {
                boolean exists = findMission(connection, missionId).isPresent();

                if(exists) {
                    updateMissionRow(connection, mission);
                } else {
                    insertMissionRow(connection, mission);
                }

                replaceCollections(connection, mission);
                connection.commit();

                return findMission(connection, missionId)
                        .orElseThrow(() -> new IllegalStateException("Миссия не найдена после сохранения в PostgreSQL"));
            } catch(SQLException exception) {
                connection.rollback();
                throw exception;
            }
        } catch(SQLException exception) {
            throw new IllegalStateException("Не удалось сохранить миссию в PostgreSQL", exception);
        }
    }

    @Override
    public Optional<Mission> findByMissionId(String missionId) {
        try(Connection connection = openConnection()) {
            return findMission(connection, missionId);
        } catch(SQLException exception) {
            throw new IllegalStateException("Не удалось получить миссию из PostgreSQL", exception);
        }
    }

    @Override
    public List<Mission> findAll() {
        String sql = "SELECT mission_id FROM missions ORDER BY mission_id";

        try(Connection connection = openConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
            List<String> missionIds = new ArrayList<>();

            while(resultSet.next()) {
                missionIds.add(resultSet.getString("mission_id"));
            }

            List<Mission> missions = new ArrayList<>();

            for(String missionId : missionIds) {
                findMission(connection, missionId).ifPresent(missions::add);
            }

            return missions;
        } catch(SQLException exception) {
            throw new IllegalStateException("Не удалось получить архив миссий из PostgreSQL", exception);
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
    }

    private void insertMissionRow(Connection connection, Mission mission) throws SQLException {
        String sql = """
                INSERT INTO missions (
                    mission_id,
                    mission_date,
                    location,
                    outcome,
                    damage_cost,
                    curse_name,
                    curse_threat_level,
                    economic_total_damage_cost,
                    economic_infrastructure_damage,
                    economic_transport_damage,
                    economic_commercial_damage,
                    economic_recovery_estimate_days,
                    economic_insurance_covered,
                    enemy_behavior_type,
                    enemy_target_priority,
                    enemy_mobility,
                    enemy_escalation_risk,
                    environment_weather,
                    environment_time_of_day,
                    environment_visibility,
                    environment_cursed_energy_density,
                    civilian_evacuated,
                    civilian_injured,
                    civilian_missing,
                    civilian_public_exposure_risk,
                    notes,
                    comment
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            fillMissionStatement(statement, mission, false);
            statement.executeUpdate();
        }
    }

    private void updateMissionRow(Connection connection, Mission mission) throws SQLException {
        String sql = """
                UPDATE missions
                SET mission_date = ?,
                    location = ?,
                    outcome = ?,
                    damage_cost = ?,
                    curse_name = ?,
                    curse_threat_level = ?,
                    economic_total_damage_cost = ?,
                    economic_infrastructure_damage = ?,
                    economic_transport_damage = ?,
                    economic_commercial_damage = ?,
                    economic_recovery_estimate_days = ?,
                    economic_insurance_covered = ?,
                    enemy_behavior_type = ?,
                    enemy_target_priority = ?,
                    enemy_mobility = ?,
                    enemy_escalation_risk = ?,
                    environment_weather = ?,
                    environment_time_of_day = ?,
                    environment_visibility = ?,
                    environment_cursed_energy_density = ?,
                    civilian_evacuated = ?,
                    civilian_injured = ?,
                    civilian_missing = ?,
                    civilian_public_exposure_risk = ?,
                    notes = ?,
                    comment = ?
                WHERE mission_id = ?
                """;

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            fillMissionStatement(statement, mission, true);
            statement.executeUpdate();
        }
    }

    private void fillMissionStatement(
            PreparedStatement statement,
            Mission mission,
            boolean updateMode
    ) throws SQLException {
        Curse curse = mission.getCurse();
        EconomicAssessment economicAssessment = mission.getEconomicAssessment();
        EnemyActivity enemyActivity = mission.getEnemyActivity();
        EnvironmentConditions environmentConditions = mission.getEnvironmentConditions();
        CivilianImpact civilianImpact = mission.getCivilianImpact();

        int index = 1;

        if(!updateMode) {
            statement.setString(index++, mission.getMissionId());
        }

        statement.setString(index++, mission.getDate());
        statement.setString(index++, mission.getLocation());
        statement.setString(index++, enumName(mission.getOutcome()));
        statement.setInt(index++, mission.getDamageCost());
        statement.setString(index++, curse == null ? null : curse.getName());
        statement.setString(index++, curse == null ? null : enumName(curse.getThreatLevel()));
        setInteger(statement, index++, economicAssessment == null ? null : economicAssessment.getTotalDamageCost());
        setInteger(statement, index++, economicAssessment == null ? null : economicAssessment.getInfrastructureDamage());
        setInteger(statement, index++, economicAssessment == null ? null : economicAssessment.getTransportDamage());
        setInteger(statement, index++, economicAssessment == null ? null : economicAssessment.getCommercialDamage());
        setInteger(statement, index++, economicAssessment == null ? null : economicAssessment.getRecoveryEstimateDays());
        setBoolean(statement, index++, economicAssessment == null ? null : economicAssessment.getInsuranceCovered());
        statement.setString(index++, enemyActivity == null ? null : enemyActivity.getBehaviorType());
        statement.setString(index++, enemyActivity == null ? null : enemyActivity.getTargetPriority());
        statement.setString(index++, enemyActivity == null ? null : enumName(enemyActivity.getMobility()));
        statement.setString(index++, enemyActivity == null ? null : enumName(enemyActivity.getEscalationRisk()));
        statement.setString(index++, environmentConditions == null ? null : environmentConditions.getWeather());
        statement.setString(index++, environmentConditions == null ? null : environmentConditions.getTimeOfDay());
        statement.setString(index++, environmentConditions == null ? null : enumName(environmentConditions.getVisibility()));
        setInteger(statement, index++, environmentConditions == null ? null : environmentConditions.getCursedEnergyDensity());
        setInteger(statement, index++, civilianImpact == null ? null : civilianImpact.getEvacuated());
        setInteger(statement, index++, civilianImpact == null ? null : civilianImpact.getInjured());
        setInteger(statement, index++, civilianImpact == null ? null : civilianImpact.getMissing());
        statement.setString(index++, civilianImpact == null ? null : enumName(civilianImpact.getPublicExposureRisk()));
        statement.setString(index++, mission.getNotes());
        statement.setString(index++, mission.getComment());

        if(updateMode) {
            statement.setString(index, mission.getMissionId());
        }
    }

    private void replaceCollections(Connection connection, Mission mission) throws SQLException {
        String missionId = mission.getMissionId();

        deleteCollection(connection, "mission_sorcerers", missionId);
        deleteCollection(connection, "mission_techniques", missionId);
        deleteCollection(connection, "mission_timeline_events", missionId);
        deleteCollection(connection, "mission_operation_tags", missionId);
        deleteCollection(connection, "mission_support_units", missionId);
        deleteCollection(connection, "mission_recommendations", missionId);
        deleteCollection(connection, "mission_artifacts_recovered", missionId);
        deleteCollection(connection, "mission_evacuation_zones", missionId);
        deleteCollection(connection, "mission_status_effects", missionId);
        deleteCollection(connection, "mission_enemy_attack_patterns", missionId);
        deleteCollection(connection, "mission_enemy_countermeasures_used", missionId);

        insertSorcerers(connection, missionId, mission.getSorcerers());
        insertTechniques(connection, missionId, mission.getTechniques());
        insertTimelineEvents(connection, missionId, mission.getOperationTimeline());
        insertStringList(connection, "mission_operation_tags", missionId, mission.getOperationTags());
        insertStringList(connection, "mission_support_units", missionId, mission.getSupportUnits());
        insertStringList(connection, "mission_recommendations", missionId, mission.getRecommendations());
        insertStringList(connection, "mission_artifacts_recovered", missionId, mission.getArtifactsRecovered());
        insertStringList(connection, "mission_evacuation_zones", missionId, mission.getEvacuationZones());
        insertStringList(connection, "mission_status_effects", missionId, mission.getStatusEffects());

        EnemyActivity enemyActivity = mission.getEnemyActivity();
        if(enemyActivity != null) {
            insertStringList(connection, "mission_enemy_attack_patterns", missionId, enemyActivity.getAttackPatterns());
            insertStringList(
                    connection,
                    "mission_enemy_countermeasures_used",
                    missionId,
                    enemyActivity.getCountermeasuresUsed()
            );
        }
    }

    private void deleteCollection(Connection connection, String tableName, String missionId) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE mission_id = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, missionId);
            statement.executeUpdate();
        }
    }

    private void insertSorcerers(Connection connection, String missionId, List<Sorcerer> sorcerers) throws SQLException {
        if(sorcerers == null || sorcerers.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO mission_sorcerers (mission_id, order_index, name, rank) VALUES (?, ?, ?, ?)";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            for(int index = 0; index < sorcerers.size(); index++) {
                Sorcerer sorcerer = sorcerers.get(index);
                statement.setString(1, missionId);
                statement.setInt(2, index);
                statement.setString(3, sorcerer == null ? null : sorcerer.getName());
                statement.setString(4, sorcerer == null ? null : enumName(sorcerer.getRank()));
                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    private void insertTechniques(Connection connection, String missionId, List<Technique> techniques) throws SQLException {
        if(techniques == null || techniques.isEmpty()) {
            return;
        }

        String sql = """
                INSERT INTO mission_techniques (mission_id, order_index, name, type, owner, damage)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            for(int index = 0; index < techniques.size(); index++) {
                Technique technique = techniques.get(index);
                statement.setString(1, missionId);
                statement.setInt(2, index);
                statement.setString(3, technique == null ? null : technique.getName());
                statement.setString(4, technique == null ? null : enumName(technique.getType()));
                statement.setString(5, technique == null ? null : technique.getOwner());
                statement.setInt(6, technique == null ? 0 : technique.getDamage());
                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    private void insertTimelineEvents(
            Connection connection,
            String missionId,
            List<OperationTimelineEvent> operationTimeline
    ) throws SQLException {
        if(operationTimeline == null || operationTimeline.isEmpty()) {
            return;
        }

        String sql = """
                INSERT INTO mission_timeline_events (mission_id, order_index, event_timestamp, event_type, description)
                VALUES (?, ?, ?, ?, ?)
                """;

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            for(int index = 0; index < operationTimeline.size(); index++) {
                OperationTimelineEvent event = operationTimeline.get(index);
                statement.setString(1, missionId);
                statement.setInt(2, index);
                statement.setString(3, event == null ? null : event.getTimestamp());
                statement.setString(4, event == null ? null : event.getType());
                statement.setString(5, event == null ? null : event.getDescription());
                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    private void insertStringList(
            Connection connection,
            String tableName,
            String missionId,
            List<String> values
    ) throws SQLException {
        if(values == null || values.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO " + tableName + " (mission_id, order_index, item_value) VALUES (?, ?, ?)";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            for(int index = 0; index < values.size(); index++) {
                statement.setString(1, missionId);
                statement.setInt(2, index);
                statement.setString(3, values.get(index));
                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    private Optional<Mission> findMission(Connection connection, String missionId) throws SQLException {
        String sql = "SELECT * FROM missions WHERE mission_id = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, missionId);

            try(ResultSet resultSet = statement.executeQuery()) {
                if(!resultSet.next()) {
                    return Optional.empty();
                }

                Mission mission = new Mission();
                mission.setMissionId(resultSet.getString("mission_id"));
                mission.setDate(resultSet.getString("mission_date"));
                mission.setLocation(resultSet.getString("location"));
                mission.setOutcome(parseEnum(resultSet.getString("outcome"), Outcome.class));
                mission.setDamageCost(resultSet.getInt("damage_cost"));
                mission.setCurse(mapCurse(resultSet));
                mission.setEconomicAssessment(mapEconomicAssessment(resultSet));
                mission.setEnemyActivity(mapEnemyActivity(resultSet, connection, missionId));
                mission.setEnvironmentConditions(mapEnvironmentConditions(resultSet));
                mission.setCivilianImpact(mapCivilianImpact(resultSet));
                mission.setNotes(resultSet.getString("notes"));
                mission.setComment(resultSet.getString("comment"));
                mission.setSorcerers(loadSorcerers(connection, missionId));
                mission.setTechniques(loadTechniques(connection, missionId));
                mission.setOperationTimeline(loadTimelineEvents(connection, missionId));
                mission.setOperationTags(loadStringList(connection, "mission_operation_tags", missionId));
                mission.setSupportUnits(loadStringList(connection, "mission_support_units", missionId));
                mission.setRecommendations(loadStringList(connection, "mission_recommendations", missionId));
                mission.setArtifactsRecovered(loadStringList(connection, "mission_artifacts_recovered", missionId));
                mission.setEvacuationZones(loadStringList(connection, "mission_evacuation_zones", missionId));
                mission.setStatusEffects(loadStringList(connection, "mission_status_effects", missionId));

                try {
                    mission.linkEntities();
                } catch(Exception exception) {
                    throw new IllegalStateException("Сохраненная миссия в PostgreSQL имеет несогласованные данные", exception);
                }

                return Optional.of(mission);
            }
        }
    }

    private Curse mapCurse(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("curse_name");
        String threatLevel = resultSet.getString("curse_threat_level");

        if(name == null && threatLevel == null) {
            return null;
        }

        Curse curse = new Curse();
        curse.setName(name);
        curse.setThreatLevel(parseEnum(threatLevel, ThreatLevel.class));
        return curse;
    }

    private EconomicAssessment mapEconomicAssessment(ResultSet resultSet) throws SQLException {
        if(allNull(
                resultSet,
                "economic_total_damage_cost",
                "economic_infrastructure_damage",
                "economic_transport_damage",
                "economic_commercial_damage",
                "economic_recovery_estimate_days",
                "economic_insurance_covered"
        )) {
            return null;
        }

        EconomicAssessment assessment = new EconomicAssessment();
        assessment.setTotalDamageCost(getNullableInt(resultSet, "economic_total_damage_cost"));
        assessment.setInfrastructureDamage(getNullableInt(resultSet, "economic_infrastructure_damage"));
        assessment.setTransportDamage(getNullableInt(resultSet, "economic_transport_damage"));
        assessment.setCommercialDamage(getNullableInt(resultSet, "economic_commercial_damage"));
        assessment.setRecoveryEstimateDays(getNullableInt(resultSet, "economic_recovery_estimate_days"));
        assessment.setInsuranceCovered(Boolean.TRUE.equals((Boolean) resultSet.getObject("economic_insurance_covered")));
        return assessment;
    }

    private EnemyActivity mapEnemyActivity(ResultSet resultSet, Connection connection, String missionId) throws SQLException {
        List<String> attackPatterns = loadStringList(connection, "mission_enemy_attack_patterns", missionId);
        List<String> countermeasuresUsed = loadStringList(connection, "mission_enemy_countermeasures_used", missionId);

        if(allNull(
                resultSet,
                "enemy_behavior_type",
                "enemy_target_priority",
                "enemy_mobility",
                "enemy_escalation_risk"
        ) && attackPatterns == null && countermeasuresUsed == null) {
            return null;
        }

        EnemyActivity enemyActivity = new EnemyActivity();
        enemyActivity.setBehaviorType(resultSet.getString("enemy_behavior_type"));
        enemyActivity.setTargetPriority(resultSet.getString("enemy_target_priority"));
        enemyActivity.setMobility(parseEnum(resultSet.getString("enemy_mobility"), Mobility.class));
        enemyActivity.setEscalationRisk(parseEnum(resultSet.getString("enemy_escalation_risk"), EscalationRisk.class));
        enemyActivity.setAttackPatterns(attackPatterns);
        enemyActivity.setCountermeasuresUsed(countermeasuresUsed);
        return enemyActivity;
    }

    private EnvironmentConditions mapEnvironmentConditions(ResultSet resultSet) throws SQLException {
        if(allNull(
                resultSet,
                "environment_weather",
                "environment_time_of_day",
                "environment_visibility",
                "environment_cursed_energy_density"
        )) {
            return null;
        }

        EnvironmentConditions conditions = new EnvironmentConditions();
        conditions.setWeather(resultSet.getString("environment_weather"));
        conditions.setTimeOfDay(resultSet.getString("environment_time_of_day"));
        conditions.setVisibility(parseEnum(resultSet.getString("environment_visibility"), Visibility.class));
        conditions.setCursedEnergyDensity(getNullableInt(resultSet, "environment_cursed_energy_density"));
        return conditions;
    }

    private CivilianImpact mapCivilianImpact(ResultSet resultSet) throws SQLException {
        if(allNull(
                resultSet,
                "civilian_evacuated",
                "civilian_injured",
                "civilian_missing",
                "civilian_public_exposure_risk"
        )) {
            return null;
        }

        CivilianImpact impact = new CivilianImpact();
        impact.setEvacuated(getNullableInt(resultSet, "civilian_evacuated"));
        impact.setInjured(getNullableInt(resultSet, "civilian_injured"));
        impact.setMissing(getNullableInt(resultSet, "civilian_missing"));
        impact.setPublicExposureRisk(
                parseEnum(resultSet.getString("civilian_public_exposure_risk"), PublicExposureRisk.class)
        );
        return impact;
    }

    private List<Sorcerer> loadSorcerers(Connection connection, String missionId) throws SQLException {
        String sql = "SELECT * FROM mission_sorcerers WHERE mission_id = ? ORDER BY order_index";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, missionId);

            try(ResultSet resultSet = statement.executeQuery()) {
                List<Sorcerer> sorcerers = new ArrayList<>();

                while(resultSet.next()) {
                    Sorcerer sorcerer = new Sorcerer();
                    sorcerer.setName(resultSet.getString("name"));
                    sorcerer.setRank(parseEnum(resultSet.getString("rank"), SorcererRank.class));
                    sorcerers.add(sorcerer);
                }

                return sorcerers.isEmpty() ? null : sorcerers;
            }
        }
    }

    private List<Technique> loadTechniques(Connection connection, String missionId) throws SQLException {
        String sql = "SELECT * FROM mission_techniques WHERE mission_id = ? ORDER BY order_index";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, missionId);

            try(ResultSet resultSet = statement.executeQuery()) {
                List<Technique> techniques = new ArrayList<>();

                while(resultSet.next()) {
                    Technique technique = new Technique();
                    technique.setName(resultSet.getString("name"));
                    technique.setType(parseEnum(resultSet.getString("type"), TechniqueType.class));
                    technique.setOwner(resultSet.getString("owner"));
                    technique.setDamage(resultSet.getInt("damage"));
                    techniques.add(technique);
                }

                return techniques.isEmpty() ? null : techniques;
            }
        }
    }

    private List<OperationTimelineEvent> loadTimelineEvents(Connection connection, String missionId) throws SQLException {
        String sql = "SELECT * FROM mission_timeline_events WHERE mission_id = ? ORDER BY order_index";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, missionId);

            try(ResultSet resultSet = statement.executeQuery()) {
                List<OperationTimelineEvent> events = new ArrayList<>();

                while(resultSet.next()) {
                    OperationTimelineEvent event = new OperationTimelineEvent();
                    event.setTimestamp(resultSet.getString("event_timestamp"));
                    event.setType(resultSet.getString("event_type"));
                    event.setDescription(resultSet.getString("description"));
                    events.add(event);
                }

                return events.isEmpty() ? null : events;
            }
        }
    }

    private List<String> loadStringList(Connection connection, String tableName, String missionId) throws SQLException {
        String sql = "SELECT item_value FROM " + tableName + " WHERE mission_id = ? ORDER BY order_index";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, missionId);

            try(ResultSet resultSet = statement.executeQuery()) {
                List<String> values = new ArrayList<>();

                while(resultSet.next()) {
                    values.add(resultSet.getString("item_value"));
                }

                return values.isEmpty() ? null : values;
            }
        }
    }

    private boolean allNull(ResultSet resultSet, String... columns) throws SQLException {
        for(String column : columns) {
            if(resultSet.getObject(column) != null) {
                return false;
            }
        }

        return true;
    }

    private int getNullableInt(ResultSet resultSet, String column) throws SQLException {
        Object value = resultSet.getObject(column);
        return value == null ? 0 : ((Number) value).intValue();
    }

    private void setInteger(PreparedStatement statement, int index, Integer value) throws SQLException {
        if(value == null) {
            statement.setObject(index, null);
        } else {
            statement.setInt(index, value);
        }
    }

    private void setBoolean(PreparedStatement statement, int index, Boolean value) throws SQLException {
        if(value == null) {
            statement.setObject(index, null);
        } else {
            statement.setBoolean(index, value);
        }
    }

    private String enumName(Enum<?> value) {
        return value == null ? null : value.name();
    }

    private <E extends Enum<E>> E parseEnum(String value, Class<E> enumClass) {
        if(value == null || value.isBlank()) {
            return null;
        }

        return Enum.valueOf(enumClass, value);
    }
}
