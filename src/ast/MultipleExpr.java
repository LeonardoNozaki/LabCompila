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
	
	public boolean isObjectCreation() {
		return false;
	}
	
    public void genC( PW pw, boolean putParenthesis ) {
    	if(putParenthesis == true) {
    		pw.print("(");
    	}
    	int cont = 0;
    	for(int i = 0; i < this.op.size(); i++) {
    		if(this.op.get(i) == Token.PLUSPLUS) {
    			cont++;
    		}
    	}
    	
    	if(cont > 0) {
    		String aux = "strInitCat(),";
    		for(int k = 0; k <= cont; k++) {
    			aux = "strcat(" + aux;
    		}
    		pw.print(aux);
    		boolean flag = false;
    		if(this.expr.get(0).getType() == Type.intType) {
    			pw.print("toStr(");
    			this.expr.get(0).genC(pw);
    			flag = true;
    		}
    		else {
    			this.expr.get(0).genC(pw);
    		}
    		for(int i = 1; i < this.expr.size(); i++) {
    			if(this.op.get(i-1) == Token.PLUSPLUS) {
    				if(flag == true) {
    					pw.print(")");
    					flag = false;
    				}
    				pw.print("), ");
    				if(this.expr.get(i).getType() == Type.intType) {
    	    			pw.print("toStr(");
    	    			this.expr.get(i).genC(pw);
    	    			flag = true;
    	    		}
    				else {
    					this.expr.get(i).genC(pw);
    				}
    			}
    			else {
    				pw.print(" " + this.op.get(i-1).toString() + " ");
    				if(this.expr.get(i).getType() == Type.intType) {
    	    			pw.print("toStr(");
    	    			this.expr.get(i).genC(pw);
    	    			flag = true;
    	    		}
    				else {
    					this.expr.get(i).genC(pw);
    				}
    			}
    		}
    		pw.print(")");
    	}
    	else {
    		this.expr.get(0).genC(pw);
    		for(int i = 1; i < this.expr.size(); i++) {
    			pw.print(" " + this.op.get(i-1).toString() + " ");
    			this.expr.get(i).genC(pw);
    		}
    	}
    	
		if(putParenthesis == true) {
    		pw.print(")");
    	}
    }
    
    public void genC(PW pw) {
    	this.genC(pw, false);
    }
   
	public void genJava(PW pw) {
		this.expr.get(0).genJava(pw);
		for(int i = 1; i < this.expr.size(); i++) {
			if(this.op.get(i-1) == Token.PLUSPLUS) {
				pw.print(" +" + " \"\" " + "+ ");
				this.expr.get(i).genJava(pw);
			}
			else {
				pw.print(" " + this.op.get(i-1).toString() + " ");
				this.expr.get(i).genJava(pw);
			}
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
			if(this.op.get(0) == Token.PLUSPLUS) {
				return Type.stringType;
			}
			else if(this.op.get(0) == Token.AND || this.op.get(0) == Token.OR) {
				return Type.booleanType;
			}
			return Type.intType;
		}
		return Type.undefinedType;
	}
	
	private ArrayList<Expr> expr;
	private ArrayList<Token> op;
}
