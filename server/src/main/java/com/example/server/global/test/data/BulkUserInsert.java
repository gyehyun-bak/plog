package com.example.server.global.test.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BulkUserInsert {

    private static String url = "jdbc:mysql://localhost:3306/plog?rewriteBatchedStatements=true&useSSL=false";
    private static String dbUsername = "root";
    private static String dbPassword = "password";

    private static final List<String> FIRST_NAMES = List.of(
            "alex", "john", "emma", "mina", "kai", "olivia", "ethan", "noah", "liam", "sophia",
            "mason", "isabella", "jacob", "mia", "william", "ava", "james", "charlotte", "benjamin", "amelia",
            "logan", "harper", "elijah", "evelyn", "lucas", "abigail", "oliver", "emily", "henry", "ella",
            "sebastian", "scarlett", "jack", "grace", "samuel", "chloe", "daniel", "victoria", "matthew", "lily",
            "aiden", "hannah", "jayden", "layla", "gabriel", "zoe", "ryan", "nora", "caleb", "riley",
            "isaac", "aria", "david", "camila", "nicholas", "penelope", "andrew", "lillian", "hunter", "bella",
            "owen", "aubrey", "christopher", "hannah", "jeremiah", "addison", "cameron", "ella", "julian", "stella",
            "eli", "natalie", "joseph", "zoey", "leonardo", "hazel", "joshua", "brooklyn", "jackson", "scarlett",
            "ryder", "victoria", "grayson", "madison", "hunter", "skylar", "julian", "paisley", "adam", "aria",
            "carter", "ellie", "nathan", "audrey", "christian", "samantha", "jonathan", "claire", "dominic", "piper"
    );

    private static final List<String> LAST_NAMES = List.of(
            "kim", "lee", "park", "choi", "cho", "kang", "yun", "han", "jung", "jang",
            "smith", "johnson", "williams", "brown", "jones", "miller", "davis", "garcia", "rodriguez", "wilson",
            "martinez", "anderson", "taylor", "thomas", "hernandez", "moore", "martin", "jackson", "thompson", "white",
            "lopez", "leeuw", "clark", "walker", "hall", "allen", "young", "king", "wright", "hill",
            "scott", "green", "adams", "baker", "gonzalez", "nelson", "carter", "mitchell", "perez", "roberts",
            "turner", "phillips", "campbell", "parker", "evans", "edwards", "collins", "stewart", "sanchez", "morris",
            "rogers", "reed", "cook", "morgan", "bell", "murphy", "bailey", "rivera", "cooper", "richardson",
            "cox", "howard", "ward", "torres", "peterson", "gray", "ramirez", "james", "watson", "brooks",
            "kelly", "sanders", "price", "bennett", "wood", "barnes", "ross", "henderson", "coleman", "jenkins",
            "perry", "powell", "long", "patterson", "hughes", "flores", "washington", "butler", "simmons", "foster"
    );

    private static final String INSERT_SQL =
            "INSERT IGNORE INTO user (username, email, oauth_provider, oauth_id, created_at, last_modified_at, created_by, last_modified_by) VALUES (?, ?, ?, ?, now(), now(), 'system', 'system')";

    public static void main(String[] args) throws SQLException {
        final int TARGET = 10_000_000; // 건수
        final int BATCH_SIZE = 2000;

        try (Connection conn =  DriverManager.getConnection(url, dbUsername, dbPassword)) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

                long start = System.currentTimeMillis();

                int firstSize = FIRST_NAMES.size();
                int lastSize = LAST_NAMES.size();

                for (int i = 0; i < TARGET; i++) {

                    // 순차적으로 first, last 선택
                    String first = FIRST_NAMES.get(i % firstSize);
                    String last = LAST_NAMES.get((i / firstSize) % lastSize);

                    // username base 만들기
                    String base = first + last;

                    // 숫자 공간 확보
                    int number = (i % 999) + 1; // 001~999
                    String numberStr = String.format("%03d", number);

                    // _ 위치 계산: 1 ~ (base.length - 1) 순환
                    int underscorePos = (i % (base.length() - 1)) + 1;

                    // username 만들기
                    String username = base.substring(0, underscorePos) + "_" + base.substring(underscorePos);
                    // 숫자 추가, 15자 초과 시 base 자르기
                    if (username.length() + 3 > 15) {
                        int allowedBaseLength = 15 - 3 - 1; // 3자리 숫자 + underscore
                        if (allowedBaseLength < underscorePos) {
                            underscorePos = allowedBaseLength; // 위치 조정
                        }
                        username = base.substring(0, underscorePos) + "_" + base.substring(underscorePos, allowedBaseLength);
                    }
                    username = username + numberStr;

                    // email unique
                    String email = username + "_" + i + "@example.com";

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
                }

                ps.executeBatch();
                conn.commit();

                long end = System.currentTimeMillis();
                System.out.println("Completed insert. Time = " + (end - start) / 1000 + "s");
            }
        }
    }
}
