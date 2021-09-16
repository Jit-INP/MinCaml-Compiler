/**
 * @author Tsotne
 */
import java.util.ArrayList;
import java.util.List;

public class TUnit extends Type {
    @Override
    public boolean equals(Object o){
        return(o instanceof TUnit );
    }

    @Override
    public String toString() {
        return "unit";
    }

    @Override
    public Type clone() {
        return new TUnit();
    }
}