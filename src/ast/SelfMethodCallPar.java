package ast;

import java.util.ArrayList;


public class SelfMethodCallPar {
	public SelfMethodCallPar(Variable variable, String methodName, ArrayList<Expr> expr){
		this.methodName = methodName;
		this.expr = expr;
		this.variable = variable;
	}
	
	public void genJava(PW pw) {
		
	}
	public void genC(PW pw) {
		
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	
	public Type getType() {
		return Type.undefinedType;
	}

	private String methodName;
	private ArrayList<Expr> expr;
	private Variable variable; 
}
