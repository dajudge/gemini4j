package org.gemini4j.core;

import java.util.List;

class CommandSuite<B> implements Suite {
    private final BrowserFactory<B> browserFactory;
    private final List<Command<B>> commands;

    CommandSuite(
            final BrowserFactory<B> browserFactory,
            final List<Command<B>> commands
    ) {
        this.browserFactory = browserFactory;
        this.commands = commands;
    }

    @Override
    public void run() {
        final Browser<B> browser = browserFactory.create();
        try {
            commands.forEach(it -> it.execute(browser));
        } finally {
            browser.shutdown();
        }
    }
}