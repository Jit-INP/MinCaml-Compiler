/******
 * @author Youhana 
 */

import java.util.*;

class Knorm {
    int index = 1, indexif = 1;
    int addTimes = 0;
    Exp wholeExpression, partExp;

    Exp computeKnorm(Exp exp) {
        
        if (exp instanceof Unit) {
            Unit e = (Unit) exp;
            return e;
        } else if (exp instanceof Bool) {
            Bool e = (Bool) exp;
            return e;
        } else if (exp instanceof Float) {
            Float e = (Float) exp;
            return e;
        } else if (exp instanceof Not) {
            Not ee = (Not) exp;
            Not exp2 = new Not(computeKnorm(ee.e));
            return exp2;
        } else if (exp instanceof Neg){
            Neg ee = (Neg) exp;
            Neg exp2 = new Neg(computeKnorm(ee.e));
            return exp2;
        } else if (exp instanceof Int) {
            Int e = (Int) exp;
            return e;
        /*************************************ADD*********************************************/
        } else if (exp instanceof Add) {
            Add e = (Add) exp;
            if (addTimes == 0)
            {
                wholeExpression = e;
                addTimes = 1;
                partExp = (Add) partExp;
            }
            if (e.e1 instanceof Int || e.e1 instanceof Var || e.e1 instanceof App)
            {
                Id id4 = new Id("v"+index);
                TVar tvar4 = new TVar("?0");
                Let exp3 = new Let(id4,tvar4,e.e2,replaceVars(wholeExpression));
                Id id3 = new Id("v"+(index+1));
                TVar tvar3 = new TVar("?0");
                Let exp2 = new Let(id3,tvar3,e.e1,exp3);
                addTimes = 0;
                index  =  index + 2;
                return exp2;
            } else { 
                Id id2 = new Id("v"+index);
                TVar tvar2 = new TVar("?0");
                index  = index + 1;
                //Var variable = new Var(id2);
                Let exp2 = new Let(id2,tvar2,e.e2,computeKnorm(e.e1));
                return exp2;
            }
        /*************************************SUB*********************************************/
        } else if (exp instanceof Sub) {
            Sub e = (Sub) exp;
            if (addTimes == 0)
            {
                wholeExpression = e;
                addTimes = 1;
            }
            if (e.e1 instanceof Int || e.e1 instanceof Var || e.e1 instanceof App)
            {
                Id id4 = new Id("v"+index);
                TVar tvar4 = new TVar("?0");
                Let exp3 = new Let(id4,tvar4,e.e2,replaceVars(wholeExpression));

                Id id3 = new Id("v"+(index+1));
                TVar tvar3 = new TVar("?0");
                Let exp2 = new Let(id3,tvar3,e.e1,exp3);
                addTimes = 0;
                index  =  index + 2;
                return exp2;
            }
            else
            {
                Id id2 = new Id("v"+index);
                TVar tvar2 = new TVar("?0");
                index  = index + 1;
                Let exp2 = new Let(id2,tvar2,e.e2,computeKnorm(e.e1));
                return exp2;
            }
        /*************************************FADD*********************************************/
        } else if (exp instanceof FAdd){
            FAdd e = (FAdd) exp;
            if (addTimes == 0)
            {
                wholeExpression = e;
                addTimes = 1;
            }
            if (e.e1 instanceof Int || e.e1 instanceof Var || e.e1 instanceof App)
            {
                Id id4 = new Id("v"+index);
                TVar tvar4 = new TVar("?0");
                Let exp3 = new Let(id4,tvar4,e.e2,replaceVars(wholeExpression));

                Id id3 = new Id("v"+(index+1));
                TVar tvar3 = new TVar("?0");
                Let exp2 = new Let(id3,tvar3,e.e1,exp3);
                addTimes = 0;
                index  =  index + 2;
                return exp2;
            }
            else
            {
                Id id2 = new Id("v"+index);
                TVar tvar2 = new TVar("?0");
                index  = index + 1;
                Let exp2 = new Let(id2,tvar2,e.e2,computeKnorm(e.e1));
                return exp2;
            }
        /*************************************FSUB*********************************************/
        } else if (exp instanceof FSub){
            FSub e = (FSub) exp;
            if (addTimes == 0)
            {
                wholeExpression = e;
                addTimes = 1;
            }
            if (e.e1 instanceof Int || e.e1 instanceof Var || e.e1 instanceof App)
            {
                Id id4 = new Id("v"+index);
                TVar tvar4 = new TVar("?0");
                Let exp3 = new Let(id4,tvar4,e.e2,replaceVars(wholeExpression));

                Id id3 = new Id("v"+(index+1));
                TVar tvar3 = new TVar("?0");
                Let exp2 = new Let(id3,tvar3,e.e1,exp3);
                addTimes = 0;
                index  =  index + 2;
                return exp2;
            }
            else
            {
                Id id2 = new Id("v"+index);
                TVar tvar2 = new TVar("?0");
                index  = index + 1;
                Let exp2 = new Let(id2,tvar2,e.e2,computeKnorm(e.e1));
                return exp2;
            }
        /*************************************FMUL*********************************************/
        } else if (exp instanceof FMul){
            FMul e = (FMul) exp;
            if (addTimes == 0)
            {
                wholeExpression = e;
                addTimes = 1;
            }
            if (e.e1 instanceof Int || e.e1 instanceof Var)
            {
                Id id4 = new Id("v"+index);
                TVar tvar4 = new TVar("?0");
                Let exp3 = new Let(id4,tvar4,e.e2,replaceVars(wholeExpression));

                Id id3 = new Id("v"+(index+1));
                TVar tvar3 = new TVar("?0");
                Let exp2 = new Let(id3,tvar3,e.e1,exp3);
                addTimes = 0;
                index  =  index + 2;
                return exp2;
            }
            else
            {
                Id id2 = new Id("v"+index);
                TVar tvar2 = new TVar("?0");
                index  = index + 1;
                Let exp2 = new Let(id2,tvar2,e.e2,computeKnorm(e.e1));
                return exp2;
            }
        /*************************************FDIV*********************************************/
        } else if (exp instanceof FDiv){
            FDiv e = (FDiv) exp;
            if (addTimes == 0)
            {
                wholeExpression = e;
                addTimes = 1;
            }
            if (e.e1 instanceof Int || e.e1 instanceof Var || e.e1 instanceof App)
            {
                Id id4 = new Id("v"+index);
                TVar tvar4 = new TVar("?0");
                Let exp3 = new Let(id4,tvar4,e.e2,replaceVars(wholeExpression));

                Id id3 = new Id("v"+(index+1));
                TVar tvar3 = new TVar("?0");
                Let exp2 = new Let(id3,tvar3,e.e1,exp3);
                addTimes = 0;
                index  =  index + 2;
                return exp2;
            }
            else
            {
                Id id2 = new Id("v"+index);
                TVar tvar2 = new TVar("?0");
                index  = index + 1;
                Let exp2 = new Let(id2,tvar2,e.e2,computeKnorm(e.e1));
                return exp2;
            }
        /*************************************LET*********************************************/
        } else if (exp instanceof Let) {
            Let e = (Let) exp;
            Let exp2 = new Let(e.id,e.t,computeKnorm(e.e1),computeKnorm(e.e2));
            return exp2;
        /*************************************VAR*********************************************/
        } else if (exp instanceof Var) {
            Var e = (Var) exp;
            return e;
        /*************************************LETREC*********************************************/
        } else if (exp instanceof LetRec){
            LetRec ee = (LetRec) exp;
            Exp DefFun = computeKnorm(ee.fd.e); 
            FunDef funDefinition = new FunDef(ee.fd.id, ee.fd.type, ee.fd.args, DefFun);
            LetRec exp2 = new LetRec(funDefinition,computeKnorm(ee.e));
            return exp2;
        /****************************************IF*********************************************/
        } else if (exp instanceof If) {
            If e = (If) exp;
            Exp N1 = e.e2;
            Exp N2 = e.e3;
            Var var1 = new Var( new Id("e"+indexif) );
            Var var2 = new Var( new Id("e"+(indexif+1)) );
            indexif = indexif + 2;
            if (e.e1 instanceof Eq){
                Eq N0 = (Eq) e.e1;
                Eq boolExp = new Eq(var1,var2); 
                If newIf = new If(boolExp,computeKnorm(N1),computeKnorm(N2));
                TVar tvar2 = new TVar("?0");
                Let exp2 = new Let(var2.id,tvar2,computeKnorm(N0.e2),newIf);
                Let exp3 = new Let(var1.id,tvar2,computeKnorm(N0.e1),exp2);
                return exp3;
            } else if (e.e1 instanceof LE){
                LE N0 = (LE) e.e1;
                LE boolExp = new LE(var1,var2);
                If newIf = new If(boolExp,computeKnorm(N1),computeKnorm(N2));
                TVar tvar2 = new TVar("?0");
                Let exp2 = new Let(var2.id,tvar2,computeKnorm(N0.e2),newIf);
                Let exp3 = new Let(var1.id,tvar2,computeKnorm(N0.e1),exp2);
                return exp3;
            } else if (e.e1 instanceof Not) {
                Not N00 = (Not) e.e1;
                if (N00.e instanceof Eq){
                    Eq N0 = (Eq) N00.e;
                    Eq equalExp = new Eq(N0.e1,N0.e2);
                    If newIf = new If(equalExp,e.e3,e.e2);
                    Exp exp2 = computeKnorm(newIf);
                    return exp2;
                } else if (N00.e instanceof LE){
                    // Less than only
                    LE N0 = (LE) N00.e;
                    LE equalExp = new LE(N0.e1,N0.e2);
                    If newIf = new If(equalExp,e.e3,e.e2);
                    Exp exp2 = computeKnorm(newIf);
                    return exp2;
                }
            } else {
                Eq equalExp = new Eq(e.e1,new Bool(false));
                If newIf = new If(equalExp,e.e3,e.e2);
                Exp exp2 = computeKnorm(newIf);
                return exp2;
        /*************************************APP***********************************************/    
            }
        } else if (exp instanceof App) {
            App e = (App) exp;
            if (e.es.get(0) instanceof App){
                Exp exp2 = computeApp(e);
                return exp2;
            } else {
                return e;
            }
        }
        return exp;
    }

    /*******************************************************************************************/
    /**********************************computeApp Function**************************************/
    /*******************************************************************************************/

    int indexApp = 0;
    Exp computeApp(Exp exp){
        App e = (App) exp;
        if (e.es.get(0) instanceof App){
            App firstArg = (App) e.es.get(0);
            if (firstArg.e instanceof App){
                ArrayList<Exp> list = new ArrayList<Exp>();
                TVar tvar3 = new TVar("?0");
                Id id3 = new Id("a"+(indexApp));
                Id id4 = new Id("a"+(indexApp+1));
                indexApp = indexApp + 2;
                Var newVar = new Var(id3);
                Var newVar2 = new Var(id4);
                list.add(newVar2);
                App firstArg2 = (App) firstArg.e;
                App newApp = new App(firstArg2.e, firstArg2.es);
                App newApp2 = new App(newVar, firstArg.es);
                App newApp3 = new App(e.e,list);
                Let exp3 = new Let(id4,tvar3,newApp2,newApp3);
                Let exp2 = new Let(id3,tvar3,newApp,exp3);
                return exp2;
            } else {
                ArrayList<Exp> list = new ArrayList<Exp>();
                TVar tvar3 = new TVar("?0");
                Id id3 = new Id("a"+(indexApp));
                Var newVar = new Var(id3);
                indexApp++;
                list.add(newVar);
                App newApp = new App(e.e,list);
                Let exp2 = new Let(id3,tvar3,computeApp(e.es.get(0)), newApp);
                return exp2;
            }
        } else {
            ArrayList<Exp> list = new ArrayList<Exp>();
            TVar tvar3 = new TVar("?0");
            Id id3 = new Id("a"+(indexApp));
            Var newVar = new Var(id3);
            indexApp++;
            list.add(newVar);
            App newApp = new App(e.e,list);
            if (e.es.size()==2){
                Id id4 = new Id("a"+(indexApp));
                indexApp++;
                Var newVar2 = new Var(id4); 
                list.add(newVar2);
                Let exp2 = new Let(id4,tvar3,e.es.get(1), newApp);
                Let exp3 = new Let(id3,tvar3,e.es.get(0), exp2);
                return exp3;
            } else {
                Let exp3 = new Let(id3,tvar3,e.es.get(0), newApp);
                return exp3;
            }
        }
    }


    /******************************************************************************************/
    /**********************************ReplaceVars Function************************************/
    /******************************************************************************************/

    int indexx = 1;

    Exp replaceVars(Exp exp){
        if (exp instanceof Unit) {
            Unit e = (Unit) exp;
            return e;
        } else if (exp instanceof Bool) {
            Id id2 = new Id("v"+indexx);
            indexx++;
            Var variable = new Var(id2);
            return variable;
        } else if (exp instanceof Int) {
            Id id2 = new Id("v"+indexx);
            indexx++;
            Var variable = new Var(id2);
            return variable;
        } else if (exp instanceof Add) {
            Add e = (Add) exp;
            Id id2 = new Id("v"+indexx);
            indexx++;
            Var variable = new Var(id2);
            Add rep = new Add(replaceVars(e.e1),variable);
            return rep;
        } else if (exp instanceof Sub) {
            Sub e = (Sub) exp;
            Id id2 = new Id("v"+indexx);
            indexx++;
            Var variable = new Var(id2);
            Sub rep = new Sub(replaceVars(e.e1),variable);
            return rep;
        } else if (exp instanceof FAdd){
            FAdd e = (FAdd) exp;
            Id id2 = new Id("v"+index);
            indexx++;
            Var variable = new Var(id2);
            FAdd rep = new FAdd(replaceVars(e.e1),variable);
            return rep;
        } else if (exp instanceof FSub){
            FSub e = (FSub) exp;
            Id id2 = new Id("v"+index);
            indexx++;
            Var variable = new Var(id2);
            FSub rep = new FSub(replaceVars(e.e1),variable);
            return rep;
        } else if (exp instanceof FMul){
            FMul e = (FMul) exp;
            Id id2 = new Id("v"+index);
            indexx++;
            Var variable = new Var(id2);
            FMul rep = new FMul(replaceVars(e.e1),variable);
            return rep;
        } else if (exp instanceof FDiv){
            FDiv e = (FDiv) exp;
            Id id2 = new Id("v"+index);
            indexx++;
            Var variable = new Var(id2);
            FDiv rep = new FDiv(replaceVars(e.e1),variable);
            return rep;
        } else if (exp instanceof Var) {
            Id id2 = new Id("v"+indexx);
            indexx++;
            Var variable = new Var(id2);
            return variable;
        } else if (exp instanceof App){
            Id id2 = new Id("v"+indexx);
            indexx++;
            Var variable = new Var(id2);
            return variable;
        }
        return exp;
    }

}
