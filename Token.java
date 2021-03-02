package interpretor;

public class Token {
Type  t;
	String value="";
	int lineno=-1;
	int column=-1;
public Token(Type t, String value) {
	this.t=t;
	this.value=value;
}
public Token(Type t, String value, int lineno, int column) {
	this.t=t;
	this.value=value;
	this.lineno=lineno;
	this.column=column;
}

public String toString() {
	return "Token({"+this.t+"}, {"+this.value+"}+" +"line: "+ lineno+
			" column: "+ column+ " )";
}

}
