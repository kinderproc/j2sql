package com.algon.j2sql.service;

import com.algon.j2sql.validation.JsonValidatorService;
import com.algon.j2sql.validation.ValidationInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RunnerServiceTest {

    @Mock
    JsonValidatorService jsonValidatorService;
    @Mock
    SqlGeneratorServiceImpl sqlGeneratorService;
    @Mock
    FileServiceImpl fileService;
    @InjectMocks
    RunnerService runnerService;

    @Test
    public void when_noArgs_then_exceptionAndMethodsNotCalled() {
        assertThrows(RuntimeException.class, () -> runnerService.run(new String[0]));
        verify(fileService, never()).getFileAsString(any());
        verify(fileService, never()).writeStringToFile(any(), any());
        verify(jsonValidatorService, never()).validate(any());
        verify(sqlGeneratorService, never()).generate(any());
    }

    @Test
    public void when_success_then_generateAndWriteCalled() {
        // given
        var input = "dummy input";
        var output = "dummy output";
        when(fileService.getFileAsString(any())).thenReturn(input);
        when(jsonValidatorService.validate(any())).thenReturn(ValidationInfo.builder().build());
        when(sqlGeneratorService.generate(any())).thenReturn(output);

        // when
        runnerService.run(new String[] {"", ""});

        // then
        verify(sqlGeneratorService).generate(any());
        verify(fileService).writeStringToFile(eq(output), any());
    }

    @Test
    public void when_destinationPathArgumentAbsent_then_destinationPathSameAsSourcePath() {
        // given
        var input = "dummy input";
        var output = "dummy output";
        when(fileService.getFileAsString(any())).thenReturn(input);
        when(jsonValidatorService.validate(any())).thenReturn(ValidationInfo.builder().build());
        when(sqlGeneratorService.generate(any())).thenReturn(output);

        // when
        runnerService.run(new String[] {"C:\\deep\\inside\\data.json"});

        // then
        verify(sqlGeneratorService).generate(any());
        verify(fileService).writeStringToFile(eq(output), eq(Path.of("C:\\deep\\inside\\data.sql")));
    }

}
