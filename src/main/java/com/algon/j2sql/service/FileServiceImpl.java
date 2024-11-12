package com.algon.j2sql.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String getFileAsString(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            log.error("Reading source file. Can't read the source file: {}. Check if file exists, has appropriate extension and format.", e.getMessage(), e);
            throw new RuntimeException("Reading source file. Can't read the source file" + e.getMessage() + ". Check if file exists, has appropriate extension and format.");
        } catch (Exception e) {
            log.error("Reading source file. Unexpected error: {}. Contact the service desk to report the problem.", e.getMessage(), e);
            throw new RuntimeException("Reading source file. Unexpected error: " + e.getMessage() + ". Contact the developer to report the problem.");
        }
    }

    @Override
    public void writeStringToFile(String data, Path path) {
        try {
            Files.write(path, data.getBytes());
        } catch (IOException e) {
            log.error("Saving destination file. Can't write the destination file: {}. Check if you have write permissions and destination path is correct.", e.getMessage(), e);
            throw new RuntimeException("Saving destination file. Can't read the source file" + e.getMessage() + ". Check if you have write permissions and destination path is correct.");
        } catch (Exception e) {
            log.error("Saving destination file. Unexpected error: {}. Contact developer to report the problem.", e.getMessage(), e);
            throw new RuntimeException("Saving destination file. Unexpected error: " + e.getMessage() + ". Contact developer to report the problem.");
        }
    }

}
