package interpretor;

public class VarSymbol extends Symbol{
	
	    public VarSymbol(String name, Symbol type) {
		super(name, type);
		// TODO Auto-generated constructor stub
	}
		

	public String toString() {
	 return "<{"+name+"}:{"+type+"}>";
	}
}
	   
