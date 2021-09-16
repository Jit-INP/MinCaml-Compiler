/**
 * @author Tsotne
 */

import java.util.ArrayList;
import java.util.List;

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


