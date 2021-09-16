/**
 * @author Tsotne
 */
import java.util.*;
public class Not extends Exp {
    final Exp e;

    Not(Exp e) {
        this.e = e;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }

    public Exp getE() {
        return e;
    }

    public String toString()
    {
        return "not " + e;
    }
}