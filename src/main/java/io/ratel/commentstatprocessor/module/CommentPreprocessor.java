package io.ratel.commentstatprocessor.module;

/**
 * packageName  : io.ratel.commentstatprocessor.module
 * fileName    : CommentPreprocessor
 * author      : dorris
 * date        : 2025. 3. 22.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 22.          dorris             최초생성
 */
public class CommentPreprocessor {
    /**
     * 댓글을 정제하여 깨끗한 형태로 반환
     * - 특수문자 제거
     * - 다중 공백을 단일 공백으로 치환
     * - 양 끝 공백 제거
     */
    public static String clean(String comment) {
        if (comment == null || comment.isBlank()) return "";

        // 특수문자 제거 (한글, 숫자, 영문, 공백만 남김)
        String cleaned = comment.replaceAll("[^가-힣a-zA-Z0-9\\s]", " ");

        // 다중 공백 제거
        cleaned = cleaned.replaceAll("\\s+", " ").trim();

        return cleaned;
    }
}
