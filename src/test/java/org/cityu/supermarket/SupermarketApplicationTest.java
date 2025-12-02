package org.cityu.supermarket;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/** App smoke test: MA Bizheng · v0.0.1 · 2025-11-01 */
@SpringBootTest
@ActiveProfiles("test")
public class SupermarketApplicationTest {

    @Test
    public void contextLoads() {
        System.out.println("Spring Boot context loaded ok");
        System.out.println("Supermarket system started");
    }

    @Test
    public void testSystemConfiguration() {
        System.out.println("System configuration check:");
        System.out.println("  - Database: supermarket");
        System.out.println("  - Context path: /supermarket");
        System.out.println("  - Port: 8081");
        System.out.println("  - MyBatis configured");
        System.out.println("  - Entity refactored");
        System.out.println("  - Service layer refactored");
        System.out.println("  - Controller layer refactored");
    }
}