/*********
 * Author: Jit Chatterjee
 *********/


import java.util.*;

class VariableVisitor implements ObjVisitor<String>{

    String vars = "";

    public String visit(Unit e) {
        return null;
    }

    public String visit(Bool e) {
        return null;
    }

    public String visit(Int e) {
        return null;
    }

    public String visit(Float e) {
        return null;
    }

    public String visit(Not e) {
        return e.e.accept(this) + null;
    }

    public String visit(Neg e) {
        return e.e.accept(this) + null;
    }

    public String visit(Add e) {
        String res1 = e.e1.accept(this);
        String res2 = e.e2.accept(this);
        return null ;
    }

    public String visit(Sub e) {
        String res1 = e.e1.accept(this);
        String res2 = e.e2.accept(this);
        return null ;
   }

    public String visit(FNeg e){
        return null;
    }

    public String visit(FAdd e){
        String res1 = e.e1.accept(this);
        String res2 = e.e2.accept(this);
        return null;
    }

    public String visit(FSub e){
        String res1 = e.e1.accept(this);
        String res2 = e.e2.accept(this);
        return null;
    }

    public String visit(FMul e) {
        String res1 = e.e1.accept(this);
        String res2 = e.e2.accept(this);
        return null;
     }

    public String visit(FDiv e){
        String res1 = e.e1.accept(this);
        String res2 = e.e2.accept(this);
        return null;
    }

    public String visit(Eq e){
        String res1 = e.e1.accept(this);
        String res2 = e.e2.accept(this);
        return null;
    }

    public String visit(LE e){
        String res1 = e.e1.accept(this);
        String res2 = e.e2.accept(this);
        return null;
    }

    public String visit(If e){
        String res1 = e.e1.accept(this);
        String res2 = e.e2.accept(this);
        String res3 = e.e3.accept(this);
        return null;
    }

    public String visit(Let e) {
        String res1 = e.e1.accept(this);
        String res2 = e.e2.accept(this);
        vars += e.id.toString() + " ";
        return vars;
    }

    public String visit(Var e){
        return null;
    }

    public String visit(LetRec e){
        String res1 = e.e.accept(this);
        String res2 = e.fd.e.accept(this);
        vars += e.fd.id.toString() + " ";
        return vars;
    }

    public String visit(App e){
        String res1 = e.e.accept(this);
        return null;
    }

    public String visit(Tuple e){
        String res1 =null;
        for (Exp exp : e.es) {
            String res = exp.accept(this);
        }
        return null;
    }

    public String visit(LetTuple e){
        String res1 = e.e1.accept(this);
        String res2 = e.e2.accept(this);
        return null;
    }

    public String visit(Array e){
        String res1 = e.e1.accept(this);
        String res2 = e.e2.accept(this);
        return null;
    }

    public String visit(Get e){
        String res1 = e.e1.accept(this);
        String res2 = e.e2.accept(this);
        return null;
    }

    public String visit(Put e){
        String res1 = e.e1.accept(this);
        String res2 = e.e2.accept(this);
        String res3 = e.e3.accept(this);
        return null;
    }
}