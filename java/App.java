/**
 * @author Tsotne
 */

import java.util.*;
public class App extends Exp {
    final Exp e;
    final List<Exp> es;

    App(Exp e, List<Exp> es) {
        this.e = e;
        this.es = es;
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

    public List<Exp> getEs() {
        return es;
    }
    public String toString(){
        return "app" ;
    }
}