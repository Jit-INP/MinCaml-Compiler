/**
 * @author Tsotne
 */
import java.util.*;
public class FunDef {
    final Id id;
    final Type type;
    final List<Id> args;
    final Exp e;

    FunDef(Id id, Type t, List<Id> args, Exp e) {
        this.id = id;
        this.type = t;
        this.args = args;
        this.e = e;
    }

    public Id getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public List<Id> getArgs() {
        return args;
    }

    public Exp getE() {
        return e;
    }
}