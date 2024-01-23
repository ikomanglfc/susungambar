package com.triyakom.susun.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.triyakom.susun.service.Hashids;
import com.triyakom.susun.shorter.Encrypt;
import com.triyakom.susun.shorter.Json3DesEncrypt;
import com.triyakom.susun.shorter.ParseShortUrlData;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableWebMvc
@Configuration
@ComponentScan({
        "com.triyakom.susun.controller",
        "com.triyakom.susun.repo",
        "com.triyakom.susun.service",
})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @PostConstruct
    public void init() {
        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("assets/js/**").addResourceLocations("assets/js/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        converter.setObjectMapper(objectMapper);
        converters.add(converter);
    }

    @Bean("DB_5.8")
    DataSource getDataSource_5_8(){
        String poolName = "susun_gambar";
        String serverName = "10.1.5.8";
        String dbName = "DB_APPS";
        String username = "webapp";
        String password = "wb4cc3ss2019";

        return createDBConn(serverName, dbName, poolName,
                username, password, 1, 10);
    }

    private HikariDataSource createDBConn(String server, String dbName, String appName,
                                          String username, String password,
                                          int min, int max){
        String connection = "jdbc:sqlserver://" + server +
                ";DatabaseName=" + dbName+ ";ApplicationName="+ appName;

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(connection);
        hikariConfig.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMinimumIdle(min);
        hikariConfig.setMaximumPoolSize(max);
        hikariConfig.setPoolName(appName);
        hikariConfig.setLeakDetectionThreshold(TimeUnit.SECONDS.toMillis(30));
        hikariConfig.setMaxLifetime(TimeUnit.MINUTES.toMillis(5));
        hikariConfig.setIdleTimeout(TimeUnit.MINUTES.toMillis(2));
        hikariConfig.setConnectionTimeout(TimeUnit.MINUTES.toMillis(2));
        hikariConfig.setConnectionTestQuery("select 1");

        return new HikariDataSource(hikariConfig);
    }

    //DO not change the key, because its linked to unique url
    @Bean
    Encrypt getEncrypt() throws Exception {
        Encrypt encrypt  = new Json3DesEncrypt("the@we$oM3Greato7earntoc0de");;
        return encrypt;
    }

    @Bean
    public ParseShortUrlData getParseShortUrlData() throws Exception {
        return new ParseShortUrlData(getEncrypt());
    }

    @Bean
    public Hashids getHashids(){
        return new Hashids("bytheawesomegreato");
    }

}
