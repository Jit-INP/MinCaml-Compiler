/**
 * @author Tsotne
 */
import java.util.*;
public class Var extends Exp {
    final Id id;

    public Id getId() {
        return id;
    }

    Var(Id id) {
        this.id = id;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }

    public String toString()
    {
        return id.toString();
    }
}