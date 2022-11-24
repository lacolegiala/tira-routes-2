package com.tira;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class MapFileReaderTest {

    @Test
    public void testMapReader() {
        MapFileReader mapFileReader = new MapFileReader();
        Assertions.assertEquals(mapFileReader.readFile("NewYork_2_256.map"), true);
    }
}