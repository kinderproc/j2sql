package com.algon.j2sql;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class TestUtilities {

    @SneakyThrows
    public static String readAllLinesFromResource(String resourceFileName) {
        var loader = Thread.currentThread().getContextClassLoader();
        try (var resourceStream = loader.getResourceAsStream(resourceFileName)) {
            return new BufferedReader(new InputStreamReader(resourceStream))
                    .lines().collect(Collectors.joining("\n"));
        }
    }
}
