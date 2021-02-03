package interpretor;

import java.util.ArrayList;

public class ProcedureSymbol extends Symbol{
ArrayList<VarSymbol> params= new ArrayList<>();
	    public ProcedureSymbol(String name, Symbol type) {
		super(name, type);
		// TODO Auto-generated constructor stub
	}
	    public ProcedureSymbol(String name, Symbol type,
	    		ArrayList<VarSymbol> params) {
	    	super(name, type);
	    	if(params!=null) {
	    		this.params=params;
	    	}
	    	
	    }
	    
	public String toString() {
		String s="procedure symbol "+name+", parameters: ";
		for(VarSymbol p: params) {
			s+=p+", ";
		}
		return s;
	}
	       
}
