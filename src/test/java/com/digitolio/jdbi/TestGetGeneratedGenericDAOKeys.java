package com.digitolio.jdbi;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.tweak.HandleCallback;

import static org.fest.assertions.api.Assertions.assertThat;

public class TestGetGeneratedGenericDAOKeys
{
    private JdbcConnectionPool ds;
    private DBI                dbi;

    @Before
    public void setUp() throws Exception
    {
        ds = JdbcConnectionPool.create("jdbc:h2:mem:test",
                                       "username",
                                       "password");
        dbi = new DBI(ds);
        dbi = StrategyAwareDBI.enhanceDBIForSnakeCase(new DBI(ds));
        dbi.withHandle(new HandleCallback<Object>()
        {
            public Object withHandle(Handle handle) throws Exception
            {
                handle.execute("create table something (id identity primary key, name varchar(32))");
                return null;
            }
        });
    }

    @After
    public void tearDown() throws Exception
    {
        ds.dispose();
    }

    public static interface DAO extends GenericDAO<Something>
    {

        @SqlQuery("select name from some_thing where id = :it")
        public String findNameById(@Bind long id);
    }

    @Test
    public void testFoo() throws Exception
    {
        DAO dao = dbi.open(DAO.class);
        long id = dao.insert(new Something("Cemo","Koc"));
        assertThat(id).isEqualTo(1);

    }

}
