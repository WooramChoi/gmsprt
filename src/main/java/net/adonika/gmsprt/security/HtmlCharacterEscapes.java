package net.adonika.gmsprt.security;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import org.springframework.web.util.HtmlUtils;

public class HtmlCharacterEscapes extends CharacterEscapes {

    private static final long serialVersionUID = -5043903059354904624L;

    @Override
    public int[] getEscapeCodesForAscii() {

        int[] asciiEscapes = standardAsciiEscapesForJSON();
        asciiEscapes['<'] = ESCAPE_CUSTOM;
        asciiEscapes['>'] = ESCAPE_CUSTOM;
        asciiEscapes['\"'] = ESCAPE_CUSTOM;
        asciiEscapes['('] = ESCAPE_CUSTOM;
        asciiEscapes[')'] = ESCAPE_CUSTOM;
        asciiEscapes['#'] = ESCAPE_CUSTOM;
        asciiEscapes['\''] = ESCAPE_CUSTOM;
        asciiEscapes['&'] = ESCAPE_CUSTOM;

        return asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {
        return new SerializedString(HtmlUtils.htmlEscape(String.valueOf((char) ch)));
    }
}
