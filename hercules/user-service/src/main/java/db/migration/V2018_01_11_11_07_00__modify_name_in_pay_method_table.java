package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.ZoneId;

public class V2018_01_11_11_07_00__modify_name_in_pay_method_table implements SpringJdbcMigration {

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute("ALTER TABLE `pay_method` CHANGE COLUMN `name` `methods` VARCHAR(255) NOT NULL");
        jdbcTemplate.execute("DELETE FROM pay_method WHERE `order_type` = " + "\'" + "WMS" + "\'" + " AND `methods` = " + "\'" + "OFFLINE" + "\'");
        jdbcTemplate.execute("UPDATE pay_method SET `methods` = " + "\'" + "ONLINE,OFFLINE" + "\'" + " WHERE `order_type` = " + "\'" + "WMS" + "\'");
    }
}
