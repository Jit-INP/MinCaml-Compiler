/**
 * @author Tsotne
 */

import java.util.ArrayList;
import java.util.List;

public class TClosure extends Type{
    @Override
    public boolean equals(Object o) {
        return (o instanceof TClosure);
    }

    @Override
    public Type clone() {
        return new TClosure();
    }

    @Override
    public String toString(){
        return "closure";
    }
}
