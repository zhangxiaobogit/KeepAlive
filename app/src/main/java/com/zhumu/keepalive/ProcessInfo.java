package com.zhumu.keepalive;

/**
 * 描 述：
 * 作 者：zxb  2020-09-18 15:42
 * 修改描述： XXX
 * 修 改 人： XXX  2020-09-18 15:42
 * 修改版本： XXX
 */
public class ProcessInfo {
    private static String name;
    private static long memSize;
    private static boolean isSys;
    private static String processName;

    public ProcessInfo(String name, long memSize, boolean isSys, String processName) {
        ProcessInfo.name = name;
        ProcessInfo.memSize = memSize;
        ProcessInfo.isSys = isSys;
        ProcessInfo.processName = processName;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        ProcessInfo.name = name;
    }

    public static long getMemSize() {
        return memSize;
    }

    public static void setMemSize(long memSize) {
        ProcessInfo.memSize = memSize;
    }

    public static boolean isIsSys() {
        return isSys;
    }

    public static void setIsSys(boolean isSys) {
        ProcessInfo.isSys = isSys;
    }

    public static String getProcessName() {
        return processName;
    }

    public static void setProcessName(String processName) {
        ProcessInfo.processName = processName;
    }
}
