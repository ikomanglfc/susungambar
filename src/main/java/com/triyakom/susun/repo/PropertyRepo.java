package com.triyakom.susun.repo;

import org.springframework.stereotype.Repository;

@Repository
public class PropertyRepo extends Repository5_9{
    private static final String APP_NAME = "susun_hti";

    public String loadProperty(String name) {
        try {
            String query = "select app_value from application_properties\n" +
                    "where app_name = ? and app_property = ?";
            return getTemplate().queryForObject(query, String.class, APP_NAME, name);
        } catch (Exception ex) {
            return null;
        }
    }
}
