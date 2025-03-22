package io.ratel.commentstatprocessor.module.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * packageName  : io.ratel.commentstatprocessor.module
 * fileName    : CommentFileWriter
 * author      : dorris
 * date        : 2025. 3. 21.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 21.          dorris             최초생성
 */
public class CommentFileWriter {
    /**
     * 텍스트 파일을 씁니다.
     *
     * @param path      파일 경로 (상대 또는 절대)
     * @param content   쓸 내용
     * @param overwrite 파일이 존재할 경우 덮어쓸지 여부
     * @throws IOException 파일 쓰기 실패 시 예외 발생
     */
    public static void writeTextFile(String path, String content, boolean overwrite){
        File file = new File(path);
        File parent = file.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }

        if (file.exists() && !overwrite) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("입력 파일 읽기 실패", e);
        }
    }
}
