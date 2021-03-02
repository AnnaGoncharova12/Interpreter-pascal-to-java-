package interpretor;

import java.util.ArrayList;
import java.util.HashMap;

public class AST {
	static CallStack stack = new CallStack();
	 static ScopedSymbolTable symtab = new ScopedSymbolTable("global", 1, null);
	static HashMap<String, Object> symbolTable = new HashMap<>();
	static ScopedSymbolTable current_scope = null;

	static class Node {
		public Object visit() {
			if (this instanceof AST.Num) {
				AST.Num node = (AST.Num) this;
				return node.visit();
			} else if (this instanceof AST.BinOp) {
				AST.BinOp node = (AST.BinOp) this;
				return node.visit();

			} else if (this instanceof AST.UnaryOp) {
				AST.UnaryOp node = (AST.UnaryOp) this;
				return node.visit();

			} else if (this instanceof AST.NoOp) {
				AST.NoOp node = (AST.NoOp) this;
				return node.visit();

			} else if (this instanceof AST.Compound) {
				AST.Compound node = (AST.Compound) this;
				return node.visit();

			} else if (this instanceof AST.Var) {
				AST.Var node = (AST.Var) this;
				return node.visit();

			} else if (this instanceof AST.Assign) {
				AST.Assign node = (AST.Assign) this;
				return node.visit();

			} else if (this instanceof AST.Param) {
				AST.Param node = (AST.Param) this;
				return node.visit();

			} else if (this instanceof AST.ProcedureDecl) {
				AST.ProcedureDecl node = (AST.ProcedureDecl) this;
				return node.visit();

			} else {
				try {
					throw new InvalidCharacterException();
				} catch (InvalidCharacterException e) {
					System.out.println("no visit method");
				}
				return 0;
			}
		}

		public void error(String error_code, Token tok) {
			try {
				throw new SemanticError(error_code, tok, error_code + " ->" + tok.value);
			} catch (SemanticError e) {
				System.out.println(e.message);
			}
		}
	}

	static class ProcedureCall extends Node {
		String proc_name;
		ArrayList<Node> actual_params;
		Token token;
		ProcedureSymbol proc_symbol;

		public ProcedureCall(String proc_name, ArrayList<Node> actual_params, Token token) {
			this.proc_name = proc_name;
			this.actual_params = actual_params;
			this.token = token;
			this.proc_symbol=null;
		}

		public Object visit() {
			this.proc_symbol=(ProcedureSymbol)current_scope.lookup(proc_name, false);
			ActivationRecord  ar = new ActivationRecord(proc_name,ARType.PROCEDURE,proc_symbol.scope_level + 1);
			for (AST.Node p : actual_params) {
				p.visit();
			}
			
  ArrayList<VarSymbol> formal_params = proc_symbol.params;
  for(int i=0;i<formal_params.size();i++) {
		
	  ar.__setitem__(formal_params.get(i).name, actual_params.get(i).visit());
  }


					    stack.push(ar);
					    
					    System.out.println("Enter procedure "+proc_name);
					    System.out.println(stack);
					    current_scope=proc_symbol.myScope;
					    proc_symbol.block_ast.visit();
					    System.out.println(stack);
					    System.out.println("Leave procedure "+proc_name);
					    stack.pop();
					    current_scope = current_scope.encl;
			return null;
		}
	}

	static class Param extends Node {
		Var var_node;
		VarType type_node;

		public Param(Var var_node, VarType type_node) {
			this.var_node = var_node;
			this.type_node = type_node;
		}
	}

	static class ProcedureDecl extends Node {
		String proc_name;
		Block block_node;
		ArrayList<Param> params;
	
		public ProcedureDecl(String proc_name, ArrayList<Param> params, Block block_node) {
			this.proc_name = proc_name;
			this.params = params;
			this.block_node = block_node;
			
		}

		public Object visit() {

			ProcedureSymbol proc_symbol = new ProcedureSymbol(proc_name, null);
			proc_symbol.block_ast=block_node;
			current_scope.define(proc_symbol);
//define vs insert not sure
			System.out.println("ENTER scope: " + proc_name);

			ScopedSymbolTable procedure_scope = new ScopedSymbolTable(proc_name, current_scope.scope_level + 1,
					current_scope);
			current_scope = procedure_scope;
			proc_symbol.myScope=procedure_scope;

			for (Param param : params) {
				Symbol param_type = current_scope.lookup(param.type_node.value, false);
				String param_name = param.var_node.value;
				VarSymbol var_symbol = new VarSymbol(param_name, param_type);
				current_scope.define(var_symbol);
				proc_symbol.params.add(var_symbol);
			}
			//this.block_node.visit();

			System.out.println(procedure_scope);
			System.out.println("LEAVE scope: " + proc_name);
			current_scope = current_scope.encl;
			return null;
		}
	}

	static class Program extends Node {
		String name;
		Block block;

		public Program(String name, Block block) {
			this.name = name;
			this.block = block;
		}

		public Object visit() {
			System.out.println("ENTER scope: global");
			ScopedSymbolTable global_scope = new ScopedSymbolTable("global", 1, null);
			ActivationRecord ar = new ActivationRecord(this.name, ARType.PROGRAM, 1);
			System.out.println("Enter program "+name);
			stack.push(ar);
			System.out.println(stack);
			current_scope = global_scope;
			
			block.visit();
			System.out.println(stack);
			stack.pop();
			System.out.println(global_scope);
			System.out.println("LEAVE scope: global");
			current_scope = current_scope.encl;
			return null;
		}
	}

	static class Block extends Node {
		Compound compound_statement;
		ArrayList<Node> declarations;

		public Block(ArrayList<Node> declarations, Compound compound_statement) {
			this.declarations = declarations;
			this.compound_statement = compound_statement;
		}

		public Object visit() {
			System.out.println("start block visiting");
			for (Node declaration : declarations) {
				declaration.visit();
			}
			compound_statement.visit();
			return null;
		}
	}

	static class VarDecl extends Node {
		Var var_node;
		VarType type_node;

		public VarDecl(Var var_node, VarType type_node) {
			this.var_node = var_node;
			this.type_node = type_node;
		}

		public Object visit() {
			String type_name = type_node.value;
			Symbol type_symbol = symtab.lookup(type_name, false);
			String var_name = var_node.value;
			System.out.println(var_name);

			Symbol var_symbol = new VarSymbol(var_name, type_symbol);
			if (current_scope.lookup(var_name, true) != null) {
				error("Error: Duplicate identifier ", var_node.token);

			}
			current_scope.define(var_symbol);
			Object val = (symtab.symbols.containsKey(var_name) ? symtab.symbols.get(var_name) : null);

			/*
			 * if (val != null) { System.out.println("Error: Duplicate identifier " +
			 * var_name + "found"); }
			 */

			// symtab.define(var_symbol);
			return null;
		}
	}

	static class VarType extends Node {
		Token token;
		String value;

		public VarType(Token token) {
			this.token = token;
			this.value = token.value;
		}

		public Object visit() {
			return null;
		}
	}

	static class NoOp extends Node {
		public Integer visit() {
			return null;
		}
	}

	static class Compound extends Node {
		ArrayList<Node> children;

		public Compound() {
			children = new ArrayList<>();
		}

		public Integer visit() {
			for (Node child : children) {
				child.visit();
			}
			return null;
		}
	}

	static class Var extends Node {
		Token token;
		String value;

		public Var(Token token) {
			this.token = token;
			this.value = token.value;
		}

		public Object visit() {

			String var_name = value;
			Symbol var_symbol = current_scope.lookup(var_name, false);
			if (var_symbol == null) {
				error("ID_NOT_FOUND", token);

			}
			Object val = (stack.peek().get(var_name));
			if (val == null) {
				try {
					throw new NullPointerException();
				} catch (NullPointerException e) {
					System.out.println("no value for the variable " + var_name);
				}
				return null;
			} else {

				return val;
			}
		}
	}

	static class Assign extends Node {
		Var left;
		Node right;
		Token op;

		public Assign(Var left, Token op, Node right) {
			System.out.println("hi");
			this.left = left;
			this.op = op;
			this.right = right;
		}

		public Integer visit() {

			String var_name = left.value;
			//Symbol var_symbol = symtab.lookup(var_name, false);
			Symbol var_symbol = current_scope.lookup(var_name, false);
			if (var_symbol == null) {
				try {
					throw new NameError(var_name);
				} catch (NameError e) {
					System.out.println(e);
				}
			}
			Object var_value=right.visit();
			stack.peek().__setitem__(var_name, var_value);
			symbolTable.put(var_name, var_value);
			return null;
		}
	}

	static class BinOp extends Node {
		Node left, right;
		Token op;

		public BinOp(Node left, Token op, Node right) {
			this.left = left;
			this.op = op;
			this.right = right;
		}

		public Object visit() {
			AST.BinOp node = this;
			Object left = this.left.visit();
			Object right = this.right.visit();
			System.out.println(left + " " + right);
			Integer il = null, ir = null;
			Float fl = null, fr = null;
			boolean flo = false;

			System.out.println(left + " " + right);
			if (left instanceof Integer && right instanceof Integer) {
				il = (Integer) left;
				ir = (Integer) right;
			} else {
				if (left instanceof Integer) {
					fl = new Float(((Integer) left).floatValue());
				} else {
					fl = (Float) left;
				}
				if (right instanceof Integer) {
					fr = new Float(((Integer) right).floatValue());

				} else {
					fr = (Float) right;
				}
				flo = true;
			}

			// System.out.println(node.op.t);
			if (node.op.t == Type.PLUS) {
				if (flo) {
					return fl + fr;
				} else {
					return il + ir;
				}
				/*
				 * Object o = null; if(left instanceof Integer) {
				 * 
				 * if(right instanceof Integer) {
				 * 
				 * o= (Integer)left+(Integer)right; } else { o= (Integer)left+(Float)right; } }
				 * else { left=(Float)left; if(right instanceof Integer) {
				 * 
				 * o=(Float)left+(Integer)right; } else { o=(Float)right+(Integer)right; } }
				 * 
				 * 
				 * System.out.println(o instanceof Float); return o;
				 */
			} else if (node.op.t == Type.MINUS) {
				if (flo) {
					return fl - fr;
				} else {
					return il - ir;
				}

			} else if (node.op.t == Type.MUL) {
				System.out.println("hey");
				System.out.println(left instanceof Integer);
				System.out.println(right instanceof Integer);
				Object o = null;
				if (flo) {
					return fl * fr;
				} else {
					return il * ir;
				}

			} else if (node.op.t == Type.FLOAT_DIV) {
				fl = new Float(((Integer) left).floatValue());
				fr = new Float(((Integer) right).floatValue());
				return fl / fr;
			}

			else {
				return il / ir;
			}
		}
	}

	static class Num extends Node {
		Object value;
		Token token;

		public Num(Token token) {
			this.token = token;
			if (token.t == Type.INTEGER_CONST) {
				value = (Integer) Integer.valueOf(token.value);
			} else {
				value = Float.valueOf(token.value);
			}
		}

		public Object visit() {
			return this.value;
		}
	}

	static class UnaryOp extends Node {
		Token op;
		AST.Node expr;

		public UnaryOp(Token op, AST.Node expr) {
			this.op = op;
			this.expr = expr;
		}

		public Object visit() {

			Object o = this.expr.visit();
			boolean integer = false;
			Integer i = null;
			Float f = null;
			if (o instanceof Integer) {
				i = (Integer) o;
				integer = true;
			} else {
				f = (Float) o;
			}
			Type op = this.op.t;
			if (op == Type.PLUS) {
				return +(integer ? i : f);
			} else {
				return -(integer ? i : f);
			}
		}
	}
}
