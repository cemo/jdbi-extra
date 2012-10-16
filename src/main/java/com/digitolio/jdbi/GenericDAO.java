package com.digitolio.jdbi;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface  GenericDAO<T>{
    @AutoDeleteByPK
    @SqlUpdate
    public Integer deleteByPK(@BindBean T t);

    @AutoUpdateByPK
    @SqlUpdate
    public Integer updateByPK(@BindBean T t);

    @AutoInsert
    @SqlUpdate
    public Integer insert(@BindBean T t);

    @AutoSelectByPK
    @StrategyAwareMapBean
    @SqlQuery
    public T selectByPK(@BindBean T t);
}
