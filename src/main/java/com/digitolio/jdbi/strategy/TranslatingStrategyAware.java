package com.digitolio.jdbi.strategy;

public interface TranslatingStrategyAware {

    public TranslatingStrategy getDbTranslatingStrategy();

    public TranslatingStrategy getPropertyTranslatingStrategy();
}
