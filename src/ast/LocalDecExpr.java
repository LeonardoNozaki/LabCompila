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
		pw.printIdent(this.type.getCname() + " _" + id + " = ");
		this.expr.genC(pw, false);
		pw.println(";");
	}
	
	public boolean isOnlyId() {
    	return false;
    }
	
	public void genJava(PW pw) {
		pw.printIdent(this.type.getJavaname() + " " + id + " = ");
		this.expr.genJava(pw);
		pw.println(";");
	}
	public Type getType() {
		return type;
	}
	
	public void genC( PW pw, boolean putParenthesis ) {
		
	}

	public String getName() {
		return this.id;
	}
	
	private Type type;
	private String id;
	private Expr expr;
	private boolean init;
}
