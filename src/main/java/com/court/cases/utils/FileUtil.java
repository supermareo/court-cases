package com.court.cases.utils;

import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static List<String> readLines(String path) throws FileNotFoundException {
        List<String> list = new ArrayList<>();
        File file = ResourceUtils.getFile("classpath:" + path);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
