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

public class TypeInt extends Type {
    
    public TypeInt() {
    	super("int");
    }
    
    
    @Override
    public String getCname() {
    	return "int";
    }

    @Override
    public String getJavaname() {
    	return "int";
    }
}