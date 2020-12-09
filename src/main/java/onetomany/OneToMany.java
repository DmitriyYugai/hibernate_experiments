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

            Mark audi = new Mark("AUDI");
            session.save(audi);

            Model one = new Model("A4");
            one.setMark(audi);
            Model two = new Model("A6");
            two.setMark(audi);
            Model three = new Model("A8");
            three.setMark(audi);
            Model four = new Model("Q2");
            four.setMark(audi);
            Model five = new Model("Q3");
            five.setMark(audi);

            session.save(one);
            session.save(two);
            session.save(three);
            session.save(four);
            session.save(five);


//            audi.addModel(session.find(Model.class, 1));
//            audi.addModel(session.find(Model.class, 2));
//            audi.addModel(session.find(Model.class, 3));
//            audi.addModel(session.find(Model.class, 4));
//            audi.addModel(session.find(Model.class, 5));

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
