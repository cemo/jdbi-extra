package com.digitolio.jdbi.strategy;

public class DefaultTranslatingStrategy implements TranslatingStrategyAware {

    @Override
    public TranslatingStrategy getPropertyTranslatingStrategy() {
        return TranslatingStrategy.UPPER;
    }

    @Override
    public int hashCode() {
        return getPropertyTranslatingStrategy().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof TranslatingStrategyAware) {
            TranslatingStrategyAware translater = (TranslatingStrategyAware) obj;
            return getPropertyTranslatingStrategy().equals(translater.getPropertyTranslatingStrategy());
        }
        return false;
    }
}
