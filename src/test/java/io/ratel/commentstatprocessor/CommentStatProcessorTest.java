package io.ratel.commentstatprocessor;

import io.ratel.commentstatprocessor.domain.schoolstat.processor.CommentStatProcessor;
import io.ratel.commentstatprocessor.common.constant.AppConst;
import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolStatResultStore;
import io.ratel.commentstatprocessor.domain.schoolstat.reader.CSVCommentReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName  : io.ratel.commentstatprocessor
 * fileName    : CommentStatProcessorTest
 * author      : dorris
 * date        : 2025. 3. 22.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 22.          dorris             최초생성
 */
@DisplayName("댓글 하나가 아니라 여러 목록을 한번에 통계하는 테스트")
public class CommentStatProcessorTest {

    private CommentStatProcessor commentStatProcessor;
    private List<String> comments;

    @BeforeEach
    public void init () {
        commentStatProcessor = new CommentStatProcessor();
        comments = List.of(
                "서울대학교, 연세대학교, 고려대학교, 부산고등학교, 충암고등학교에서 모임합니다.",
                "서울대, 연대, 고대, 부산고, 충암고",
                "서울 대학교 연대~~ 고대,, 부산고... 충암고??",
                "서울 대학교 연대~~ 고대,, 부산고... 충암고?? 그래 내가 오늘 충암고에 갔어 충암고등학교에서 서울대 간 사람이 얼마나 되더라? 연대도 갔었나?",
                "불광동 살고있는 우리 불광중학우분들 저 좀 도와주세요 ㅠㅠ "
        );
    }

    @DisplayName("[성공] 하드코딩된 댓글 내용에서 학교명을 추출하여 통계")
    @Test
    public void givenCommentsOfHardCoding_whenCountAllSchoolsName_thenTotalStatMap(){
        //given

        String schoolNameForValidation = "충암고등학교";

        //when
        commentStatProcessor.countSchoolsInComments(comments);
        SchoolStatResultStore.displayMap();
        int count = SchoolStatResultStore.getCount(schoolNameForValidation);

        //then
        assertThat(count).isEqualTo(6);
    }

    @DisplayName("[성공] Csv의 모든 댓글 내용에서 학교명을 추출하여 통계")
    @Test
    public void givenCommentsFromCsv_whenCountAllSchoolsName_thenTotalStatMap() throws IOException {
        //given

        String schoolNameForValidation = "창현고등학교";
        String inputFilePath = AppConst.DEFAULT_INPUT_DIR + File.separator + AppConst.DEFAULT_INPUT_FILE_NAME;
        File file = Path.of(inputFilePath).toFile();
        //when
        List<String> commentsFromCsv = CSVCommentReader.read(file.getPath());
        //when
        commentStatProcessor.countSchoolsInComments(commentsFromCsv);
        SchoolStatResultStore.displayMap();
        int count = SchoolStatResultStore.getCount(schoolNameForValidation);

        //then
        assertThat(count).isEqualTo(1);
    }
}
