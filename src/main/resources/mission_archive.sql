CREATE TABLE IF NOT EXISTS missions (
    mission_id VARCHAR(100) PRIMARY KEY,
    mission_date VARCHAR(100),
    location TEXT,
    outcome VARCHAR(50),
    damage_cost INTEGER NOT NULL DEFAULT 0,
    curse_name TEXT,
    curse_threat_level VARCHAR(50),
    economic_total_damage_cost INTEGER,
    economic_infrastructure_damage INTEGER,
    economic_transport_damage INTEGER,
    economic_commercial_damage INTEGER,
    economic_recovery_estimate_days INTEGER,
    economic_insurance_covered BOOLEAN,
    enemy_behavior_type TEXT,
    enemy_target_priority TEXT,
    enemy_mobility VARCHAR(50),
    enemy_escalation_risk VARCHAR(50),
    environment_weather TEXT,
    environment_time_of_day TEXT,
    environment_visibility VARCHAR(50),
    environment_cursed_energy_density INTEGER,
    civilian_evacuated INTEGER,
    civilian_injured INTEGER,
    civilian_missing INTEGER,
    civilian_public_exposure_risk VARCHAR(50),
    notes TEXT,
    comment TEXT
);

CREATE TABLE IF NOT EXISTS mission_sorcerers (
    mission_id VARCHAR(100) NOT NULL REFERENCES missions (mission_id) ON DELETE CASCADE,
    order_index INTEGER NOT NULL,
    name TEXT,
    rank VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS mission_techniques (
    mission_id VARCHAR(100) NOT NULL REFERENCES missions (mission_id) ON DELETE CASCADE,
    order_index INTEGER NOT NULL,
    name TEXT,
    type VARCHAR(50),
    owner TEXT,
    damage INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS mission_timeline_events (
    mission_id VARCHAR(100) NOT NULL REFERENCES missions (mission_id) ON DELETE CASCADE,
    order_index INTEGER NOT NULL,
    event_timestamp TEXT,
    event_type TEXT,
    description TEXT
);

CREATE TABLE IF NOT EXISTS mission_operation_tags (
    mission_id VARCHAR(100) NOT NULL REFERENCES missions (mission_id) ON DELETE CASCADE,
    order_index INTEGER NOT NULL,
    item_value TEXT
);

CREATE TABLE IF NOT EXISTS mission_support_units (
    mission_id VARCHAR(100) NOT NULL REFERENCES missions (mission_id) ON DELETE CASCADE,
    order_index INTEGER NOT NULL,
    item_value TEXT
);

CREATE TABLE IF NOT EXISTS mission_recommendations (
    mission_id VARCHAR(100) NOT NULL REFERENCES missions (mission_id) ON DELETE CASCADE,
    order_index INTEGER NOT NULL,
    item_value TEXT
);

CREATE TABLE IF NOT EXISTS mission_artifacts_recovered (
    mission_id VARCHAR(100) NOT NULL REFERENCES missions (mission_id) ON DELETE CASCADE,
    order_index INTEGER NOT NULL,
    item_value TEXT
);

CREATE TABLE IF NOT EXISTS mission_evacuation_zones (
    mission_id VARCHAR(100) NOT NULL REFERENCES missions (mission_id) ON DELETE CASCADE,
    order_index INTEGER NOT NULL,
    item_value TEXT
);

CREATE TABLE IF NOT EXISTS mission_status_effects (
    mission_id VARCHAR(100) NOT NULL REFERENCES missions (mission_id) ON DELETE CASCADE,
    order_index INTEGER NOT NULL,
    item_value TEXT
);

CREATE TABLE IF NOT EXISTS mission_enemy_attack_patterns (
    mission_id VARCHAR(100) NOT NULL REFERENCES missions (mission_id) ON DELETE CASCADE,
    order_index INTEGER NOT NULL,
    item_value TEXT
);

CREATE TABLE IF NOT EXISTS mission_enemy_countermeasures_used (
    mission_id VARCHAR(100) NOT NULL REFERENCES missions (mission_id) ON DELETE CASCADE,
    order_index INTEGER NOT NULL,
    item_value TEXT
);
