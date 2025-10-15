package com.codeup.novabook.util.csv;

import com.codeup.novabook.domain.AccessLevel;
import com.codeup.novabook.domain.Member;
import com.codeup.novabook.domain.MemberRole;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public final class MemberCsv {
    private MemberCsv() {}

    public static List<Member> read(Reader reader) throws Exception {
        try (CSVReader csv = new CSVReader(reader)) {
            List<Member> out = new ArrayList<>();
            String[] row;
            boolean headerSkipped = false;
            while ((row = csv.readNext()) != null) {
                if (!headerSkipped) { headerSkipped = true; continue; }
                if (row.length < 3) continue;
                String name = row[0];
                MemberRole role = MemberRole.valueOf(row[1]);
                AccessLevel level = AccessLevel.valueOf(row[2]);
                Member m = new Member(name, role, level);
                out.add(m);
            }
            return out;
        }
    }

    public static void write(List<Member> members, Writer writer) throws Exception {
        try (CSVWriter csv = new CSVWriter(writer)) {
            csv.writeNext(new String[]{"name","role","accessLevel"});
            for (Member m : members) {
                csv.writeNext(new String[]{
                        m.getName(),
                        m.getRole().name(),
                        m.getAccessLevel().name()
                });
            }
            csv.flush();
        }
    }
}
