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

public class Println extends Statement{
	public Println(ArrayList<Expr> expr) {
		this.expr = expr;
	}
	public void genC(PW pw) {
		
	}
	
	public void genJava(PW pw) {
		pw.printIdent("System.out.println(\"\" + ");
		if(expr.size() > 0) {
			if(this.expr.get(0).getType() == Type.intType) {
				pw.print("(");
				this.expr.get(0).genJava(pw);
				pw.print(")");
			}
			else {
				this.expr.get(0).genJava(pw);
			}	
		}
		for(int i = 1; i < expr.size(); i++) {
			pw.print(" + ");
			if(this.expr.get(i).getType() == Type.intType) {
				pw.print("(");
				this.expr.get(i).genJava(pw);
				pw.print(")");
			}
			else {
				this.expr.get(i).genJava(pw);
			}	
		}
		pw.println(");");
	}

	private ArrayList<Expr> expr = null;
}

