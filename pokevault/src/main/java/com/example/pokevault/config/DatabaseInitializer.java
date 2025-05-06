package com.example.pokevault.config;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@Configuration
public class DatabaseInitializer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final int MAX_RETRIES = 10;
    private static final int RETRY_DELAY_SECONDS = 5;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private Environment environment;
    
    // This method will run after the application is ready but before accepting requests
    @EventListener(ApplicationReadyEvent.class)
    public void initializeDatabase() {
        LOGGER.info("Checking database connection...");
        
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try (Connection connection = dataSource.getConnection()) {
                LOGGER.info("Database connection established successfully!");
                
                // We can initialize any required data here if needed
                // new JdbcTemplate(dataSource).execute("SELECT 1");
                
                return; // Connection successful, exit the method
            } catch (Exception e) {
                LOGGER.warn("Database connection attempt {} failed: {}", attempt, e.getMessage());
                
                if (attempt < MAX_RETRIES) {
                    LOGGER.info("Retrying in {} seconds...", RETRY_DELAY_SECONDS);
                    try {
                        TimeUnit.SECONDS.sleep(RETRY_DELAY_SECONDS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    LOGGER.error("Failed to connect to database after {} attempts", MAX_RETRIES);
                    // We don't throw an exception here to allow the application to start
                    // even if the database is not available initially
                }
            }
        }
    }
}
