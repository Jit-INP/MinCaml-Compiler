/**
 * @author Tsotne
 */

import java.util.ArrayList;
import java.util.List;

public class TInt extends Type {
    @Override
    public boolean equals(Object o){
        return(o instanceof TInt );
    }

    @Override
    public String toString() {
        return "int";
    }

    @Override
    public Type clone() {
        return new TInt();
    }
}

