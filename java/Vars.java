/*
@author Youhana
*/
import java.util.*;

class Vars {
    ArrayList<String> varList = new ArrayList<String>();

    ArrayList<String> computeVars(Exp exp) {
        if (exp instanceof Unit) {
            Unit e = (Unit) exp;
        } else if (exp instanceof Bool) {
            Bool e = (Bool) exp;
        } else if (exp instanceof Int) {
            Int e = (Int) exp;
            //System.out.println(e.i);
        } else if (exp instanceof Bool) {
            Bool e = (Bool) exp;
        }else if (exp instanceof Float) {
            Float e = (Float) exp;
        }
        else if (exp instanceof Not) {
            Not e = (Not) exp;
            computeVars(e.e);
        } else if (exp instanceof Neg) {
            Neg e = (Neg) exp;
            computeVars(e.e);
        } else if (exp instanceof Add) {
            Add e = (Add) exp;
            computeVars(e.e1);
            computeVars(e.e2);
        } else if (exp instanceof Sub) {
            Sub e = (Sub) exp;
            computeVars(e.e1); 
            computeVars(e.e2);
        } else if (exp instanceof FNeg) {
            FNeg e = (FNeg) exp;
            computeVars(e.e);
        } else if (exp instanceof FAdd) {
            FAdd e = (FAdd) exp;
            computeVars(e.e1); 
            computeVars(e.e2);
        } else if (exp instanceof FSub) {
            FSub e = (FSub) exp;
            computeVars(e.e1);
            computeVars(e.e2);
        } else if (exp instanceof FMul) {
            FMul e = (FMul) exp;
            computeVars(e.e1); 
            computeVars(e.e2);
        } else if (exp instanceof FDiv) {
            FDiv e = (FDiv) exp;
            computeVars(e.e1); 
            computeVars(e.e2);
        } else if (exp instanceof Eq) {
            Eq e = (Eq) exp;
            computeVars(e.e1); 
            computeVars(e.e2);
        } else if (exp instanceof LE) {
            LE e = (LE) exp;
            computeVars(e.e1); 
            computeVars(e.e2);
        } else if (exp instanceof If) {
            If e = (If) exp;
            computeVars(e.e1); 
            computeVars(e.e2); 
            computeVars(e.e3);
        } else if (exp instanceof Let) {
            Let e = (Let) exp;
            varList.add(e.id.id);
            computeVars(e.e1); 
            computeVars(e.e2);
        } else if (exp instanceof Var) {
            //***************************************************** */
            Var e = (Var) exp;
            if (!varList.contains(e.id.id)){
                varList.add(e.id.id);
            }
            //***************************************************** */
        } else if (exp instanceof LetRec) {
            LetRec e = (LetRec) exp;
            computeVars(e.e); 
            computeVars(e.fd.e);
        } else if (exp instanceof App) {
            App e = (App) exp;
            computeVars(e.e);
            for (Exp e1 : e.es) {
               computeVars(e1);
            }
        } else if (exp instanceof Tuple) {
            Tuple e = (Tuple) exp;
            for (Exp e1 : e.es) {
               computeVars(e1);
            }
        } else if (exp instanceof LetTuple) {
            LetTuple e = (LetTuple) exp;
            computeVars(e.e1); 
            computeVars(e.e2);
        } else if (exp instanceof Array) {
            Array e = (Array) exp;
            computeVars(e.e1); 
            computeVars(e.e2);
        } else if (exp instanceof Get) {
            Get e = (Get) exp;
            computeVars(e.e1); 
            computeVars(e.e2);
        }  else if (exp instanceof Put) {
            Put e = (Put) exp;
            computeVars(e.e1); 
            computeVars(e.e2); 
            computeVars(e.e3);
        } else {
            // shouldn't happen
            assert(false);
        }
        return varList;
    }
}
