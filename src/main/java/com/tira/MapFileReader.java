package com.tira;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class MapFileReader {
    public boolean readFile(String fileName) {
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
