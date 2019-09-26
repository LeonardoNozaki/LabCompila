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

public class TypeUndefined extends Type {
    // variables that are not declared have this type
    
	public TypeUndefined() { super("undefined"); }
   
   	public String getCname() {
	   return "int";
   	}
   
   	@Override
  	public String getJavaname() {
   		return "int";
   	} 
}
