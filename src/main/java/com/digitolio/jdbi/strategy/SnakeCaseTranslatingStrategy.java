package com.digitolio.jdbi.strategy;

public class SnakeCaseTranslatingStrategy implements TranslatingStrategyAware {

    @Override
    public TranslatingStrategy getPropertyTranslatingStrategy() {
        return TranslatingStrategy.UPPER_UNDERSCORE;
    }
}
