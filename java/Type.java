/**
 * @author Tsotne
 */

import java.util.ArrayList;
import java.util.List;

abstract class Type {
    private static int x = 0;
    static Type gen() {
        return new TVar("?" + x++);
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract Type clone();

    @Override
    public abstract String toString();

    @Override
    public int hashCode() {
        return x;
    }
}
/*
public class TUnit extends Type {
    @Override
    public boolean equals(Object o){
        return(o instanceof TUnit );
    }

    @Override
    public String toString() {
        return "unit";
    }

    @Override
    public Type clone() {
        return new TUnit();
    }
}

public class TBool extends Type {
    @Override
    public boolean equals(Object o){
        return(o instanceof TBool );
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public Type clone() {
        return new TBool();
    }
}

public class TInt extends Type {
    @Override
    public boolean equals(Object o){
        return(o instanceof TInt );
    }

    @Override
    public String toString() {
        return "int";
    }

    @Override
    public Type clone() {
        return new TInt();
    }
}

public class TFloat extends Type {
    @Override
    public boolean equals(Object o){
        return(o instanceof TFloat );
    }

    @Override
    public String toString() {
        return "float";
    }

    @Override
    public Type clone() {
        return new TFloat();
    }
}

public class TFun extends Type {
    private List<Type> args;
    private Type returnType;

    public List<Type> getArgs() {
        return args;
    }

    public Type getReturnType() {
        return returnType;
    }

    public TFun(){
        this.args = new ArrayList<Type>();
        this.args.add(Type.gen());
        this.returnType = Type.gen();
    }

    public TFun(List<Type> args, Type returnType){
        this.args = new ArrayList<Type>(args);
        if(args.isEmpty()){
            this.args.add(Type.gen());
        }
        this.returnType = returnType;
    }

    public TFun(TFun tf){
        this.args = new ArrayList<Type>();
        for(Type t : tf.args)
            this.args.add(t.clone());
        this.returnType = tf.returnType.clone();
    }

    private TFun(int nb_args){
        this.args = new ArrayList<Type>();
        for(int i=0; i<nb_args; i++)
            this.args.add(Type.gen());
        this.returnType = Type.gen();
    }
    //for parser
    public static TFun gen(int nb_args){
        return new TFun(nb_args);
    }

    @Override
    public String toString() {
        String result = "(fun : ";
        int size = args.size();
        for(int i = 0; i<size; i++){
            result = result + args.get(i) + " -> ";
        }
        result = result + ")";
        return result;
    }

    public boolean equals(Object o) {
        if(o instanceof TFun){
            return (this.args.equals(((TFun)o).args)) && (this.returnType == ((TFun)o).returnType);
        }
        return false;
    }

    public boolean isDefined(){
        for(Type t : this.args){
            if(t instanceof TVar)
                return false;
            }
        return !(this.returnType instanceof TVar);
    }

    @Override
    public Type clone() {
        return new TFun(this);
    }
}

public class TTuple extends Type {
    private List<Type> elem;

    public List<Type> getElem() {
        return elem;
    }

    public TTuple(List<Type> elem) {
        this.elem = elem;
    }

    public TTuple(TTuple tt){
        this.elem = tt.elem;
    }

    @Override
    public String toString() {
        int i = 1;
        String result = "(";
        result = result + elem.get(0);
        while (i < elem.size()) {
            result = result + "," + elem.get(i);
            i++;
        }
        result = result + ")";
        return result;
    }

    @Override
    public Type clone() {
        List<Type> types = new ArrayList<Type>();
        for(Type t : this.elem)
            types.add(t.clone());
        return new TTuple(types);
    }

    public boolean equals(Object o) {
        if (o instanceof TTuple)
            return this.elem.equals(((TTuple) o).elem);

        return false;
    }

    public boolean isDefined() {
        for(Type t : this.elem){
            if(t instanceof TVar)
                return false;
        }
        return true;
    }
}

public class TArray extends Type {
    private Type type;

    public Type getType() {
        return type;
    }
    public TArray(Type t) {
        this.type = t;
    }
    public TArray(){
        this.type = Type.gen();
    }

    public boolean equals(Object o) {
        if(!(o instanceof TArray))
            return false;
        else
            return this.type.equals(((TArray)o).type);
    }

    @Override
    public Type clone() {
        return new TArray(type.clone());
    }
    @Override
    public String toString(){
        return "[" + this.type + "]";
    }
}

public class TClosure extends Type{
    @Override
    public boolean equals(Object o) {
        return (o instanceof TClosure);
    }

    @Override
    public Type clone() {
        return new TClosure();
    }

    @Override
    public String toString(){
        return "closure";
    }
}

public class TVar extends Type {
    String v;
    TVar(String v) {
        this.v = v;
    }
    public TVar(TVar tv){
        this.v = tv.v;
    }
    @Override
    public String toString() {
        return v; 
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof TVar)
            return this.v.equals(((TVar)o).v);
        else return false;
    }
    @Override
    public Type clone() {
        return new TVar(v);
    }
}

*/