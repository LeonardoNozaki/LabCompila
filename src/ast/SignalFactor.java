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
import lexer.*;

public class SignalFactor extends Expr{
	public SignalFactor(Token op, Expr expr) {
		this.op = op;
		this.expr = expr;
	}
	
	public boolean isOnlyId() {
		if(this.op == Token.NOT) {
			return false;
		}
    	return expr.isOnlyId();
    }
	
	public boolean isObjectCreation() {
		return false;
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
    	
    }

	@Override
	public void genC(PW pw) {
		//this.genC(pw, false);
	}
	
	public void genJava(PW pw) {
		pw.print(this.op.toString());
		this.expr.genJava(pw);
	}
	
	@Override
	public Type getType() {
		if(this.op == Token.NOT) {
			return Type.booleanType;
		}
		return expr.getType(); //Nao sei se precisa disso, ou é so return intType
	}
	
	private Token op;
	private Expr expr;
}
