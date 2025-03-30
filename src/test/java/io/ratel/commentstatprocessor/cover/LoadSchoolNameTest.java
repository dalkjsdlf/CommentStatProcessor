package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.module.extern.SchoolFetcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName  : io.ratel.commentstatprocessor
 * fileName    : LoadSchoolNameTest
 * author      : dorris
 * date        : 2025. 3. 21.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 21.          dorris             최초생성
 */
@DisplayName("OPEN API로 학교명을 가져오는 테스트")
@SpringBootTest
public class LoadSchoolNameTest {

    private final SchoolFetcher schoolFetcher;

    public LoadSchoolNameTest(@Autowired SchoolFetcher schoolFetcher) {
        this.schoolFetcher = schoolFetcher;
    }

    @DisplayName("학교정보를 불러온다.")
    @Test
    public void givenPageSize_whenGetSchoolName_then(){
        //given
        String searchName = "충암고등학교";
        int pageSize = 100;

        //when
        Map<String, List<String>> schools = schoolFetcher.fetchAllSchools(pageSize);

        //then
        assertThat(schools.get(searchName)).isNotNull();

    }
}
