
package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.common.exception.CommentProcessorException;
import io.ratel.commentstatprocessor.domain.schoolstat.reader.CSVCommentReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CSVCommentReaderTest {

    private static final String basePath = "test/tmp/csv";

    @Test
    @DisplayName("정상적인 CSV 파일에서 댓글을 읽어온다")
    void givenValidCSV_whenRead_thenReturnsList() throws IOException {
        String path = basePath + "/valid.csv";
        Files.createDirectories(Path.of(basePath));
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(""댓글1","댓글2","댓글3"");
        }

        List<String> result = CSVCommentReader.read(path);
        assertThat(result).containsExactly("댓글1", "댓글2", "댓글3");
    }

    @Test
    @DisplayName("빈 셀이나 공백 셀은 무시된다")
    void givenEmptyCells_whenRead_thenIgnoresThem() throws IOException {
        String path = basePath + "/empty-cells.csv";
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(""댓글1","   ","","댓글2"");
        }

        List<String> result = CSVCommentReader.read(path);
        assertThat(result).containsExactly("댓글1", "댓글2");
    }

    @Test
    @DisplayName("파일이 없을 경우 예외를 던진다")
    void givenInvalidPath_whenRead_thenThrowsException() {
        String path = basePath + "/no-file.csv";

        assertThatThrownBy(() -> CSVCommentReader.read(path))
            .isInstanceOf(CommentProcessorException.class)
            .hasMessageContaining("CSV 읽기 실패");
    }

    @Test
    @DisplayName("빈 파일을 읽을 경우 빈 리스트를 반환한다")
    void givenEmptyFile_whenRead_thenReturnsEmptyList() throws IOException {
        String path = basePath + "/empty.csv";
        Files.createFile(Path.of(path));

        List<String> result = CSVCommentReader.read(path);
        assertThat(result).isEmpty();
    }
}
