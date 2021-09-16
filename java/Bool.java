/**
 * @author Tsotne
 */
import java.util.*;
public class Bool extends Exp {
    final boolean b;

    Bool(boolean b) {
        this.b = b;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }

    public boolean getB() {
        return b;
    }

    public String toString()
    {
        return Boolean.toString(b);
    }
}