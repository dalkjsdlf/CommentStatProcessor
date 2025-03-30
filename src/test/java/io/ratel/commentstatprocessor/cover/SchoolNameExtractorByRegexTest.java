
package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.domain.schoolstat.extractor.SchoolNameExtractor;
import io.ratel.commentstatprocessor.domain.schoolstat.extractor.SchoolNameExtractorByRegex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SchoolNameExtractorByRegexTest {

    private final SchoolNameExtractor extractor = new SchoolNameExtractorByRegex();

    @Test
    @DisplayName("약어가 포함된 댓글에서 정식 학교명이 추출된다")
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
    @DisplayName("false positive 문구는 제거된다")
    void givenFalsePositivePhrase_whenExecute_thenIgnored() {
        String comment = "서울대공원에 다녀왔어. 서울 대공원 너무 좋았어.";
        List<String> result = extractor.execute(comment);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("공백 및 특수문자가 있어도 추출된다")
    void givenCommentWithSpacesAndSymbols_whenExecute_thenExtractsCorrectly() {
        String comment = "창현 고등학교는 정말 좋은 학교입니다!!!";
        List<String> result = extractor.execute(comment);

        assertThat(result).contains("창현고등학교");
    }

    @Test
    @DisplayName("빈 문자열은 빈 리스트 반환")
    void givenEmptyComment_whenExecute_thenReturnsEmptyList() {
        String comment = "";
        List<String> result = extractor.execute(comment);

        assertThat(result).isEmpty();
    }
}
