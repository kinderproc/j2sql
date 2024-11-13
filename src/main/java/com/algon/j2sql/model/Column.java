package com.algon.j2sql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Column {

    private String name;

    private String type;

    private Boolean required;

    private Boolean primary;

}
