package org.gemini4j.simile.caret;

import java.util.function.Function;

enum StateEnum {
    INIT_STATE(InitState::new),
    CARET_DETECTED_STATE(CaretDetectedState::new);

    private Function<IgnoreCaretComparator, State> stateFactory;

    StateEnum(final Function<IgnoreCaretComparator, State> stateFactory) {
        this.stateFactory = stateFactory;
    }

    State createStateInsatnce(final IgnoreCaretComparator comparator) {
        return stateFactory.apply(comparator);
    }
}
