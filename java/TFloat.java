/**
 * @author Tsotne
 */

import java.util.ArrayList;
import java.util.List;

public class TFloat extends Type {
    @Override
    public boolean equals(Object o){
        return(o instanceof TFloat );
    }

    @Override
    public String toString() {
        return "float";
    }

    @Override
    public Type clone() {
        return new TFloat();
    }
}

