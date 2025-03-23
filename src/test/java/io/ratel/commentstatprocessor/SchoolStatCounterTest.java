package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolStatResultStore;
import io.ratel.commentstatprocessor.domain.schoolstat.processor.SchoolStatCounter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName  : io.ratel.commentstatprocessor
 * fileName    : SchoolStatCounterTest
 * author      : dorris
 * date        : 2025. 3. 22.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 22.          dorris             최초생성
 */
@DisplayName("학교명을 추출하여 통계를 내는 테스트")
public class SchoolStatCounterTest {

    private SchoolStatCounter schoolStatCounter;

    @BeforeEach
    public void init() {
        schoolStatCounter = new SchoolStatCounter();
    }


    @DisplayName("[성공] List에 있는 학교명의 개수를 세주는 기능")
    @Test
    public void givenComment_whenCountSchools_thenStatMap(){
        SchoolStatResultStore.clear();

        //given
        // 공백, 특수문자, 약어 커버됨
        String comment = "서울 대학교 연대~~ 고대,, 부산고... 충암고?? 그래 내가 오늘 충암고에 갔어 충암고등학교에서 서울대 간 사람이 얼마나 되더라? 연대도 갔었나?";

        //when
        schoolStatCounter.countSchools(comment);

        Map<String, Integer> map = SchoolStatResultStore.getMap();
        // then
        // 충암고등학교 3 ,서울대학교 2, 연세대학교2, 고려대학교1, 부산대학교1
        assertThat(map.get("충암고등학교")).isEqualTo(3);
        assertThat(map.get("서울대학교")).isEqualTo(2);
        assertThat(map.get("연세대학교")).isEqualTo(2);
        assertThat(map.get("고려대학교")).isEqualTo(1);
        assertThat(map.get("부산고등학교")).isEqualTo(1);
    }
}
