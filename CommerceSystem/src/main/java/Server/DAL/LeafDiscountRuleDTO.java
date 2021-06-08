package Server.DAL;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Property;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@Embedded
@BsonDiscriminator("LeafDiscountRuleDTO")

public class LeafDiscountRuleDTO extends DiscountRuleDTO {

    @Property(value = "id")
    protected int id;

    @Property(value = "discount")
    protected double discount;

    public LeafDiscountRuleDTO(){
        // For Morphia
    }

    public LeafDiscountRuleDTO(int id, double discount) {
        this.id = id;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}