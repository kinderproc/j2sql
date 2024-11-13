package com.algon.j2sql.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Database {

    private String name;

    List<Table> tables;

    List<ForeignKey> foreignKeys;

    List<Index> indexes;

}
