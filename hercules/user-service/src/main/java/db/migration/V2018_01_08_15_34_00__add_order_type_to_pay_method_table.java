package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class V2018_01_08_15_34_00__add_order_type_to_pay_method_table implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        ZoneId zone = ZoneId.of("Etc/UTC");

        jdbcTemplate.execute("ALTER TABLE pay_method ADD COLUMN `order_type` VARCHAR(36) AFTER `name`;");

        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT `enterprise_id`, `created_by` FROM `pay_method`;");
        list.forEach(e -> {
            String now = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(zone).format(Instant.now());
            String enterpriseId = (String) e.get("enterprise_id");
            String createdBy = (String) e.get("created_by");
            jdbcTemplate.execute("DELETE FROM pay_method WHERE enterprise_id = " + "\'" + enterpriseId + "\'");
            jdbcTemplate.execute("INSERT INTO pay_method (`id`, `name`, `order_type`, `enterprise_id`, `created_at`, `updated_at`, `created_by`, `updated_by`, `version`)"
                    + " VALUES (\'" + UUID.randomUUID().toString() + "\', "
                    + "\'" + "ONLINE" + "\',"
                    + "\'" + "WMS" + "\',"
                    + "\'" + enterpriseId + "\',"
                    + "\'" + now + "\',"
                    + "\'" + now + "\',"
                    + "\'" + createdBy + "\',"
                    + "\'" + createdBy + "\',"
                    + "\'" + 0 + "\')");
            jdbcTemplate.execute("INSERT INTO pay_method (`id`, `name`, `order_type`, `enterprise_id`, `created_at`, `updated_at`, `created_by`, `updated_by`, `version`)"
                    + " VALUES (\'" + UUID.randomUUID().toString() + "\', "
                    + "\'" + "OFFLINE" + "\',"
                    + "\'" + "WMS" + "\',"
                    + "\'" + enterpriseId + "\',"
                    + "\'" + now + "\',"
                    + "\'" + now + "\',"
                    + "\'" + createdBy + "\',"
                    + "\'" + createdBy + "\',"
                    + "\'" + 0 + "\')");
            jdbcTemplate.execute("INSERT INTO pay_method (`id`, `name`, `order_type`, `enterprise_id`, `created_at`, `updated_at`, `created_by`, `updated_by`, `version`)"
                    + " VALUES (\'" + UUID.randomUUID().toString() + "\', "
                    + "\'" + "ONLINE" + "\',"
                    + "\'" + "ACG" + "\',"
                    + "\'" + enterpriseId + "\',"
                    + "\'" + now + "\',"
                    + "\'" + now + "\',"
                    + "\'" + createdBy + "\',"
                    + "\'" + createdBy + "\',"
                    + "\'" + 0 + "\')");
            jdbcTemplate.execute("INSERT INTO pay_method (`id`, `name`, `order_type`, `enterprise_id`, `created_at`, `updated_at`, `created_by`, `updated_by`, `version`)"
                    + " VALUES (\'" + UUID.randomUUID().toString() + "\', "
                    + "\'" + "ONLINE" + "\',"
                    + "\'" + "MWP" + "\',"
                    + "\'" + enterpriseId + "\',"
                    + "\'" + now + "\',"
                    + "\'" + now + "\',"
                    + "\'" + createdBy + "\',"
                    + "\'" + createdBy + "\',"
                    + "\'" + 0 + "\')");
        });

    }
}
