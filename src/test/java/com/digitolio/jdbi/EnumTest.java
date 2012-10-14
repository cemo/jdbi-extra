package com.digitolio.jdbi;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author C.Koc
 */
public class EnumTest {

    private DBI dbi;
    private Handle handle;

    @Before
    public void setUp() throws Exception {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        dbi = new DBI(ds);

        dbi.registerArgumentFactory(new EnumArgumentFactory());
        handle = dbi.open();
        handle.execute("create table match (opponent varchar(100) primary key, place VARCHAR(64) CHECK place IN('AWAY', 'HOME'))");

    }

    @Test
    public void testEnumArgument() throws Exception {
        BeanMappingDao db = dbi.onDemand(BeanMappingDao.class);
        Match that = new Match(PLACE.AWAY, "Barcelona");
        db.insertNewMatch(that);

        Match expected = db.findByName("Barcelona");
        assertThat(expected).isEqualTo(that);
    }

    public static interface BeanMappingDao {

        @SqlUpdate("insert into match (opponent, place) values (:opponent, :place)")
        public void insertNewMatch(@BindBean Match match);

        @SqlQuery("select opponent, place from match where opponent = :team")
        @MapResultForSnakeCaseAsBean
        public Match findByName(@Bind("team") String team);

    }

    public enum PLACE {
        AWAY, HOME
    }

    public static class Match {
        PLACE place;
        String opponent;

        public Match() {
        }

        public Match(PLACE place, String opponent) {
            this.place = place;
            this.opponent = opponent;
        }

        public PLACE getPlace() {
            return place;
        }

        public void setPlace(PLACE place) {
            this.place = place;
        }

        public String getOpponent() {
            return opponent;
        }

        public void setOpponent(String opponent) {
            this.opponent = opponent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Match match = (Match) o;

            if (opponent != null ? !opponent.equals(match.opponent) : match.opponent != null) {
                return false;
            }
            if (place != match.place) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = place != null ? place.hashCode() : 0;
            result = 31 * result + (opponent != null ? opponent.hashCode() : 0);
            return result;
        }
    }

}
