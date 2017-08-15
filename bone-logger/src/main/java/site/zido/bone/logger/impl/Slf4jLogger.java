package site.zido.bone.logger.impl;

import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;
import site.zido.bone.logger.Logger;

public class Slf4jLogger extends Logger{
    private org.slf4j.Logger logger;
    private static final String FQCN = Logger.class.getName();
    Slf4jLogger(String name) {
        super(name);
        logger = LoggerFactory.getLogger(name);
    }

    Slf4jLogger(Class<?> classzz) {
        super(classzz);
        logger = LoggerFactory.getLogger(classzz);
    }

    @Override
    public void info(String msg) {
        if (logger.isInfoEnabled()) {
            if (logger instanceof LocationAwareLogger) {
                ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.INFO_INT, msg, null, null);
            } else {
                logger.info(msg);
            }
        }
    }

    @Override
    public void error(String msg) {
        if (logger.isErrorEnabled()) {
            if (logger instanceof LocationAwareLogger) {
                ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, null);
            } else {
                logger.error(msg);
            }
        }
    }

    @Override
    public void debug(String msg) {
        if (logger.isDebugEnabled()) {
            if (logger instanceof LocationAwareLogger) {
                ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.DEBUG_INT, msg, null, null);
            } else {
                logger.debug(msg);
            }
        }
    }

    @Override
    public void warn(String msg) {
        if (logger.isWarnEnabled()) {
            if (logger instanceof LocationAwareLogger) {
                ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.WARN_INT, msg, null, null);
            } else {
                logger.warn(msg);
            }
        }
    }

    @Override
    public void trace(String msg) {
        if (logger.isTraceEnabled()) {
            if (logger instanceof LocationAwareLogger) {
                ((LocationAwareLogger) logger).log(null, FQCN, LocationAwareLogger.TRACE_INT, msg, null, null);
            } else {
                logger.trace(msg);
            }
        }
    }
}
