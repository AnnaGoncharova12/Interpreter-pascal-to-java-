package interpretor;

import java.util.HashMap;

public class Error extends Exception{
String message;
Token token;
HashMap<ErrorCode, String> hm= new HashMap<>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public Error(String cl,Token token, String message) {
	this.token=token;
	this.message=cl+" : "+ message;
	  hm.put(ErrorCode.UNEXPECTED_TOKEN, "Unexpected token");
	  hm.put(ErrorCode.ID_NOT_FOUND, "Identifier not found");
	  hm.put(ErrorCode.DUPLICATE_ID ,  "Duplicate id found");
}
}
