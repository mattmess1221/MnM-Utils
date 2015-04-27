package mnm.mods.util;

import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A log helper class
 */
public class LogHelper {

    private final Logger LOGGER;
    private Level level = Level.INFO;

    private LogHelper(String name) {
        this.LOGGER = LogManager.getLogger(name);
    }

    public static LogHelper getLogger(String name) {
        return new LogHelper(name);
    }

    public static LogHelper getLogger() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        StackTraceElement ele = elements[2];
        return getLogger(ele.getClassName());
    }

    public void log(Level level, Object message) {
        LOGGER.log(level, message);
    }

    public void log(Level level, Throwable t) {
        LOGGER.throwing(level, t);
    }

    public void throwing(Level level, String message, Throwable throwable) {
        LOGGER.log(level, message, throwable);
    }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        this.level = level;
        try {
            Method method = LOGGER.getClass().getMethod("setLevel", Level.class);
            method.invoke(level);
        } catch (Exception e) {
            error("The logger doesn't support setting the level", e);
        }
    }

    public void off(Object object) {
        this.log(Level.OFF, object);
    }

    public void off(String message, Throwable throwable) {
        this.throwing(Level.OFF, message, throwable);
    }

    public void fatal(Object object) {
        this.log(Level.FATAL, object);
    }

    public void fatal(String message, Throwable throwable) {
        this.throwing(Level.FATAL, message, throwable);
    }

    public void error(Object object) {
        this.log(Level.ERROR, object);
    }

    public void error(String message, Throwable throwable) {
        this.throwing(Level.ERROR, message, throwable);
    }

    public void warn(Object object) {
        this.log(Level.WARN, object);
    }

    public void warn(String message, Throwable throwable) {
        this.throwing(Level.WARN, message, throwable);
    }

    public void info(Object object) {
        this.log(Level.INFO, object);
    }

    public void info(String message, Throwable throwable) {
        this.throwing(Level.INFO, message, throwable);
    }

    public void debug(Object object) {
        this.log(Level.DEBUG, object);
    }

    public void debug(String message, Throwable throwable) {
        this.throwing(Level.DEBUG, message, throwable);
    }

    public void trace(Object object) {
        this.log(Level.TRACE, object);
    }

    public void trace(String message, Throwable throwable) {
        this.throwing(Level.TRACE, message, throwable);
    }

    public void all(Object object) {
        this.log(Level.ALL, object);
    }

    public void all(String message, Throwable throwable) {
        this.throwing(Level.ALL, message, throwable);
    }
}
