package interpretor;

public class Lexer {
	String text;
	int pos;
	char curr;
	public Lexer(String text) {
		this.text=text;
		pos=0;
	
		if(text.length()!=0) {
		curr=text.charAt(pos);
		}
	}
	public char peek() {
	   int peek_pos = this.pos + 1;
	    if( peek_pos > text.length() - 1) {
	        return '#';
	    }
	    else {
	        return text.charAt(peek_pos);
	    }
}
	public Token RESERVED_KEYWORDS (String label) {
		if(label.equals("BEGIN")) {
			return new Token(Type.BEGIN, "BEGIN");
		}
		else if(label.equals("END")) {
			return new Token(Type.END, "END");
		}  
		else if(label.equals("PROGRAM")) {
			return new Token(Type.PROGRAM, "PROGRAM");
		}
		else if(label.equals("VAR")) {
			return new Token(Type.VAR, "VAR");
		}
		else if(label.equals("DIV")) {
			return new Token(Type.INTEGER_DIV, "DIV");
		}
		else if(label.equals("INTEGER")) {
			return new Token(Type.INTEGER, "INTEGER");
		}
		else if(label.equals("REAL")) {
			return new Token(Type.REAL, "REAL");
		}
		else if(label.equals("PROCEDURE")) {
			return new Token(Type.PROCEDURE, "PROCEDURE");
		}
		else {
			return new Token(Type.ID,label );
		}
	}
	public void skip_comment() {
	    while (curr!= '}') {
	        advance();
	    }
	    advance() ;
	}
	public Token _id() {
	  String  result = "";
	    while (curr!='#' && (Character.isDigit(curr)||Character.isLetter(curr))) {
	        result += curr;
	        advance();
	    }
	    Token token = RESERVED_KEYWORDS(result);
	    return token;
	}
	public void error() {
		try {
			throw new InvalidCharacterException();
		}
		catch(InvalidCharacterException e){
			System.out.println(e);
		}
	}
	public void advance(){
	    pos++;
	    if (this.pos > this.text.length() - 1) {
	        curr='#';
	    }
	    else {
	        this.curr = text.charAt(pos);
	    }
	}
	public void skip_whitespace() {
	    while( curr!='#'&& curr==' ') {
	       advance();
	    }
	}
	
	public Token number() {
	  
	   String result = "";
	   Token token=null;
	    while( curr!='#' && Character.isDigit(curr)){
	        result +=curr;
	       advance();
	    }

	    if (curr == '.') {
	        result +=curr;
	        advance();

	        while (
	        		 curr!='#' && Character.isDigit(curr)
	        ) {
	            result += curr;
	            advance();
	        }
	      
	        token =new Token(Type.REAL_CONST, result);
	    }
	    else {
	    	token =new Token(Type.INTEGER_CONST, result);
	    }
	    return token;
	}
	//breaks sentence into tokens
	public Token get_next_token() {
		
	    while (curr!='#') {
	    	if (curr==' ') {
	        	
	           skip_whitespace();
	           continue;
	        }
	    	 if (Character.isLetter(curr)){
	             return _id();
	    	 }
	             else  if (curr == ':' &&peek() == '='){
	             advance();
	            advance();
	            String str=":=";
	             return new Token(Type.ASSIGN, str);
	             }
	             else   if (curr == ';') {
	             advance();
	             String str=";";
	             return new Token(Type.SEMI, str);
	             }
	             else    if (curr == '.') {
	             advance();
	             String str=".";
	             return new Token(Type.DOT, str);
	             }
	            
	        else if (curr == '+') {
	            advance();
	            String plus="+";
	            return new Token(Type.PLUS, plus);
	        }
	        else if (curr == '-') {
	           advance();
	           String minus="-";
	            return new Token(Type.MINUS, minus);
	        }
	        else if (curr == '*') {
                advance();
                String mul="*";
                return new Token(Type.MUL, mul);
	        }
                else if (curr == '/') {
                advance();
                String div="/";
                return new Token(Type.FLOAT_DIV, div);
                }

                else     if (curr == '(') {
                advance();
                String lp="(";
                return new Token(Type.LPAREN, lp);
                }
                else   if (curr == ')'){
               advance();
               String rp=")";
                return new Token(Type.RPAREN, rp);
	    }
                else  if (curr == '{'){
	              advance();
	             skip_comment();
	     
                }
                else	 if (Character.isDigit(curr)) {
	             return number();
	    }
       

                else if (curr== ':'){
	              advance();
	              String str=":";
	              return new Token(Type.COLON, str);
                }

                else  if (curr == ',') {
	              advance();
	              String str=",";
	              return new Token(Type.COMMA, str);
                }
                else if (curr == '/') {
	              advance();
	              String str="/";
	              return new Token(Type.FLOAT_DIV, str);
                }
	         
	        else {
	        	this.error();
	        }
	    }
	    String eof="#";
	    return  new Token(Type.EOF, eof );
}
}
