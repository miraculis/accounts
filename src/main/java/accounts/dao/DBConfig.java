package accounts.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.clickhouse.ClickHouseDataSource;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

import java.util.concurrent.TimeUnit;

/**
 * Created by Светлана on 08.05.2017.
 */
@Configuration
public class DBConfig {
    @Value("${jdbc.host}")
    private String clickhouseHost;

    @Value("${jdbc.port}")
    private String clickhousePort;

    @Value("${jdbc.db}")
    private String clickhouseDatabase;

    @Bean
    public ClickHouseDataSource clickHouseDataSource() {
        final String url = "jdbc:clickhouse://" + clickhouseHost + ":" + clickhousePort + "/" + clickhouseDatabase;
        final ClickHouseProperties clickHouseProperties = new ClickHouseProperties();
        return new ClickHouseDataSource(url, clickHouseProperties);
    }

    @Bean
    public NamedParameterJdbcTemplate clickHouseNamedJdbcTemplate(ClickHouseDataSource clickHouseDataSource) {
        return new NamedParameterJdbcTemplate(clickHouseDataSource);
    }
}
