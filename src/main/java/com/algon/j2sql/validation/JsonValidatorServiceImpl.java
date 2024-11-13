package com.algon.j2sql.validation;

import com.algon.j2sql.model.Column;
import com.algon.j2sql.model.Database;
import com.algon.j2sql.model.ForeignKey;
import com.algon.j2sql.model.Index;
import com.algon.j2sql.model.Table;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JsonValidatorServiceImpl implements JsonValidatorService {

    private final List<String> dataTypes = List.of("UUID", "TIMESTAMP", "NUMERIC", "VARCHAR", "INTEGER");

    private final StringBuilder errorsBuffer = new StringBuilder();

    public ValidationInfo validate(String jsonString) {
        var errorsBufferLength = 1000;
        var database = parseInputJson(jsonString);
        var tables = database.getTables();

        errorsBuffer.setLength(0);
        validateDatabase(database);
        validateTables(tables);
        validateForeignKeys(database.getForeignKeys(), database.getTables());
        validateIndexes(database.getIndexes(), database.getTables());

        String errors = errorsBuffer.length() > errorsBufferLength ? errorsBuffer.substring(0, errorsBufferLength) + " <...>" : errorsBuffer.toString();

        return ValidationInfo.builder()
                .database(database)
                .errors(errors)
                .build();
    }

    private Database parseInputJson(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(jsonString, Database.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Processing input data. Json parsing exception. Check if json structure is correct.", e);
        }
    }

    private void validateDatabase(Database database) {
        if (database.getName() == null) {
            appendError("Database name is not specified");
        }
    }

    private void validateTables(List<Table> tables) {
        tables.forEach(table -> {
            if (table.getName() == null) {
                appendError("Table name is not specified");
            }

            if (table.getSchema() == null) {
                appendError("Table schema is not specified");
            }

            validateColumns(table.getName(), table.getColumns());
        });

        Map<String, Long> tablesPerName = tables.stream()
                .filter(table -> Objects.nonNull(table.getName()))
                .collect(Collectors.groupingBy(Table::getName, Collectors.counting()));
        tablesPerName.forEach((k, v) -> {
            if (v > 1) appendError("Table name " + k + " is not unique");
        });
    }

    private void validateColumns(String tableName, List<Column> columns) {
        long numberOfPrimaryKeys =  columns.stream()
                .filter(column -> Objects.nonNull(column.getPrimary()))
                .filter(Column::getPrimary)
                .count();

        if (numberOfPrimaryKeys == 0) {
            appendError("Table " + tableName + " doesn't have primary key");
        } else if (numberOfPrimaryKeys > 1) {
            appendError("Table " + tableName + " can't have more then one primary key");
        }

        columns.forEach(column -> {
            if (column.getName() == null) {
                appendError("Column name is not specified");
            }

            if (column.getPrimary() == null) {
                appendError("Column 'primary' field is not specified");
            }

            if (column.getPrimary() == null) {
                appendError("Column 'required' field is not specified");
            }

            if (column.getType() == null || !dataTypes.contains(column.getType())) {
                errorsBuffer.append(String.format("Column %s must have one of the next types %s. ", column.getName(), dataTypes));
            }
        });

        Map<String, Long> columnsPerName = columns.stream()
                .filter(column -> Objects.nonNull(column.getName()))
                .collect(Collectors.groupingBy(Column::getName, Collectors.counting()));
        columnsPerName.forEach((k, v) -> {
            if (v > 1) appendError("Column name " + k + " is not unique in " + tableName + " table");
        });
    }

    private void validateForeignKeys(List<ForeignKey> foreignKeys, List<Table> tables) {

        Map<String, Long> foreignKeysPerName = foreignKeys.stream()
                .collect(Collectors.groupingBy(ForeignKey::getName, Collectors.counting()));
        foreignKeysPerName.forEach((k, v) -> {
            if (v > 1) appendError("Foreign key name " + k + " is not unique");
        });

        foreignKeys.forEach(foreignKey -> {
            tables.stream()
                    .filter(table -> Objects.nonNull(table.getName()))
                    .filter(table -> table.getName().equals(foreignKey.getTable()))
                    .findFirst()
                    .ifPresentOrElse(
                            (table) -> {
                                table.getColumns().stream()
                                        .filter(column -> column.getName().equals(foreignKey.getColumn()))
                                        .findFirst()
                                        .ifPresentOrElse(
                                                column -> {},
                                                () -> appendError("Foreign key " + foreignKey.getName() + " doesn't have corresponding column")
                                        );
                            },
                            () -> appendError("Foreign key " + foreignKey.getName() + " doesn't have corresponding table"));

            tables.stream()
                    .filter(table -> Objects.nonNull(table.getName()))
                    .filter(table -> table.getName().equals(foreignKey.getParentTable()))
                    .findFirst()
                    .ifPresentOrElse(
                            (table) -> {
                                table.getColumns().stream()
                                        .filter(column -> column.getName().equals(foreignKey.getParentColumn()))
                                        .findFirst()
                                        .ifPresentOrElse(
                                                column -> {},
                                                () -> appendError("Foreign key " + foreignKey.getName() + " doesn't have corresponding parent column")
                                        );
                            },
                            () -> appendError("Foreign key " + foreignKey.getName() + " doesn't have corresponding parent table"));
        });
    }

    private void validateIndexes(List<Index> indexes, List<Table> tables) {
        Map<String, Long> indexesPerName = indexes.stream()
                .collect(Collectors.groupingBy(Index::getName, Collectors.counting()));
        indexesPerName.forEach((k, v) -> {
            if (v > 1) appendError("Index name " + k + " is not unique");
        });

        indexes.forEach(index -> {
            tables.stream()
                    .filter(table -> Objects.nonNull(table.getName()))
                    .filter(table -> table.getName().equals(index.getParentTable()))
                    .findFirst()
                    .ifPresentOrElse(
                            (table) -> {
                                table.getColumns().stream()
                                        .filter(column -> column.getName().equals(index.getParentColumn()))
                                        .findFirst()
                                        .ifPresentOrElse(
                                                column -> {},
                                                () -> appendError("Index " + index.getName() + " doesn't have corresponding parent column")
                                        );
                            },
                            () -> appendError("Index " + index.getName() + " doesn't have corresponding parent table"));
        });
    }

    private void appendError(String text) {
        errorsBuffer.append(text);
        errorsBuffer.append(". ");
    }

}
