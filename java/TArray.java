/**
 * @author Tsotne
 */

import java.util.ArrayList;
import java.util.List;

public class TArray extends Type {
    private Type type;

    public Type getType() {
        return type;
    }
    public TArray(Type t) {
        this.type = t;
    }
    public TArray(){
        this.type = Type.gen();
    }

    public boolean equals(Object o) {
        if(!(o instanceof TArray))
            return false;
        else
            return this.type.equals(((TArray)o).type);
    }

    @Override
    public Type clone() {
        return new TArray(type.clone());
    }
    @Override
    public String toString(){
        return "[" + this.type + "]";
    }
}

