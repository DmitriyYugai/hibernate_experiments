package onetomany;

import javax.persistence.*;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "marks")
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "mark")
    private List<Model> models = new ArrayList<>();

    public Mark() {
    }

    public Mark(String name) {
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

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public void addModel(Model model) {
        models.add(model);
    }
}
