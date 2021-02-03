package interpretor;

public class NameError extends Exception {
	private static final long serialVersionUID = 1L;
	String var;
public NameError(String e) {
	var=e;
	
}
	public String toString() {
		return var;
	}
}
