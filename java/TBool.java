/**
 * @author Tsotne
 */

import java.util.ArrayList;
import java.util.List;

public class TBool extends Type {
    @Override
    public boolean equals(Object o){
        return(o instanceof TBool );
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public Type clone() {
        return new TBool();
    }
}
