/**
 * @author Tsotne
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class alphaCon implements ObjVisitor<Exp>  {
    // Environment of variable names
    private HashMap<String, Stack<String>> env = new HashMap<String, Stack<String>> ();

    // vars in Let/LetRec
    private HashMap<String, Stack<String>> usedVars = new HashMap<String, Stack<String>> ();

    // funcs
    private HashSet<String> funcs = new HashSet<String>();

    private boolean isAfterLet = false;

    @Override
    public Exp visit(Unit e) {
        return e;
    }

    @Override
    public Exp visit(Bool e) {
        return e;
    }

    @Override
    public Exp visit(Int e) {
        return e;
    }

    @Override
    public Exp visit(Float e) {
        return e;
    }

    @Override
    public Exp visit(Not e) {
        return new Not(e.e.accept(this));
    }

    @Override
    public Exp visit(Neg e) {
        return new Neg(e.e.accept(this));
    }

    @Override
    public Exp visit(Add e) {
        return new Add(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(Sub e) {
        return new Sub(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(FNeg e) {
        return new FNeg(e.e.accept(this));
    }

    @Override
    public Exp visit(FAdd e) {
        return new FAdd(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(FSub e) {
        return new FSub(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(FMul e) {
        return new FMul(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(FDiv e) {
        return new FDiv(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(Eq e) {
        return new Eq(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(LE e) {
        return new LE(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(If e) {
        return new If(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
    }

    @Override
    public Exp visit(Let e) {
        Var genVars = new Var(Id.gen());
        Stack<String> stack = env.get(e.id.toString());

        // case where the variable does not exist in the environment
        if(stack == null){
            stack = new Stack<String>();
            env.put(e.id.toString(), stack);
        }

        stack.push(genVars.id.toString()); // add to stack

        Let returnedLet = new Let(genVars.id, e.t, e.e1.accept(this), e.e2.accept(this));
        isAfterLet = true;

        Stack<String> usesOfThisVar = usedVars.get(genVars.id.toString());

        if(usesOfThisVar == null){ // never used
            usesOfThisVar = new Stack<String>();
            usedVars.put(e.id.toString(), usesOfThisVar);
        }

        HashSet<String> set = new HashSet<String>();
        while(!usesOfThisVar.isEmpty()){
            String key = usesOfThisVar.pop();

            if ((stack.size() > 1 && funcs.contains(key)) || !funcs.contains(key)){
                Stack<String> s = env.get(key);
                if (!s.empty() && !set.contains(key)){
                    s.pop();
                    set.add(key);
                }
            }
        }

        return returnedLet;
    }

    @Override
    public Exp visit(Var e) {
        Stack<String> stack = env.get(e.id.toString());

        // case where the variable does not exist in the environment
        if(stack == null){
            stack = new Stack<String>();
            env.put(e.id.toString(), stack);
        }

        if (!stack.empty()){
            if(!isAfterLet){ // var = func
                if (funcs.contains(e.id.toString())){
                    return new Var(new Id(stack.peek()));
                }else{
                    Var var = new Var(Id.gen());
                    stack.push(var.id.toString());
                    return var;
                }
            }else{ // local var
                Stack<String> usesOfThisVar = env.get(e.id.toString());
                if (usesOfThisVar == null){
                    usesOfThisVar = new Stack<String>();
                    env.put(e.id.toString(), usesOfThisVar);
                }
                usesOfThisVar.push(e.id.toString());
                Var var = new Var(new Id(stack.peek()));
                return var;
            }
        }else{
            return e;
        }
    }

    @Override
    public Exp visit(LetRec e) {
        funcs.add(e.fd.id.toString());
        Id newId = Id.gen();
        Stack<String> stack = env.get(e.fd.id.toString());

        if (stack == null) {
            stack = new Stack<String>();
            env.put(e.fd.id.toString(), stack);
        }

        stack.push(newId.toString());
        List<Id> idList = new LinkedList<Id>();

        for (Id arg: e.fd.args){
            Id new_arg = Id.gen();
            Stack<String> argStack = env.get(arg.toString());

            if (argStack == null){
                argStack = new Stack<String>();
                env.put(arg.toString(), argStack);
            }

            argStack.push(new_arg.toString());
            idList.add(new_arg);
        }

        isAfterLet = true;
        Exp exp = e.fd.e.accept(this);
        FunDef funDef = new FunDef(e.fd.id, e.fd.type, idList, exp);

        for (Id arg: e.fd.args){
            Stack<String> s = env.get(arg.toString());
            if (!s.empty()) {
                s.pop();
            }
        }

        isAfterLet = false;
        return new LetRec(funDef, e.e.accept(this));
    }

    @Override
    public Exp visit(App e) {
        Exp app = e.e.accept(this);
        List<Exp> expList = new LinkedList<Exp>();

        for (Exp exp: e.es){
            expList.add(exp.accept(this));
        }

        return new App(app, expList);
    }

    @Override
    public Exp visit(Tuple e) {
        List<Exp> expList = new LinkedList<Exp>();
        isAfterLet = true;

        for (Exp exp: e.es){
            expList.add(exp.accept(this));
        }

        return new Tuple(expList);
    }

    @Override
    public Exp visit(LetTuple e) {
        List<Id> idLists = new LinkedList<Id>();

        for (Id id: e.ids){
            Id newId = Id.gen();
            Stack<String> stack = env.get(id.toString());

            if (stack == null){
                stack = new Stack<String>();
                env.put(id.toString(), stack);
            }

            stack.push(newId.toString());
            idLists.add(newId);
        }

        return new LetTuple(idLists, e.ts, e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(Array e) {
        return new Array(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(Get e) {
        return new Get(e.e1.accept(this), e.e2.accept(this));
    }

    @Override
    public Exp visit(Put e) {
        return new Put(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
    }

}