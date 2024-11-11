package com.algon.j2sql.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

@Slf4j
@RequiredArgsConstructor
public class RunnerService {

    private final SqlGeneratorServiceImpl sqlGeneratorServiceImpl;

    private final FileService fileService;

    public void run(String[] args) {
        if (args.length == 0) {
            log.error("Checking command line arguments. There are no arguments. You have to specify at least source file location.");
            throw new RuntimeException("Checking command line arguments. There are no arguments. You have to specify at least source file location.");
        }

        var sourcePath = Path.of(args[0]);
        var source = fileService.getFileAsString(sourcePath);
        var destinationPath = Path.of(getDestinationPathString(args, sourcePath));
        var output = sqlGeneratorServiceImpl.generate(source);
        fileService.writeStringToFile(output, destinationPath);
    }

    private String getDestinationPathString(String[] args, Path sourcePath) {
        var destinationPathString = "";

        if (args.length == 1) {
            var absolutePathString = sourcePath.toAbsolutePath().toString();
            destinationPathString = absolutePathString.substring(0, absolutePathString.lastIndexOf(".")) + ".sql";
        } else {
            destinationPathString = args[1];
        }

        return destinationPathString;
    }

}
