package com.example.demo.service.neo4j.util;

import org.apache.commons.lang3.StringUtils;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jfd on 7/13/17.
 */
public class Neo4jUtil {

    private static final Logger logger = LoggerFactory.getLogger(Neo4jUtil.class);

    private static final String HOST = "192.168.1.151";
    private static final String USERNAME = "neo4j";
    private static final String PASSWORD = "Neo4j";

    public static Driver getNeo4jDriver(){

        String host = StringUtils.isNotEmpty(HOST) ? HOST : HOST;
        String username = StringUtils.isNotEmpty(USERNAME) ? USERNAME : USERNAME;
        String password = StringUtils.isNotEmpty(PASSWORD) ? PASSWORD : PASSWORD;

        Driver driver = GraphDatabase.driver( "bolt://"+host+":7687", AuthTokens.basic( username, password ) );
        logger.info("Init driver-{}",driver);
        return driver;
    }

}
