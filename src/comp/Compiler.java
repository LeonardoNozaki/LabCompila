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

import java.io.PrintWriter;
import java.util.ArrayList;
import ast.*;
import lexer.Lexer;
import lexer.Token;

public class Compiler {

	public Compiler() { }

	// compile must receive an input with an character less than
	// p_input.lenght
	public Program compile(char[] input, PrintWriter outError) {
		ArrayList<CompilationError> compilationErrorList = new ArrayList<>();
		signalError = new ErrorSignaller(outError, compilationErrorList);
		symbolTable = new SymbolTable();
		lexer = new Lexer(input, signalError);
		signalError.setLexer(lexer);

		Program program = null;
		lexer.nextToken();
		program = program(compilationErrorList);
		return program;
	}

	private Program program(ArrayList<CompilationError> compilationErrorList) {
		ArrayList<MetaobjectAnnotation> metaobjectCallList = new ArrayList<>();
		ArrayList<TypeCianetoClass> CianetoClassList = new ArrayList<>();
		Program program = new Program(CianetoClassList, metaobjectCallList, compilationErrorList);
		boolean thereWasAnError = false;
		do {
			try {
				while ( lexer.token == Token.ANNOT ) {
					metaobjectAnnotation(metaobjectCallList);
				}
				CianetoClassList.add(classDec());
			}
			catch( CompilerError e) {
				// if there was an exception, there is a compilation error
				thereWasAnError = true;
			}
			catch ( RuntimeException e ) {
				e.printStackTrace();
				thereWasAnError = true;
			}

		} while ( lexer.token == Token.CLASS ||
				(lexer.token == Token.ID && lexer.getStringValue().equals("open") ) ||
				lexer.token == Token.ANNOT );
		if ( !thereWasAnError && lexer.token != Token.EOF ) {
			try {
				error("End of file expected");
			}
			catch( CompilerError e) {
			}
		}
		return program;
	}

	/**  parses a metaobject annotation as <code>{@literal @}cep(...)</code> in <br>
     * <code>
     * {@literal @}cep(5, "'class' expected") <br>
     * class Program <br>
     *     func run { } <br>
     * end <br>
     * </code>
     *

	 */
	@SuppressWarnings("incomplete-switch")
	private void metaobjectAnnotation(ArrayList<MetaobjectAnnotation> metaobjectAnnotationList) {
		String name = lexer.getMetaobjectName();
		int lineNumber = lexer.getLineNumber();
		lexer.nextToken();
		ArrayList<Object> metaobjectParamList = new ArrayList<>();
		boolean getNextToken = false;
		if ( lexer.token == Token.LEFTPAR ) {
			// metaobject call with parameters
			lexer.nextToken();
			while ( lexer.token == Token.LITERALINT || lexer.token == Token.LITERALSTRING ||
					lexer.token == Token.ID ) {
				switch ( lexer.token ) {
				case LITERALINT:
					metaobjectParamList.add(lexer.getNumberValue());
					break;
				case LITERALSTRING:
					metaobjectParamList.add(lexer.getLiteralStringValue());
					break;
				case ID:
					metaobjectParamList.add(lexer.getStringValue());
				}
				lexer.nextToken();
				if ( lexer.token == Token.COMMA )
					lexer.nextToken();
				else
					break;
			}
			if ( lexer.token != Token.RIGHTPAR )
				error("')' expected after annotation with parameters");
			else {
				getNextToken = true;
			}
		}
		switch ( name ) {
		case "nce":
			if ( metaobjectParamList.size() != 0 )
				error("Annotation 'nce' does not take parameters");
			break;
		case "cep":
			if ( metaobjectParamList.size() != 3 && metaobjectParamList.size() != 4 )
				error("Annotation 'cep' takes three or four parameters");
			if ( !( metaobjectParamList.get(0) instanceof Integer)  ) {
				error("The first parameter of annotation 'cep' should be an integer number");
			}
			else {
				int ln = (Integer ) metaobjectParamList.get(0);
				metaobjectParamList.set(0, ln + lineNumber);
			}
			if ( !( metaobjectParamList.get(1) instanceof String) ||  !( metaobjectParamList.get(2) instanceof String) )
				error("The second and third parameters of annotation 'cep' should be literal strings");
			if ( metaobjectParamList.size() >= 4 && !( metaobjectParamList.get(3) instanceof String) )
				error("The fourth parameter of annotation 'cep' should be a literal string");
			break;
		case "annot":
			if ( metaobjectParamList.size() < 2  ) {
				error("Annotation 'annot' takes at least two parameters");
			}
			for ( Object p : metaobjectParamList ) {
				if ( !(p instanceof String) ) {
					error("Annotation 'annot' takes only String parameters");
				}
			}
			if ( ! ((String ) metaobjectParamList.get(0)).equalsIgnoreCase("check") )  {
				error("Annotation 'annot' should have \"check\" as its first parameter");
			}
			break;
		default:
			error("Annotation '" + name + "' is illegal");
		}
		metaobjectAnnotationList.add(new MetaobjectAnnotation(name, metaobjectParamList));
		if ( getNextToken ) lexer.nextToken();
	}

	private TypeCianetoClass classDec() {
		boolean extend = false;
		String superclassName = "";
		
		if ( lexer.token == Token.ID && lexer.getStringValue().equals("open") ) {
			//Verificar se precisa de mais algo para o open
			lexer.nextToken();
		}
		if ( lexer.token != Token.CLASS ) error("'class' expected");
		
		lexer.nextToken();
		
		if ( lexer.token != Token.ID ) error("Identifier expected");
		
		String className = lexer.getStringValue();
		lexer.nextToken();
		
		if ( lexer.token == Token.EXTENDS ) {
			lexer.nextToken();
			if ( lexer.token != Token.ID ) error("Identifier expected");
			
			superclassName = lexer.getStringValue();
			extend = true;
			lexer.nextToken();
		}

		/*memberList();
		if ( lexer.token != Token.END) error("'end' expected");
		
		lexer.nextToken();
		*/
		
		if(extend == true) {
			classdec = new TypeCianetoClass(className, superclassName);
		}
		else {
			classdec = new TypeCianetoClass(className);
		}
		
		memberList();
		
		if ( lexer.token != Token.END) error("'end' expected");
		
		lexer.nextToken();
		
		return classdec;
	}

	private void memberList() {
		Qualifier quali;
		MethodDec methodDec;
		
		while ( true ) {
			quali = qualifier();
			if ( lexer.token == Token.VAR ) {
				fieldDec(quali);
			}
			else if ( lexer.token == Token.FUNC ) {
				//ver onde add cada metododec q volta, quais qualifier considera public e quais private
				methodDec = methodDec(quali);
			}
			else {
				break;
			}
		}
	}

	private void error(String msg) {
		this.signalError.showError(msg);
	}

	private void check(Token shouldBe, String msg) {
		//Se o token for diferente, exibe a mensagem e nao vai para o proximo token
		//Se o token for igual, vai para o proximo token
		
		if ( lexer.token != shouldBe ) {
			error(msg);
		}
		else {
			lexer.nextToken();
		}
	}

	private MethodDec methodDec(Qualifier qualifier) {
		lexer.nextToken();
		Type tipoRetorno = Type.voidType;
		ArrayList<ParamDec> paramDec = null;
		ArrayList<Statement> statementList = null;
		String id = "";
		
		if ( lexer.token == Token.ID ) {
			// unary method
			id = lexer.getStringValue();
			lexer.nextToken();
		}
		else if ( lexer.token == Token.IDCOLON ) {
			id = lexer.getStringValue();
			paramDec = formalParamDec();
		}
		else {
			error("An identifier or identifer: was expected after 'func'");
		}
		
		if ( lexer.token == Token.MINUS_GT ) {
			// method declared a return type
			lexer.nextToken();
			tipoRetorno = type();
		}
		if ( lexer.token != Token.LEFTCURBRACKET ) {
			error("'{' expected");
		}
		else {
			lexer.nextToken();
		}

		statementList = statementList();
		
		if ( lexer.token != Token.RIGHTCURBRACKET ) {
			error("'}' expected");
		}
		else {
			lexer.nextToken();
		}
		
		return new MethodDec(qualifier, id, paramDec, tipoRetorno, statementList);
	}

	private ArrayList<ParamDec> formalParamDec(){
		lexer.nextToken();
		ArrayList<ParamDec> p = new ArrayList<ParamDec>();
		Type tipo;
		ParamDec param;
		String id;
		while( true ) {
			tipo = type();
			if(tipo == Type.undefinedType) {
				error("A type was expected");
			}
			if(lexer.token == Token.ID) {
				id = lexer.getStringValue();
				param = new ParamDec(id, tipo);
				p.add(param);
			}
			else {
				error("An ID was expected");
			}
			lexer.nextToken();
			if ( lexer.token != Token.COMMA ) {
				return p;
			}
		}
	}
	
	private ArrayList<Statement> statementList() {
		ArrayList<Statement> statementList = new ArrayList<Statement>();
		  // only '}' is necessary in this test
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statementList.add(statement());
		}
		return statementList;
	}

	private Statement statement() {
		boolean checkSemiColon = true;
		Statement stat = null;
		switch ( lexer.token ) {
		case IF:
			stat = ifStat();
			checkSemiColon = false;
			break;
		case WHILE:
			stat = whileStat();
			checkSemiColon = false;
			break;
		case RETURN:
			stat = returnStat();
			break;
		case BREAK:
			stat = breakStat();
			break;
		case SEMICOLON:
			stat = semicolonStat();
			checkSemiColon = false;
			break;
		case REPEAT:
			stat = repeatStat();
			break;
		case VAR:
			//stat = localDec();
			break;
		case ASSERT:
			stat = assertStat();
			break;
		default:
			if ( lexer.token == Token.ID && lexer.getStringValue().equals("Out") ) {
				//stat = writeStat();
			}
			else {
				stat = expr();
			}

		}
		if ( checkSemiColon ) {
			check(Token.SEMICOLON, "';' expected");
		}
		return stat;
	}

	private void localDec() {
		// incompleto, falta terminar essa
		lexer.nextToken();
		Type type = type();
		boolean flag = false;
		
		check(Token.ID, "A variable name was expected");
		if ( lexer.token == Token.COMMA ) {
			ArrayList<Statement> stat = new ArrayList<Statement>();
			flag = true;
			lexer.nextToken();
			while ( lexer.token == Token.ID ) {
				lexer.nextToken();
				if ( lexer.token == Token.COMMA ) {
					lexer.nextToken();
				}
				else {
					break;
				}
			}
		}
		
		if ( lexer.token == Token.ASSIGN ) {
			lexer.nextToken();
			// check if there is just one variable
			if(flag) {
				error("there are two or more variable in assignment");
			}
			Expr expr = expr();
		}

	}

	private Statement semicolonStat() {
		lexer.nextToken();
		return new SemicolonStat();
	}
	private Statement repeatStat() {
		lexer.nextToken();
		ArrayList<Statement> stat = new ArrayList<Statement>();
		
		while ( lexer.token != Token.UNTIL && lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.EOF) {
			stat.add(statement());
		}
		
		check(Token.UNTIL, "missing keyword 'until'");
		Expr expr = expr();
		if(expr.getType() != Type.booleanType) {
			error("'repeat' expression expected Boolean Type");
		}
		return new RepeatStat(expr, stat);
	}

	private Statement breakStat() {
		lexer.nextToken();
		return new BreakStat();
	}

	private Statement returnStat() {
		lexer.nextToken();
		Expr expr = expr();
		return new ReturnStat(expr);
	}

	private Statement whileStat() {
		lexer.nextToken();
		Expr expr = expr();
		if(expr.getType() != Type.booleanType) {
			error("'while' expression expected Boolean Type");
		}
		
		ArrayList<Statement> stat = new ArrayList<Statement>();
		
		check(Token.LEFTCURBRACKET, "missing '{' after the 'while' expression");
		
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.EOF ) {
			stat.add(statement());
		}
		
		check(Token.RIGHTCURBRACKET, "missing '}' after 'while' body");
		
		return new WhileStat(expr, stat);
	}

	private Statement ifStat() {
		lexer.nextToken();
		Expr expr = expr();
		if(expr.getType() != Type.booleanType) {
			error("'If' expression expected Boolean Type");
		}
		
		ArrayList<Statement> leftStat = new ArrayList<Statement>();
		ArrayList<Statement> rightStat = new ArrayList<Statement>();
		
		check(Token.LEFTCURBRACKET, "'{' expected after the 'if' expression");
		
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.ELSE && lexer.token != Token.EOF) {
			leftStat.add(statement());
		}
		
		check(Token.RIGHTCURBRACKET, "'}' was expected");
		
		if ( lexer.token == Token.ELSE ) {
			lexer.nextToken();
			check(Token.LEFTCURBRACKET, "'{' expected after 'else'");
			
			while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.EOF) {
				rightStat.add(statement());
			}
			
			check(Token.RIGHTCURBRACKET, "'}' was expected");
		}
		
		return new IfStat(expr, leftStat, rightStat);
	}

	/**

	 */
	private void writeStat() {
		lexer.nextToken();
		check(Token.DOT, "a '.' was expected after 'Out'");
		//lexer.nextToken();
		check(Token.IDCOLON, "'print:' or 'println:' was expected after 'Out.'");
		String printName = lexer.getStringValue();
		expr();
	}

	private Expr expr() {
		Expr left, right;
		right = null;
		left = simpleExpr();
		Token op = null;
		if(lexer.token == Token.EQ || lexer.token == Token.LT || lexer.token == Token.GT || lexer.token == Token.LE || lexer.token == Token.GE || lexer.token == Token.NEQ) {
			op = lexer.token;
			lexer.nextToken();
			right = simpleExpr();
			return new CompositeExpr(op, left, right);
		}
		return left;
	}

	private Expr simpleExpr() {
		ArrayList<Expr> expr = new ArrayList<Expr>();
		ArrayList<Token> op = new ArrayList<Token>();
		boolean flag = false;
		Expr left = sumSubExpr();
		expr.add(left);
		while(lexer.token == Token.PLUSPLUS) {
			op.add(lexer.token);
			lexer.nextToken();
			expr.add(sumSubExpr());
			flag = true;
		}
		if(!flag) {
			return new SimpleExpr(left);
		}
		else {
			return new MultipleExpr(expr, op);
		}
	}
	
	private Expr sumSubExpr() {
		ArrayList<Expr> expr = new ArrayList<Expr>();
		ArrayList<Token> op = new ArrayList<Token>();
		boolean flag = false;
		Expr left = term();
		expr.add(left);
		while(lexer.token == Token.PLUS || lexer.token == Token.MINUS || lexer.token == Token.OR) {
			op.add(lexer.token);
			lexer.nextToken();
			expr.add(term());
			flag = true;
		}
		if(!flag) {
			return new SimpleExpr(left);
		}
		else {
			return new MultipleExpr(expr, op);
		}	
	}
	
	private Expr term() {
		ArrayList<Expr> expr = new ArrayList<Expr>();
		ArrayList<Token> op = new ArrayList<Token>();
		boolean flag = false;
		Expr left = signalFactor();
		expr.add(left);
		while(lexer.token == Token.MULT || lexer.token == Token.DIV || lexer.token == Token.AND) {
			op.add(lexer.token);
			lexer.nextToken();
			expr.add(signalFactor());
			flag = true;
		}
		if(!flag) {
			return new SimpleExpr(left);
		}
		else {
			return new MultipleExpr(expr, op);
		}
	}
	
	private Expr signalFactor() {
		Token op = null;
		boolean flag = false;
		if(lexer.token == Token.MINUS || lexer.token == Token.PLUS) {
			op = lexer.token;
			lexer.nextToken();
			flag = true;
		}
		Expr expr = factor();
		//Verificar se signal é so pra intType
		if(flag == true && expr.getType() == Type.intType) {
			return new SignalFactor(op, expr);
		}
		else if(flag == true && expr.getType() != Type.intType) {
			error("Signal only expected before int value");
		}
		return expr;
	}
	
	private Expr factor() {
		Expr expr, retorno;
		switch(lexer.token) {
			case LITERALINT:	
				retorno = new LiteralInt(lexer.getNumberValue());
				lexer.nextToken();
				return retorno;
			case TRUE:
				retorno = new LiteralBoolean(true);
				lexer.nextToken();
				return retorno;
			case FALSE:
				retorno = new LiteralBoolean(false);
				lexer.nextToken();
				return retorno;
			case LITERALSTRING:
				retorno = new LiteralString(lexer.getLiteralStringValue());
				lexer.nextToken();
				return retorno;
			case LEFTPAR:
				lexer.nextToken();
				expr = expr();
				check(Token.RIGHTPAR, "Right parenthesis expected after expression");
				return new ParenthesisExpr(expr);
			case NOT:
				lexer.nextToken();
				expr = factor();
				return new SignalFactor(Token.NOT, expr);
			case NULL:
				lexer.nextToken();
				return new NullExpr();
			case ID:
				String idName = lexer.getStringValue();
				if(idName.equals("In")) {
					lexer.nextToken();
					return readExpr();
				}
				lexer.nextToken();
				if(lexer.token != Token.DOT) {
					lexer.nextToken();
					return;
				}
				else if(lexer.token == Token.DOT){
					lexer.nextToken();
					if(lexer.getStringValue().equals("new") && lexer.token == Token.ID) {
						lexer.nextToken();
						return new ObjectCreation(idName);
					}
					else if(lexer.token == Token.ID || lexer.token == Token.IDCOLON){
						
					}
					else {
						error("new, id or id: expected after .");
					}
				}
			case SUPER:
				lexer.nextToken();
				return primaryExpr();
			case SELF:
				lexer.nextToken();
				return primaryExpr();
			default:
				error("Factor expected");
				return null;
		}
		
	}
	
	private void fieldDec(Qualifier qualifier) {
		String name;
		
		lexer.nextToken();
		Type typeVar = type();
		if(typeVar == Type.undefinedType) {
			error("Undefined type found");
		}
		
		if ( lexer.token != Token.ID ) {
			this.error("A field name was expected");
		}
		else {
			while ( lexer.token == Token.ID  ) {
				name = lexer.getStringValue();
				classdec.addField(new FieldDec(qualifier, name, typeVar));
				
				lexer.nextToken();
				if ( lexer.token == Token.COMMA ) {
					lexer.nextToken();
				}
				else {
					break;
				}
			}
		}
	}

	private Type type() {
		if ( lexer.token == Token.INT) {
			lexer.nextToken();
			return Type.intType;
		}
		else if( lexer.token == Token.BOOLEAN) {
			lexer.nextToken();
			return Type.booleanType;
		}
		else if( lexer.token == Token.STRING ) {
			lexer.nextToken();
			return Type.stringType;
		}
		else if ( lexer.token == Token.ID ) {
			lexer.nextToken();
			TypeId tipo = new TypeId(lexer.getStringValue());
			return tipo;
		}
		else {
			this.error("A type was expected");
			return Type.undefinedType;
		}
	}


	private Qualifier qualifier() {
		if ( lexer.token == Token.PRIVATE ) {
			lexer.nextToken();
			return new Qualifier("private");
		}
		else if ( lexer.token == Token.PUBLIC ) {
			lexer.nextToken();
			return new Qualifier("public");
		}
		else if ( lexer.token == Token.OVERRIDE ) {
			lexer.nextToken();
			if ( lexer.token == Token.PUBLIC ) {
				lexer.nextToken();
				return new Qualifier("override public");
			}
			return new Qualifier("override");
		}
		else if ( lexer.token == Token.FINAL ) {
			lexer.nextToken();
			if ( lexer.token == Token.PUBLIC ) {
				lexer.nextToken();
				return new Qualifier("final public");
			}
			else if ( lexer.token == Token.OVERRIDE ) {
				lexer.nextToken();
				if ( lexer.token == Token.PUBLIC ) {
					lexer.nextToken();
					return new Qualifier("final override public");
				}
				return new Qualifier("final override");
			}
			return new Qualifier("final");
		}
		return new Qualifier("");
	}
	/**
	 * change this method to 'private'.
	 * uncomment it
	 * implement the methods it calls
	 */
	public Statement assertStat() {

		lexer.nextToken();
		int lineNumber = lexer.getLineNumber();
		expr();
		if ( lexer.token != Token.COMMA ) {
			this.error("',' expected after the expression of the 'assert' statement");
		}
		lexer.nextToken();
		if ( lexer.token != Token.LITERALSTRING ) {
			this.error("A literal string expected after the ',' of the 'assert' statement");
		}
		String message = lexer.getLiteralStringValue();
		lexer.nextToken();
		if ( lexer.token == Token.SEMICOLON )
			lexer.nextToken();

		return null;
	}




	private LiteralInt literalInt() {

		LiteralInt e = null;

		// the number value is stored in lexer.getToken().value as an object of
		// Integer.
		// Method intValue returns that value as an value of type int.
		int value = lexer.getNumberValue();
		lexer.nextToken();
		return new LiteralInt(value);
	}

	private static boolean startExpr(Token token) {

		return token == Token.FALSE || token == Token.TRUE
				|| token == Token.NOT || token == Token.SELF
				|| token == Token.LITERALINT || token == Token.SUPER
				|| token == Token.LEFTPAR || token == Token.NULL
				|| token == Token.ID || token == Token.LITERALSTRING;

	}

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;
	private TypeCianetoClass classdec;

}
