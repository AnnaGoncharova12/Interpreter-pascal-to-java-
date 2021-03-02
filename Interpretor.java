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
	String input="PROGRAM Main;"+ 
			"PROCEDURE Alpha(a : INTEGER; b : INTEGER);" + 
			"VAR x : INTEGER;" + 
			"" + 
			"   PROCEDURE Beta(a : INTEGER; b : INTEGER);" + 
			"   VAR x : INTEGER;" + 
			"   BEGIN " + 
			"      x := a * 10 + b * 2;" + 
			"   END; " + 
			 
			"BEGIN" + 
			"   x := (a + b ) * 2;" + 
			
			"   Beta(5, 10);  " + 
			"END; " + 
			 
			"BEGIN " + 
		
			"   Alpha(3 + 5, 7);   " + 
			
			"END.  ";
			
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
