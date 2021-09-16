/**
 * @author Tsotne
 */

import java.util.ArrayList;
import java.util.List;

public class TTuple extends Type {
    private List<Type> elem;

    public List<Type> getElem() {
        return elem;
    }

    public TTuple(List<Type> elem) {
        this.elem = elem;
    }

    public TTuple(TTuple tt){
        this.elem = tt.elem;
    }

    @Override
    public String toString() {
        int i = 1;
        String result = "(";
        result = result + elem.get(0);
        while (i < elem.size()) {
            result = result + "," + elem.get(i);
            i++;
        }
        result = result + ")";
        return result;
    }

    @Override
    public Type clone() {
        List<Type> types = new ArrayList<Type>();
        for(Type t : this.elem)
            types.add(t.clone());
        return new TTuple(types);
    }

    public boolean equals(Object o) {
        if (o instanceof TTuple)
            return this.elem.equals(((TTuple) o).elem);

        return false;
    }

    public boolean isDefined() {
        for(Type t : this.elem){
            if(t instanceof TVar)
                return false;
        }
        return true;
    }
}