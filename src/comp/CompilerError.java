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

package comp;

public class CompilerError extends RuntimeException {
	public CompilerError(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorMessage() { return errorMessage; }

	private String errorMessage;
	private static final long serialVersionUID = 1L;

}
