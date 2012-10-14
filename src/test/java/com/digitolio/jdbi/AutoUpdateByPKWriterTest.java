package com.digitolio.jdbi;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public class AutoUpdateByPKWriterTest {

    private DBI dbi;
    private Handle handle;

    @Before
    public void setUp() throws Exception {
       JdbcDataSource ds = new JdbcDataSource();
       ds.setURL("jdbc:h2:mem:test");
       dbi = new DBI(ds);
       handle = dbi.open();
       handle.execute("create table person (user_id int primary key, user_name varchar(100) , child_count int, cousin_count int)");
    }

    @Test
    public void testBeanMapperFactory() throws Exception {
        BeanMappingDao db = dbi.onDemand(BeanMappingDao.class);
        db.insertPerson(new Person(4249517, "Cemo", 2, 7));
        db.updateByPK(new Person(4249517, "Cemo2", null, 7));
    }

    @After
    public void tearDown() throws Exception {
       handle.execute("drop table person");
       handle.close();
    }

    public static interface BeanMappingDao {

       @AutoUpdateByPK
       @SqlUpdate
       public Integer updateByPK(@BindBean Person person);

       @SqlUpdate("insert into person (user_id, user_name, child_count, cousin_count) values (:userId, :userName, :childCount, :cousinCount)")
       public void insertPerson(@BindBean Person person);
    }

    public static class Person{
        private Integer userId;
        private String userName;
        private Integer childCount;
        private Integer cousinCount;

        public Person() {
        }

        public Person(Integer userId, String userName, Integer childCount, Integer cousinCount) {
            this.userId = userId;
            this.userName = userName;
            this.childCount = childCount;
            this.cousinCount = cousinCount;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Integer getChildCount() {
            return childCount;
        }

        public void setChildCount(Integer childCount) {
            this.childCount = childCount;
        }

        public Integer getCousinCount() {
            return cousinCount;
        }

        public void setCousinCount(Integer cousinCount) {
            this.cousinCount = cousinCount;
        }
    }


}
