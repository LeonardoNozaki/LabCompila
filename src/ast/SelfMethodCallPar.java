package ast;

import java.util.ArrayList;


public class SelfMethodCallPar extends Expr{
	public SelfMethodCallPar(Variable variable, MethodDec method, ArrayList<Expr> expr){
		this.method = method;
		this.expr = expr;
		this.variable = variable;
	}
	
	public boolean isOnlyId() {
		return false;
	}
	
	public boolean isObjectCreation() {
		return false;
	}
	
	public void genJava(PW pw) {
		pw.print("this.");
		this.variable.genJava(pw);
		pw.print("." + this.method.getName() + "( ");
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

	private MethodDec method;
	private ArrayList<Expr> expr;
	private Variable variable; 
}
