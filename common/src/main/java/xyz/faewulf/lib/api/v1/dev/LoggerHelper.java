package xyz.faewulf.lib.api.v1.dev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * A helper class that creates a logger with an automatic prefix for all log messages.
 * This uses Java's dynamic proxy to intercept logging calls and prepend a custom prefix.
 */
public class LoggerHelper implements InvocationHandler {
    private final Logger delegate;
    private final String prefix;

    private LoggerHelper(Logger delegate, String prefix) {
        this.delegate = delegate;
        this.prefix = prefix;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Check if the method is one of the logging methods
        String methodName = method.getName();
        if ((methodName.equals("debug") || methodName.equals("info") ||
                methodName.equals("warn") || methodName.equals("error") ||
                methodName.equals("trace"))
                && args != null && args.length > 0 && args[0] instanceof String) {
            // Prepend the prefix to the message
            args[0] = "[" + prefix + "] " + args[0];
        }
        return method.invoke(delegate, args);
    }

    // Factory method to create a prefixed logger

    /**
     * Creates a new logger with an automatic prefix for all log messages.
     *
     * @param prefix The prefix to add to all log messages.
     * @return {@link Logger} instance that automatically prefixes messages.
     */
    public static Logger createLogger(String prefix) {
        Logger originalLogger = LoggerFactory.getLogger(prefix);
        return (Logger) Proxy.newProxyInstance(
                originalLogger.getClass().getClassLoader(),
                new Class[]{Logger.class},
                new LoggerHelper(originalLogger, prefix)
        );
    }
}
