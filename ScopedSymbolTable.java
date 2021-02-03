package interpretor;

import java.util.HashMap;
import java.util.HashSet;

public class ScopedSymbolTable {
	HashMap<String, Symbol> symbols;
	String scope_name;
	ScopedSymbolTable encl;
	int scope_level;

	public ScopedSymbolTable(String scope_name, int scope_level, ScopedSymbolTable encl) {
		symbols = new HashMap<>();
		this.scope_name = scope_name;
		this.scope_level = scope_level;
		this.encl=encl;
		init_builtins();
	}

	public void init_builtins() {
		define(new BuiltinTypeSymbol("INTEGER", null));
		define(new BuiltinTypeSymbol("REAL", null));
	}

	public String toString() {
		System.out.println("SCOPE (SCOPED SYMBOL TABLE)");
	    System.out.println("Scope name "+ scope_name);
	    System.out.println("Scope level "+ scope_level);
		String s = "Scope (Scoped symbol table) contents: ";
		for (String str : symbols.keySet()) {
			s += symbols.get(str) + ", ";
		}

		return s;
	}

	public void define(Symbol symbol) {
		System.out.println("Define:" + symbol);
		symbols.put(symbol.name, symbol);
	}

	public Symbol lookup(String name) {
		System.out.println("Looking up: " + name);
		if (symbols.containsKey(name)) {
			return symbols.get(name);
		} else if(encl!=null) {
			return encl.lookup(name);
		}
		else {
			return null;
		}
	}
}
