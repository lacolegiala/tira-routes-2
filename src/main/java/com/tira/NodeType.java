package com.tira;

public enum NodeType {
    FREE('.'),
    BLOCKED('@');
    private final char t ;
    NodeType(char type) {
        this.t = type;
    }

    Boolean isFree() {
        return this.t == '.';
    }
}
