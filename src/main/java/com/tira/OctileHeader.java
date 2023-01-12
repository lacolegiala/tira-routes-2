package com.tira;

import java.util.List;

public class OctileHeader implements IMapHeader{
    private boolean isOctile = false;
    private int height;
    private int width;
    public int getHeight() {
        return height;
    }

    @Override
    public boolean isValidHeader(List<String> hdrLines) {
        if (hdrLines.get(0).compareTo("type octile") == 0 &&
                hdrLines.get(3).compareTo("map") == 0
        ) {
            System.out.println("This is octile header");
            this.isOctile = true;
            // read the dimensions
            String[] dLine = hdrLines.get(1).split(" ");
            if (dLine[0].compareTo("height") == 0) {
                this.height = Integer.valueOf(dLine[1]);
            }
            dLine = hdrLines.get(2).split(" ");
            if (dLine[0].compareTo("width") == 0) {
                this.width = Integer.valueOf(dLine[1]);
            }
        }
        return isOctile;
    }

    @Override
    public int headerLines() {
        return 4;   // octile header uses 4 lines in the file
    }

    public int getWidth() {
        return width;
    }
    public OctileHeader() {
    }
}
