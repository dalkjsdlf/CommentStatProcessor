package io.ratel.commentstatprocessor.cover;

import io.ratel.commentstatprocessor.common.exception.CommentProcessorException;
import io.ratel.commentstatprocessor.domain.schoolstat.extract.SchoolNameExtractor;
import io.ratel.commentstatprocessor.domain.schoolstat.extract.SchoolNameExtractorByRegex;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.ratel.commentstatprocessor.common.exception.CommentProcessorErrorCode.COMMENT_NULL_INPUT;
import static org.assertj.core.api.Assertions.*;

/**
 * SchoolNameExtractorByRegexTest
 *
 * 댓글에서 약어나 정식 명칭으로부터 학교명을 추출하는 정규식 기반 추출기 테스트
 *
 * ✅ 테스트 목록
 * ------------------------------------------------------------------------------
 * [정상]
 * 1. 약어가 포함된 댓글 → 정식 학교명 반환
 * 2. 정식명칭이 중복될 경우 → 여러 번 추출
 * 3. 공백/특수문자 포함 → 정상 추출
 *
 * [엣지/예외]
 * 4. false positive(서울대공원 등) 제거
 * 5. 빈 문자열 → 빈 리스트
 * 6. null 입력 → NPE 또는 빈 리스트
 * 7. 유사 단어 (대학교수, 고속도로) → 추출되지 않음
 * 8. 이모지 + 줄바꿈 포함된 문자열 → 추출 성공
 * 9. 대소문자 혼합된 문자열 처리 가능
 * ------------------------------------------------------------------------------
 */
public class SchoolNameExtractorByRegexTest {

    private final SchoolNameExtractor extractor = new SchoolNameExtractorByRegex();

    @Test
    @DisplayName("약어가 포함된 댓글에서 정식 학교명이 추출된다 - Phase2")
    @Disabled
    void givenCommentWithAbbreviation_whenExecute_thenReturnsOfficialName() {
        String comment = "창현고 학생들 모여라! 연대 출신 선생님도 계심.";
        List<String> result = extractor.execute(comment);

        assertThat(result).contains("창현고등학교", "연세대학교");
    }

    @Test
    @DisplayName("정식명칭이 중복될 경우 여러 번 추출된다")
    void givenCommentWithRepeatedName_whenExecute_thenMultipleOccurrences() {
        String comment = "충암고등학교 충암고등학교 충암고등학교";
        List<String> result = extractor.execute(comment);

        assertThat(result).containsExactly("충암고등학교", "충암고등학교", "충암고등학교");
    }

    @Test
    @DisplayName("빈 문자열은 빈 리스트 반환")
    void givenEmptyComment_whenExecute_thenReturnsEmptyList() {
        List<String> result = extractor.execute("");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("null 입력 시 예외가 발생하거나 방어처리된다")
    void givenNullInput_whenExecute_thenReturnsEmptyOrThrows() {
        assertThatThrownBy(() -> extractor.execute(null))
                .isInstanceOf(CommentProcessorException.class)
                .hasMessageContaining(COMMENT_NULL_INPUT.getMessage())
                .hasFieldOrPropertyWithValue("errorCode", COMMENT_NULL_INPUT);
    }

    @Test
    @DisplayName("학교명과 비슷한 단어는 추출되지 않는다")
    void givenNonSchoolLikeWords_whenExecute_thenReturnsEmpty() {
        String comment = "그는 유명한 대학교수이다. 고속도로를 지나갔다.";
        List<String> result = extractor.execute(comment);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("이모지와 줄바꿈 등이 있어도 추출된다 약어때문에 - Phase2")
    @Disabled
    void givenEmojiAndNewlines_whenExecute_thenExtractsNormally() {
        String comment = "연대는 정말 최고야 💙\n고대도 멋져!!";
        List<String> result = extractor.execute(comment);

        assertThat(result).contains("연세대학교", "고려대학교");
    }

    @Test
    @DisplayName("대소문자가 섞인 입력도 추출 가능하다 -> Phase2에 요구사항 추가")
    @Disabled
    void givenMixedCase_whenExecute_thenExtractsCorrectly() {
        String comment = "Seouldae is good. KOreaUniv is good too.";
        List<String> result = extractor.execute(comment);

        // 정규식에 소문자도 매칭되도록 구현되어 있다면
        assertThat(result).contains("서울대학교", "고려대학교");
    }

    @Test
    @DisplayName("false positive 문구는 제거된다 - Phase2")
    @Disabled
    void givenFalsePositivePhrase_whenExecute_thenIgnored() {
        String comment = "서울대공원에 다녀왔어. 서울 대공원 너무 좋았어.";
        List<String> result = extractor.execute(comment);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("공백 및 특수문자가 있어도 추출된다 - Phase2")
    @Disabled
    void givenCommentWithSpacesAndSymbols_whenExecute_thenExtractsCorrectly() {
        String comment = "창현 고등학교는 정말 좋은 학교입니다!!!";
        List<String> result = extractor.execute(comment);

        assertThat(result).contains("창현고등학교");
    }
}