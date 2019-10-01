package ast;

import java.util.ArrayList;


public class SelfMethodCallPar {
	public SelfMethodCallPar(Variable variable, String methodName, ArrayList<Expr> expr){
		this.methodName = methodName;
		this.expr = expr;
		this.variable = variable;
	}
	
	public void genJava(PW pw) {
		pw.print("self.");
		this.variable.genJava(pw);
		pw.print("." + this.methodName + "( ");
		if(this.expr.size() > 0) {
			this.expr.get(0).genJava(pw);
		}
		for(int i = 1; i < this.expr.size(); i++) {
			pw.print(", ");
			this.expr.get(i).genJava(pw);
		}
		pw.print(" )");
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
