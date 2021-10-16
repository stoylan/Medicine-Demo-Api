package medicine.demo.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String barcode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicine")
    private List<Ingredient> ingredients = new ArrayList<>();

    public Medicine(Long id, String name,String barcode){
        this.id = id;
        this.name = name;
        this.barcode = barcode;
    }
    public Medicine addIngredient(Ingredient ingredient){
        ingredient.setMedicine(this);
        this.getIngredients().add(ingredient);
        return this;
    }

    @Override
    public String toString(){
        return "Medicine[id=" + id +", name="+name+", barcode="+barcode+"]";
    }
}
