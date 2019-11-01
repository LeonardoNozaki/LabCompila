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

public class AssertStat extends Statement{
	public AssertStat( Expr expr, String message ) {
		this.expr = expr;
		this.message = message;
	}
	
	public void genJava(PW pw) {
		pw.printIdent("if(!( " );
		this.expr.genJava(pw);
		pw.println(" )){");
		pw.add();
		pw.printlnIdent("System.out.print(\"" + message + "\");");
		pw.sub();
		pw.printlnIdent("}");
	}

	public void genC( PW pw ) {
		pw.printIdent("if( " );
		this.expr.genC(pw, true);
		pw.println(" == false){");
		pw.add();
		pw.printlnIdent("puts(\"" + message + "\");");
		pw.sub();
		pw.printlnIdent("}");
	}

	private Expr expr;
	private String message;
}
