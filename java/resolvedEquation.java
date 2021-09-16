/**
 * @author Tsotne
 */

//Equation solved between a variable type and a type
public class resolvedEquation {
    private TVar t1;
    private Type t2;

    public resolvedEquation(TVar t1, Type t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public TVar getT1() {
        return t1;
    }

    public Type getT2() {
        return t2;
    }

    @Override
    public String toString(){
        return t1 + " = " + t2;
    }
}