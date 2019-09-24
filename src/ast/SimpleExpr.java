package ast;

public class SimpleExpr extends Expr{
	public SimpleExpr(Expr expr) {
		this.expr = expr;
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
    	
    }

	@Override
	public void genC(PW pw) {
		//this.genC(pw, false);
	}
	
	@Override
	public Type getType() {
		return expr.getType();
	}
	
	private Expr expr;
}
