package interpretor;

public class Token {
Type  t;
	String value="";
public Token(Type t, String value) {
	this.t=t;
	this.value=value;
}
/*
public String toString() {
	return "Token({"+this.type+"}, {"+this.value+"})";
}
*/
}
