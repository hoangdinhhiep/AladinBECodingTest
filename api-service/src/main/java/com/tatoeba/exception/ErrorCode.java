package com.tatoeba.exception;

public final class ErrorCode {
    private ErrorCode() {
    }

    public static final String ERR_GENERAL = "000.000";
    public static final String ERR_NOT_FOUND = "000.404";
    public static final String ERR_RECORD_DELETED = ERR_NOT_FOUND + ".002";
    public static final String ERR_RECORD_NOT_ACTIVE = ERR_NOT_FOUND + ".003";
    public static final String ERR_PERMISSION = "000.403";

    public static final String ERR_BAD_REQUEST = "000.400";
    public static final String ERR_NULL = ERR_BAD_REQUEST + ".000";
    public static final String ERR_WRONG_DATA = ERR_BAD_REQUEST + ".001";
    public static final String ERR_CONFLICT_DATA = "000.409";
    public static final String ERR_DUPLICATE_DATA = ERR_CONFLICT_DATA + ".000";
    public static final String ERR_TOO_LARGE_DATA = "000.413";
}