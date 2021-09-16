/**
 * @author Tsotne
 */
import java.util.*;
public class LetTuple extends Exp {
    final List<Id> ids;
    final List<Type> ts;
    final Exp e1;
    final Exp e2;

    LetTuple(List<Id> ids, List<Type> ts, Exp e1, Exp e2) {
        this.ids = ids;
        if(ts == null){
            ts = new ArrayList<Type>();
            for(int i=0; i<ids.size(); i++)
                ts.add(Type.gen());
        }

        this.ts = ts;
        this.e1 = e1;
        this.e2 = e2;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }

    public List<Id> getIds() {
        return ids;
    }

    public List<Type> getTs() {
        return ts;
    }

    public Exp getE1() {
        return e1;
    }

    public Exp getE2() {
        return e2;
    }

    public String toString(){
        return "let tuple" ;
    }
}