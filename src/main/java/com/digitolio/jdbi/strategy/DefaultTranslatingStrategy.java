package com.digitolio.jdbi.strategy;

public class DefaultTranslatingStrategy implements TranslatingStrategyAware {

    @Override
    public TranslatingStrategy getDbTranslatingStrategy() {
        return TranslatingStrategy.LOWER_CAMEL;
    }

    @Override
    public TranslatingStrategy getPropertyTranslatingStrategy() {
        return TranslatingStrategy.UPPER;
    }

    @Override
    public int hashCode() {
        return getDbTranslatingStrategy().hashCode() + getPropertyTranslatingStrategy().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TranslatingStrategyAware) {
            TranslatingStrategyAware translater = (TranslatingStrategyAware) obj;
            return getDbTranslatingStrategy().equals(translater.getDbTranslatingStrategy()) && getPropertyTranslatingStrategy()
                .equals(translater.getPropertyTranslatingStrategy());
        }
        return false;
    }
}
