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
        CommentFileReader fileReader = new CommentFileReader();
        File file = fileReader.read(inputPath);

        CSVCommentReader commentReader = new CSVCommentReader();
        List<String> comments = commentReader.read(file);

        SchoolStatCounter counter = new SchoolStatCounter();
        for (String comment : comments) {
            counter.countSchools(comment);
        }

        String result = counter.statMapToText();

        CommentFileWriter writer = new CommentFileWriter();
        writer.write(result, outputPath, overwrite);
    }
}
