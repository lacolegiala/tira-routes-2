package com.tira;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MapFileReader {

    private MapGrid mapGrid ;

    public MapFileReader(String fileName, IMapHeader mapHeader) {
        // Path path = Paths.get(fileName);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<String> headerLines = new ArrayList<>();
            for (int i = 0; i < mapHeader.headerLines(); i++) {
                String currentLine = reader.readLine();
                headerLines.add(currentLine);
            }
            // Assume the file contains the header provided
            if (mapHeader.isValidHeader(headerLines)) {
                System.out.println("map: height: "+mapHeader.getHeight() + " width "+mapHeader.getWidth());
                // then read the rest of the stuff....assuming there are mapHeader.getHeight() lines
                // containing mapHeader.getWidth() characters that define the free and blocked cells
                mapGrid = new MapGrid(mapHeader.getWidth(), mapHeader.getHeight());
                for (int y = 0; y < mapHeader.getHeight(); y++) {
                    String mapLine = reader.readLine();
                    for (int x = 0; x < mapHeader.getWidth(); x++) {
                        if (mapLine.charAt(x) == '.') {
                            mapGrid.setNodeType(x,y,NodeType.FREE);
                        } else {
                            mapGrid.setNodeType(x,y,NodeType.BLOCKED);
                        }
                    }
                }
            }
            reader.close();

        } catch(Exception ex) {
            System.out.println("failed to load:"+fileName);
        }

    }

    MapGrid getMapGrid() {
        if (mapGrid != null) {
            return this.mapGrid;
        } else {
            return new MapGrid(0, 0);
        }

    }

    String getMapString() {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < mapGrid.getSizeY(); y++) {
            for (int x = 0; x < mapGrid.getSizeX(); x++) {
                if (mapGrid.getNode(x,y).getNodeType() == NodeType.FREE) {
                    sb.append('.');
                } else {
                    sb.append('@');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
