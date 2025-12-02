package org.cityu.supermarket.dto.doc;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * DB table structure description
 */
@Schema(description = "Database table structure description")
public class DatabaseTableDoc {

    @Schema(description = "Table name", example = "member")
    private String name;

    @Schema(description = "Business meaning", example = "Member information table")
    private String description;

    @Schema(description = "Primary key details", example = "PRIMARY KEY(memberid)")
    private String primaryKey;

    @Schema(description = "Index or constraint notes", example = "CHECK constraints, unique index, etc.")
    private String remarks;

    @Schema(description = "Column definitions")
    private List<DatabaseColumnDoc> columns = new ArrayList<>();

    public DatabaseTableDoc() {
    }

    private DatabaseTableDoc(String name, String description, String primaryKey, String remarks) {
        this.name = name;
        this.description = description;
        this.primaryKey = primaryKey;
        this.remarks = remarks;
    }

    public static DatabaseTableDoc of(String name, String description,
                                      String primaryKey, String remarks) {
        return new DatabaseTableDoc(name, description, primaryKey, remarks);
    }

    public DatabaseTableDoc addColumn(DatabaseColumnDoc column) {
        this.columns.add(column);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<DatabaseColumnDoc> getColumns() {
        return columns;
    }

    public void setColumns(List<DatabaseColumnDoc> columns) {
        this.columns = columns;
    }
}
