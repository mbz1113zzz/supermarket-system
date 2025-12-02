package org.cityu.supermarket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.documentation.DatabaseSchemaRegistry;
import org.cityu.supermarket.dto.doc.DatabaseTableDoc;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Database model docs for Knife4j/Swagger field descriptions
 */
@CrossOrigin
@RestController
@RequestMapping("/docs/database")
@Tag(name = "Database Dictionary", description = "Display core business tables and field constraints")
public class DatabaseDocController {

    @GetMapping("/tables")
        @Operation(summary = "List core data tables",
            description = "Return field descriptions, data types, and constraints for key entities (member, membership card, product, order, transaction record)")
    public SupermarketResult<List<DatabaseTableDoc>> listCoreTables() {
        return SupermarketResult.success(DatabaseSchemaRegistry.coreTables());
    }
}
