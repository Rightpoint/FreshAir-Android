package com.raizlabs.freshair;

public class LogLevel {
    public static final int VERBOSE = Integer.parseInt("000001", 2);
    public static final int DEBUG = Integer.parseInt("000010", 2);
    public static final int INFO = Integer.parseInt("000100");
    public static final int WARNINGS = Integer.parseInt("001000", 2);
    public static final int ERRORS = Integer.parseInt("010000", 2);
    public static final int WTF = Integer.parseInt("100000", 2);
    public static final int ALL = VERBOSE | DEBUG | INFO | WARNINGS | ERRORS | WTF;
    public static final int NONE = 0;
}