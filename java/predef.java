/**
 * @author Tsotne
 */

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public abstract class predef {
    private static Map<String,Type> predef_m;
    private static List<String> predef_s;

    public static void initialisation(){
        predef_m = new Hashtable<String,Type>();
        predef_s = new ArrayList<String>();
        ArrayList<Type> args = new ArrayList<Type>();
        args.add(new TInt());
        predef_m.put("print_int",new TFun(args,new TUnit()));
        predef_m.put("float_of_int",new TFun(args,new TFloat()));
        args = new ArrayList<Type>();
        args.add(new TUnit());
        predef_m.put("print_newline",new TFun(args,new TUnit()));
        args = new ArrayList<Type>();
        args.add(new TFloat());
        predef_m.put("sin",new TFun(args,new TFloat()));
        predef_m.put("cos",new TFun(args,new TFloat()));
        predef_m.put("sqrt",new TFun(args,new TFloat()));
        predef_m.put("abs_float",new TFun(args,new TFloat()));
        predef_m.put("int_of_float",new TFun(args,new TInt()));
        predef_m.put("truncate",new TFun(args,new TInt()));
        predef_s.add("print_int");
        predef_s.add("float_of_int");
        predef_s.add("print_newline");
        predef_s.add("sin");
        predef_s.add("cos");
        predef_s.add("sqrt");
        predef_s.add("abs_float");
        predef_s.add("int_of_float");
        predef_s.add("truncate");
    }

    public static Map<String, Type> getPredef_m() {
        return predef_m;
    }

    public static List<String> getPredef_s() {
        return predef_s;
    }
}