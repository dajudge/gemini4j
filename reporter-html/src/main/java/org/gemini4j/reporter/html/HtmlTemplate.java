package org.gemini4j.reporter.html;

import java.io.InputStream;

public enum HtmlTemplate {
    STANDARD("org/gemini4j/reporter/html/templates/standard.html");

    private final String url;

    HtmlTemplate(final String url) {
        this.url = url;
    }

    public InputStream openStream() {
        return HtmlTemplate.class.getClassLoader().getResourceAsStream(url);
    }
}
