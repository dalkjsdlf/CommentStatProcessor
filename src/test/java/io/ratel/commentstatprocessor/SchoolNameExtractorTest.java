package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.domain.schoolstat.extractor.SchoolNameExtractor;
import io.ratel.commentstatprocessor.domain.schoolstat.extractor.SchoolNameExtractorByRegex;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

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
@Slf4j
public class SchoolNameExtractorTest {

    private SchoolNameExtractor schoolNameExtractor;

    @BeforeEach
    public void init () {
        schoolNameExtractor = new SchoolNameExtractorByRegex();
    }
    @DisplayName("[성공] 학교이름(정식명칭)한개 추출")
    @ParameterizedTest
    @CsvSource({
            "2025 충암고등학교 동문들 모여라., 충암고등학교",
    })
    public void givenCommentNormalSchoolName_whenExtractSchool_thenSchoolNames(String comment, String schoolName) {
        // given

        // when
        List<String> schoolNames = schoolNameExtractor.execute(comment);

        // then
        assertThat(schoolNames).contains(schoolName);
    }

    @DisplayName("[성공] 학교이름(약어) 한개 추출")
    @ParameterizedTest
    @CsvSource({
            "서울대 갔다온 사람?, 서울대학교",
    })
    public void givenCommentWithAbbreviation_whenExtractSchool_thenExtracted(String comment, String schoolName) {

        // when
        List<String> result = schoolNameExtractor.execute(comment);

        // then
        assertThat(result).contains(schoolName);
    }

    @DisplayName("[성공] 학교이름(공백 및 특수문자 포함) 한개 추출")
    @ParameterizedTest
    @CsvSource({
            "연세대학교!!! 진짜 좋았지... , 연세대학교",
    })
    public void givenCommentWithSymbol_whenExtractSchool_thenExtracted(String comment, String schoolName) {
        // when
        List<String> result = schoolNameExtractor.execute(comment);

        // then
        assertThat(result).contains(schoolName);
    }

    @DisplayName("[성공] 학교이름(정식명칭) 5개 추출")
    @ParameterizedTest
    @MethodSource("testCast1")
    public void givenCommentWith5NormalNames_whenExtractSchool_thenAllExtracted(String comment, List<String> expectedSchools) {
        // given
        /*
         * comment         -> "서울대학교, 연세대학교, 고려대학교, 부산고등학교, 충암고등학교에서 모임합니다.",
         * expectedSchools -> List.of("서울대학교", "연세대학교", "고려대학교", "부산고등학교", "충암고등학교"
        * */
        // when
        List<String> result = schoolNameExtractor.execute(comment);
        log.debug(">>>> {}",result);
        // then
        assertThat(result).containsAll(expectedSchools);
    }

    @DisplayName("[성공] 학교이름(약어) 5개 추출")
    @ParameterizedTest
    @MethodSource("testCast2")
    public void givenCommentWith5Abbreviations_whenExtractSchool_thenAllExtracted(String comment, List<String> expectedSchools) {
        // when
        List<String> result = schoolNameExtractor.execute(comment);

        // then
        assertThat(result).containsAll(expectedSchools);
    }

    @DisplayName("[성공] 학교이름(공백 및 특수문자 포함) 5개 추출")
    @ParameterizedTest
    @MethodSource("testCast3")
    public void givenCommentWith5SchoolsAndSymbols_whenExtractSchool_thenAllExtracted(String comment, List<String> expectedSchools) {

        // when
        List<String> result = schoolNameExtractor.execute(comment);

        // then
        assertThat(result).containsAll(expectedSchools);
    }

    @DisplayName("[실패] 학교이름 유사 단어가 포함되어도 추출되지 않아야 함")
    @ParameterizedTest
    @CsvSource({
            "나는 오늘 서울대공원을 갔어요. 서울 대공원은 참 멋있었어요. 서울대 공원 다음에 또 갈래요., 서울대학교"
    })
    public void givenCommentWithSimilarButDifferentWord_whenExtractSchool_thenNotExtracted(String comment, String schoolName) {
        // when
        List<String> result =  schoolNameExtractor.execute(comment);

        // then
        assertThat(result).doesNotContain(schoolName);
        assertThat(result).isEmpty();
    }

    private static Stream<Arguments> testCast1() {
        return selectCase("1");
    }
    private static Stream<Arguments> testCast2() {
        return selectCase("2");
    }
    private static Stream<Arguments> testCast3() {
        return selectCase("3");
    }

    private static Stream<Arguments> selectCase(String caseNumber) {
        return provideCommentsAndExpectedSchools()
                .filter(args -> caseNumber.equals(args.get()[0]))
                .map(args -> Arguments.of(args.get()[1], args.get()[2]));
    }

    private static Stream<Arguments> provideCommentsAndExpectedSchools() {

        return Stream.of(
                Arguments.of("1","서울대학교, 연세대학교, 고려대학교, 부산고등학교, 충암고등학교에서 모임합니다.", List.of("서울대학교", "연세대학교", "고려대학교", "부산고등학교", "충암고등학교")),
                Arguments.of("2","서울대, 연대, 고대, 부산고, 충암고", List.of("서울대학교", "연세대학교", "고려대학교", "부산고등학교", "충암고등학교")),
                Arguments.of("3","서울 대학교 연대~~ 고대,, 부산고... 충암고??", List.of("서울대학교", "연세대학교", "고려대학교", "부산고등학교", "충암고등학교"))
        );
    }
}
