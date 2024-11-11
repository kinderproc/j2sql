package com.algon.j2sql;

import com.algon.j2sql.service.FileServiceImpl;
import com.algon.j2sql.service.RunnerService;
import com.algon.j2sql.service.SqlGeneratorServiceImpl;
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
    SqlGeneratorServiceImpl sqlGeneratorService;
    @Mock
    FileServiceImpl fileService;
    @InjectMocks
    RunnerService runnerService;

    @Test
    public void when_NoArgs_then_Exception() {
        String[] arr = new String[0];
        assertThrows(RuntimeException.class, () -> runnerService.run(arr));
        verify(sqlGeneratorService, never()).generate(any(String.class));
    }

    @Test
    public void when_Success_then_GenerateAndWriteCalled() {
        // given
        var input = "dummy input";
        var output = "dummy output";
        when(fileService.getFileAsString(any(Path.class))).thenReturn(input);
        when(sqlGeneratorService.generate(input)).thenReturn(output);

        // when
        runnerService.run(new String[] {"", ""});

        // then
        verify(sqlGeneratorService).generate(any(String.class));
        verify(fileService).writeStringToFile(eq(output), any(Path.class));
    }

    @Test
    public void when_DestinationPathArgumentAbsent_then_DestinationPathSameAsSourcePath() {
        // given
        var input = "dummy input";
        var output = "dummy output";
        when(fileService.getFileAsString(any(Path.class))).thenReturn(input);
        when(sqlGeneratorService.generate(input)).thenReturn(output);

        // when
        runnerService.run(new String[] {"C:\\deep\\inside\\data.json"});

        // then
        verify(sqlGeneratorService).generate(any(String.class));
        verify(fileService).writeStringToFile(eq(output), eq(Path.of("C:\\deep\\inside\\data.sql")));
    }

}
