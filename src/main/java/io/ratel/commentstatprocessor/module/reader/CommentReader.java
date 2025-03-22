package io.ratel.commentstatprocessor.module.reader;

import java.io.File;
import java.util.List;

/**
 * packageName  : io.ratel.commentstatprocessor.module
 * fileName    : CommentReader
 * author      : dorris
 * date        : 2025. 3. 21.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 21.          dorris             최초생성
 */
public interface CommentReader {
    List<String> read(String filePath);
}
