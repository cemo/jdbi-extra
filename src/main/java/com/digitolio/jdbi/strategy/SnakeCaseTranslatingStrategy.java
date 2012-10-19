package com.digitolio.jdbi.strategy;

public class SnakeCaseTranslatingStrategy implements TranslatingStrategyAware {

    @Override
    public TranslatingStrategy getPropertyTranslatingStrategy() {
        return TranslatingStrategy.UPPER_UNDERSCORE;
    }

    @Override
    public int hashCode() {
        return getPropertyTranslatingStrategy().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TranslatingStrategyAware) {
            TranslatingStrategyAware translater = (TranslatingStrategyAware) obj;
            return getPropertyTranslatingStrategy().equals(translater.getPropertyTranslatingStrategy());
        }
        return false;

    }
}
