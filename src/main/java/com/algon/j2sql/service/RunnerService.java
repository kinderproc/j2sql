package com.algon.j2sql.service;

import com.algon.j2sql.validation.JsonValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Slf4j
@RequiredArgsConstructor
@Service
public class RunnerService {

    private final FileService fileService;

    private final JsonValidatorService jsonValidatorService;

    private final SqlGeneratorService sqlGeneratorService;

    public void run(String[] args) {
        if (args.length == 0) {
            log.error("Checking command line arguments. There are no arguments. You have to specify at least source file location.");
            throw new RuntimeException("Checking command line arguments. There are no arguments. You have to specify at least source file location.");
        }

        var sourcePath = Path.of(args[0]);
        var source = fileService.getFileAsString(sourcePath);
        var destinationPath = Path.of(getDestinationPathString(args, sourcePath));
        var validationInfo = jsonValidatorService.validate(source);
        if (!Strings.isEmpty(validationInfo.getErrors())) {
            throw new RuntimeException("Input json validation. Schema has errors. Errors: " + validationInfo.getErrors());
        }
        var output = sqlGeneratorService.generate(validationInfo.getDatabase());
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
