package com.github.braisdom.objsql.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SQLFile {

    private List<String> sqls = new ArrayList<>();

    public SQLFile(String classpathFile) throws IOException {
        loadSqls(classpathFile);
    }

    private void loadSqls(String classpathFile) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(classpathFile);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } finally {
            inputStream.close();
            bufferedReader.close();
        }
        String[] rawSqls = stringBuilder.toString().split(";");
        sqls.addAll(Arrays.asList(rawSqls));
    }

    public List<String> getSqls() {
        return sqls;
    }
}
