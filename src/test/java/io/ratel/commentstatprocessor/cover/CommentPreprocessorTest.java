
package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.domain.schoolstat.extractor.CommentPreprocessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentPreprocessorTest {

    @Test
    @DisplayName("정상 문장 내 특수문자 제거 및 공백 정리")
    void givenNormalComment_whenClean_thenRemovesSpecialCharsAndCleansWhitespace() {
        String input = "창현고!! 학생들 모여라~~~ #축제";
        String result = CommentPreprocessor.clean(input);

        assertThat(result).isEqualTo("창현고 학생들 모여라 축제");
    }

    @Test
    @DisplayName("다중 공백을 하나로 치환한다")
    void givenMultipleSpaces_whenClean_thenSingleSpace() {
        String input = "창현고     학생들     모여라";
        String result = CommentPreprocessor.clean(input);

        assertThat(result).isEqualTo("창현고 학생들 모여라");
    }

    @Test
    @DisplayName("특수문자만 있을 경우 빈 문자열 반환")
    void givenOnlySpecialChars_whenClean_thenReturnsEmpty() {
        String input = "!@#$%^&*()_+=-[]{}";
        String result = CommentPreprocessor.clean(input);

        assertThat(result).isEqualTo("");
    }

    @Test
    @DisplayName("null 입력 시 빈 문자열 반환")
    void givenNull_whenClean_thenReturnsEmpty() {
        String result = CommentPreprocessor.clean(null);

        assertThat(result).isEqualTo("");
    }

    @Test
    @DisplayName("빈 문자열 입력 시 빈 문자열 반환")
    void givenEmptyString_whenClean_thenReturnsEmpty() {
        String result = CommentPreprocessor.clean("");

        assertThat(result).isEqualTo("");
    }
}
