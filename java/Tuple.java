/**
 * @author Tsotne
 */
import java.util.*;
public class Tuple extends Exp {
    final List<Exp> es;

    Tuple(List<Exp> es) {
        this.es = es;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }

    public List<Exp> getEs() {
        return es;
    }

    public String toString(){
        return "tuple" ;
    }
}