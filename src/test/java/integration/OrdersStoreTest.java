package integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class OrdersStoreTest {

    private BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(3);
        StringBuilder builder1 = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/update_001.sql")))
        ) {
            br.lines().forEach(line -> builder1.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement("DROP TABLE IF EXISTS orders").executeUpdate();
        pool.getConnection().prepareStatement(builder1.toString()).executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name1", "description1"));
        List<Order> all = (List<Order>) store.findAll();
        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenFindById() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name1", "description1"));
        Order rsl = store.findById(1);
        assertThat(rsl.getName(), is("name1"));
        assertThat(rsl.getDescription(), is("description1"));
    }

    @Test
    public void whenFindByName() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name1", "description1"));
        store.save(Order.of("name1", "description2"));
        List<Order> all = (List<Order>) store.findByName("name1");
        assertThat(all.size(), is(2));
        assertThat(all.get(0).getName(), is("name1"));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(1).getName(), is("name1"));
        assertThat(all.get(1).getDescription(), is("description2"));
    }

    @Test
    public void whenUpdate() {
        OrdersStore store = new OrdersStore(pool);
        store.save(Order.of("name1", "description1"));
        Order order = Order.of("name2", "description2");
        store.update(1, order);
        Order rsl = store.findById(1);
        assertThat(rsl.getName(), is("name2"));
        assertThat(rsl.getDescription(), is("description2"));
    }
}