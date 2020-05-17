package me.rubl.loftcoin.util;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import timber.log.Timber;

public class DebugTree extends Timber.DebugTree {

    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {

        StackTraceElement[] stackTrace = new Throwable().fillInStackTrace().getStackTrace();
        int lineOfOurCall = 5;
        StackTraceElement ste = stackTrace[lineOfOurCall];

        String className = ste.getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);

        super.log(priority, tag, String.format(
                Locale.US, "[%s] %s.%s(%s:%d): %s", Thread.currentThread().getName(),
                className, ste.getMethodName(), ste.getFileName(), ste.getLineNumber(), message
        ), t);
    }
}
