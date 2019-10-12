package ast;

public class LocalDecExpr extends Variable{
	public LocalDecExpr(Type type, String id, Expr expr) {
		this.id = id;
		this.type = type;
		this.expr = expr;
		this.init = false;
	}
	
	public boolean getInit() {
		return this.init;
	}
	
	public boolean isObjectCreation() {
		return false;
	}
	
	public void genC(PW pw) {

	}
	
	public boolean isOnlyId() {
    	return false;
    }
	
	public void genJava(PW pw) {
		pw.printIdent(this.type.getJavaname() + id + " = ");
		this.expr.genJava(pw);
		pw.println(";");
	}
	public Type getType() {
		return type;
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}

	private Type type;
	private String id;
	private Expr expr;
	private boolean init;
}
