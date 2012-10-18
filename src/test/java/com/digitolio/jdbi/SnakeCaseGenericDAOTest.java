package com.digitolio.jdbi;

import com.digitolio.jdbi.strategy.SnakeCaseTranslatingStrategy;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import static org.fest.assertions.api.Assertions.assertThat;

public class SnakeCaseGenericDAOTest {

    private DBI dbi;
    private Handle handle;


    @Before
    public void setUp() throws Exception {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        dbi = new StrategyAwareDBI(ds, new SnakeCaseTranslatingStrategy());
        handle = dbi.open();
        handle.execute("create table person (user_id int primary key, user_name varchar(100) , child_count int, cousin_count int)");
    }

    @Test
    public void testBeanMapperFactory() throws Exception {
        PersonGenericDAO db = dbi.onDemand(PersonGenericDAO.class);
        Integer insertCount = db.insert(new Person(4249517, "Cemo", 2, 7));
        assertThat(insertCount).isEqualTo(1);

        Integer updateCount = db.updateByPK(new Person(4249517, "Cemalettin Koc", null, 7));
        assertThat(updateCount).isEqualTo(1);

        Person person = db.selectByPK(new Person(33322211, null, null, null));
        assertThat(person).isNull();
        person = db.selectByPK(new Person(4249517, null, null, null));
        assertThat(person).isNotNull();
        assertThat(person.getUserName()).isEqualTo("Cemalettin Koc");

        int deleteCount = db.deleteByPK(new Person(4249517, "Cemo2", null, 7));
        assertThat(deleteCount).isEqualTo(1);
    }

    @After
    public void tearDown() throws Exception {
        handle.execute("drop table person");
        handle.close();
    }

    public static interface PersonGenericDAO extends GenericDAO<Person> {}

    public static class Person {
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
