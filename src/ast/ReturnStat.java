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

public class ReturnStat extends Statement{
	public ReturnStat( Expr expr, Type type) {
		this.expr = expr;
		this.type = type;
	}

	public void genC( PW pw ) {
		pw.printIdent("return ");
		if(type instanceof TypeCianetoClass) {
			if(type.getName().equals(expr.getType().getName())) {
				this.expr.genC(pw, false);
			}
			else {
				pw.print("(" + type.getCname() + "*) ");
				this.expr.genC(pw, false);
			}
		}
		else {
			this.expr.genC(pw, false);
		}
		pw.println(";");
	}
	
	public void genJava(PW pw) {
		pw.printIdent("return ");
		this.expr.genJava(pw);
		pw.println(";");
	}

	private Expr expr;
	private Type type;
}
