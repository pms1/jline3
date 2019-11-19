/*
 * Copyright (c) 2002-2019, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * https://opensource.org/licenses/BSD-3-Clause
 */
package org.jline.reader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Parser {

    ParsedLine parse(String line, int cursor, ParseContext context) throws SyntaxError;

    default ParsedLine parse(String line, int cursor) throws SyntaxError {
        return parse(line, cursor, ParseContext.UNSPECIFIED);
    }

    default boolean isEscapeChar(char ch) {
        return ch == '\\';
    }

    static String getCommand(final String line) {
        String out = null;
        Pattern  patternCommand = Pattern.compile("^\\s*[a-zA-Z]{1,}[a-zA-Z0-9]*=([a-zA-Z]{1,}[a-zA-Z0-9]*)(\\s+|$)");
        Matcher matcher = patternCommand.matcher(line);
        if (matcher.find()) {
            out = matcher.group(1);
        } else {
            out = line.trim().split("\\s+")[0];
        }
        return out;
    }

    enum ParseContext {
        UNSPECIFIED,

        /** Try a real "final" parse.
         * May throw EOFError in which case we have incomplete input.
         */
        ACCEPT_LINE,

        /** Parse to find completions (typically after a Tab).
         * We should tolerate and ignore errors.
         */
        COMPLETE,

        /** Called when we need to update the secondary prompts.
         * Specifically, when we need the 'missing' field from EOFError,
         * which is used by a "%M" in a prompt pattern.
         */
        SECONDARY_PROMPT
    }
}
