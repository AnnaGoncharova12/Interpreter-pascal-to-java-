package interpretor;

import java.util.HashMap;

public class TokenType {
	HashMap<Type, String> hm = new HashMap<>();
	HashMap<String, Type> hm1 = new HashMap<>();
	public TokenType() {
		hm.put( Type.PLUS, "+");
		hm.put( Type.MINUS ,"-");
		hm.put( Type. MUL ,"*");
		hm.put( Type. FLOAT_DIV ,"/");
		hm.put( Type.LPAREN ,"(");
			hm.put( Type. RPAREN,")");
		hm.put( Type.SEMI, ";");
		hm.put( Type. DOT ,".");
				hm.put( Type. COLON , ":");
		hm.put( Type. COMMA  ,",");
		hm.put( Type. PROGRAM,"PROGRAM");
		hm.put( Type.  INTEGER ,"INTEGER");
		hm.put( Type.	    REAL , "REAL");
		hm.put( Type.    INTEGER_DIV ,"DIV");
		hm.put( Type.	    VAR ,"VAR");
		hm.put( Type.	    PROCEDURE ,"PROCEDURE");
		hm.put( Type.	    BEGIN,"BEGIN");
		hm.put( Type.    END ,"END");
		hm.put( Type.	    ID,"ID");
		hm.put( Type.    INTEGER_CONST, "INTEGER_CONST");
		hm.put( Type.	    REAL_CONST , "REAL_CONST");
		hm.put( Type.	    ASSIGN ,":=");
		hm.put( Type.	    EOF ,"EOF");
		for(Type t: hm.keySet()) {
			hm1.put(hm.get(t), t);
		}
	}
}
