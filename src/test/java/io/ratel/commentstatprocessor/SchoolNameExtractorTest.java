package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.module.Extractor.SchoolNameExtractor;
import io.ratel.commentstatprocessor.module.Extractor.SchoolNameExtractorByRegex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName  : io.ratel.commentstatprocessor
 * fileName    : SchoolNameExtractorTest
 * author      : dorris
 * date        : 2025. 3. 22.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 22.          dorris             최초생성
 */

/*
* 테스트 케이스
* 학교이름(정식명칭)한개 추출
* 학교이름(약어) 한개 추출
* 학교이름(공백 및 특수문자포함) 한개추출
*
*  학교이름(정식명칭)5개 추출
 * 학교이름(약어) 5개 추출
 * 학교이름(공백 및 특수문자포함) 5개추출
* */
@DisplayName("SchoolNameExtractorTest")
public class SchoolNameExtractorTest {

    private SchoolNameExtractor schoolNameExtractor;

    @BeforeEach
    public void init () {
        schoolNameExtractor = new SchoolNameExtractorByRegex();
    }
    @DisplayName("[성공] 학교이름(정식명칭)한개 추출")
    @Test
    public void givenCommentNormalSchoolName_whenExtractSchool_thenSchoolNames() {
        // given
        String schoolName = "충암고등학교";
        String comment = "2025 충암고등학교 동문들 모여라.";

        // when
        List<String> schoolNames = schoolNameExtractor.execute(comment);

        // then
        assertThat(schoolNames).contains(schoolName);
    }

    @DisplayName("[성공] 학교이름(약어) 한개 추출")
    @Test
    public void givenCommentWithAbbreviation_whenExtractSchool_thenExtracted() {
        // given
        String schoolName = "서울대학교";
        String comment = "서울대 갔다온 사람?";

        // when
        List<String> result = schoolNameExtractor.execute(comment);

        // then
        assertThat(result).contains(schoolName);
    }

    @DisplayName("[성공] 학교이름(공백 및 특수문자 포함) 한개 추출")
    @Test
    public void givenCommentWithSymbol_whenExtractSchool_thenExtracted() {
        // given
        String schoolName = "연세대학교";
        String comment = "연세대학교!!! 진짜 좋았지... ";

        // when
        List<String> result = schoolNameExtractor.execute(comment);

        // then
        assertThat(result).contains(schoolName);
    }

    @DisplayName("[성공] 학교이름(정식명칭) 5개 추출")
    @Test
    public void givenCommentWith5NormalNames_whenExtractSchool_thenAllExtracted() {
        // given
        List<String> expected = List.of("서울대학교", "연세대학교", "고려대학교", "부산고등학교", "충암고등학교");
        String comment = "서울대학교, 연세대학교, 고려대학교, 부산고등학교, 충암고등학교에서 모임합니다.";

        // when
        List<String> result = schoolNameExtractor.execute(comment);
        System.out.println(">>>> "+result);
        // then
        assertThat(result).containsAll(expected);
    }

    @DisplayName("[성공] 학교이름(약어) 5개 추출")
    @Test
    public void givenCommentWith5Abbreviations_whenExtractSchool_thenAllExtracted() {
        // given
        List<String> expected = List.of("서울대학교", "연세대학교", "고려대학교", "부산고등학교", "충암고등학교");
        String comment = "서울대, 연대, 고대, 부산고, 충암고";

        // when
        List<String> result = schoolNameExtractor.execute(comment);

        // then
        assertThat(result).containsAll(expected);
    }

    @DisplayName("[성공] 학교이름(공백 및 특수문자 포함) 5개 추출")
    @Test
    public void givenCommentWith5SchoolsAndSymbols_whenExtractSchool_thenAllExtracted() {
        // given
        List<String> expected = List.of("서울대학교", "연세대학교", "고려대학교", "부산고등학교", "충암고등학교");
        String comment = "서울 대학교 연대~~ 고대,, 부산고... 충암고??";

        // when
        List<String> result = schoolNameExtractor.execute(comment);

        // then
        assertThat(result).containsAll(expected);
    }

    @DisplayName("[실패] 학교이름 유사 단어가 포함되어도 추출되지 않아야 함")
    @Test
    public void givenCommentWithSimilarButDifferentWord_whenExtractSchool_thenNotExtracted() {
        // given
        String comment = "나는 오늘 서울대공원을 갔어요. 서울 대공원은 참 멋있었어요. 서울대 공원 다음에 또 갈래요.";
        // when
        List<String> result =  schoolNameExtractor.execute(comment);

        // then
        assertThat(result).doesNotContain("서울대학교");
        assertThat(result).isEmpty();
    }
}
