package ast;

public class LocalDecExpr extends Variable{
	public LocalDecExpr(Type type, String id, Expr expr) {
		this.id = id;
		this.type = type;
		this.expr = expr;
	}
	
	public void genC(PW pw) {

	}
	public void genJava(PW pw) {
		
	}
	public Type getType() {
		return type;
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}

	private Type type;
	private String id;
	private Expr expr;

}
