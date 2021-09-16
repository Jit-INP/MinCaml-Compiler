/**
 * @author Tsotne
 */
import java.util.*;
public class Int extends Exp {
    final int i;

    Int(int i) {
        this.i = i;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }

    public int getI() {
        return i;
    }

    public String toString()
    {
        return Integer.toString(i);
    }
}