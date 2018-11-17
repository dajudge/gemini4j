package org.gemini4j.diesel;

import org.gemini4j.core.Gemini4jContext;

import java.util.List;

class CommandSuite<B> implements Suite {
    private final String suiteName;
    private final Gemini4jContext<B> context;
    private final List<Command<B>> commands;

    CommandSuite(
            final String suiteName,
            final Gemini4jContext<B> context,
            final List<Command<B>> commands
    ) {
        this.suiteName = suiteName;
        this.context = context;
        this.commands = commands;
    }

    @Override
    public void run() {
        context.getSnapper().nextTest(suiteName);
        try {
            commands.forEach(it -> it.execute(context.getBrowser()));
        } finally {
            context.shutdown();
        }
    }
}
