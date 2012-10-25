package com.digitolio.jdbi;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import static org.fest.assertions.api.Assertions.assertThat;

public class TestGetGeneratedGenericDAOKeys {

    private DBI dbi;
    private Handle handle;

    @Before
    public void setUp() throws Exception {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        dbi = StrategyAwareDBI.enhanceDBIForSnakeCase(new DBI(ds));
        handle = dbi.open();
        handle.execute("create table some_thing (id identity primary key, name varchar(32))");
    }

    @After
    public void tearDown() throws Exception {
        handle.close();
    }

    public static interface DAO extends GenericDAO<SomeThing> {

        @SqlQuery("select name from some_thing where id = :it")
        public String findNameById(@Bind long id);
    }

    @Test
    public void testFoo() throws Exception {
        DAO dao = dbi.open(DAO.class);
        long id = dao.insert(new SomeThing("Cemo", "Koc"));
        assertThat(id).isEqualTo(1);

    }

}
