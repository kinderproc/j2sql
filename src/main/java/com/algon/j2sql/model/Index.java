package com.algon.j2sql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Index {

    private String name;

    private String reference;

    private String parentTable;

    private String parentColumn;

}
