package io.ratel.commentstatprocessor.module;

import io.ratel.commentstatprocessor.module.file.CommentFileReader;
import io.ratel.commentstatprocessor.module.file.CommentFileWriter;
import io.ratel.commentstatprocessor.module.reader.CSVCommentReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * packageName  : io.ratel.commentstatprocessor.business.comment.service
 * fileName    : CommentStatProcessor
 * author      : dorris
 * date        : 2025. 3. 21.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 21.          dorris             최초생성
 */
@Component
public class CommentStatProcessor {

    public void countSchoolsInComments(List<String> comments) {
        SchoolStatCounter counter = new SchoolStatCounter();
        for (String comment : comments) {
            counter.countSchools(comment);
        }
    }

    public void aggregateSchoolCounts(String inputPath, String outputPath, String logPath, boolean overwrite) {
        List<String> comments = CSVCommentReader.read(inputPath);

        SchoolStatCounter counter = new SchoolStatCounter();
        for (String comment : comments) {
            counter.countSchools(comment);
        }

        String result = SchoolStatContext.statMapToString();

        CommentFileWriter.writeTextFile(outputPath,result, overwrite);
    }
}
