package com.digitolio.jdbi.table;

import com.digitolio.jdbi.strategy.TranslatingStrategyAware;

public class TranslateTablePair {

    Class<?> type;

    TranslatingStrategyAware translater;

    public TranslateTablePair(Class type, TranslatingStrategyAware translater) {
        this.type = type;
        this.translater = translater;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public TranslatingStrategyAware getTranslater() {
        return translater;
    }

    public void setTranslater(TranslatingStrategyAware translater) {
        this.translater = translater;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TranslateTablePair that = (TranslateTablePair) o;

        if (translater != null ? !translater.equals(that.translater) : that.translater != null) {
            return false;
        }
        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (translater != null ? translater.hashCode() : 0);
        return result;
    }
}
