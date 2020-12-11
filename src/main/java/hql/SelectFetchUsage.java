package hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;


public class SelectFetchUsage implements AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();

    public Candidate save(Candidate candidate) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Job job1 = new Job("Job1");
            Job job2 = new Job("Job2");
            Job job3 = new Job("Job3");
            JobBase jobBase = new JobBase("Jobs");
            jobBase.add(job1);
            jobBase.add(job2);
            jobBase.add(job3);
            session.save(jobBase);
            candidate.setJobBase(jobBase);
            session.save(candidate);
            session.getTransaction().commit();
        }
        return candidate;
    }

    public Candidate findById(int id) {
        Candidate rsl = null;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("select c from Candidate c " +
                    "join fetch c.jobBase jb " +
                    "join fetch  jb.jobs " +
                    "where c.id = :id");
            query.setParameter("id", id);
            rsl = (Candidate) query.uniqueResult();
            session.getTransaction().commit();
        }
        return rsl;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    public static void main(String[] args) {
        try (SelectFetchUsage sfu = new SelectFetchUsage()) {
            sfu.save(new Candidate("Dmitry", 2, 2));
            System.out.println(sfu.findById(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
