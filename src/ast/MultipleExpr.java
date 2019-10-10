/* ==========================================================================
 * Universidade Federal de Sao Carlos - Campus Sorocaba
 * Disciplina: Laboratorio de Compiladores
 * Prof. Jose Guimaraes
 *
 * Trabalho de Laboratorio de Compiladores - The Cianeto Language
 *
 * Aluno: Bruno Rizzi       RA: 743515
 * Aluno: Leonardo Nozaki   RA: 743561
 * ========================================================================== */

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
	
	public void genJava(PW pw) {
		this.expr.get(0).genJava(pw);
		for(int i = 0; i < this.expr.size(); i++) {
			pw.print(" " + this.op.get(i-1).toString() + " ");
			this.expr.get(i).genJava(pw);
		}
	}
	
	public boolean isOnlyId() {
    	int tam = this.expr.size();
    	if(tam == 1) {
    		return this.expr.get(0).isOnlyId();
    	}
    	else {
    		return false;
    	}
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
