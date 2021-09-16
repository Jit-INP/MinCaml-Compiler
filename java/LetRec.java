/**
 * @author Tsotne
 */
import java.util.*;
public class LetRec extends Exp {
    final FunDef fd;
    final Exp e;

    LetRec(FunDef fd, Exp e) {
        this.fd = fd;
        this.e = e;
    }

    <E> E accept(ObjVisitor<E> v) {
        return v.visit(this);
    }
    void accept(Visitor v) {
        v.visit(this);
    }

    public FunDef getFd() {
        return fd;
    }

    public Exp getE() {
        return e;
    }

    public String toString(){
        return "letrec" ;
    }
}