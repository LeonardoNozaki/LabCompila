package ast;

public class SelfExpr extends Expr {
	public void genJava(PW pw) {
		pw.print("this");
	}
	
	public void genC(PW pw) {
		
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	
	public Type getType() {
		return Type.undefinedType;
	}

}
