/**
 * @author Tsotne
 */


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class typeChecker {

    private boolean ok;
    private Hashtable<String,Type> symbols;

    public typeChecker(){
        symbols = new Hashtable<String,Type>();
    }
    public boolean check(Exp AST){

        Map <String,Type> predefined = predef.getPredef_m();

        this.ok = true;
        List<typeEquation> equations = generateEquations(AST,predefined,new TUnit()," ");
        if(!ok){
            System.exit(1);
        }
        solveEquations(equations);

        if(!ok){
            System.exit(1);
        }
        return ok;
    }

    public Hashtable<String,Type> getSymbols(){
        return this.symbols;
    }

    private List<typeEquation> generateEquations(Exp exp, Map<String,Type> env, Type t, String context){

        List<typeEquation> result = new ArrayList<typeEquation>();
        
        if(exp instanceof Var){
            Var varexp = (Var) exp;
            Type type_var = env.get(varexp.getId().getId());
            if(type_var == null){
                System.err.println("Error : Unbound value " + varexp.getId().getId());
                ok = false;
            }
            typeEquation teq = new typeEquation(type_var,t);
            result.add(teq); 
            
        }
        else if(exp instanceof Unit){
            typeEquation teq = new typeEquation(new TUnit(),t);
            result.add(teq);
        }
        else if(exp instanceof Bool){
            typeEquation teq = new typeEquation(new TBool(),t);
            result.add(teq);
        }
        else if(exp instanceof Int){
            typeEquation teq = new typeEquation(new TInt(),t);
            result.add(teq);
        }
        else if(exp instanceof Float){
            typeEquation teq = new typeEquation(new TFloat(),t);
            result.add(teq);
        }
        else if(exp instanceof Not){
            Not notexp = (Not) exp;
            typeEquation teq = new typeEquation(new TBool(),t);
            result.add(teq);
            List<typeEquation> l = generateEquations(notexp.getE(), env, new TBool(),context);
            result.addAll(l);
        }
        else if(exp instanceof Neg){
            Neg negexp = (Neg) exp;
            typeEquation teq = new typeEquation(new TInt(),t);
            result.add(teq);
            List<typeEquation> l = generateEquations(negexp.getE(), env, new TInt(),context);
            result.addAll(l);
        }
        else if(exp instanceof FNeg){
            FNeg fnegexp = (FNeg) exp;
            typeEquation teq = new typeEquation(new TFloat(),t);
            result.add(teq);
            List<typeEquation> l = generateEquations(fnegexp.getE(), env, new TFloat(),context);
            result.addAll(l);
        }
        else if(exp instanceof Add){
            Add addexp = (Add) exp;
            typeEquation teq = new typeEquation(new TInt(),t);
            result.add(teq);
            List<typeEquation> l1 = generateEquations(addexp.getE1(), env, new TInt(),context);
            List<typeEquation> l2 = generateEquations(addexp.getE2(), env, new TInt(),context);
            result.addAll(l1);
            result.addAll(l2);
        }
        else if(exp instanceof Sub){
            Sub subexp = (Sub) exp;
            typeEquation teq = new typeEquation(new TInt(),t);
            result.add(teq);
            List<typeEquation> l1 = generateEquations(subexp.getE1(), env, new TInt(),context);
            List<typeEquation> l2 = generateEquations(subexp.getE2(), env, new TInt(),context);
            result.addAll(l1);
            result.addAll(l2);
        }
        else if(exp instanceof FAdd){
            FAdd faddexp = (FAdd) exp;
            typeEquation teq = new typeEquation(new TFloat(),t);
            result.add(teq);
            List<typeEquation> l1 = generateEquations(faddexp.getE1(), env, new TFloat(),context);
            List<typeEquation> l2 = generateEquations(faddexp.getE2(), env, new TFloat(),context);
            result.addAll(l1);
            result.addAll(l2);
        }
        else if(exp instanceof FSub){
            FSub fsubexp = (FSub) exp;
            typeEquation teq = new typeEquation(new TFloat(),t);
            result.add(teq);
            List<typeEquation> l1 = generateEquations(fsubexp.getE1(), env, new TFloat(),context);
            List<typeEquation> l2 = generateEquations(fsubexp.getE2(), env, new TFloat(),context);
            result.addAll(l1);
            result.addAll(l2);
        }
        else if(exp instanceof FMul){
            FMul fmulexp = (FMul) exp;
            typeEquation teq = new typeEquation(new TFloat(),t);
            result.add(teq);
            List<typeEquation> l1 = generateEquations(fmulexp.getE1(), env, new TFloat(),context);
            List<typeEquation> l2 = generateEquations(fmulexp.getE2(), env, new TFloat(),context);
            result.addAll(l1);
            result.addAll(l2);
        }
        else if(exp instanceof FDiv){
            FDiv fdivexp = (FDiv) exp;
            typeEquation teq = new typeEquation(new TFloat(),t);
            result.add(teq);
            List<typeEquation> l1 = generateEquations(fdivexp.getE1(), env, new TFloat(),context);
            List<typeEquation> l2 = generateEquations(fdivexp.getE2(), env, new TFloat(),context);
            result.addAll(l1);
            result.addAll(l2);
        }
        else if(exp instanceof If){
            If ifexp = (If) exp;
            List<typeEquation> l = generateEquations(ifexp.getE1(), env, new TBool(),context);
            List<typeEquation> l1 = generateEquations(ifexp.getE2(), env, t,context);
            List<typeEquation> l2 = generateEquations(ifexp.getE3(), env, t,context);
            result.addAll(l);
            result.addAll(l1);
            result.addAll(l2);
        }
        else if(exp instanceof Let){
            Let letexp = (Let) exp;
            Type type_id = letexp.getT();
            List<typeEquation> l1 = generateEquations(letexp.getE1(), env, type_id, context);
            Map<String,Type> env2 = new Hashtable<String,Type>(env);
            env2.put(letexp.getId().getId(), type_id);
            symbols.put(context + letexp.getId().getId(), type_id);
            List<typeEquation> l2 = generateEquations(letexp.getE2(), env2, t, context);
            result.addAll(l1);
            result.addAll(l2);
        }
        else if(exp instanceof LetRec){
            LetRec letrecexp = (LetRec) exp;
            TFun type_fun = (TFun) letrecexp.getFd().getType();
            String id_fun = letrecexp.getFd().getId().getId();
            List<Id> ids_args = letrecexp.getFd().getArgs();
            Map<String,Type> env2 = new Hashtable<String,Type>(env);
            String fun_context = context + id_fun + " ";
            for(int i = 0; i<ids_args.size(); i++){
                //FOR DEBUG PURPOSE
                //System.out.println(letrecexp.getFd().getE());
                //System.out.println(type_fun.getArgs().get(i));
                env2.put(ids_args.get(i).getId(), type_fun.getArgs().get(i));
                symbols.put(fun_context + ids_args.get(i).getId(), type_fun.getArgs().get(i));
            }
            env2.put(id_fun, type_fun);

            List<typeEquation> l1 = generateEquations(letrecexp.getFd().getE(), env2, type_fun.getReturnType(), fun_context);

            Map<String,Type> env3 = new Hashtable<String,Type>(env);
            env3.put(id_fun, type_fun);
            symbols.put(context + id_fun, type_fun);
            List<typeEquation> l2 = generateEquations(letrecexp.getE(), env3, t, context);
            result.addAll(l1);
            result.addAll(l2);
        }
        else if(exp instanceof App){
            App appexp = (App) exp;
            List<Exp> args = appexp.getEs();
            List<Type> types_args = new ArrayList<Type>();
            for(Exp arg : args){
                Type type_arg = Type.gen();
                types_args.add(type_arg);
                List<typeEquation> l = generateEquations(arg, env, type_arg, context);
                result.addAll(l);
            }
            Type type_retour = Type.gen();
            typeEquation teq = new typeEquation(type_retour,t);
            result.add(teq);
            Type type_fun = new TFun(types_args,type_retour);
            List<typeEquation> l = generateEquations(appexp.getE(), env, type_fun, context);
            result.addAll(l);
        }
        else if(exp instanceof Eq){
            Eq eqexp = (Eq) exp;
            typeEquation teq = new typeEquation(new TBool(),t);
            result.add(teq);
            Type type_eq = Type.gen();
            List<typeEquation> l1 = generateEquations(eqexp.getE1(), env, type_eq, context);
            List<typeEquation> l2 = generateEquations(eqexp.getE2(), env, type_eq, context);
            result.addAll(l1);
            result.addAll(l2);
        }
        else if(exp instanceof LE){
            LE leexp = (LE) exp;
            typeEquation teq = new typeEquation(new TBool(),t);
            result.add(teq);
            Type type_le = Type.gen();
            List<typeEquation> l1 = generateEquations(leexp.getE1(), env, type_le, context);
            List<typeEquation> l2 = generateEquations(leexp.getE2(), env, type_le, context);
            result.addAll(l1);
            result.addAll(l2);
        }
        else if(exp instanceof Tuple){
            Tuple tupleexp = (Tuple) exp;
            List<Type> liste_type = new ArrayList<Type>();
            for(Exp elem : tupleexp.getEs()){
                Type type_elem = Type.gen();
                liste_type.add(type_elem);
                List<typeEquation> l = generateEquations(elem, env, type_elem, context);
                result.addAll(l);
            }
            result.add(new typeEquation(new TTuple(liste_type),t));
        }
        else if(exp instanceof LetTuple){
            LetTuple lettupleexp = (LetTuple) exp;
            List<Type> types_tuple = lettupleexp.getTs();
            List<Id> ids_tuple = lettupleexp.getIds();
            List<typeEquation> l1 = generateEquations(lettupleexp.getE1(), env, new TTuple(types_tuple), context);
            Map<String,Type> env2 = new Hashtable<String,Type>(env);
            for(int i = 0; i<types_tuple.size(); i++){
                env2.put(ids_tuple.get(i).getId(), types_tuple.get(i));
            }
            List<typeEquation> l2 = generateEquations(lettupleexp.getE2(), env2, t, context);
            result.addAll(l1);
            result.addAll(l2);
        }
        else if(exp instanceof Array){
            Array arrayexp = (Array) exp;
            Type type_array = Type.gen();
            typeEquation teq = new typeEquation(new TArray(type_array),t);
            result.add(teq);
            List<typeEquation> l1 = generateEquations(arrayexp.getE1(), env, new TInt(), context);
            List<typeEquation> l2 = generateEquations(arrayexp.getE2(), env, type_array, context);
            result.addAll(l1);
            result.addAll(l2);
        }
        else if(exp instanceof Get){
            Get getexp = (Get) exp;
            Type type_array = Type.gen();
            typeEquation teq = new typeEquation(type_array,t);
            result.add(teq);
            List<typeEquation> l1 = generateEquations(getexp.getE1(), env, new TArray(type_array), context);
            List<typeEquation> l2 = generateEquations(getexp.getE2(), env, new TInt(), context);
            result.addAll(l1);
            result.addAll(l2);
        }
        else if(exp instanceof Put){
            Put putexp = (Put) exp;
            Type type_array = Type.gen();
            typeEquation teq = new typeEquation(new TUnit(),t);
            result.add(teq);
            List<typeEquation> l1 = generateEquations(putexp.getE1(), env, new TArray(type_array), context);
            List<typeEquation> l2 = generateEquations(putexp.getE2(), env, new TInt(), context);
            List<typeEquation> l3 = generateEquations(putexp.getE3(), env, type_array, context);
            result.addAll(l1);
            result.addAll(l2);
            result.addAll(l3);
        }
       
        return result;
    }

    private void solveEquations(List<typeEquation> equations) {
        if(equations.isEmpty())
            return;

        typeEquation current = equations.get(0);
        equations.remove(0);

        Type t1 = current.getT1();
        Type t2 = current.getT2();
        String error_msg = "Error : incompatible types " + t1 + " and " + t2;
        Class<?> c1 = t1.getClass();
        Class<?> c2 = t2.getClass();
        if(c1.equals(c2)){
            if((t1 instanceof TInt) || (t1 instanceof TBool) || (t1 instanceof TFloat) || (t1 instanceof TUnit)){
                solveEquations(equations);
            }
            else if(t1 instanceof TVar){
                if(((TVar)t1).equals(((TVar)t2))){
                    //?a = ?a
                    solveEquations(equations);
                }
                else{
                    //?a = ?b
                    equations = replaceAll(equations,(TVar) t1,t2);
                    syncSym(new resolvedEquation((TVar)t1,t2));
                    solveEquations(equations);
                }
            }
            else if(t1 instanceof TArray){
                TArray ta1 = (TArray) t1;
                TArray ta2 = (TArray) t2;
                //replace the current equation by an equation between the two types of arrays
                equations.add(new typeEquation(ta1.getType(),ta2.getType()));
                solveEquations(equations);
            }
            else if(t1 instanceof TFun){
                TFun tf1 = (TFun) t1;
                TFun tf2 = (TFun) t2;
                List<Type> l1 = tf1.getArgs();
                List<Type> l2 = tf2.getArgs();

                if(l1.size() != l2.size()){
                    System.err.println(error_msg);
                    ok = false;
                }
                else{
                    for(int i = 0; i<l1.size(); i++){
                        equations.add(new typeEquation(l1.get(i),l2.get(i)));
                    }
                    equations.add(new typeEquation(tf1.getReturnType(),tf2.getReturnType()));
                    solveEquations(equations);
                }
            }
            else{ // t1 instanceof TTuple
                TTuple tt1 = (TTuple) t1;
                TTuple tt2 = (TTuple) t2;
                List<Type> l1 = tt1.getElem();
                List<Type> l2 = tt2.getElem();
                if(l1.size() != l2.size()){
                    System.err.println(error_msg);
                    ok = false;
                }
                else{
                    for(int i = 0; i<l1.size(); i++){
                        equations.add(new typeEquation(l1.get(i),l2.get(i)));
                    }
                    solveEquations(equations);
                }
            }
        }
        else{
            // t1 != t2
            if(t2 instanceof TVar){
                // If t2 is TVar we exchange it with t2
                TVar tmp = new TVar(((TVar) t2)+"");
                t2 = t1;
                t1 = tmp;
            }
            if(t1 instanceof TVar){
                if(t2 instanceof TFun){
                    if(((TFun)t2).isDefined()){
                        equations = replaceAll(equations,(TVar) t1,t2);
                        syncSym(new resolvedEquation((TVar)t1,t2));
                    }
                    else
                        equations.add(current);
                }
                else if(t2 instanceof TArray){
                    if(!(((TArray)t2).getType() instanceof TVar)){
                        equations = replaceAll(equations,(TVar) t1,t2);
                        syncSym(new resolvedEquation((TVar)t1,t2));
                    }
                    else
                        equations.add(current);
                }
                else if(t2 instanceof TTuple){
                    if(((TTuple)t2).isDefined()){
                        equations = replaceAll(equations,(TVar) t1,t2);
                        syncSym(new resolvedEquation((TVar)t1,t2));
                    }
                    else
                        equations.add(current);
                }
                else{
                    equations = replaceAll(equations,(TVar) t1,t2);
                    syncSym(new resolvedEquation((TVar)t1,t2));
                }
                solveEquations(equations);
            }
            else{
                System.err.println(error_msg);
                ok = false;
            }
        }
    }


    private Type replace(Type t, TVar t1, Type t2){
        if(t == null)
            return t;

        if(t.equals(t1))
            return t2;
        else if(t instanceof TArray){
            if(((TArray)t).getType().equals(t1))
                return new TArray(t2);
            else
                return t;
        }
        else if(t instanceof TFun){
            List<Type> l = ((TFun)t).getArgs();
            List<Type> new_l = new ArrayList<Type>();
            for(Type type : l){
                if(type.equals(t1))
                    new_l.add(t2);
                else
                    new_l.add(type);
            }
            Type newReturnType;
            Type oldReturnType = ((TFun)t).getReturnType();
            if(oldReturnType.equals(t1))
                newReturnType = t2;
            else
                newReturnType = oldReturnType;
            return new TFun(new_l,newReturnType);
        }
        else if(t instanceof TTuple){
            List<Type> l = ((TTuple)t).getElem();
            List<Type> new_l = new ArrayList<Type>();
            for(Type type : l){
                if(type.equals(t1))
                    new_l.add(t2);
                else
                    new_l.add(type);
            }
            return new TTuple(new_l);
        }
        else
            return t;
    }

    private List<typeEquation> replaceAll(List<typeEquation> equations, TVar t1, Type t2) {
        List<typeEquation> result = new ArrayList<typeEquation>();
        for(typeEquation eq : equations)
            result.add(new typeEquation(replace(eq.getT1(),t1,t2),replace(eq.getT2(),t1,t2)));

        return result;
    }

    private void syncSym(resolvedEquation req) {
        TVar current = req.getT1();
        Enumeration<String> keys = symbols.keys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            Type currentSym = symbols.get(key);
            symbols.put(key, replace(currentSym,current,req.getT2()));
        }
    }

}