package com.algon.j2sql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForeignKey {

    private String table;

    private String name;

    private String column;

    private String parentTable;

    private String parentColumn;

}
