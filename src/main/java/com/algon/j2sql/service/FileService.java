package com.algon.j2sql.service;

import java.nio.file.Path;

public interface FileService {

    String getFileAsString(Path path);

    void writeStringToFile(String data, Path path);

}
