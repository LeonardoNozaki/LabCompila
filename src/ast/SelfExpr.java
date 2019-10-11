package ast;

public class SelfExpr extends Expr {
	public SelfExpr(TypeCianetoClass type) {
		this.type = type;
	}
	
	public void genJava(PW pw) {
		pw.print("this");
	}
	
	public boolean isOnlyId() {
    	return true;
    }
	
	public void genC(PW pw) {
		
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}
	
	public Type getType() {
		return type;
	}

	private TypeCianetoClass type;
}
