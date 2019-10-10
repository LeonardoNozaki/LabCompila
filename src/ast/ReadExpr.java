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

public class ReadExpr extends Expr {
	public ReadExpr(String name) {
		this.name = name;
	}
	
	public boolean isOnlyId() {
    	return false;
    }
	
	public void genJava(PW pw) {
		//pw.printlnIdent("Scanner scanner = new Scanner(System.in);");
		if(this.name.equals("readInt")) {
			pw.print("scanner.nextInt()");
		}
		if(this.name.equals("readString")) {
			pw.print("scanner.nextLine()");
		}
		
			
	}
	
	public void genC(PW pw, boolean putParenthesis) {
		
	}
	
	public Type getType() {
		if(name.equals("readInt")) {
			return Type.intType;
		}
		else if(name.contentEquals("readString")){
			return Type.stringType;
		}
		else {
			return Type.undefinedType;
		}
	}
	
	private String name;
}
