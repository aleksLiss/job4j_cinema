package ru.job4j.cinema.configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sql2o.Sql2o;
import org.sql2o.converters.ConverterException;
import org.sql2o.quirks.NoQuirks;
import org.sql2o.quirks.Quirks;

import javax.sql.DataSource;
import java.time.LocalDateTime;

import org.sql2o.converters.Converter;

import java.sql.Timestamp;

@Configuration
public class DatasourceConfiguration {

    @Bean
    public DataSource connectionPool(@Value("${datasource.url}") String url,
                                     @Value("${datasource.username}") String userName,
                                     @Value("${datasource.password}") String password) {
        return new BasicDataSource() {
            {
                setUrl(url);
                setUsername(userName);
                setPassword(password);
            }
        };
    }

    @Bean
    public Sql2o databaseClient(DataSource dataSource) {
        return new Sql2o(dataSource, createConverters());
    }

    private Quirks createConverters() {
        return new NoQuirks() {
            {
                converters.put(LocalDateTime.class, new Converter<LocalDateTime>() {

                    @Override
                    public LocalDateTime convert(Object value) throws ConverterException {
                        if (value == null) {
                            return null;
                        }
                        if (!(value instanceof Timestamp)) {
                            throw new ConverterException("Invalid value to convert");
                        }
                        return ((Timestamp) value).toLocalDateTime();
                    }

                    @Override
                    public Object toDatabaseParam(LocalDateTime value) {
                        return value == null ? null : Timestamp.valueOf(value);
                    }

                });
            }
        };
    }
}