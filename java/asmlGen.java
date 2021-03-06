/**
 * @author Tsotne
 */
import java.util.*;

public class asmlGen implements ObjVisitor<String> {

    private static boolean inApp = false ;
    private static boolean binaryOp = false ;
    private static boolean not = false ;
    private static boolean iff = false ;

    static String asml;
    static String declaration ="";
    static String declarationFloat ="";
    static String entryPoint ="\nlet _ = \n\t";

    static int cp ;
    private static int vn = 0 ;
    private static boolean defined = false ;

    public asmlGen()
    {
        cp = 0 ;
    }

    public String newVar(int c){
        return String.format("v%s", c);
    }

    public String visit(Unit e) {
        return e.toString();
    }

    public String visit(Bool e) {
        if(e.b == false) {
            return String.format("%s",0);
        }
        return String.format("%s",1);
    }

    public String visit(Int e) {
        return String.format("%s",e.i);
    }

    public String visit(Float e) {
        String s = String.format("%.2f", e.f);
        s=s.replace(',','.');
        return s;
    }

    public String visit(Not e) {
        return String.format("%s",e.e.accept(this));
    }

    public String visit(Neg e) {
        String res ="";
        if(e.e instanceof Var){
            res += String.format("neg %s",e.e.accept(this));
        } else {
            asmlGen.entryPoint += String.format("\n\tlet %s = %s in ", newVar(vn),e.e.accept(this));
            res += String.format("neg %s", newVar(vn));
            vn++;
        }
        return res ;
    }

    public String visit(FNeg e) {
        String res ="";
        if(e.e instanceof Var){
            res += String.format("fneg %s",e.e.accept(this));
        } else {
            String v1 = newVar(vn++);
            asmlGen.declarationFloat += String.format("\nlet _%s = %s ",v1,e.e.accept(this));
            asmlGen.entryPoint += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s +0) in",v1,v1, newVar(vn),v1);
            res += String.format("fneg %s", newVar(vn));
            vn++;
        }
        return res ;
    }

    public String visit(Add e) {
        String res ="";
        if(!defined){
            if(e.e1 instanceof Var){
                if(e.e2 instanceof Var){
                    res += String.format("add %s %s",e.e1.accept(this), e.e2.accept(this));
                } else {
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in ", newVar(vn),e.e2.accept(this));
                    res += String.format("add %s %s ", newVar(vn),e.e1.accept(this));
                    vn++;
                }
            } else {
                if(e.e2 instanceof Var){
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in ", newVar(vn),e.e1.accept(this));
                    res += String.format("add %s %s ", newVar(vn),e.e2.accept(this));
                    vn++;
                } else {
                    String v1= newVar(vn);
                    vn++;
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in \n\tlet %s = %s in ",v1,e.e2.accept(this), newVar(vn),e.e1.accept(this));
                    res += String.format("add %s %s ",v1, newVar(vn));
                    vn++;
                }
            }
            return res ;
        }
        else {
            if(e.e1 instanceof Int) {
                if(e.e2 instanceof Var){
                    return String.format("add %s %s ",e.e2.accept(this),e.e1.accept(this));
                } else {
                    String v1= newVar(vn);
                    vn++;
                    asmlGen.declaration += String.format("\n\tlet %s = %s in \n\tlet %s = %s in ",v1,e.e2.accept(this), newVar(vn),e.e1.accept(this));
                    res += String.format("add %s %s ",v1, newVar(vn));
                    vn++;
                }
            }
            return String.format("\nadd %s %s",e.e1.accept(this),e.e2.accept(this));
        }
    }

    public String visit(Sub e) {
        String res ="";
        if(!defined){
            if(e.e1 instanceof Var){
                if(e.e2 instanceof Var){
                    res += String.format("sub %s %s",e.e1.accept(this), e.e2.accept(this));
                } else {
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in ", newVar(vn),e.e2.accept(this));
                    res += String.format("sub %s %s", newVar(vn),e.e1.accept(this));
                    vn++;
                }
            } else {
                if(e.e2 instanceof Var){
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in ", newVar(vn),e.e1.accept(this));
                    res += String.format("sub %s %s", newVar(vn),e.e2.accept(this));
                    vn++;
                } else {
                    String v1= newVar(vn);
                    vn++;
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in \n\tlet %s = %s in ",v1,e.e2.accept(this), newVar(vn),e.e1.accept(this));
                    res += String.format("sub %s %s",v1, newVar(vn));
                    vn++;
                }
            }
            return res ;
        }
        else{
            if(e.e1 instanceof Int) {
                if(e.e2 instanceof Var){
                    return String.format("sub %s %s",e.e2.accept(this),e.e1.accept(this));
                } else {
                    String v1= newVar(vn);
                    vn++;
                    asmlGen.declaration += String.format("\n\tlet %s = %s in \n\tlet %s = %s in ",v1,e.e2.accept(this), newVar(vn),e.e1.accept(this));
                    res += String.format("sub %s %s",v1, newVar(vn));
                    vn++;
                }
            }
            return String.format("sub %s %s",e.e1.accept(this),e.e2.accept(this));
        }
    }


    public String visit(FAdd e){
        String res ="";
        if(!defined){
            if(e.e1 instanceof Var){
                if(e.e2 instanceof Var){
                    res += String.format("fadd %s %s",e.e1.accept(this), e.e2.accept(this));
                } else {
                    String v = newVar(vn++);
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s",v,e.e2.accept(this));
                    res += String.format("fadd _%s %s", newVar(vn),e.e1.accept(this));
                    vn++;
                }
            } else {
                if(e.e2 instanceof Var){
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s", newVar(vn),e.e1.accept(this));
                    res += String.format("fadd _%s %s", newVar(vn),e.e2.accept(this));
                    vn++;
                } else {
                    String v1= newVar(vn);
                    vn++;
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), newVar(vn),e.e1.accept(this));
                    res += String.format("fadd _%s _%s",v1, newVar(vn));
                    vn++;
                }
            }
            return res ;
        }
        else {
            if(e.e1 instanceof Var){
                if(e.e2 instanceof Var){
                    res += String.format("fadd %s %s",e.e1.accept(this), e.e2.accept(this));
                } else {
                    String v = newVar(vn++);
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s",v,e.e2.accept(this));
                    asmlGen.declaration += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in",v,v, newVar(vn),v);
                    res += String.format("fadd %s %s", newVar(vn),e.e1.accept(this));
                    vn++;
                }
            } else {
                if(e.e2 instanceof Var){
                    String v = newVar(vn++);
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s",v,e.e1.accept(this));
                    asmlGen.declaration += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in",v,v, newVar(vn),v);
                    res += String.format("fadd %s %s", newVar(vn),e.e2.accept(this));
                    vn++;
                } else {
                    String v1= newVar(vn++);
                    vn++;
                    String v2 = newVar(vn);
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), v2,e.e1.accept(this));
                    asmlGen.declaration += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in \n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in",v1,v1, newVar(vn),v1,v2,v2, newVar(vn++),v2);
                    res += String.format("fadd %s %s", newVar(vn), newVar(vn--));
                    vn++;
                }
            }
            return res ;
        }
    }

    public String visit(FSub e){
        String res ="";
        if(!defined){
            if(e.e1 instanceof Var){
                if(e.e2 instanceof Var){
                    res += String.format("fsub %s %s",e.e1.accept(this), e.e2.accept(this));
                } else {
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s", newVar(vn),e.e2.accept(this));
                    res += String.format("fsub _%s %s", newVar(vn),e.e1.accept(this));
                    vn++;
                }
            } else {
                if(e.e2 instanceof Var){
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s", newVar(vn),e.e1.accept(this));
                    res += String.format("fsub _%s %s", newVar(vn),e.e2.accept(this));
                    vn++;
                } else {
                    String v1= newVar(vn);
                    vn++;
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), newVar(vn),e.e1.accept(this));
                    res += String.format("fsub _%s _%s",v1, newVar(vn));
                    vn++;
                }
            }
            return res ;
        }
        else{
            if(e.e1 instanceof Var){
                if(e.e2 instanceof Var){
                    res += String.format("fsub %s %s",e.e1.accept(this), e.e2.accept(this));
                } else {
                    String v = newVar(vn++);
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s",v,e.e2.accept(this));
                    asmlGen.declaration += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in",v,v, newVar(vn),v);
                    res += String.format("fsub %s %s", newVar(vn),e.e1.accept(this));
                    vn++;
                }
            } else {
                if(e.e2 instanceof Var){
                    String v = newVar(vn++);
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s",v,e.e1.accept(this));
                    asmlGen.declaration += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in",v,v, newVar(vn),v);
                    res += String.format("fsub %s %s", newVar(vn),e.e2.accept(this));
                    vn++;
                } else {
                    String v1= newVar(vn++);
                    vn++;
                    String v2 = newVar(vn);
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), v2,e.e1.accept(this));
                    asmlGen.declaration += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in \n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in",v1,v1, newVar(vn),v1,v2,v2, newVar(vn++),v2);
                    res += String.format("fsub %s %s", newVar(vn), newVar(vn--));
                    vn++;
                }
            }
            return res ;
        }
    }

    public String visit(FMul e) {
        String res ="";
        if(!defined){

            if(e.e1 instanceof Var){
                if(e.e2 instanceof Var){
                    res += String.format("fmul %s %s",e.e1.accept(this), e.e2.accept(this));
                } else {
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s", newVar(vn),e.e2.accept(this));
                    res += String.format("fmul _%s %s", newVar(vn),e.e1.accept(this));
                    vn++;
                }
            } else {
                if(e.e2 instanceof Var){
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s", newVar(vn),e.e1.accept(this));
                    res += String.format("fmul _%s %s", newVar(vn),e.e2.accept(this));
                    vn++;
                } else {
                    String v1= newVar(vn);
                    vn++;
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), newVar(vn),e.e1.accept(this));
                    res += String.format("fmul _%s _%s",v1, newVar(vn));
                    vn++;
                }
            }
            return res ;
        }
        else{
            if(e.e1 instanceof Var){
                if(e.e2 instanceof Var){
                    res += String.format("fmul %s %s",e.e1.accept(this), e.e2.accept(this));
                } else {
                    String v = newVar(vn++);
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s",v,e.e2.accept(this));
                    asmlGen.declaration += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in",v,v, newVar(vn),v);
                    res += String.format("fmul %s %s", newVar(vn),e.e1.accept(this));
                    vn++;
                }
            } else {
                if(e.e2 instanceof Var){
                    String v = newVar(vn++);
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s",v,e.e1.accept(this));
                    asmlGen.declaration += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in",v,v, newVar(vn),v);
                    res += String.format("fmul %s %s", newVar(vn),e.e2.accept(this));
                    vn++;
                } else {
                    String v1= newVar(vn++);
                    vn++;
                    String v2 = newVar(vn);
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), v2,e.e1.accept(this));
                    asmlGen.declaration += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in \n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in",v1,v1, newVar(vn),v1,v2,v2, newVar(vn++),v2);
                    res += String.format("fmul %s %s", newVar(vn), newVar(vn--));
                    vn++;
                }
            }
            return res ;
        }
    }


    public String visit(FDiv e){
        String res ="";
        if(!defined){

            if(e.e1 instanceof Var){
                if(e.e2 instanceof Var){
                    res += String.format("fdiv %s %s",e.e1.accept(this), e.e2.accept(this));
                } else {
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s", newVar(vn),e.e2.accept(this));
                    res += String.format("fdiv _%s %s", newVar(vn),e.e1.accept(this));
                    vn++;
                }
            } else {
                if(e.e2 instanceof Var){
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s", newVar(vn),e.e1.accept(this));
                    res += String.format("fdiv _%s %s", newVar(vn),e.e2.accept(this));
                    vn++;
                } else {
                    String v1= newVar(vn);
                    vn++;
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), newVar(vn),e.e1.accept(this));
                    res += String.format("fdiv _%s _%s",v1, newVar(vn));
                    vn++;
                }
            }
            return res ;
        }
        else {
            if(e.e1 instanceof Var){
                if(e.e2 instanceof Var){
                    res += String.format("fdiv %s %s",e.e1.accept(this), e.e2.accept(this));
                } else {
                    String v = newVar(vn++);
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s",v,e.e2.accept(this));
                    asmlGen.declaration += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in",v,v, newVar(vn),v);
                    res += String.format("fdiv %s %s", newVar(vn),e.e1.accept(this));
                    vn++;
                }
            } else {
                if(e.e2 instanceof Var){
                    String v = newVar(vn++);
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s",v,e.e1.accept(this));
                    asmlGen.declaration += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in",v,v, newVar(vn),v);
                    res += String.format("fdiv %s %s", newVar(vn),e.e2.accept(this));
                    vn++;
                } else {
                    String v1= newVar(vn++);
                    vn++;
                    String v2 = newVar(vn);
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s \nlet _%s = %s",v1,e.e2.accept(this), v2,e.e1.accept(this));
                    asmlGen.declaration += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in \n\tlet %s = _%s in \n\tlet %s = mem(%s + 0) in",v1,v1, newVar(vn),v1,v2,v2, newVar(vn++),v2);
                    res += String.format("fdiv %s %s", newVar(vn), newVar(vn--));
                    vn++;
                }
            }
            return res ;
        }
    }

    public String visit(Eq e){
        String res ="";
        String v1 ="";
        String v2="";
        if(!defined){
            if(e.e1 instanceof Float){
                asmlGen.declaration = String.format("\nlet _z%s = %s \nlet _z%s = %s",cp++,e.e1.accept(this),cp++,e.e2.accept(this));
                res += String.format("_z%s = _z%s", cp--,cp++);
            } else if(e.e1 instanceof Bool || e.e1 instanceof Int) {
                if(e.e2 instanceof Int || e.e2 instanceof Bool) {
                    String v = newVar(vn) ;
                    vn++;
                    v1 = newVar(vn++) ;
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in \n\t let %s = %s in ",v,e.e1.accept(this),v1,e.e2.accept(this));
                    if(not){
                        res += String.format("%s != %s", v,v1);
                        not = false ;
                    } else {
                        res += String.format("%s = %s", v,v1);
                    }
                    vn++;
                } else {
                    String v = newVar(vn) ;
                    vn++;
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in ",v,e.e1.accept(this));
                    if(not){
                        res += String.format("%s != %s", v,e.e2.accept(this));
                        not = false ;
                    } else {
                        res += String.format("%s = %s", v,e.e2.accept(this));
                    }
                }
            } else if(e.e1 instanceof App || e.e2 instanceof App) {
                if(e.e1 instanceof App) {
                    String txt = e.e1.accept(this);
                    v1 = newVar(vn++);
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in ",v1,txt);
                }else {
                    v1 = e.e1.accept(this);
                } if(e.e2 instanceof App){
                    String txt = e.e2.accept(this);
                    v2 = newVar(vn++);
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in ",v2,txt);
                }else {
                    v2 = e.e2.accept(this);
                }
                if(not){
                    res += String.format("%s >= %s", v1,v2);
                    not = false ;
                } else {
                    res += String.format("%s <= %s", v1,v2);
                }
            } else {
                if(not){
                    res += String.format("%s != %s", e.e1.accept(this),e.e2.accept(this));
                    not = false ;
                } else {
                    res += String.format("%s = %s", e.e1.accept(this),e.e2.accept(this));
                }
            }
        }
        else {
            if(e.e1 instanceof Float){
                asmlGen.declaration = String.format("\nlet _z%s = %s \nlet _z%s = %s",cp++,e.e1.accept(this),cp++,e.e2.accept(this));
                res += String.format("_z%s = _z%s", cp--,cp++);
            } else if(e.e1 instanceof Bool || e.e1 instanceof Int) {
                if(e.e2 instanceof Bool || e.e2 instanceof Int){
                    String v = newVar(vn) ;
                    vn++;
                    v1 = newVar(vn++) ;
                    asmlGen.declaration += String.format("\n\tlet %s = %s in \n\t let %s = %s in ",v,e.e1.accept(this),v1,e.e2.accept(this));
                    if(not){
                        res += String.format("%s != %s", v,v1);
                        not = false ;
                    } else {
                        res += String.format("%s = %s", v,v1);
                    }
                    vn++;
                }
                else if(e.e1 instanceof App || e.e2 instanceof App) {
                    if(e.e1 instanceof App) {
                        String txt = e.e1.accept(this);
                        v1 = newVar(vn++);
                        asmlGen.declaration += String.format("\n\tlet %s = %s in ",v1,txt);
                    }else {
                        v1 = e.e1.accept(this);
                    } if(e.e2 instanceof App){
                        String txt = e.e2.accept(this);
                        v2 = newVar(vn++);
                        asmlGen.declaration += String.format("\n\tlet %s = %s in ",v2,txt);
                    }else {
                        v2 = e.e2.accept(this);
                    }
                    if(not){
                        res += String.format("%s >= %s", v1,v2);
                        not = false ;
                    } else {
                        res += String.format("%s <= %s", v1,v2);
                    }
                } else {
                    String v = newVar(vn) ;
                    vn++;
                    asmlGen.declaration += String.format("\n\tlet %s = %s in ",v,e.e1.accept(this));
                    if(not){
                        res += String.format("%s != %s", v,e.e2.accept(this));
                        not = false ;
                    } else {
                        res += String.format("%s = %s", v,e.e2.accept(this));
                    }
                    vn++;
                }
            } else {
                if(not){
                    res += String.format("%s != %s", e.e1.accept(this),e.e2.accept(this));
                    not = false ;
                } else {
                    res += String.format("%s = %s", e.e1.accept(this),e.e2.accept(this));
                }
            }
        }
        return res ;
    }

    public String visit(LE e){
        String res ="";
        String v1="";
        String v2="";
        if(!defined){
            if(e.e1 instanceof Float){
                asmlGen.declaration = String.format("\nlet _z%s = %s \nlet _z%s = %s",cp++,e.e1.accept(this),cp++,e.e2.accept(this));
                res += String.format("_z%s <= _z%s", cp--,cp++);
            } else if(e.e1 instanceof Bool || e.e1 instanceof Int) {
                if(e.e2 instanceof Int || e.e2 instanceof Bool) {
                    String v = newVar(vn) ;
                    vn++;
                    v1 = newVar(vn++) ;
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in \n\tlet %s = %s in ",v,e.e1.accept(this),v1,e.e2.accept(this));
                    if(not){
                        res += String.format("%s >= %s", v,v1);
                        not = false ;
                    } else {
                        res += String.format("%s <= %s", v,v1);
                    }
                    vn++;
                } else {
                    String v = newVar(vn) ;
                    vn++;
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in",v,e.e1.accept(this));
                    if(not){
                        res += String.format("%s >= %s", v,e.e2.accept(this));
                        not = false ;
                    } else {
                        res += String.format("%s <= %s", v,e.e2.accept(this));
                    }
                }

            } else if(e.e1 instanceof App || e.e2 instanceof App) {
                if(e.e1 instanceof App) {
                    String txt = e.e1.accept(this);
                    v1 = newVar(vn++);
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in ",v1,txt);
                }else {
                    v1 = e.e1.accept(this);
                }

                if(e.e2 instanceof App){
                    String txt = e.e2.accept(this);
                    v2 = newVar(vn++);
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in ",v2,txt);
                }else {
                    v2 = e.e2.accept(this);
                }
                if(not){
                    res += String.format("%s >= %s", v1,v2);
                    not = false ;
                } else {
                    res += String.format("%s <= %s", v1,v2);
                }
            } else if(not){
                res += String.format("%s >= %s", e.e1.accept(this),e.e2.accept(this));
                not = false ;
            }

            else {
                res += String.format("%s <= %s", e.e1.accept(this),e.e2.accept(this));
            }
            return res;
        }
        else{
            if(e.e1 instanceof Float){
                asmlGen.declaration = String.format("\nlet _z%s = %s \nlet _z%s = %s",cp++,e.e1.accept(this),cp++,e.e2.accept(this));
                res += String.format("_z%s <= _z%s", cp--,cp++);
            } else if(e.e1 instanceof Bool || e.e1 instanceof Int) {
                if(e.e2 instanceof Int || e.e2 instanceof Bool) {
                    String v = newVar(vn) ;
                    vn++;
                    v1 = newVar(vn++) ;
                    asmlGen.declaration += String.format("\n\tlet %s = %s in \n\t let %s = %s in ",v,e.e1.accept(this),v1,e.e2.accept(this));
                    if(not){
                        res += String.format("%s >= %s", v,v1);
                        not = false ;
                    } else {
                        res += String.format("%s <= %s", v,v1);
                    }
                    vn++;
                } else {
                    String v = newVar(vn) ;
                    vn++;
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in",v,e.e1.accept(this));
                    if(not){
                        res += String.format("%s >= %s", v,e.e2.accept(this));
                        not = false ;
                    }else {
                        res += String.format("%s <= %s", v,e.e2.accept(this));
                    }
                }
            } else if(e.e1 instanceof App || e.e2 instanceof App) {
                if(e.e1 instanceof App) {
                    String txt = e.e1.accept(this);
                    v1 = newVar(vn++);
                    asmlGen.declaration += String.format("\n\tlet %s = %s in ",v1,txt);
                }else {
                    v1 = e.e1.accept(this);
                }
                if(e.e2 instanceof App){
                    String txt = e.e2.accept(this);
                    v2 = newVar(vn++);
                    asmlGen.declaration += String.format("\n\tlet %s = %s in ",v2,txt);
                }else {
                    v2 = e.e2.accept(this);
                }
                if(not){
                    res += String.format("%s >= %s", v1,v2);
                    not = false ;
                }
                else {
                    res += String.format("%s <= %s", v1,v2);
                }
            } else {
                if(not){
                    res += String.format("%s >= %s", e.e1.accept(this),e.e2.accept(this));
                    not = false ;
                } else {
                    res += String.format("%s <= %s", e.e1.accept(this),e.e2.accept(this));
                }
            }
            return res;}
    }

    public String visit(If e){
        String res ="";
        String haut ="";
        if(e.e1 instanceof Not){
            not = true;
        }
        if(!defined){
            if (e.e2 instanceof Float){
                asmlGen.declaration += String.format("let _z%s = %s \nlet _z%s = %s",cp++,e.e2.accept(this),cp,e.e3.accept(this));
                if(e.e1 instanceof Bool){
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in", newVar(vn),e.e1.accept(this));
                    res += String.format("\n\tif %s = %s ", newVar(vn), newVar(vn++));
                } else {
                    res += String.format("\n\tif %s ",e.e1.accept(this));
                }
                cp--;
                if(iff){
                    res += String.format("then ( \n\t\tlet %s = _z%s in %s\n\t) else (\n\t\tlet %s = _z%s in %s\n\t)",asml,cp++,asml,asml,cp++,asml);
                } else {
                    res += String.format("then ( \n\t\t_z%s \n\t) else (\n\t\t _z%s \n\t)",cp++,cp++);
                }
                return res;
            } else {
                if(e.e1 instanceof Bool){
                    if(((Bool)e.e1).b == true) {
                        asmlGen.entryPoint += String.format("\n\tlet %s = %s in ", newVar(vn),e.e1.accept(this));
                        res += String.format("\n\tif %s = %s ", newVar(vn), newVar(vn++));
                    } else {
                        String v = newVar(vn++);
                        asmlGen.entryPoint += String.format("\n\tlet %s = %s in \n\tlet %s = 1",v,e.e1.accept(this), newVar(vn));
                        res += String.format("\n\tif %s = %s ",v, newVar(vn++));
                    }
                } else {
                    res += String.format("\n\tif %s ",e.e1.accept(this));
                }
                if(iff){
                    res += String.format("then ( \n\t\tlet %s = %s in %s\n\t) else (\n\t\tlet %s = %s in %s\n\t)",asml,e.e2.accept(this),asml,asml,e.e3.accept(this),asml);
                } else {
                    res += String.format("then ( \n\t\t%s \n\t) else (\n\t\t %s \n\t)",e.e2.accept(this),e.e3.accept(this));
                }
            }
            return res ;
        }
        else{
            if (e.e2 instanceof Float){
                String v1 ="";
                String v2="";
                v1 = String.format("_z%s",cp++);
                asmlGen.declaration = String.format("\nlet %s = %s \n%s",v1,e.e2.accept(this),asmlGen.declaration);
                if(e.e3 instanceof Float) {
                    v2 = String.format("_z%s",cp);
                    asmlGen.declaration  = String.format("\nlet %s = %s \n%s",v2,e.e3.accept(this),asmlGen.declaration);
                } else {
                    v2 = e.e3.accept(this);
                }
                if(e.e1 instanceof Bool){
                    if(((Bool)e.e1).b == true) {
                        res += String.format("\n\tlet %s = %s in ", newVar(vn),e.e1.accept(this));
                        res += String.format("\n\tif %s = %s ", newVar(vn), newVar(vn++));
                    } else {
                        String v = newVar(vn++);
                        res += String.format("\n\tlet %s = %s in \n\tlet %s = 1",v,e.e1.accept(this), newVar(vn));
                        res += String.format("\n\tif %s = %s ",v, newVar(vn++));
                    }
                } else {
                    res += String.format("\n\tif %s ",e.e1.accept(this));
                }
                if(iff){
                    res += String.format("then ( \n\t\tlet %s = z%s in %s\n\t) else (\n\t\tlet %s = z%s in %s\n\t)",asml,v1,asml,asml,v2,asml);

                } else {
                    res += String.format("then ( \n\t\t%s \n\t) else (\n\t\t %s \n\t)",v1,v2);
                }
                return res;
            }else {
                if(e.e1 instanceof Bool){
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in ", newVar(vn),e.e1.accept(this));
                    res += String.format("\n\tif %s = %s ", newVar(vn), newVar(vn++));
                } else {
                    res += String.format("\n\tif %s ",e.e1.accept(this));
                }
                if(iff){
                    res += String.format("then ( \n\t\tlet %s = %s in %s\n\t) else (\n\t\tlet %s = %s in %s\n\t)",asml,e.e2.accept(this),asml,asml,e.e3.accept(this),asml);
                } else {
                    res += String.format("then ( \n\t\t%s \n\t) else (\n\t\t %s \n\t)",e.e2.accept(this),e.e3.accept(this));
                }
                return res;
            }
        }
    }

    public String visit(Let e) {
        String haut ="";
        String res ="";
        if(!defined){

            if((e.e1) instanceof Float)
            {
                asmlGen.declarationFloat += String.format("\nlet _%s = %s",e.id,e.e1.accept(this));
                res += String.format("let %s = _%s in \n\tlet %s = mem(%s +0) in ", e.id,e.id, newVar(vn),e.id);
                if((e.e1 instanceof Let?true:false) || e.e1 instanceof If){
                    res += "\n\t";
                }
                if(!(e.e2 instanceof Let?true:false) && !(e.e2 instanceof App)){
                    res += String.format("%s \n\t ",e.e2.accept(this));
                } else {
                    res += String.format("\n\t %s",e.e2.accept(this));
                }


            } else if (e.e1 instanceof If) {
                iff = true ;
                asml = e.id.id ;
                res += String.format("%s in ",e.e1.accept(this));
                if(!(e.e2 instanceof Let?true:false) && !(e.e2 instanceof App)){
                    res += String.format("%s \n\t ",e.e2.accept(this));
                } else {
                    res += String.format("\n\t %s",e.e2.accept(this));
                }
                iff = false ;
            } else {
                res += String.format("\n\tlet %s = ",e.id);
                if((e.e1 instanceof Let ? true:false) || e.e1 instanceof If){
                    res += "\n\t";
                }
                res += String.format("%s in %s ",e.e1.accept(this),e.e2.accept(this));
            }
            return res;
        }
        else{
            if(e.e1 instanceof Float){
                asmlGen.declarationFloat += String.format("\nlet _%s = %s \n", e.id,e.e1.accept(this)) ;
                res += String.format("let %s = _%s in \n\tlet %s = mem(%s +0) in ", e.id,e.id, newVar(vn),e.id);
                if(e.e2 instanceof Var) {
                    res += String.format("%s \n", e.e2.accept(this));
                } else {
                    res += String.format("\n %s", e.e2.accept(this));
                }
            } else {
                res += String.format("\n\tlet %s = %s in %s", e.id.id,e.e1.accept(this),e.e2.accept(this));
            }
        }
        return res;
    }


    public String visit(Var e){
        e.id.id=e.id.id.replace('?','_');
        return e.id.id;
    }



    static <E> String printIds(List<E> l, String op) {
        if (l.isEmpty()) {
            return "";
        }
        String t = "" ;
        Iterator<E> it = l.iterator();
        t += it.next();
        while (it.hasNext()) {
            t += op + it.next();
        }
        return t ;

    }

    public String printExps(List<Exp> l, String op,String id) {
        String t ="";
        String txt="";
        for(Exp e : l){
            if(!defined){
                if(e instanceof Var){
                    t += e + " ";
                } else if (e instanceof Float) {
                    String v = newVar(vn++) ;
                    asmlGen.declarationFloat += String.format("\nlet _%s = %s",v,((Float)e).f);
                    asmlGen.entryPoint += String.format("\n\tlet %s = _%s in \n\tlet %s = mem(%s +0) in",v,v, newVar(vn),v);
                    t += newVar(vn++) ;
                } else if (e instanceof App || e instanceof Not || e instanceof Neg || e instanceof FNeg || e instanceof Eq ||
                        e instanceof LE || e instanceof Add || e instanceof Sub || e instanceof FAdd || e instanceof FSub
                        || e instanceof FMul || e instanceof FDiv ||  e instanceof If) {
                    if(inApp) {
                        inApp = false ;
                        txt = e.accept(this);
                        inApp = true ;
                    } else {
                        txt = e.accept(this);
                    }
                    if(inApp){
                        System.out.println("uuuuu");
                        txt += String.format(" in call _min_caml_%s %s ",id,t) ;
                        t = String.format("\n\tlet %s = %s ", newVar(vn),txt);
                    } else if(e instanceof Eq || e instanceof LE || e instanceof Add || e instanceof Sub || e instanceof FAdd || e instanceof FSub
                            || e instanceof FMul || e instanceof FDiv ){
                        binaryOp = true;
                        txt += String.format(" in call _min_caml_%s %s ",id,t) ;
                        t = String.format("\n\tlet %s = %s ", newVar(vn),txt);
                    } else if(e instanceof Not || e instanceof Neg || e instanceof FNeg) {
                        binaryOp = true;
                        String v = newVar(vn++);
                        asmlGen.entryPoint += String.format("\n\tlet %s = %s in \n\tlet %s = call _min_caml_%s %s in ",v,txt, newVar(vn),id,v) ;
                    } else if (e instanceof If){
                        txt += String.format(" in call _min_caml_%s %s ",id,t) ;
                        t = String.format("\n\tlet %s = %s ", newVar(vn),txt);
                    } else {
                        asmlGen.entryPoint += String.format("\n\tlet %s = %s in ", newVar(vn),txt);
                    }
                    t += newVar(vn) + " ";
                    vn++;
                } else {
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in ", newVar(vn),e.accept(this));
                    t += newVar(vn) + " ";
                    vn++;
                }
            }
            else{
                if(e instanceof Var){
                    t += e + " ";
                } else {
                    if (e instanceof App) {
                        if(inApp) {
                            inApp = false ;
                            txt = e.accept(this);
                            inApp = true ;
                        } else {
                            txt = e.accept(this);
                        }
                        if(inApp){
                            txt += String.format(" in call _%s %s",id,t) ;
                            t = String.format("\n\tlet %s = %s", newVar(vn),txt);
                        } else {
                            t += String.format("\n\tlet %s = %s", newVar(vn),txt);
                        }
                    } else {
                        asmlGen.declaration += String.format("\n\tlet %s = %s in ", newVar(vn),e.accept(this));
                    }
                    t += newVar(vn) + " ";
                    vn++;
                }
            }
        }
        return t ;
    }

    public String visit(LetRec e){
        asmlGen.declaration += String.format("\nlet _%s %s = \n\t",e.fd.id,printIds(e.fd.args, " "),printIds(e.fd.args, " "));
        defined=true;
        String a = e.fd.e.accept(this);
        asmlGen.declaration += String.format ("%s",a);
        defined=false;
        String res = e.e.accept(this);
        return res;
    }

    public String visit(App e){
        String res ="";
        if(e.e instanceof Var && (((Var)e.e).id.id.equals("print_int") || ((Var)e.e).id.id.equals("print_float") || ((Var)e.e).id.id.equals("truncate") || ((Var)e.e).id.id.equals("print_newline") || ((Var)e.e).id.id.equals("int_of_float") || ((Var)e.e).id.id.equals("float_of_int"))){

            for(Exp r : e.es){
                if(r instanceof Let ? true:false)
                {
                    inApp = true;
                    res += printExps(e.es, " ", e.e.accept(this));
                }
            }
            if(((Var)e.e).id.id.equals("print_float") ){
                String v = newVar(vn++) ;
                res += String.format("\n\tlet %s = call _min_caml_int_of_float %s in ",v,printExps(e.es, " ",e.e.accept(this)), newVar(vn),v);
                res += String.format("call _min_caml_print_int %s ",v);
            } else if(!inApp){
                String txt = printExps(e.es, " ",e.e.accept(this));
                if(binaryOp){
                    res += txt ;
                } else
                    res += String.format("\n\tcall _min_caml_%s %s ",e.e.accept(this),txt);
            }
        } else {
            for(Exp r : e.es){
                if ((!(r instanceof Var) && !(r instanceof Int) )){
                    inApp = true ;
                    res += printExps(e.es, " ",e.e.accept(this));
                    inApp = true ;
                }
            }
            if(!inApp) {
                String txt = e.e.accept(this);
                String str2 = "call";
                if(txt.toLowerCase().contains(str2.toLowerCase())){
                    asmlGen.entryPoint += String.format("\n\tlet %s = %s in", newVar(vn),txt);
                    res += String.format("call _%s %s ", newVar(vn++),printExps(e.es, " ",e.e.accept(this)));
                } else
                    res += String.format("call _%s %s ",txt,printExps(e.es, " ",e.e.accept(this)));

            }
            inApp = false;
        }
        return res ;
    }

    public String visit(Tuple e){
        return String.format("%s",printIds(e.es, " "));
    }

    public String visit(LetTuple e){
        return String.format("let %s = 1 in \n\tlet %s = call _min_caml_create_array %s %s in %s", newVar(vn),printIds(e.ids,""), newVar(vn++), e.e1.accept(this),e.e2.accept(this));
    }

    public String visit(Array e){
        String res ="";
        String v1 = "";
        String v2 = "";
        String v = newVar(vn++);
        if(e.e1 instanceof Int){
            v2 = newVar(vn++);
            asmlGen.entryPoint += String.format("\n\tlet %s = %s in",v2,e.e1.accept(this));
        } else if (e.e1 instanceof Var){
            v2 = e.e1.accept(this);
        }
        if(e.e2 instanceof Int){
            v1 = newVar(vn++);
            asmlGen.entryPoint += String.format("\n\tlet %s = %s in",v1,e.e2.accept(this));
            res += String.format(" call _min_caml_create_float_array %s %s",v,v2,v1);
        } else if (e.e2 instanceof Var){
            v1 = e.e2.accept(this);
            res += String.format(" call _min_caml_create_float_array %s %s",v,v2,v1);
        } else if (e.e2 instanceof Float){
            v1 = newVar(vn++);
            String v3 = newVar(vn++);
            asmlGen.entryPoint += String.format("\n\tlet %s = _%s in\n\tlet %s = mem(%s + 0) in",v1,v1,v3,v1);
            asmlGen.declarationFloat += String.format("\nlet _%s = %s",v1,e.e2.accept(this));
            res += String.format(" call _min_caml_create_float_array %s %s",v,v2,v1);
        }
        return res;
    }

    public String visit(Get e){
        String v = newVar(vn);
        asmlGen.entryPoint += String.format("\n\tlet %s = mem(%s + %s) in",v, e.e1.accept(this), e.e2.accept(this));
        return v;
    }

    public String visit(Put e){
        return String.format("mem(%s + %s) <- %s", e.e1.accept(this), e.e2.accept(this),e.e3.accept(this));
    }


}