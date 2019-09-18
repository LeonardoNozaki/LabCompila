
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
		
		TypeCianetoClass classdec;
		if(extend == true) {
			classdec = new TypeCianetoClass(className, superclassName);
		}
		else {
			classdec = new TypeCianetoClass(className);
		}
		
		memberList(classdec);
		
		if ( lexer.token != Token.END) error("'end' expected");
		
		lexer.nextToken();
		
		return classdec;
	}

	private void memberList(TypeCianetoClass classdec) {
		String quali;
		while ( true ) {
			quali = qualifier();
			if ( lexer.token == Token.VAR ) {
				fieldDec(classdec, quali);
			}
			else if ( lexer.token == Token.FUNC ) {
				methodDec(classdec, quali);
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
		if ( lexer.token != shouldBe ) {
			error(msg);
		}
	}

	private void methodDec(TypeCianetoClass classdec, String qualifier) {
		lexer.nextToken();
		Type tipoRetorno = Type.voidType;
		ArrayList<ParamDec> paramDec;
		ArrayList<Statement> statementList;
		
		if ( lexer.token == Token.ID ) {
			// unary method
			lexer.nextToken();
		}
		else if ( lexer.token == Token.IDCOLON ) {
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
		lexer.nextToken();
		statementList = statementList();
		if ( lexer.token != Token.RIGHTCURBRACKET ) {
			error("'}' expected");
		}
		lexer.nextToken();

		//aqui
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
		Statement stat;
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
			lexer.nextToken();
			checkSemiColon = false;
			break;
		case REPEAT:
			stat = repeatStat();
			break;
		case VAR:
			stat = localDec();
			break;
		case ASSERT:
			stat = assertStat();
			break;
		default:
			if ( lexer.token == Token.ID && lexer.getStringValue().equals("Out") ) {
				stat = writeStat();
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
		lexer.nextToken();
		type();
		check(Token.ID, "A variable name was expected");
		while ( lexer.token == Token.ID ) {
			lexer.nextToken();
			if ( lexer.token == Token.COMMA ) {
				lexer.nextToken();
			}
			else {
				break;
			}
		}
		if ( lexer.token == Token.ASSIGN ) {
			lexer.nextToken();
			// check if there is just one variable
			expr();
		}

	}

	private void repeatStat() {
		lexer.nextToken();
		while ( lexer.token != Token.UNTIL && lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END ) {
			statement();
		}
		check(Token.UNTIL, "missing keyword 'until'");
	}

	private void breakStat() {
		lexer.nextToken();

	}

	private void returnStat() {
		lexer.nextToken();
		expr();
	}

	private void whileStat() {
		lexer.nextToken();
		expr();
		check(Token.LEFTCURBRACKET, "missing '{' after the 'while' expression");
		lexer.nextToken();
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.EOF ) {
			statement();
		}
		check(Token.RIGHTCURBRACKET, "missing '}' after 'while' body");
	}

	private Statement ifStat() {
		lexer.nextToken();
		Expr expr;
		expr = expr();
		ArrayList<Statement> leftStat = new ArrayList<Statement>();
		ArrayList<Statement> rightStat = new ArrayList<Statement>();
		check(Token.LEFTCURBRACKET, "'{' expected after the 'if' expression");
		lexer.nextToken();
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.ELSE && lexer.token != Token.EOF) {
			leftStat.add(statement());
		}
		check(Token.RIGHTCURBRACKET, "'}' was expected");
		if ( lexer.token == Token.ELSE ) {
			lexer.nextToken();
			check(Token.LEFTCURBRACKET, "'{' expected after 'else'");
			lexer.nextToken();
			while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.EOF) {
				rightStat.add(statement());
			}
			check(Token.RIGHTCURBRACKET, "'}' was expected");
		}
		IfStat stat = new IfStat(expr, leftStat, rightStat);
		return stat;
	}

	/**

	 */
	private void writeStat() {
		lexer.nextToken();
		check(Token.DOT, "a '.' was expected after 'Out'");
		lexer.nextToken();
		check(Token.IDCOLON, "'print:' or 'println:' was expected after 'Out.'");
		String printName = lexer.getStringValue();
		expr();
	}

	private Expr expr() {
		SimpleExpr left, right;
		right = null;
		left = simpleExpr();
		Token op = null;
		if(lexer.token == Token.EQ || lexer.token == Token.LT || lexer.token == Token.GT || lexer.token == Token.LE || lexer.token == Token.GE || lexer.token == Token.NEQ) {
			op = lexer.token;
			lexer.nextToken();
			right = simpleExpr();
		}
		return new Expr(op, left, right);
	}

	private SimpleExpr simpleExpr() {
		ArrayList<SumSubExpr> expr = new ArrayList<SumSubExpr>;
		expr.add(sumSubExpr());
		while(lexer.token == Token.PLUSPLUS) {
			lexer.nextToken();
			expr.add(sumSubExpr());
		}
		return new SimpleExpr(expr);
	}
	
	private void fieldDec(TypeCianetoClass classdec, String qualifier) {
		lexer.nextToken();
		Type typeVar = type();
		if(typeVar == Type.undefinedType) {
			error("Undefined type found");
		}
		String name;
		if ( lexer.token != Token.ID ) {
			this.error("A field name was expected");
		}
		else {
			while ( lexer.token == Token.ID  ) {
				name = lexer.getStringValue();
				classdec.addField(qualifier, name, typeVar);
				
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


	private String qualifier() {
		if ( lexer.token == Token.PRIVATE ) {
			lexer.nextToken();
			return "private";
		}
		else if ( lexer.token == Token.PUBLIC ) {
			lexer.nextToken();
			return "public";
		}
		else if ( lexer.token == Token.OVERRIDE ) {
			lexer.nextToken();
			if ( lexer.token == Token.PUBLIC ) {
				lexer.nextToken();
				return "override public";
			}
			return "override";
		}
		else if ( lexer.token == Token.FINAL ) {
			lexer.nextToken();
			if ( lexer.token == Token.PUBLIC ) {
				lexer.nextToken();
				return "final public";
			}
			else if ( lexer.token == Token.OVERRIDE ) {
				lexer.nextToken();
				if ( lexer.token == Token.PUBLIC ) {
					lexer.nextToken();
					return "final override public";
				}
				return "final override";
			}
			return "final";
		}
		return "";
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

}
