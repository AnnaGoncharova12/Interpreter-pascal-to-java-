package interpretor;

import java.util.HashMap;

public class Interpretor {
Parser parser;

	    public Interpretor( Parser P) {
	      this.parser = P;
	    }
	
	 public Object interpret() {
	       AST.Node tree = this.parser.parse();
	        return tree.visit();
	    }

public static void main(String[] args) {
	String input="PROGRAM Part12;" + 
			"VAR" + 
			"   x, y: REAL;"  + 
			"PROCEDURE P1(a : INTEGER);" + 
			"VAR " + 
			"   y : INTEGER; BEGIN END; BEGIN END." 
			;
	Interpretor i= new Interpretor(new Parser(new Lexer(input)));
i.interpret();
/*
	System.out.println(AST.symtab);
	for(String s: AST.symbolTable.keySet()) {
		System.out.println(s+" "+ AST.symbolTable.get(s));
	}
	*/
}
}
