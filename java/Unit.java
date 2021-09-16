/**
 * @author Tsotne
 */
import java.util.*;
public class Unit extends Exp {
    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }

    public String toString()
    {
        return "unit";
    }
}