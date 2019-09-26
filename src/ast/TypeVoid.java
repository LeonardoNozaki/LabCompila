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

public class TypeVoid extends Type {
	
	public TypeVoid() { 
		super("void"); 
	}
	
	public String getCname() {
		return "void";
	}
	
	@Override
   	public String getJavaname() {
    	return "void";
    }
}
