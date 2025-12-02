package org.cityu.supermarket.dto.doc;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Describes a database column field
 */
@Schema(description = "Database column metadata description")
public class DatabaseColumnDoc {

    @Schema(description = "Column name", example = "memberId")
    private String name;

    @Schema(description = "Data type and optional length", example = "VARCHAR(50)")
    private String dataType;

    @Schema(description = "Whether null values are allowed", example = "false")
    private boolean nullable;

    @Schema(description = "Business meaning", example = "Member unique identifier")
    private String description;

    @Schema(description = "Constraint details such as primary/unique/check rules", example = "PRIMARY KEY")
    private String constraints;

    public DatabaseColumnDoc() {
    }

    private DatabaseColumnDoc(String name, String dataType, boolean nullable,
                              String description, String constraints) {
        this.name = name;
        this.dataType = dataType;
        this.nullable = nullable;
        this.description = description;
        this.constraints = constraints;
    }

    public static DatabaseColumnDoc of(String name, String dataType, boolean nullable,
                                       String description, String constraints) {
        return new DatabaseColumnDoc(name, dataType, nullable, description, constraints);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }
}
