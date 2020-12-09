package onetomany;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class OneToMany {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Model one = new Model("A4");
            Model two = new Model("A6");
            Model three = new Model("A8");
            Model four = new Model("Q2");
            Model five = new Model("Q3");

            session.save(one);
            session.save(two);
            session.save(three);
            session.save(four);
            session.save(five);

            Mark audi = new Mark("AUDI");
            audi.addModel(session.find(Model.class, 1));
            audi.addModel(session.find(Model.class, 2));
            audi.addModel(session.find(Model.class, 3));
            audi.addModel(session.find(Model.class, 4));
            audi.addModel(session.find(Model.class, 5));

            session.save(audi);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
