/**
 * @author Tsotne
 */

import java.util.ArrayList;
import java.util.List;

public class TVar extends Type {
    String v;
    TVar(String v) {
        this.v = v;
    }
    public TVar(TVar tv){
        this.v = tv.v;
    }
    @Override
    public String toString() {
        return v;
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof TVar)
            return this.v.equals(((TVar)o).v);
        else return false;
    }
    @Override
    public Type clone() {
        return new TVar(v);
    }
}

