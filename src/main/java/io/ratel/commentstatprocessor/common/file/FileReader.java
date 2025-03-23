package io.ratel.commentstatprocessor.common.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * packageName  : io.ratel.commentstatprocessor.module
 * fileName    : CommentFileReader
 * author      : dorris
 * date        : 2025. 3. 21.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 21.          dorris             최초생성
 */
public class FileReader {

    /**
     * 텍스트 파일을 한 문자열로 읽습니다.
     *
     * @param path 파일 경로
     * @return 파일 내용 전체
     * @throws IOException 읽기 실패 시 예외 발생
     */
    public static String readTextFile(String path) throws IOException {
        Path filePath = Path.of(path);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }

    /**
     * 텍스트 파일을 줄 단위로 읽습니다.
     *
     * @param path 파일 경로
     * @return 각 줄을 원소로 하는 리스트
     * @throws IOException 읽기 실패 시 예외 발생
     */
    public static List<String> readTextFileLines(String path) throws IOException {
        Path filePath = Path.of(path);
        return Files.readAllLines(filePath, StandardCharsets.UTF_8);
    }
}
