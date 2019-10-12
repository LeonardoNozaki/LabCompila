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
		isRepetitionState = false;
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
		boolean open = false;
		String superclassName = "";
		String className = "";
		if ( lexer.token == Token.ID && lexer.getStringValue().equals("open") ) {
			//Verificar se precisa de mais algo para o open
			lexer.nextToken();
			open = true;
		}
		if ( lexer.token != Token.CLASS ) 
			error("'class' expected");
		else
			lexer.nextToken();
		
		if ( lexer.token != Token.ID ) 
			error("Identifier expected");
		else {
			className = lexer.getStringValue();
			lexer.nextToken();
		}
		classdec = new TypeCianetoClass(className);
		classdec.setOpen(open);
		if(!className.isEmpty()) {
			if(symbolTable.getInClass(className) == null) {
				symbolTable.putInClass(className, classdec);
			}
			else {
				error(className + " has already been declared");
			}
		}
		
		if ( lexer.token == Token.EXTENDS ) {
			lexer.nextToken();
			if ( lexer.token != Token.ID ) error("Identifier expected");
			
			superclassName = lexer.getStringValue();
			TypeCianetoClass superclass = (TypeCianetoClass) symbolTable.getInClass(superclassName);
			if(superclass == null){
				error(superclassName + " does not existe");
			}
			else if(superclass.getOpen()){
				classdec.setSuperClass(superclass);
			}
			else if(className.equals(superclassName)) {
				error("Class '" + className + "' is inheriting from itself");
			}
			else {
				error(superclassName + " cannot be extended");
			}
			lexer.nextToken();
		}

		/*memberList();
		if ( lexer.token != Token.END) error("'end' expected");
		
		lexer.nextToken();
		*/
		
		memberList();
		
		if ( lexer.token != Token.END) error("Class member or 'end' expected");
		
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
				methodDec = methodDec(quali);
				if(quali.isPrivate()) {
					if(!classdec.addMethodPrivate(methodDec)) {
						error(methodDec.getName() + " already been declared");
					}
				}
				
				else {
					if(!classdec.addMethodPublic(methodDec)) {
						error(methodDec.getName() + " already been declared");
					}
				}
			}
			else {
				break;
			}
			
		}
		
	}

	private void error(String msg) {
		this.signalError.showError(msg);
	}
	
	private void errorBefore(String msg) {
		this.signalError.showErrorBefore(msg);
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
		returnFlag = false;
		boolean returnNeed = false;
		Type tipoRetorno = Type.voidType;
		ArrayList<ParamDec> paramDec = null;
		ArrayList<Statement> statementList = null;
		String id = "";
		symbolTable.add();
		if ( lexer.token == Token.ID ) {
			// unary method
			id = lexer.getStringValue();
			lexer.nextToken();
		}
		else if ( lexer.token == Token.IDCOLON ) {
			id = lexer.getStringValue();
			paramDec = formalParamDec();
			for(int i = 0; i < paramDec.size(); i++) {
				symbolTable.putInLocal(paramDec.get(i).getName(), paramDec.get(i));
			}
		}
		else {
			error("An identifier or identifier: was expected after 'func'");
		}
		if(qualifier.hasOverride()) {
			if(classdec.getMethodPublic(id) == null) {
				error("There is no method with the same name as " + id + " to override");
			}
		}
		if ( lexer.token == Token.MINUS_GT ) {
			// method declared a return type
			lexer.nextToken();
			returnNeed = true;
			tipoRetorno = type();
		}
		if(qualifier.hasOverride()) {
			if(classdec.getSuper() != null) {
					MethodDec methodDec = new MethodDec(qualifier, id, paramDec, tipoRetorno, statementList);
					if(!classdec.getSuper().sameSignature(methodDec)) {
						error("Method '" + methodDec.getName() + "' of the subclass '" + classdec.getName() + 
								"' has a signature different from the same method of superclass '" + classdec.getSuper().getName() + "'");
					}
				
			}
		}
		if ( lexer.token != Token.LEFTCURBRACKET ) {
			error("'{' expected");
		}
		else {
			lexer.nextToken();
		}

		statementList = statementList();
		
		if(returnNeed) {
			if(!returnFlag) {
				error("missing 'return' statement");
			}
		}
		
		if(returnFlag) {
			if(!returnNeed) {
				error("'return' statement is not needed");
			}
		}
		
		if ( lexer.token != Token.RIGHTCURBRACKET ) {
			error("'}' expected");
		}
		else {
			lexer.nextToken();
		}
		symbolTable.removeLocalIdent();
		
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
				lexer.nextToken();
			}
			else {
				error("An ID was expected");
			}
			
			if ( lexer.token != Token.COMMA ) {
				return p;
			}
			else {
				lexer.nextToken();
			}
		}
	}
	
	private ArrayList<Statement> statementList() {
		ArrayList<Statement> statementList = new ArrayList<Statement>();
		  // only(?) '}' is necessary in this test
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.EOF) {
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
				stat = assignExpr();
			}
			break;
		}
		if ( checkSemiColon ) {
			if(lexer.token != Token.SEMICOLON) {
				errorBefore("';' expected before '"+ lexer.token + "'");
			}
			else {
				lexer.nextToken();
			}
		}
		
		return stat;
	}
	
	private AssignExpr assignExpr() {
		Expr left = null, right = null;
		left = expr();
		if(lexer.token == Token.ASSIGN) {
			if(left.isOnlyId() == false) {
				error("Variable expected at the left-hand side of a assignment");
			}
			
			lexer.nextToken();
			right = expr();
			Type l = left.getType();
			Type r = right.getType();
			TypeCianetoClass tcc = (TypeCianetoClass) symbolTable.getInClass(l.getName());
			if(tcc != null && right != null) {
				if(right.isObjectCreation()) {
					
				}
			}
			if(l == Type.booleanType){
				if(r != Type.booleanType) {
					error("Type error: value of the right-hand side is not type of the variable of the left-hand side.");
				}
			}
			else if(l == Type.intType) {
				if(r != Type.intType) {
					error("Type error: value of the right-hand side is not type of the variable of the left-hand side.");
				}
			}
			else if(l == Type.stringType) {
				if(r != Type.stringType) {
					error("Type error: value of the right-hand side is not type of the variable of the left-hand side.");
				}
			}			
			else if(l == Type.undefinedType || r == Type.undefinedType || r == Type.nullType){
				//Tipo indefinido, nao se sabe o tipo correto
			}
			else if(l == Type.nullType) {
				error("left-hand side is null");
			}
			else if(l == Type.voidType || r == Type.voidType) {
				error("Assignment without type");
			}
			//tratar type cianeto clas
			return new CompositeAssign(left, right);
		}
		return new SimpleAssign(left);
	}

	private LocalDec localDec() {
		lexer.nextToken();
		Type type = type();
		boolean flag = false;
		String id = "";
		LocalVar aux;
		//array nao seria de LocalVar?
		ArrayList<Variable> local = null;
		
		if(lexer.token == Token.ID) {
			id = lexer.getStringValue();
			local = new ArrayList<Variable>();
			if(symbolTable.getInLocal(id) != null) {
				error("Variable '" + id + "' already been declared");
			}
			else {
				aux = new LocalVar(id, type);
				symbolTable.putInLocal(id, aux);
				local.add(aux);
			}
			lexer.nextToken();
			if ( lexer.token == Token.COMMA ) {
				flag = true;
				lexer.nextToken();
				if(lexer.token != Token.ID) {
					error("Missing identifier");
				}
				while ( lexer.token == Token.ID ) {
					id = lexer.getStringValue();
					if(symbolTable.getInLocal(id) != null) {
						error("Variable '" + id + "' already been declared");
					}
					else {
						aux = new LocalVar(id, type);
						symbolTable.putInLocal(id, aux);
						local.add(aux);
					}
					lexer.nextToken();
					if ( lexer.token == Token.COMMA ) {
						lexer.nextToken();
						if(lexer.token != Token.ID) {
							error("Missing identifier");
						}
					}
					else {
						break;
					}
				}
			}
		}
		else {
			error("Identifier expected");
		}

		if ( lexer.token == Token.ASSIGN ) {
			lexer.nextToken();
			// check if there is just one variable
			if(flag) {
				error("there are two or more variable in assignment");
			}
			Expr expr = expr();
			Variable v = new LocalDecExpr(type, id, expr);
			return new LocalDec(v);
		}
		else {
			Variable v = new LocalDecList(type, local);
			return new LocalDec(v);
		}
	}

	private Statement semicolonStat() {
		lexer.nextToken();
		return new SemicolonStat();
	}
	private Statement repeatStat() {
		lexer.nextToken();
		ArrayList<Statement> stat = new ArrayList<Statement>();
		symbolTable.add();
		
		isRepetitionState = true;
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.UNTIL && lexer.token != Token.END && lexer.token != Token.EOF) {
			stat.add(statement());
		}
		isRepetitionState = false;
		
		check(Token.UNTIL, "missing keyword 'until'");
		symbolTable.sub();
		Expr expr = expr();
		if(expr.getType() != Type.booleanType) {
			error("'repeat' expression expected Boolean Type");
		}
		return new RepeatStat(expr, stat);
	}

	private Statement breakStat() {
		lexer.nextToken();
		if(isRepetitionState == false) {
			error("'break' statement found outside a 'while' or 'repeat-until' statement");
		}
		return new BreakStat();
	}

	private Statement returnStat() {
		lexer.nextToken();
		//verificar se tipo de retorno bate com o tipo do metodo
		Expr expr = expr();
		returnFlag = true;
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
		symbolTable.add();
		
		isRepetitionState = true;
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.EOF ) {
			stat.add(statement());
		}
		isRepetitionState = false;
		
		check(Token.RIGHTCURBRACKET, "missing '}' after 'while' body");
		symbolTable.sub();
		
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
		symbolTable.add();
		
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.ELSE && lexer.token != Token.EOF) {
			leftStat.add(statement());
		}
		
		check(Token.RIGHTCURBRACKET, "'}' was expected");
		symbolTable.sub();
		
		if ( lexer.token == Token.ELSE ) {
			lexer.nextToken();
			check(Token.LEFTCURBRACKET, "'{' expected after 'else'");
			symbolTable.add();
			
			while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.EOF) {
				rightStat.add(statement());
			}
			
			check(Token.RIGHTCURBRACKET, "'}' was expected");
			symbolTable.sub();
		}
		
		return new IfStat(expr, leftStat, rightStat);
	}

	/**

	 */
	private Statement writeStat() {
		lexer.nextToken();
		check(Token.DOT, "a '.' was expected after 'Out'");
		ArrayList<Expr> expr = new ArrayList<Expr>();
		Expr aux;
		
		if(lexer.token == Token.IDCOLON && lexer.getStringValue().equals("print:")){
			do{
				lexer.nextToken();
				aux = expr();
				expr.add(aux);
				if(aux.getType() == Type.booleanType) {
					error("Attempt to print a boolean expression");
				}
			} while(lexer.token == Token.COMMA);
			return new Print(expr);
		}	
		else if(lexer.token == Token.IDCOLON && lexer.getStringValue().equals("println:")) {
			do{
				lexer.nextToken();
				aux = expr();
				expr.add(aux);
				if(aux.getType() == Type.booleanType) {
					error("Attempt to print a boolean expression");
				}
			} while(lexer.token == Token.COMMA);
			return new Println(expr);
		}
		else {
			error("'print:' or 'println:' was expected after 'Out.'");
			return new Print(expr);
		}
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
			//fazer semantico
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
			return left;
		}
		else {
			return new MultipleExpr(expr, op);
		}
	}
	
	private Expr sumSubExpr() {
		ArrayList<Expr> expr = new ArrayList<Expr>();
		ArrayList<Token> op = new ArrayList<Token>();
		
		boolean flag = false;
		Token opAtual;
		Expr left = term();
		Expr right;
		expr.add(left);
		Type l = left.getType();
		Type r = Type.undefinedType;
		
		while(lexer.token == Token.PLUS || lexer.token == Token.MINUS || lexer.token == Token.OR) {
			opAtual = lexer.token;
			op.add(lexer.token);
			
			lexer.nextToken();
			
			right = term();
			expr.add(right);
			flag = true;
			
			r = right.getType();
			
			if(opAtual == Token.PLUS || opAtual == Token.MINUS) {
				if((l == Type.intType || l == Type.undefinedType) && (r == Type.intType || r == Type.undefinedType)) {
					if(l == Type.undefinedType || r == Type.undefinedType) {
						l = Type.undefinedType;
					}
					else {
						l = Type.intType;
					}
				}
				else {
					error("Operator '" + opAtual + "' expected int type");
					l = Type.undefinedType;
				}
			}
			else {
				if((l == Type.booleanType || l == Type.undefinedType) && (r == Type.booleanType || r == Type.undefinedType)) {
					if(l == Type.undefinedType || r == Type.undefinedType) {
						l = Type.undefinedType;
					}
					else {
						l = Type.booleanType;
					}
				}
				else {
					error("Operator '" + opAtual + "' expected boolean type");
					l = Type.undefinedType;
				}
			}
		}
		if(!flag) {
			return left;
		}
		else {
			return new MultipleExpr(expr, op);
		}	
	}
	
	private Expr term() {
		ArrayList<Expr> expr = new ArrayList<Expr>();
		ArrayList<Token> op = new ArrayList<Token>();
		
		boolean flag = false;
		Token opAtual;
		Expr left = signalFactor();
		Expr right;
		expr.add(left);
		Type l = left.getType();
		Type r = Type.undefinedType;
		
		while(lexer.token == Token.MULT || lexer.token == Token.DIV || lexer.token == Token.AND) {
			opAtual = lexer.token;
			op.add(lexer.token);
			lexer.nextToken();
			
			right = signalFactor();
			expr.add(right);
			flag = true;
			
			r = right.getType();
			if(opAtual == Token.MULT || opAtual == Token.DIV) {
				if((l == Type.intType || l == Type.undefinedType) && (r == Type.intType || r == Type.undefinedType)) {
					if(l == Type.undefinedType || r == Type.undefinedType) {
						l = Type.undefinedType;
					}
					else {
						l = Type.intType;
					}
				}
				else {
					error("Operator '" + opAtual + "' expected int type");
					l = Type.undefinedType;
				}
			}
			else {
				if((l == Type.booleanType || l == Type.undefinedType) && (r == Type.booleanType || r == Type.undefinedType)) {
					if(l == Type.undefinedType || r == Type.undefinedType) {
						l = Type.undefinedType;
					}
					else {
						l = Type.booleanType;
					}
				}
				else {
					error("Operator '" + opAtual + "' expected boolean type");
					l = Type.undefinedType;
				}
			}
		}
		if(!flag) {
			return left;
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
		
		if(flag == true && expr.getType() == Type.intType) {
			return new SignalFactor(op, expr);
		}
		else if(flag == true && expr.getType() != Type.intType) {
			error("Signal '" + op + "' only expected before int value");
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
				check(Token.RIGHTPAR, "')' expected");
				return new ParenthesisExpr(expr);
			case NOT:
				lexer.nextToken();
				expr = factor();
				if(expr.getType() != Type.booleanType) {
					error("Operator '!' does not accepts '" + expr.getType().getName() + "' values");
				}
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
					Variable v = (Variable) symbolTable.getInLocal(idName);
					if(v == null) {
						error("Variable '" + idName + "' was not declared");
						return new LocalVar(idName);
					}
					return new LocalVar(idName, v.getType());
				}
				else if(lexer.token == Token.DOT){
					lexer.nextToken();
					if(lexer.getStringValue().equals("new:") && lexer.token == Token.IDCOLON) {
						error("'new' does not take any parameter");
						return new ObjectCreation(Type.undefinedType);
					}
					else if(lexer.getStringValue().equals("new") && lexer.token == Token.ID) {
						lexer.nextToken();
						Type type = (Type)symbolTable.getInClass(idName);
						if(type != null) {
							return new ObjectCreation(type);
						}
						else {
							error("Cannot initialize undeclared class" + idName);
							return new ObjectCreation(Type.undefinedType);
						}
					}
					else if(lexer.token == Token.ID) {
						String idMethod = lexer.getStringValue();
						lexer.nextToken();
						//verificar se o primeiro id é uma variable do tipo type cianeto class
						//chamada de metodo, é feito pelo RTS
						//return new MethodCall(Token.SUPER, idName, idMethod);
						Variable var = (Variable)symbolTable.getInLocal(idName);
						if(var == null) {
							error(idName + " was not declared");
						}
						else {
							Type type = var.getType();
							if(type instanceof TypeCianetoClass) {
								TypeCianetoClass typecianeto = (TypeCianetoClass)type;
								MethodDec md = typecianeto.getMethodPublic(idMethod);
								if(md != null) {
									if(md.getType() == Type.voidType) {
										return new MethodCall(var, idMethod, false);
									}
									return new MethodCall(var, idMethod, true);
								}
								else {
									error(idMethod + " was not declared in class of object " + idName);
								}
							}
							else {
								error("Variable '" + idName + "' is not an object");
							}
						}
					}
					else if(lexer.token == Token.IDCOLON){
						String idMethod = lexer.getStringValue();
						lexer.nextToken();
						ArrayList<Expr> exprs = new ArrayList<Expr>();
						exprs.add(expr());
						while(lexer.token == Token.COMMA) {
							lexer.nextToken();
							exprs.add(expr());
						}
						//verificar se o primeiro id é uma variable do tipo type cianeto class
						//chamada de metodo com parametro, é feito pelo RTS
						//return new MethodCallPar(Token.SUPER, idName, idMethod, exprs);
						Variable var = (Variable)symbolTable.getInLocal(idName);
						if(var == null) {
							error(idName + " was not declared");
						}
						else {
							Type type = var.getType();
							if(type instanceof TypeCianetoClass) {
								TypeCianetoClass typecianeto = (TypeCianetoClass)type;
								MethodDec md = typecianeto.getMethodPublic(idMethod);
								if(md != null) {
									if(md.comparePar(exprs) == false) {
										error(idMethod + " has different parameters");
									}
									if(md.getType() == Type.voidType) {
										return new MethodCallPar(var, idMethod, exprs, false);
									}
									return new MethodCallPar(var, idMethod, exprs, true);
								}
								else {
									error(idMethod + " was not declared in class of object " + idName);
								}
							}
							else {
								error(idName + " is not an object");
							}
						}
					}
					else {
						error("new, id or id: expected after .");
						return null;
					}
				}
				return null;
			case SUPER:
				lexer.nextToken();
				if(lexer.token == Token.DOT) {
					lexer.nextToken();
					if(lexer.token == Token.ID) {
						String idMethod = lexer.getStringValue();
						lexer.nextToken();
						//verificar se o primeiro id é uma variable do tipo type cianeto class
						//chamada de metodo, é feito pelo RTS
						//return new TokenMethodCall(Token.SUPER, idMethod);
						TypeCianetoClass superclass = classdec.getSuper();
						MethodDec md = null;
						if(superclass != null) {
							md = superclass.getMethodPublic(idMethod);
							if(md == null) {
								error("superclass " + superclass.getName() + " does not have the public method " + idMethod);
							}
						}
						else {
							error(classdec.getName() + " does not extend from any class");
						}
						return new TokenMethodCall(Token.SUPER, md);
					}
					else if(lexer.token == Token.IDCOLON){
						String idMethod = lexer.getStringValue();
						lexer.nextToken();
						ArrayList<Expr> exprs = new ArrayList<Expr>();
						exprs.add(expr());
						while(lexer.token == Token.COMMA) {
							lexer.nextToken();
							exprs.add(expr());
						}
						//verificar se o primeiro id é uma variable do tipo type cianeto class
						//chamada de metodo com parametro, é feito pelo RTS
						//return new TokenMethodCallPar(Token.SUPER, idMethod, exprs);
						TypeCianetoClass superclass = classdec.getSuper();
						MethodDec md = null;
						if(superclass != null) {
							md = superclass.getMethodPublic(idMethod);
							if(md == null) {
								error("superclass " + superclass.getName() + " does not have the public method " + idMethod);
							}
							else {
								if(md.comparePar(exprs) == false) {
									error(idMethod + " has different parameters");
								}
							}
						}
						else {
							error(classdec.getName() + " does not extend from any class");
						}
						return new TokenMethodCallPar(Token.SUPER, md, exprs);
					}
					else {
						error("id or id: expected after .");
						return null;
					}
				}
				else {
					error("'.' expected after super");
					return null;
				}
			case SELF:
				lexer.nextToken();
				if(lexer.token == Token.DOT) {
					lexer.nextToken();
					if(lexer.token == Token.IDCOLON) {
						String idMethod = lexer.getStringValue();
						lexer.nextToken();
						ArrayList<Expr> exprs = new ArrayList<Expr>();
						exprs.add(expr());
						while(lexer.token == Token.COMMA) {
							lexer.nextToken();
							exprs.add(expr());
						}
						
						MethodDec md = classdec.getMethod(idMethod);
						if(md != null) {
							if(md.comparePar(exprs) == false) {
								error(idMethod + " has different parameters");
							}
						}
						else {
							error(idMethod + "was not declared");
						}
						return new TokenMethodCallPar(Token.SELF, md, exprs);
					}
					else if(lexer.token == Token.ID) {
						String id = lexer.getStringValue();
						lexer.nextToken();
						if(lexer.token == Token.DOT) {
							lexer.nextToken();
							if(lexer.token == Token.IDCOLON) {
								String idMethod = lexer.getStringValue();
								lexer.nextToken();
								ArrayList<Expr> exprs = new ArrayList<Expr>();
								exprs.add(expr());
								while(lexer.token == Token.COMMA) {
									lexer.nextToken();
									exprs.add(expr());
								}
								
								FieldDec fd = classdec.getField(id);
								MethodDec md = null;
								if(fd != null) {
									TypeCianetoClass classFD = (TypeCianetoClass) symbolTable.getInClass(fd.getTypeName());
									if(classFD != null) {
										md = classFD.getMethodPublic(idMethod);
										if(md != null) {
											if(md.comparePar(exprs) == false) {
												error(idMethod + " has different parameters");
											}
										}
										else {
											error(idMethod + " has not declared in " + id);
										}
									}
									else {
										error("type of " + id + "does not exist");
									}
								}
								else {
									error(id + "field was not declared");
								}
								return new SelfMethodCallPar(fd, md, exprs);
							}
							else if(lexer.token == Token.ID) {
								String idMethod = lexer.getStringValue();
								lexer.nextToken();
								
								FieldDec fd = classdec.getField(id);
								MethodDec md = null;
								if(fd != null) {
									TypeCianetoClass classFD = (TypeCianetoClass) symbolTable.getInClass(fd.getTypeName());
									if(classFD != null) {
										md = classFD.getMethodPublic(idMethod);
										if(md == null) {
											error(idMethod + " has not declared in " + id);
										}
									}
								}
								else {
									error(id + "field was not declared");
								}
								return new SelfMethodCall(fd, md);
							}
							else {
								error("id or id: expected after .");
								FieldDec fd = classdec.getField(id);
								return new SelfField(fd);
							}
						}
						else {
							MethodDec md = classdec.getMethod(id);
							if(md != null) {
								return new TokenMethodCall(Token.SELF, md);
							}
							else {
								FieldDec fd = classdec.getField(id);
								if(fd != null) {
									return new SelfField(fd);
								}
								else {
									error("Field or Method " + id + "has not been declared");
									return new SelfField(fd);
								}
							}
						}
					}
					else {
						error("id or id: expected after .");
						return new SelfExpr(classdec);
					}
				}
				else {
					return new SelfExpr(classdec);
				}
			default:
				error("Expression expected");
				return null;
		}
	}
	
	private Expr readExpr() {
		check(Token.DOT, "'.' expected after 'In'");
		if(lexer.token == Token.ID && lexer.getStringValue().contentEquals("readInt")) {
			lexer.nextToken();
			return new ReadExpr("readInt");
		}
		else if(lexer.token == Token.ID && lexer.getStringValue().contentEquals("readString")) {
			lexer.nextToken();
			return new ReadExpr("readString");
		}
		else {
			error("Method " + lexer.getStringValue() + " does not belong to the command 'In.");
			return new ReadExpr("");
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
				if(!classdec.addField(new FieldDec(qualifier, name, typeVar))) {
					error("Variable '" + name + "' has been already declared");
				}
				if(!qualifier.isPrivate() && !qualifier.isVoid()) {
					error("Attempt to declare public instance variable '" + name + "'");
				}
				lexer.nextToken();
				if ( lexer.token == Token.COMMA ) {
					lexer.nextToken();
				}
				else {
					break;
				}
			}
			if(lexer.token == Token.SEMICOLON) {
				lexer.nextToken();
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
			//mudar para retornar a classe q ja existe 
			//lexer.getStringValue();
			Type type;
			type = (Type)symbolTable.getInClass(lexer.getStringValue());
			if(type != null) {
				lexer.nextToken();
				return type;
			}
			else {
				error("type '" + lexer.getStringValue() + "' is not a valid type");
				lexer.nextToken();
				return Type.undefinedType;
			}
			
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
		String message = "";
		Expr expr = expr();
		if(expr.getType() != Type.booleanType) {
			error("'Assert' expression expected Boolean Type");
		}
		if ( lexer.token != Token.COMMA ) {
			this.error("',' expected after the expression of the 'assert' statement");
		}
		else {
			lexer.nextToken();
		}	
		if ( lexer.token != Token.LITERALSTRING ) {
			this.error("A literal string expected after the ',' of the 'assert' statement");
		}
		else {
			message = lexer.getLiteralStringValue();
			lexer.nextToken();
		}

		return new AssertStat(expr, message);
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
	private boolean returnFlag;
	private boolean isRepetitionState;

}
