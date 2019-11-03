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

public class CompositeAssign extends AssignExpr {
	public CompositeAssign(Expr left, Expr right) {
		this.left = left;
		this.right = right;
	}
	
	public void genC(PW pw) {
		Type l = this.left.getType();
		Type r = this.right.getType();
		if(l instanceof TypeCianetoClass) {
			if(this.right instanceof ObjectCreation) {
				if(r.getName().equals(l.getName()) == false) {
					pw.printIdent("");
					this.left.genC(pw, false);
					pw.print(" = (_class_" + r.getName() + " *) ");
					this.right.genC(pw, false);
					pw.println(";");
					return;
				}
			}
			else if(r instanceof TypeCianetoClass) {
				if(r.getName().equals(l.getName()) == false) {
					pw.printIdent("");
					this.left.genC(pw, false);
					pw.print(" = (_class_" + r.getName() + " *) ");
					this.right.genC(pw, false);
					pw.println(";");
					return;
				}
			}
		}
		pw.printIdent("");
		this.left.genC(pw, false);
		pw.print(" = ");
		this.right.genC(pw, false);
		pw.println(";");
	}
	
	public void genJava(PW pw) {
		pw.printIdent("");
		this.left.genJava(pw);
		pw.print(" = ");
		this.right.genJava(pw);
		pw.println(";");
	}
	
	private Expr left, right;
}


