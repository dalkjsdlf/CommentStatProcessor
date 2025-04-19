package io.ratel.commentstatprocessor.domain.extern.fetcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import io.ratel.commentstatprocessor.common.constant.AppConst;
import io.ratel.commentstatprocessor.domain.schoolstat.store.SchoolInfoStore;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * packageName  : io.ratel.commentstatprocessor.module
 * fileName    : SchoolFetcher
 * author      : dorris
 * date        : 2025. 3. 21.
 * description :
 * ================================================
 * DATE              AUTHOR              NOTE
 * ================================================
 * 2025. 3. 21.          dorris             최초생성
 */

@NoArgsConstructor
@Slf4j
public class SchoolFetcher {

    public static void fetchAllSchoolsMap(int pageSize) {
        List<String[]> output = new ArrayList<>();
        output.add(new String[]{"Type", "Name"}); // CSV 헤더

        int page = 1;

        while (true) {
            String url = AppConst.BASE_URL + "?KEY=" + AppConst.API_KEY + "&Type=json&pIndex=" + page + "&pSize=" + pageSize;

            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() != 200) {
                    break;
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String json = in.lines().reduce("", (a, b) -> a + b);
                in.close();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(json);

                // 응답에 데이터가 없으면 종료
                if (!root.has("schoolInfo")) break;

                JsonNode rows = root.get("schoolInfo").get(1).get("row");
                if (rows == null || !rows.isArray() || rows.isEmpty()) break;

                for (JsonNode row : rows) {
                    String schoolName = row.get("SCHUL_NM").asText();
                    String type = row.get("SCHUL_KND_SC_NM").asText(); // ex. 초등학교, 중학교, 고등학교

                    String normalizedType = normalizeType(type);
                    if (normalizedType != null) {
                        output.add(new String[]{normalizedType, schoolName});
                    }
                }

                page++;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }


            try (CSVWriter writer = new CSVWriter(new FileWriter("input/official_school_list.csv"))) {
                writer.writeAll(output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String normalizeType(String type) {
        if (type.contains("초")) return "초등학교";
        if (type.contains("중")) return "중학교";
        if (type.contains("고")) return "고등학교";
        if (type.contains("대")) return "대학교";
        return null;
    }

    public static void main (String[] args) throws Exception {
        fetchAllSchoolsMap(1000);
    }
    public static void fetchAllSchools(int pageSize) {
        int page = 1;
        while (true) {
            String url = AppConst.BASE_URL + "?KEY=" + AppConst.API_KEY + "&Type=json&pIndex=" + page + "&pSize=" + pageSize;

            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() != 200) {
                    break;
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String json = in.lines().reduce("", (a, b) -> a + b);
                in.close();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(json);

                // 응답에 schoolInfo가 없거나 row가 비어있으면 종료
                if (!root.has("schoolInfo")) break;

                JsonNode rows = root.get("schoolInfo").get(1).get("row");
                if (rows == null || !rows.isArray() || rows.isEmpty()) break;

                for (JsonNode row : rows) {
                    String schoolName = row.get("SCHUL_NM").asText();
                    SchoolInfoStore.add(schoolName);
                }

                page++;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
