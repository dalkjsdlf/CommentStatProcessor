package io.ratel.commentstatprocessor.cover;

import io.ratel.commentstatprocessor.domain.schoolstat.extract.CommentPreprocessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CommentPreprocessorTest
 *
 * 댓글 문자열에서 특수문자 제거, 공백 정리 등 정제 기능 테스트
 *
 * ✅ 테스트 목록
 * ------------------------------------------------------------------------------
 * 1. 정상 문장에서 특수문자 제거
 * 2. 여러 공백 → 하나의 공백
 * 3. 특수문자만 있는 경우 → 빈 문자열
 * 4. null 입력 시 → 빈 문자열
 * 5. 빈 문자열 입력 시 → 빈 문자열
 * 6. 앞뒤 공백만 → 제거되는지 확인
 * 7. 탭/줄바꿈 등 제어문자 → 공백 정리 확인
 * 8. 한글 + 영어 + 숫자 혼합 → 그대로 유지
 * 9. 이모지 포함 → 제거되는지 확인
 * ------------------------------------------------------------------------------
 */
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

    @Test
    @DisplayName("앞뒤 공백만 있을 경우 제거된다")
    void givenTrimOnlySpaces_whenClean_thenReturnsEmpty() {
        String input = "      ";
        String result = CommentPreprocessor.clean(input);

        assertThat(result).isEqualTo("");
    }

    @Test
    @DisplayName("탭, 줄바꿈, 캐리지 리턴 등을 공백으로 정리")
    void givenControlCharacters_whenClean_thenConvertsToSingleSpace() {
        String input = "창현고\t학생들\n모여라\r축제";
        String result = CommentPreprocessor.clean(input);

        assertThat(result).isEqualTo("창현고 학생들 모여라 축제");
    }

    @Test
    @DisplayName("한글 + 영어 + 숫자는 유지되고 특수문자만 제거된다")
    void givenMixedAlphaNumeric_whenClean_thenOnlySpecialCharsRemoved() {
        String input = "창현고123 HighSchool#1!";
        String result = CommentPreprocessor.clean(input);

        assertThat(result).isEqualTo("창현고123 HighSchool1");
    }

    @Test
    @DisplayName("이모지가 포함된 문장은 이모지도 제거된다")
    void givenEmojiComment_whenClean_thenEmojiRemoved() {
        String input = "창현고 👍 최고 😂";
        String result = CommentPreprocessor.clean(input);

        assertThat(result).isEqualTo("창현고 최고");
    }
}