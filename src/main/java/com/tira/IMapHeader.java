package com.tira;

import java.util.List;

public interface IMapHeader {
    int headerLines() ;
    int getWidth();
    int getHeight();
    boolean isValidHeader(List<String> headerLines);
}
