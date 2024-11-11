package com.algon.j2sql;

import com.algon.j2sql.service.SqlGeneratorService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class Runner {

    private final SqlGeneratorService sqlGeneratorService = new SqlGeneratorService();

    public void run(String[] args) {
        if (args.length == 0) {
            log.error("Checking command line arguments. There are no arguments. You have to specify at least source file location.");
            throw new RuntimeException("Checking command line arguments. There are no arguments. You have to specify at least source file location.");
        }

        var sourcePath = Path.of(args[0]);
        var destinationPathString = "";

        if (args.length == 1) {
            var absolutePathString = sourcePath.toAbsolutePath().toString();
            destinationPathString = absolutePathString.substring(0, absolutePathString.lastIndexOf(".")) + ".sql";
        } else {
            destinationPathString = args[1];
        }

        var destinationPath = Path.of(destinationPathString);

        var source = "";

        try {
            source = Files.readString(sourcePath);
        } catch (IOException e) {
            log.error("Reading source file. Can't read the source file: {}. Check if file exists, has appropriate extension and format.", e.getMessage(), e);
            throw new RuntimeException("Reading source file. Can't read the source file" + e.getMessage() + ". Check if file exists, has appropriate extension and format.");
        } catch (Exception e) {
            log.error("Reading source file. Unexpected error: {}. Contact the service desk to report the problem.", e.getMessage(), e);
            throw new RuntimeException("Reading source file. Unexpected error: " + e.getMessage() + ". Contact the developer to report the problem.");
        }

        var output = sqlGeneratorService.generate(source);

        try {
            Files.write(destinationPath, output.getBytes());
        } catch (IOException e) {
            log.error("Saving destination file. Can't write the destination file: {}. Check if you have write permissions and destination path is correct.", e.getMessage(), e);
            throw new RuntimeException("Saving destination file. Can't read the source file" + e.getMessage() + ". Check if you have write permissions and destination path is correct.");
        } catch (Exception e) {
            log.error("Saving destination file. Unexpected error: {}. Contact developer to report the problem.", e.getMessage(), e);
            throw new RuntimeException("Saving destination file. Unexpected error: " + e.getMessage() + ". Contact developer to report the problem.");
        }
    }

}
