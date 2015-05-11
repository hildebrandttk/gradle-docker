package se.transmode.gradle.plugins.docker

import org.junit.Test
import se.transmode.gradle.plugins.docker.client.LoggingAppendable

class LoggingApplendableTest {

    @Test
    void replaceLastNewline_testNullString_expectNull(){
        assert null == LoggingAppendable.replaceLastNewline(null)
    }

    @Test
    void replaceLastNewline_testEmptyString_expectEmptyString(){
        assert '' == LoggingAppendable.replaceLastNewline('')
    }

    @Test
    void replaceLastNewline_testNewlineOnly_expectEmptyString(){
        assert ''.equals(LoggingAppendable.replaceLastNewline('\n'))
    }

    @Test
    void replaceLastNewline_testStringWithNewline_expectStringWithoutNewline(){
        assert 'Test' == LoggingAppendable.replaceLastNewline('Test\n')
    }

    @Test
    void replaceLastNewline_testStringWithNewlineInTheMiddle_expectStringWithNewlineInTheMiddle(){
        assert 'Test\n' +
                '123' == LoggingAppendable.replaceLastNewline('Test\n123')
    }
}
