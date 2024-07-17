package com.urlshortener.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class UrlMappingDao {
    @Autowired
    JdbcTemplate jdbcTemplate;


}
