package com.triyakom.susun.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;


public class Repository5_9 extends Dao{

    @Autowired
    public void setDataSource(DataSource source){
        this.dataSource = source;
        template = new JdbcTemplate(source);
        namedTemplate = new NamedParameterJdbcTemplate(source);
    }
}
