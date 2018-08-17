package com.bintutu.shop.utils;

import android.util.Log;

import com.bintutu.shop.ShopApplication;

/**
 * Created by qianxiang on 2018/7/19.
 */
public class DebugLog {

    static String className;
    static String methodName;
    static int lineNumber;

    private DebugLog() {

    }

    public static boolean isDebuggable() {
        return Boolean.parseBoolean(ShopApplication.getInstance().GetDebug()+"");
    }

    private static String createLog(String log) {
        return "[" + methodName + ":" + lineNumber + "]" + log;


    }

    private static void getMethodNames(StackTraceElement[] stackTraceElements) {
        if (stackTraceElements != null && stackTraceElements.length > 1) {
            StackTraceElement element = stackTraceElements[1];
            className = element.getFileName();
            methodName = element.getMethodName();
            lineNumber = element.getLineNumber();
        }
    }


    public static void e(String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }

    public static void e(String message, Throwable t) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message), t);
    }

    public static void i(String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void d(String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void v(String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void wtf(String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }

    public static void e(String tag, String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.e(tag, className + createLog(message));
    }

    public static void i(String tag, String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.i(tag, className + createLog(message));
    }

    public static void d(String tag, String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.d(tag, className + createLog(message));
    }

    public static void w(String tag, String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.w(tag, className + createLog(message));
    }

    public static void v(String tag, String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.v(tag, className + createLog(message));
    }

    public static void wtf(String tag, String message) {
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(tag, className + createLog(message));
    }
}


