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

public class TypeString extends Type {
    
    public TypeString() {
        super("String");
    }
    
    @Override
    public String getCname() {
    	return "char *";
    }

    @Override
   	public String getJavaname() {
    	return "String";
    }
}