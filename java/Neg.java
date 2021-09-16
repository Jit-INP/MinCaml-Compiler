/**
 * @author Tsotne
 */
import java.util.*;
public class Neg extends Exp {
    final Exp e;

    Neg(Exp e) {
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
        return "-" + e;
    }
}