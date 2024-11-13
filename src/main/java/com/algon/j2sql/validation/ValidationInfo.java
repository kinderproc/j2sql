package com.algon.j2sql.validation;

import com.algon.j2sql.model.Database;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationInfo {

    private final Database database;

    private final String errors;

}
