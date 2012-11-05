package com.digitolio.jdbi.strategy;

public class DefaultTranslatingStrategy implements TranslatingStrategyAware {

    @Override
    public TranslatingStrategy getPropertyTranslatingStrategy() {
        return TranslatingStrategy.UPPER;
    }
}
