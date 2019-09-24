package ast;
import java.util.ArrayList;
import lexer.Token;

public class MultipleExpr extends Expr{
	public MultipleExpr(ArrayList<Expr> expr, ArrayList<Token> op) {
		this.expr = expr;
		this.op = op;
	}
	
    public void genC( PW pw, boolean putParenthesis ) {
    	
    }
    
	@Override
	public void genC(PW pw) {
		//this.genC(pw, false);
	}
	
	@Override
	public Type getType() {
		int tam = this.op.size();
		if(tam > 1){
			for(int i = 0; i < tam; i++) {
				if(this.op.get(i) == Token.PLUSPLUS) {
					return Type.stringType;
				}
				else if(this.op.get(i) == Token.AND || this.op.get(i) == Token.OR) {
					return Type.booleanType;
				}
			}
			return Type.intType;
		}
		else if(tam == 1){ 
			return this.expr.get(0).getType();
		}
		return Type.undefinedType;
	}
	
	private ArrayList<Expr> expr;
	private ArrayList<Token> op;
}
