package com.algon.j2sql.service;

import com.algon.j2sql.TestUtilities;
import com.algon.j2sql.validation.JsonValidatorServiceImpl;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JsonValidatorServiceTest {

    @InjectMocks
    JsonValidatorServiceImpl jsonValidatorService;

    @Test
    @SneakyThrows
    void when_inputCorrect_thenJsonParsedAndVerificationPassed() {
        // given
        var jsonString = TestUtilities.readAllLinesFromResource("correct_input.json");

        // when
        var output = jsonValidatorService.validate(jsonString);
        var database = output.getDatabase();

        // then
        assertNotNull(output);
        assertTrue(Strings.isEmpty(output.getErrors()));
        assertEquals("books", database.getName());
        assertEquals(2, database.getTables().size());
        assertEquals(1, database.getForeignKeys().size());
        assertEquals(1, database.getIndexes().size());
    }

    @Test
    @SneakyThrows
    void when_inputNotCorrect_thenErrorsReturned() {
        // given
        var jsonString = TestUtilities.readAllLinesFromResource("not_correct_input.json");
        String errors = "Table name is not specified. Column name is not specified. Column id must have one of the next types [UUID, TIMESTAMP, NUMERIC, VARCHAR, INTEGER]. Table schema is not specified. Table schema is not specified. Table goods doesn't have primary key. Column 'primary' field is not specified. Column 'required' field is not specified. Table schema is not specified. Table goods can't have more then one primary key. Table name goods is not unique. Table name customer is not unique. Foreign key name fk_order_customer_id is not unique. Foreign key fk_order_customer_id doesn't have corresponding table. Foreign key fk_order_customer_id doesn't have corresponding table. Foreign key fk_order_customer_id doesn't have corresponding parent table. Index name idx_order_customer_id is not unique. Index idx_order_customer_id doesn't have corresponding parent table. Index idx_order_customer_id doesn't have corresponding parent table. ";

        // when
        var output = jsonValidatorService.validate(jsonString);

        // then
        assertNotNull(output);
        assertEquals(errors, output.getErrors());
    }
}
