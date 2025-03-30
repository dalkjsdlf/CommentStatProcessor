
package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.common.exception.CommentProcessorException;
import io.ratel.commentstatprocessor.common.file.FileWriter;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

public class FileWriterTest {

    static final String basePath = "test/tmp/filewriter";

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(Path.of(basePath));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(Path.of(basePath))
                .map(Path::toFile)
                .sorted((a, b) -> -a.compareTo(b)) // delete children first
                .forEach(File::delete);
    }

    @Test
    void givenNonExistentFile_whenWrite_thenFileIsCreated() {
        String path = basePath + "/newfile.txt";
        FileWriter.writeTextFile(path, "Hello, World!", true);

        File file = new File(path);
        assertThat(file.exists()).isTrue();
    }

    @Test
    void givenExistingFileAndNoOverwrite_whenWrite_thenSkipWriting() throws IOException {
        String path = basePath + "/existing.txt";
        Files.write(Path.of(path), "Original".getBytes());

        FileWriter.writeTextFile(path, "Modified", false);
        String content = Files.readString(Path.of(path));

        assertThat(content).isEqualTo("Original");
    }

    @Test
    void givenNoParentDir_whenWrite_thenCreatesParentAndWritesFile() {
        String path = basePath + "/subdir/newfile.txt";
        FileWriter.writeTextFile(path, "New Content", true);

        assertThat(Files.exists(Path.of(path))).isTrue();
    }

    @Test
    void givenIOException_whenWrite_thenThrowsCustomException() {
        // 시뮬레이션: 디렉토리를 파일로 바꿔 IOException 유도
        String path = basePath + "/invalidDir.txt";
        new File(path).mkdirs(); // 디렉토리로 만들어버림

        assertThatThrownBy(() -> {
            FileWriter.writeTextFile(path + "/child.txt", "test", true);
        }).isInstanceOf(CommentProcessorException.class)
          .hasMessageContaining("파일 쓰기 실패");
    }
}
