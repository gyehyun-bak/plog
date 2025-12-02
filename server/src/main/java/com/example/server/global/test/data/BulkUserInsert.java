package com.example.server.global.test.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BulkUserInsert {

    private static String url = "jdbc:mysql://localhost:3306/plog?rewriteBatchedStatements=true&useSSL=false";
    private static String dbUsername = "root";
    private static String dbPassword = "password";

    private static final String INSERT_SQL =
            "INSERT INTO user (username, email, oauth_provider, oauth_id, created_at, last_modified_at, created_by, last_modified_by) " +
                    "VALUES (?, ?, ?, ?, now(), now(), 'system', 'system')";

    // a~z, 0~9, _
    private static final char[] CHARSET;

    static {
        StringBuilder sb = new StringBuilder();
        for (char c = '0'; c <= '9'; c++) sb.append(c);
        for (char c = 'a'; c <= 'z'; c++) sb.append(c);
        sb.append('_');
        CHARSET = sb.toString().toCharArray();
    }

    // 문자열 N진법 증가 함수
    private static boolean nextString(StringBuilder s) {
        int base = CHARSET.length;  // 37
        int i = s.length() - 1;

        while (i >= 0) {
            int pos = indexOf(CHARSET, s.charAt(i));
            if (pos < base - 1) {
                s.setCharAt(i, CHARSET[pos + 1]); // 자리 증가
                return true;
            } else {
                s.setCharAt(i, CHARSET[0]); // 자리 올림
                i--;
            }
        }

        // 모두 오버플로우 → 길이 증가
        if (s.length() < 15) {
            s.append(CHARSET[0]);
            return true;
        }

        return false;
    }

    private static int indexOf(char[] arr, char c) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == c) return i;
        return -1;
    }

    public static void main(String[] args) throws SQLException {

        final long TARGET = 10_000_000L;   // 생성할 개수
        final int BATCH_SIZE = 1000;

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

                long start = System.currentTimeMillis();

                StringBuilder current = new StringBuilder("0"); // 시작값

                for (long i = 0; i < TARGET; i++) {

                    String username = current.toString();
                    String email = username + "@example.com";

                    ps.setString(1, username);
                    ps.setString(2, email);
                    ps.setString(3, null);
                    ps.setString(4, null);
                    ps.addBatch();

                    if (i % BATCH_SIZE == 0 && i > 0) {
                        ps.executeBatch();
                        conn.commit();
                        System.out.println("Inserted: " + i);
                    }

                    boolean ok = nextString(current);
                    if (!ok) {
                        throw new RuntimeException("No more strings available (limit reached)");
                    }
                }

                ps.executeBatch();
                conn.commit();

                long end = System.currentTimeMillis();
                System.out.println("Completed. Time = " + (end - start) / 1000 + "s");
            }
        }
    }
}

