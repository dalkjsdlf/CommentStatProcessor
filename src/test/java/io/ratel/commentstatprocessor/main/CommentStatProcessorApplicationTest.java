package io.ratel.commentstatprocessor.main;

/**
 * packageName  : io.ratel.commentstatprocessor.main
 * fileName    : CommentStatProcessorApplicationTest
 * author      : dorris
 * date        : 2025. 3. 23.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 23.          dorris             최초생성
 */

import io.ratel.commentstatprocessor.CommentStatProcessorApplication;
import io.ratel.commentstatprocessor.common.constant.AppConst;
import io.ratel.commentstatprocessor.common.exception.CommentProcessorErrorCode;
import io.ratel.commentstatprocessor.common.exception.CommentProcessorException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Main Application 실행 테스트")
public class CommentStatProcessorApplicationTest {

    static final String INPUT_FILE = AppConst.DEFAULT_INPUT_DIR + File.separator + AppConst.DEFAULT_INPUT_FILE_NAME;
    static final String OUTPUT_FILE =  AppConst.DEFAULT_OUTPUT_DIR + File.separator + AppConst.DEFAULT_OUTPUT_FILE_NAME;

    @BeforeEach
    void setUp() throws Exception {
        Files.createDirectories(Path.of("test/tmp"));
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(Path.of(OUTPUT_FILE));
    }

    @Test
    @DisplayName("main() 실행 시 output 파일이 생성된다")
    void givenArgs_whenMainRuns_thenOutputIsCreated() {
        // given
        String[] args = {
                "--input=" + INPUT_FILE,
                "--output=" + OUTPUT_FILE,
                "--loglevel=INFO"
        };

        // when
        CommentStatProcessorApplication.main(args);

        // then
        File file = new File(OUTPUT_FILE);
        assertThat(file.exists()).isTrue();
        assertThat(file.length()).isGreaterThan(0);
    }
}
