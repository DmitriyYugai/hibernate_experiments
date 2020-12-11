package hql;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobBases")
public class JobBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs = new ArrayList<>();

    public JobBase() {
    }

    public JobBase(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void add(Job job) {
        jobs.add(job);
    }

    @Override
    public String toString() {
        return "JobBase{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", jobs=" + jobs +
                '}';
    }
}
