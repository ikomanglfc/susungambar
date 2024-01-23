package com.triyakom.susun.repo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Dao {
    protected DataSource dataSource;
    protected JdbcTemplate template;
    protected NamedParameterJdbcTemplate namedTemplate;


    public void setDataSource(DataSource source){
        this.dataSource = source;
        template = new JdbcTemplate(source);
        namedTemplate = new NamedParameterJdbcTemplate(source);
    }

    public JdbcTemplate getTemplate() {
        return template;
    }

    public NamedParameterJdbcTemplate getNamedTemplate() {
        return namedTemplate;
    }

    protected Number insertAndGetKey(String query, String col, Object... param){
        KeyHolder key = new GeneratedKeyHolder();
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update((conn) -> {
            PreparedStatement ps = conn.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < param.length; i++) {
                ps.setObject(i + 1, param[i]);
            }
            return ps;
        }, key);

        return key.getKey();
    }

    protected RowMapper arrayObjectMapper(){
        return new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                int col = rs.getMetaData().getColumnCount();
                Object[] o = new Object[col];
                for (int i = 0; i < col; i++) {
                    o[i] = rs.getObject(i + 1);
                }
                return o;
            }
        };
    }

    protected SimpleJdbcInsert createSimpleInsert(){
        return new SimpleJdbcInsert(template);
    }


}
