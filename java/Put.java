/**
 * @author Tsotne
 */
import java.util.*;
public class Put extends Exp {
    final Exp e1;
    final Exp e2;
    final Exp e3;

    Put(Exp e1, Exp e2, Exp e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }

    void accept(Visitor v) {
        v.visit(this);
    }

    public Exp getE1() {
        return e1;
    }

    public Exp getE2() {
        return e2;
    }

    public Exp getE3() {
        return e3;
    }
    public String toString(){
        return "put" ;
    }
}