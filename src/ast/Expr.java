package ast;
import lexer.*;

/*abstract*/ public class Expr extends Statement {
	public Expr(Token op, SimpleExpr left, SimpleExpr right) {
		this.op = op;
		this.left = left;
		this.right = right;
	}
    //abstract public void genC( PW pw, boolean putParenthesis );
	@Override
	public void genC(PW pw) {
		//this.genC(pw, false);
	}

      // new method: the type of the expression
    //abstract public Type getType();
	
	private Token op;
	private SimpleExpr left, right;
}