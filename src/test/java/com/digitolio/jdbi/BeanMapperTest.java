package com.digitolio.jdbi;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import static org.junit.Assert.assertTrue;

public class BeanMapperTest {

   private DBI dbi;
   private Handle handle;

   @Before
   public void setUp() throws Exception {
      JdbcDataSource ds = new JdbcDataSource();
      ds.setURL("jdbc:h2:mem:test");
      dbi = StrategyAwareDBI.enhanceDBIForSnakeCase(new DBI(ds));
      handle = dbi.open();
      handle.execute("create table beans (user_id int primary key, user_name varchar(100))");
   }

   @Test
   public void testBeanMapperFactory() throws Exception {
      BeanMappingDao db = dbi.onDemand(BeanMappingDao.class);
      Bean cemo = new Bean(100, "Cemo");
      db.insertBean(cemo);
      Bean byName = db.findByName("Cemo");
      assertTrue(cemo.equals(byName));
   }


   @After
   public void tearDown() throws Exception {
      handle.execute("drop table beans");
      handle.close();
   }


   public static interface BeanMappingDao {

      @SqlQuery("select user_id, user_name from beans where user_name = :name")
      @StrategyAwareMapBean
      public Bean findByName(@Bind("name") String name);

      @SqlUpdate("insert into beans (user_id, user_name) values (:userId, :userName)")
      public void insertBean(@BindBean Bean bean);
   }

   public static class Bean {
      private Integer userId;
      private String userName;

      public Bean() {
      }

      public Bean(Integer userId, String userName) {
         this.userId = userId;
         this.userName = userName;
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

      @Override
      public boolean equals(Object o) {
         if(this == o) return true;
         if(o == null || getClass() != o.getClass()) return false;

         Bean bean = (Bean) o;

         if(userId != null ? !userId.equals(bean.userId) : bean.userId != null) return false;
         if(userName != null ? !userName.equals(bean.userName) : bean.userName != null) return false;

         return true;
      }

      @Override
      public int hashCode() {
         int result = userId != null ? userId.hashCode() : 0;
         result = 31 * result + (userName != null ? userName.hashCode() : 0);
         return result;
      }
   }
}
