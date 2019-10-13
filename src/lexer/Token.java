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

package lexer;

public enum Token {

    AND("&&"),
    ANNOT("~annotation"),
    ASSERT("assert"),
    ASSIGN("="),
    BOOLEAN("Boolean"),
    BREAK("break"),
    CLASS("class"),
    COMMA(","),
    DIV("/"),
    DOT("."),
    ELSE("else"),
    END("end"),
    EOF("~eof"),
    EQ("=="),
    EXTENDS("extends"),
    FALSE("false"),
    FINAL("final"),
    FUNC("func"),
    GE(">="),
    GT(">"),
    ID("~ident"),
    IDCOLON("~ident:"),
    IF("if"),
    INT("Int"),
    LE("<="),
    LEFTCURBRACKET("{"),
    LEFTPAR("("),
    LITERALINT("~number"),
    LITERALSTRING("~literalString"),
    LT("<"),
    MINUS("-"),
    MINUS_GT("->"),
    MULT("*"),
    NEQ("!="),
    NOT("!"),
    NULL("nil"),
    PLUSPLUS("++"),
    OR("||"),
    OVERRIDE("override"),
    PLUS("+"),
    PRIVATE("private"),
    PUBLIC("public"),
    REPEAT("repeat"),
    RETURN("return"),
    RIGHTCURBRACKET("}"),
    RIGHTPAR(")"),
    SELF("self"),
    SEMICOLON(";"),
    STRING("String"),
    SUPER("super"),
    TRUE("true"),
    UNTIL("until"),
    VAR("var"),
    WHILE("while");

	Token(String name) {
		this.name = name;
	}

	@Override public String toString() {
		return name;
	}
	private String name;
}