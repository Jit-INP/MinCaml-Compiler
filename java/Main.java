import java_cup.runtime.*;
import java.io.*;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

public class Main  {
  static public void main(String argv[]) {

    int c;
    String arg;
    LongOpt[] longopts = new LongOpt[5];

    StringBuffer sb1 = new StringBuffer();
    StringBuffer sb2 = new StringBuffer();
    longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
    longopts[1] = new LongOpt("output", LongOpt.REQUIRED_ARGUMENT, sb2, 'o');
    longopts[2] = new LongOpt("input", LongOpt.REQUIRED_ARGUMENT, sb1, 'i');
    longopts[3] = new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'v');
    longopts[3] = new LongOpt("asml", LongOpt.NO_ARGUMENT, null, 'a');
    //
    Getopt g = new Getopt("testprog", argv, "ho:i:vtpSa;", longopts);
    g.setOpterr(false); // We'll do our own error handling
    //
    boolean typeCheckOnly=false;
    boolean parseOnly=false;
    String  inputFile=null;
    String  outputFile=null;
    boolean genArmFromAmsl=false;
    boolean genAsmlFlag=false;

    boolean boutputfile=false;

    while ((c = g.getopt()) != -1)
      switch (c)
      {
        case 'i':
          arg = g.getOptarg();
          inputFile=arg;
          break;
        //
        case 'o':
          arg = g.getOptarg();
          outputFile=arg;
          boutputfile=true;
          break;
        //
        case 'S':
          genArmFromAmsl=true;
          break;
        //
        case 'a':
          genAsmlFlag=true;
          break;
        //
        case 't':
          typeCheckOnly=true;
          break;
        case 'p':
          parseOnly=true;
          break;
        //
        case 'h':
          System.out.println("MinCaml compiler help:");
          System.out.println("for files should be provided full path");
          System.out.println("-p                      - parse only");
          System.out.println("-t                      - type check only");
          System.out.println("-S                      - generate asm from asml file");
          System.out.println("--help or -h            - display help");
          System.out.println("--asml or -a            - generate asml file");
          System.out.println("--version or -v         - display version");
          System.out.println("--input inFile or -i    - input file");
          System.out.println("--output outFile or -o  - output file");
          System.exit(0);
          break;
        //
        case 'v':
          System.out.println("version -- 1.0.1");
          System.exit(0);
          break;
        case '?':
          System.out.println("The option '" + (char)g.getOptopt() +
                  "' is not valid");
          break;
        //
        default:
          System.out.println("wrong");
          break;
      }

      if(inputFile==null)
      {
        if(argv.length==0) {
          System.out.println("input file must be defined!");
          System.exit(1);
        }
        else
        {
          inputFile=argv[0];
        }
      }

      if(genArmFromAmsl)
      {
        if(argv.length==1)
        {
          System.out.println("input file must be defined!");
          System.exit(1);
        }
        inputFile=argv[1];

        Process process;
        try{
          Path path = Paths.get(inputFile);
          String fileName = path.getFileName().toString();
          String data[] = fileName.split(".asml");
          String fileNameWithoutExt = data[0];

          String asmFile = path.getParent()+"/"+fileNameWithoutExt + ".s";

          if(outputFile==null)
          {
            outputFile=asmFile;
          }

          String cmd="python3 ../ASML2ASMpyt/main.py -fo "+inputFile+" "+outputFile;
          //System.out.println(cmd);
          process = Runtime.getRuntime().exec(cmd);
        }catch(Exception e) {
          System.out.println("Exception Raised" + e.toString());
        }
      }else if(parseOnly){
        try {
          if(argv.length==1)
          {
            System.out.println("input file must be defined!");
            System.exit(1);
          }
          inputFile=argv[1];
          System.out.println("Parse only");

          Parser p = new Parser(new Lexer(new FileReader(inputFile)));
          Exp expression = (Exp) p.parse().value;   
          assert (expression != null);

          predef.initialisation();
  
          System.out.println("------ AST ------");
          expression.accept(new PrintVisitor());
          System.out.println();
  
          System.out.println("\n\n------ Height of the AST ----");
          int height = Height.computeHeight(expression);
          System.out.println("using Height.computeHeight: " + height);
  
          ObjVisitor<Integer> v = new HeightVisitor();
          height = expression.accept(v);
          System.out.println("using HeightVisitor: " + height);
        } catch (Exception e) {
          System.out.println("Error : Parser");
          System.exit(1);
        }
      }else if(typeCheckOnly){
        try {
          if(argv.length==1)
          {
            System.out.println("input file must be defined!");
            System.exit(1);
          }
          inputFile=argv[1];
          predef.initialisation();

          Parser p = new Parser(new Lexer(new FileReader(inputFile)));
          Exp expression = (Exp) p.parse().value;

          assert (expression != null);

          System.out.println("------ Type Check ------");
          typeChecker checker = new typeChecker();
          boolean ok = false;
          ok = checker.check(expression);
          if(ok){
            System.out.println("Well typed code");
          }else{
            System.out.println("Code is not well typed");
          }
        } catch (Exception e) {
          System.out.println("Error : Type Check");
          System.exit(1);
        }
        
      }
      else if(genAsmlFlag)
      {
        try {
          if(argv.length==1)
          {
            System.out.println("input file must be defined!");
            System.exit(1);
          }
          inputFile=argv[1];

          Parser p = new Parser(new Lexer(new FileReader(inputFile)));
          Exp expression = (Exp) p.parse().value;
          assert (expression != null);

          predef.initialisation();

          System.out.println("------ AST ------");
          expression.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n------ Height of the AST ----");
          int height = Height.computeHeight(expression);
          System.out.println("using Height.computeHeight: " + height);

          ObjVisitor<Integer> v = new HeightVisitor();
          height = expression.accept(v);
          System.out.println("using HeightVisitor: " + height);


          System.out.println("\n\n------ Type Check ------");
          typeChecker checker = new typeChecker();
          boolean ok = false;
          ok = checker.check(expression);
          if(ok){
            System.out.println("Well typed code");
          }else{
            System.out.println("Code is not well typed");
          }


          System.out.println("\n\n----Var Computation-----");
          Vars varss = new Vars();
          ArrayList<String> varList = varss.computeVars(expression);
          System.out.println(varList);

          System.out.println("\n\n---- Duplicated AST ----");
          ObjVisitor<Exp> x = new DuplicateVisitor();
          Exp duplicated_expression = expression.accept(x);
          duplicated_expression.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n---- Set of variables ----");
          ObjVisitor<String> w = new VariableVisitor();
          String vars = expression.accept(w);
          System.out.println("Set of Variables in AST using visitor: " + vars);

          System.out.println("\n\n------ AST Knormalized ------");
          Knorm knorms = new Knorm();
          Exp expression3 = knorms.computeKnorm(expression);
          expression3.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n------ Alpha Convertion------");
          alphaCon ac=new alphaCon();
          Exp alpha=expression.accept(ac);
          alpha.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n------ Nested Let Reduction ------");
          nestedLetReduc nlr=new nestedLetReduc();
          Exp e_nlr=expression3.accept(nlr);
          e_nlr.accept(new PrintVisitor());
          System.out.println();

          Path path = Paths.get(inputFile);
          String fileName = path.getFileName().toString();
          String data[] = fileName.split(".ml");
          String fileNameWithoutExt = data[0];

          System.out.println("\n\n------ ASML------");
          asmlGen ag=new asmlGen();
          String asml = e_nlr.accept(ag);

          asml = asmlGen.declarationFloat + asmlGen.declaration + asmlGen.entryPoint + asml;
          String asmlFile = path.getParent()+"/"+fileNameWithoutExt + ".asml";
          PrintWriter wr = new PrintWriter( new BufferedWriter( new FileWriter(asmlFile)));
          wr.print(asml);
          wr.close();
          System.out.println(asml);
          System.out.println();
          }
           catch (Exception e) {
          e.printStackTrace();
        }
      }else if(boutputfile){
        try {
          PrintStream o = new PrintStream(new FileOutputStream(outputFile));
          PrintStream console = System.out;
          System.setOut(o);
          
          Parser p = new Parser(new Lexer(new FileReader(inputFile)));
          Exp expression = (Exp) p.parse().value;   
          assert (expression != null);
    
          predef.initialisation();
    
          System.out.println("------ AST ------");
          expression.accept(new PrintVisitor());
          System.out.println();
    
          System.out.println("\n\n------ Height of the AST ----");
          int height = Height.computeHeight(expression);
          System.out.println("using Height.computeHeight: " + height);
    
          ObjVisitor<Integer> v = new HeightVisitor();
          height = expression.accept(v);
          System.out.println("using HeightVisitor: " + height);
    
        
          System.out.println("\n\n------ Type Check ------");
          typeChecker checker = new typeChecker();
          boolean ok = false;
          ok = checker.check(expression);
          if(ok){
            System.out.println("Well typed code");
          }else{
            System.out.println("Code is not well typed");
          }
            
          
          System.out.println("\n\n----Var Computation-----");
          Vars varss = new Vars();
          ArrayList<String> varList = varss.computeVars(expression);
          System.out.println(varList);
    
          System.out.println("\n\n---- Duplicated AST ----");
          ObjVisitor<Exp> x = new DuplicateVisitor();
          Exp duplicated_expression = expression.accept(x);
          duplicated_expression.accept(new PrintVisitor());
          System.out.println();
    
          System.out.println("\n\n---- Set of variables ----");
          ObjVisitor<String> w = new VariableVisitor();
          String vars = expression.accept(w);
          System.out.println("Set of Variables in AST using visitor: " + vars);
    
          System.out.println("\n\n------ AST Knormalized ------");
          Knorm knorms = new Knorm();
          Exp expression3 = knorms.computeKnorm(expression);
          expression3.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n------ Alpha Convertion------");
          alphaCon ac=new alphaCon();
          Exp alpha=expression3.accept(ac);
          alpha.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n------ Nested Let Reduction ------");
          nestedLetReduc nlr=new nestedLetReduc();
          Exp e_nlr=expression3.accept(nlr);
          e_nlr.accept(new PrintVisitor());
          System.out.println();

          Path path = Paths.get(inputFile);
          String fileName = path.getFileName().toString();
          String data[] = fileName.split(".ml");
          String fileNameWithoutExt = data[0];

          System.out.println("\n\n------ ASML------");
          asmlGen ag=new asmlGen();
          String asml = e_nlr.accept(ag);

          asml = asmlGen.declarationFloat + asmlGen.declaration + asmlGen.entryPoint + asml;
          String asmlFile = path.getParent()+"/"+fileNameWithoutExt + ".asml";
          PrintWriter wr = new PrintWriter( new BufferedWriter( new FileWriter(asmlFile)));
          wr.print(asml);
          wr.close();
          System.out.println(asml);
          System.out.println();

          String asmFile =path.getParent()+ "/" + fileNameWithoutExt + ".s";
          Process process;
          try{
            String cmd="python3 ../ASML2ASMpyt/main.py -fo "+asmlFile+" "+asmFile;
            //System.out.println(cmd);
            process = Runtime.getRuntime().exec(cmd);
          }catch(Exception e) {
            System.out.println("Exception Raised" + e.toString());
          }
        
        } catch (Exception e) {
            System.out.println("Writing output to file error");
        }
        
      }else{
        try {

          Parser p = new Parser(new Lexer(new FileReader(inputFile)));
          Exp expression = (Exp) p.parse().value;   
          assert (expression != null);
    
          predef.initialisation();
    
          System.out.println("------ AST ------");
          expression.accept(new PrintVisitor());
          System.out.println();
    
          System.out.println("\n\n------ Height of the AST ----");
          int height = Height.computeHeight(expression);
          System.out.println("using Height.computeHeight: " + height);
    
          ObjVisitor<Integer> v = new HeightVisitor();
          height = expression.accept(v);
          System.out.println("using HeightVisitor: " + height);
    
        
          System.out.println("\n\n------ Type Check ------");
          typeChecker checker = new typeChecker();
          boolean ok = false;
          ok = checker.check(expression);
          if(ok){
            System.out.println("Well typed code");
          }else{
            System.out.println("Code is not well typed");
          }
            
          
          System.out.println("\n\n----Var Computation-----");
          Vars varss = new Vars();
          ArrayList<String> varList = varss.computeVars(expression);
          System.out.println(varList);
    
          System.out.println("\n\n---- Duplicated AST ----");
          ObjVisitor<Exp> x = new DuplicateVisitor();
          Exp duplicated_expression = expression.accept(x);
          duplicated_expression.accept(new PrintVisitor());
          System.out.println();
    
          System.out.println("\n\n---- Set of variables ----");
          ObjVisitor<String> w = new VariableVisitor();
          String vars = expression.accept(w);
          System.out.println("Set of Variables in AST using visitor: " + vars);
    
          System.out.println("\n\n------ AST Knormalized ------");
          Knorm knorms = new Knorm();
          Exp expression3 = knorms.computeKnorm(expression);
          expression3.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n------ Alpha Convertion------");
          alphaCon ac=new alphaCon();
          Exp alpha=expression3.accept(ac);
          alpha.accept(new PrintVisitor());
          System.out.println();

          System.out.println("\n\n------ Nested Let Reduction ------");
          nestedLetReduc nlr=new nestedLetReduc();
          Exp e_nlr=expression3.accept(nlr);
          e_nlr.accept(new PrintVisitor());
          System.out.println();

          Path path = Paths.get(inputFile);
          String fileName = path.getFileName().toString();
          String data[] = fileName.split(".ml");
          String fileNameWithoutExt = data[0];

          System.out.println("\n\n------ ASML------");
          asmlGen ag=new asmlGen();
          String asml = e_nlr.accept(ag);

          asml = asmlGen.declarationFloat + asmlGen.declaration + asmlGen.entryPoint + asml;
          String asmlFile = path.getParent()+"/"+fileNameWithoutExt + ".asml";
          PrintWriter wr = new PrintWriter( new BufferedWriter( new FileWriter(asmlFile)));
          wr.print(asml);
          wr.close();
          System.out.println(asml);
          System.out.println();

          String asmFile =path.getParent()+ "/" + fileNameWithoutExt + ".s";
          Process process;
          try{
            String cmd="python3 ../ASML2ASMpyt/main.py -fo "+asmlFile+" "+asmFile;
            //System.out.println(cmd);
            process = Runtime.getRuntime().exec(cmd);
          }catch(Exception e) {
            System.out.println("Exception Raised" + e.toString());
          }

        } catch (Exception e) {
          e.printStackTrace();
        }

      
      }
  }
}

