package interpretor;

import java.util.ArrayList;

public class Parser {

Token currToken;
Lexer lexer;

	public Parser(Lexer l) {
		lexer=l;
			currToken=lexer.get_next_token();
			
		}
	public AST.Block block(){
	   // """block : declarations compound_statement"""
		ArrayList<AST.Node>  declaration_nodes =this.declarations();
	   AST.Compound compound_statement_node = this.compound_statement();
	   AST.Block node = new AST.Block(declaration_nodes, compound_statement_node);
	    return node;
	}

	ArrayList<AST.Param> formal_parameter_list() {
		/*
		  """ formal_parameter_list : formal_parameters
        | formal_parameters SEMI formal_parameter_list
        */
		ArrayList<AST.Param> params=new ArrayList<>();
		while(currToken.t!=Type.RPAREN) {
			params.addAll(formal_parameters());
			if(currToken.t!=Type.RPAREN) {
				eat(Type.SEMI);
			}
		}
        return params;
	}
	ArrayList<AST.Param>formal_parameters() {
		/*
	    """ formal_parameters : ID (COMMA ID)* COLON type_spec """
	    param_nodes = []
	    */
		ArrayList<AST.Param> params=new ArrayList<>();
		ArrayList<AST.Var> vars=new ArrayList<>();
		while(currToken.t==Type.ID) {
			vars.add(new AST.Var(currToken));
			eat(Type.ID);
			if(currToken.t==Type.COMMA) {
				eat(Type.COMMA);
			}
		}
		eat(Type.COLON);
		AST.VarType ty=type_spec();
		for(AST.Var v: vars) {
			params.add(new AST.Param(v, ty));
		}
		return params;
}
	public ArrayList<AST.Node> declarations(){
   
       ArrayList<AST.Node> declarations = new ArrayList<AST.Node>();
    if (currToken.t == Type.VAR) {
        eat(Type.VAR);
        while (currToken.t == Type.ID){
        	ArrayList<AST.VarDecl> var_decl = variable_declaration();
            declarations.addAll(var_decl);
            eat(Type.SEMI);
        }
    }
    while (currToken.t == Type.PROCEDURE) {
       eat(Type.PROCEDURE);
        String proc_name = currToken.value;
        eat(Type.ID);
       
        ArrayList<AST.Param> params= new ArrayList<>();
        if(currToken.t==Type.LPAREN) {
        	eat(Type.LPAREN);
        	System.out.println("hey");
        	params=formal_parameter_list();
        	eat(Type.RPAREN);
        }
        eat(Type.SEMI);
        AST.Block block_node = block();
       AST.ProcedureDecl proc_decl = new AST.ProcedureDecl(proc_name,params, block_node);
        declarations.add(proc_decl);
        eat(Type.SEMI);
    }
    return declarations;
	}
	public ArrayList<AST.VarDecl> variable_declaration() {
	   // """variable_declaration : ID (COMMA ID)* COLON type_spec"""
	   ArrayList<AST.Var> var_nodes= new ArrayList<>();
		var_nodes.add(new AST.Var(currToken));
	    eat(Type.ID);

	    while (currToken.t == Type.COMMA) {
	     eat(Type.COMMA);
	        var_nodes.add(new AST.Var(currToken));
	        eat(Type.ID);
	    }
	   eat(Type.COLON);

	   AST.VarType type_node = type_spec();
	   ArrayList<AST.VarDecl> var_declarations=new ArrayList<AST.VarDecl>();
	   for (AST.Var var_node : var_nodes) {
	    
		   var_declarations.add(new AST.VarDecl(var_node, type_node));
	        
	   }
	    return var_declarations;
}
	public AST.VarType type_spec() {
	  
	   Token token = currToken;
	    if (currToken.t == Type.INTEGER) {
	        eat(Type.INTEGER);
	    }
	    else {
	       eat(Type.REAL);
	    }
	   return new AST.VarType(token);
	
	}
	public AST.Program program() {
		 eat(Type.PROGRAM);
		  AST.Var  var_node = variable();
		  String  prog_name = var_node.value;
		   eat(Type.SEMI);
		    AST.Block block_node = block();
		AST.Program   node = new AST.Program(prog_name, block_node);

	    eat(Type.DOT);
	    return node;
}

	public AST.Compound compound_statement(){
	
	    eat(Type.BEGIN);
	   ArrayList<AST.Node> nodes = statement_list();
	    eat(Type.END);

	   AST.Compound root = new  AST.Compound();
	    for (AST.Node node : nodes) {
	        root.children.add(node);
	    }
	    return root;
}

	public ArrayList<AST.Node> statement_list() {
	 
	    AST.Node node = statement();

	    ArrayList<AST.Node> results = new ArrayList<AST.Node>();
results.add(node);
	    while (currToken.t == Type.SEMI) {
	        eat(Type.SEMI);
	        results.add(statement());
	    }
	    if (currToken.t == Type.ID) {
	        error();
	    }
	    return results;
	}
	public AST.Node statement() {
	 
	    if (currToken.t == Type.BEGIN) {
	     AST.Compound   node = compound_statement();
	        return node;
	    }
	        else if (currToken.t == Type.ID) {
	       AST.Assign node = assignment_statement();
	        return node;
	        }
	        else {
	      AST.NoOp  node = this.empty();
	        return node;
	    }
	   
}

	public AST.Assign assignment_statement() {
	   
	    AST.Var left = variable();
	    Token token = currToken;
	    eat(Type.ASSIGN);
	   AST.Node right = expr();
	   AST.Assign node = new AST.Assign(left, token, right);
	    return node;
	    }
	public AST.Var variable(){
	 
	   AST.Var node = new AST.Var(currToken);
	    eat(Type.ID);
	    return node;
	    }

	public AST.NoOp empty() {
	
	    return new AST.NoOp();
}
	public void error() {
		try {
			throw new ParseException();
		}
		catch(ParseException e){
			System.out.println("hi");
			System.out.println(e);
		}
	}
	public void  eat(Type type) {
	
		/*
	    # compare the current token type with the passed token
	    # type and if they match then "eat" the current token
	    # and assign the next token to the self.current_token,
	    # otherwise raise an exception.
	    */
	    if (currToken.t == type) {
	        currToken = lexer.get_next_token();
	    }
	    else {
	    System.out.println("hello "+type );
	        error();
	    }
	}
	public AST.Node factor() {

		Token token = currToken;
		
		if (token.t == Type.PLUS) {
			eat(Type.PLUS);
			AST.Node result = new AST.UnaryOp(token, factor());
			return result;
		} else if (token.t == Type.MINUS) {
			eat(Type.MINUS);
			AST.Node result = new AST.UnaryOp(token, factor());
			return result;
		} else if (token.t == Type.INTEGER_CONST) {
	        eat(Type.INTEGER_CONST);
	        return new AST.Num(token);
		}
		else if (token.t == Type.REAL_CONST) {
	        eat(Type.REAL_CONST);
	        return new AST.Num(token);
		}else if (token.t == Type.LPAREN) {
			eat(Type.LPAREN);
			AST.Node result = expr();
			eat(Type.RPAREN);
			return result;
		} else {
			 
			    AST.Node    node = variable();
			        return node;
		}

	}
	public AST.Node term() {
	    
	
		Token token=null;
		AST.Node toReturn=factor();
			        while (currToken.t==Type.MUL||currToken.t== Type.INTEGER_DIV
			        		||currToken.t== Type.FLOAT_DIV) {
			            token = currToken;
			            System.out.println(token.t);
			            if (token.t == Type.MUL) {
			                eat(Type.MUL);
			             
			            }
			            else if (token.t== Type.FLOAT_DIV) {
			                eat(Type.FLOAT_DIV);
			              
			            }
			            else {
			            	eat(Type.INTEGER_DIV);
			            }
			            toReturn= new AST.BinOp(toReturn, token, factor());
			        }
			       
			        return toReturn;
	}
	public AST.Node expr() {
		   
		   
		 Token     token=null;
		 AST.Node node = term();
		            while (currToken.t==Type.PLUS||currToken.t==Type.MINUS) {
		             token = currToken;
		                if (token.t == Type.PLUS) {
		                   eat(Type.PLUS);
		                 
		                }
		                else {
		                    eat(Type.MINUS);
		                 
		                }
		                node =new AST.BinOp(node, token, term());
		}
		           
		            return node;
		}
	public AST.Program parse() {
	   AST.Program node =program();
	    	    if( currToken.t!= Type.EOF){
	    	        error();
	    	    }
	    	    return node;
	}
}
