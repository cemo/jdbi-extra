package com.digitolio.jdbi;

import com.digitolio.jdbi.annotations.*;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface GenericDAO<T>{

    @AutoDeleteByPK
    @SqlUpdate
    public Integer deleteByPK(@BindBean T t);

    @AutoUpdateByPK
    @SqlUpdate
    public Integer updateByPK(@BindBean T t);

    @AutoUpdatePartialByPK
    @SqlUpdate
    public Integer updatePartialByPK(@BindBean T t);

    @AutoInsert
    @GetGeneratedKeys
    @SqlUpdate
    public Long insert(@BindBean T t);

    @AutoSelectByPK
    @StrategyAwareMapBean
    @SqlQuery
    public T selectByPK(@BindBean T t);
}
