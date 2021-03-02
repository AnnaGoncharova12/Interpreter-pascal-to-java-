package interpretor;

public class Symbol {
String name;
Symbol type;
int scope_level;
	  public Symbol(String name, Symbol type) {
	        this.name = name;
	        
	     this.type = type;
	     scope_level=0;
	  }
}
