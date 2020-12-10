package hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class HqlUsage implements AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();

    public Candidate save(Candidate candidate) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(candidate);
            session.getTransaction().commit();
        }
        return candidate;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from Candidate");
            candidates = query.list();
            session.getTransaction().commit();
        }
        return candidates;
    }

    public Candidate findById(int id) {
        Candidate candidate = null;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            candidate = session.find(Candidate.class, id);
            session.getTransaction().commit();
        }
        return candidate;
    }

    public List<Candidate> findByName(String name) {
        List<Candidate> candidates = new ArrayList<>();
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from Candidate c where c.name = :name")
                    .setParameter("name", name);
            candidates = query.list();
            session.getTransaction().commit();
        }
        return candidates;
    }

    public boolean update(int id, String name, int exp, int sal) {
        boolean rsl = false;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("update Candidate " +
                    "set name = :name, experience = :exp, salary = :sal " +
                    "where id = :id")
                    .setParameter("name", name)
                    .setParameter("exp", exp)
                    .setParameter("sal", sal)
                    .setParameter("id", id);
            rsl = query.executeUpdate() > 0;
            session.getTransaction().commit();
        }
        return rsl;
    }

    public boolean delete(String name) {
        boolean rsl = false;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("delete from Candidate where name = :name")
                    .setParameter("name", name);
            rsl = query.executeUpdate() > 0;
            session.getTransaction().commit();
        }
        return rsl;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    public static void main(String[] args) {
        try (HqlUsage hql = new HqlUsage()) {
            hql.save(new Candidate("Dmitry", 2, 2));
            hql.save(new Candidate("Sergey", 3, 3));
            hql.save(new Candidate("Anton", 4, 4));

            System.out.println(hql.findAll());
            System.out.println(hql.findById(2));
            System.out.println(hql.findByName("Anton"));
            hql.update(1, "DIMA", 5, 5);
            System.out.println(hql.findAll());
            hql.delete("DIMA");
            hql.delete("Sergey");
            System.out.println(hql.findAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
