package se.transmode.gradle.plugins.docker.client

import org.slf4j.Logger

class LoggingAppendable implements Appendable {
    public static final char NEWLINE = '\n' as char
    private final Logger logger;
    private final String logMethod;

    public LoggingAppendable(Logger logger, String logMethod) {
        this.logger = logger;
        this.logMethod = logMethod;
    }

    @Override
    public Appendable append(CharSequence csq) throws IOException {
        return logValue(csq)
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return logValue(csq)
    }

    @Override
    public Appendable append(char c) throws IOException {
        if(c != NEWLINE){
            logger."$logMethod"("$c");
        }
        return this;
    }

    static CharSequence replaceLastNewline(CharSequence csq) {
        if (csq != null
                && csq.length() != 0
                && csq.charAt(csq.length() - 1) == (NEWLINE)) {
            return csq.subSequence(0, csq.length() - 1)
        }
        csq
    }

    LoggingAppendable logValue(CharSequence csq) {
        def replacedValue = replaceLastNewline(csq)
        if (replacedValue != null && replacedValue.length() > 0) {
            logger."$logMethod"(replacedValue);
        }
        return this;
    }
}
