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
		isRepetitionState = 0;
		return program;
	}

	private Program program(ArrayList<CompilationError> compilationErrorList) {
		ArrayList<MetaobjectAnnotation> metaobjectCallList = new ArrayList<>();
		ArrayList<TypeCianetoClass> CianetoClassList = new ArrayList<>();
		Program program = new Program(CianetoClassList, metaobjectCallList, compilationErrorList);
		TypeCianetoClass classAtual = null;		
		boolean flagProgram = false;
		boolean thereWasAnError = false;
	
		do {
			try {
				while ( lexer.token == Token.ANNOT ) {
					metaobjectAnnotation(metaobjectCallList);
				}
				classAtual = classDec();		
				CianetoClassList.add(classAtual);		
				if(classAtual.getName().equals("Program")) {		
					flagProgram = true;		
					if(classAtual.getMethod("run") == null) {		
						errorBefore("Class 'Program' must have a method called 'run'");		
					}		
				}
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
		if(flagProgram == false) {		
			try{
				errorBefore("Class 'Program' expected");		
			}
			catch( CompilerError e) {
				thereWasAnError = true;
			}
		}
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
			int sizeParamList = metaobjectParamList.size();
			if ( sizeParamList < 2 || sizeParamList > 4 )
				error("Annotation 'cep' takes two, three, or four parameters");

			if ( !( metaobjectParamList.get(0) instanceof Integer) ) {
				error("The first parameter of annotation 'cep' should be an integer number");
			}
			else {
				int ln = (Integer ) metaobjectParamList.get(0);
				metaobjectParamList.set(0, ln + lineNumber);
			}
			if ( !( metaobjectParamList.get(1) instanceof String) )
				error("The second parameter of annotation 'cep' should be a literal string");
			if ( sizeParamList >= 3 && !( metaobjectParamList.get(2) instanceof String) )
				error("The third parameter of annotation 'cep' should be a literal string");
			if ( sizeParamList >= 4 && !( metaobjectParamList.get(3) instanceof String) )
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
		symbolTable.removeLocalIdent();
		
		boolean open = false;
		String superclassName = "";
		String className = "";
		if ( lexer.token == Token.ID && lexer.getStringValue().equals("open") ) {
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
				error("Class '" + className + "' has already been declared");
			}
		}
		
		if ( lexer.token == Token.EXTENDS ) {
			lexer.nextToken();
			if ( lexer.token != Token.ID ) error("Name of superclass expected");
			
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
				error("Class '" + superclassName + "' cannot be extended");
			}
			lexer.nextToken();
		}
		
		memberList();
		
		if ( lexer.token != Token.END) error("Class member or 'end' expected");
		
		lexer.nextToken();
		symbolTable.removeLocalIdent();
		return classdec;
	}

	private void memberList() {
		Qualifier quali;
		
		while ( true ) {
			quali = qualifier();
			if ( lexer.token == Token.VAR ) {
				fieldDec(quali);
			}
			else if ( lexer.token == Token.FUNC ) {
				methodDec(quali);
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
		if ( lexer.token != shouldBe ) {
			error(msg);
		}
		else {
			lexer.nextToken();
		}
	}

	private void methodDec(Qualifier qualifier) {
		lexer.nextToken();
		returnFlag = false;
		returnNeed = false;
		returnType = Type.voidType;
		ArrayList<ParamDec> paramDec = null;
		ArrayList<Statement> statementList = null;
		String id = "";
		MethodDec methodDec = null;
		boolean flagRun = false;
		
		symbolTable.add();
		if ( lexer.token == Token.ID ) {
			id = lexer.getStringValue();
			lexer.nextToken();
		}
		else if ( lexer.token == Token.IDCOLON ) {
			id = lexer.getStringValue();
			paramDec = formalParamDec();
		}
		else {
			error("An identifier or identifier: was expected after 'func'");
		}
		
		if(symbolTable.getInClass(id) != null) {
			error("Method '" + id + "' has same name of an Class");
		}
		if(classdec.getName().equals("Program") && id.equals("run:")) {		
			error("Method 'run:' of class 'Program' cannot take parameters");		
		}
		if(classdec.getName().equals("Program") && id.equals("run")) {		
			flagRun = true;
			if(qualifier.isPrivate()) {
				error("Method 'run' of class 'Program' cannot be private");
			}
		}
		
		methodDec = new MethodDec(qualifier, id);
		methodDec.setParamDec(paramDec);
		methodDec.setReturn(Type.voidType);
		
		if(qualifier.isPrivate()) {
			if(!classdec.addMethodPrivate(methodDec)) {
				error("Method '" + methodDec.getName() + "' already been declared");
			}
		}
		else {
			if(!classdec.addMethodPublic(methodDec)) {
				error("Method '" + methodDec.getName() + "' already been declared");
			}
		}
		
		if ( lexer.token == Token.MINUS_GT ) {
			if(flagRun == true) {
				error("Method 'run' of class 'Program' with a return value type");
			}
			lexer.nextToken();
			returnNeed = true;
			returnType = type();
			methodDec.setReturn(returnType);
		}
		
		if(qualifier.hasOverride()) {
			if(classdec.getSuper() != null) {
				if(!classdec.getSuper().sameSignature(methodDec)) {
					error("Method '" + methodDec.getName() + "' of the subclass '" + classdec.getName() + 
							"' cannot redefine the same method of superclass '" + classdec.getSuper().getName() + "'");
				}
			}
			else {
				error("method does not override or implement a method from a supertype");
			}
		}
		else if(classdec.getSuper() != null) {
			if(classdec.getSuper().getMethodPublic(id) != null) {
				if(classdec.getSuper().sameSignature(methodDec)) {
					error("'override' expected before overridden method '" + id + "'");
				}
				else {
					error("Method '" + id + "' is being redefined in subclass '" +  classdec.getName() + "' with a signature different from the method of superclass '" + classdec.getSuper().getNameSuper(id) + "'");
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
		symbolTable.sub();
		methodDec.setStatement(statementList);
		
		if(returnNeed) {
			if(!returnFlag) {
				error("missing 'return' statement");
			}
		}	
		
		if ( lexer.token != Token.RIGHTCURBRACKET ) {
			error("'}' expected");
		}
		else {
			lexer.nextToken();
		}	
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
				Variable v = (Variable) symbolTable.getInLocal(id);
				if(v != null) {
					error("Parameter '" + id + "' already been declared");
				}
				else{
					symbolTable.putInLocal(id, param);
				}
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
			
			if(left instanceof SelfExpr) {
				error("Cannot change the value of self");
			}
			
			if(l == Type.booleanType){
				if(r != Type.booleanType) {
					error("Type error: type of the right-hand side is not type of the variable of the left-hand side.");
				}
			}
			else if(l == Type.intType) {
				if(r != Type.intType) {
					error("Type error: type of the right-hand side is not type of the variable of the left-hand side.");
				}
			}
			else if(l == Type.stringType) {
				if(r != Type.nullType && r != Type.stringType) {
					error("Type error: type of the right-hand side is not type of the variable of the left-hand side.");
				}
			}			
			else if(l == Type.nullType) {
				error("left-hand side is null");
			}
			else if(l == Type.voidType || r == Type.voidType) {
				error("Assignment without type");
			}
			else if(l instanceof TypeCianetoClass) {
				if(right instanceof ObjectCreation) {
					TypeCianetoClass classtype = (TypeCianetoClass) r;
					if(!classtype.searchType(l.getName())) {
						error("Type error: type of the right-hand side of the assignment is not an instance of the left-hand side");
					}
				}
				else if(r instanceof TypeCianetoClass) {
					TypeCianetoClass classtype = (TypeCianetoClass) r;
					if(!classtype.searchType(l.getName())) {
						error("Type error: type of the right-hand side of the assignment is not a subclass of the left-hand side");
					}
				}
				else if(r != Type.nullType && r != Type.undefinedType){
					error("Type error: type of the right-hand side is not type of the variable of the left-hand side.");
				}
			}
			return new CompositeAssign(left, right);
		}
		else {
			if(left instanceof MethodCall) {
				MethodCall mc = (MethodCall) left;
				if(mc.getType() != Type.voidType) {
					error("Message send '" + mc.getName() + "' returns a value that is not used");
				}
			}
			return new SimpleAssign(left);
		}
	}

	private LocalDec localDec() {
		lexer.nextToken();
		Type type = type();
		boolean flag = false;
		String id = "";
		LocalVar aux;
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
		
		isRepetitionState = isRepetitionState + 1;
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.UNTIL && lexer.token != Token.END && lexer.token != Token.EOF) {
			stat.add(statement());
		}
		isRepetitionState = isRepetitionState - 1;
		
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
		if(isRepetitionState == 0) {
			error("'break' statement found outside a 'while' or 'repeat-until' statement");
		}
		return new BreakStat();
	}

	private Statement returnStat() {
		lexer.nextToken();
		Expr expr = expr();
		returnFlag = true;
		if(!returnNeed) {
			error("Illegal 'return' statement. Method returns 'void'");
		}
		else {
			if(returnType.getClass() == expr.getType().getClass()) {
				if(returnType instanceof TypeCianetoClass) {
					TypeCianetoClass classtype = (TypeCianetoClass) expr.getType();
					if(!classtype.searchType(returnType.getName())) {
						error("Type error: type of the expression returned is not subclass of the method return type");
					}
				}
			}
			else if(returnType == Type.stringType && expr.getType() == Type.nullType) {
				return new ReturnStat(expr);
			}
			else if(returnType instanceof TypeCianetoClass && expr.getType() == Type.nullType) {
				return new ReturnStat(expr);
			}
			else {
				error("Type error: type of the expression returned is not type of the method return type");
			}
		}
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
		
		isRepetitionState = isRepetitionState + 1;
		while ( lexer.token != Token.RIGHTCURBRACKET && lexer.token != Token.END && lexer.token != Token.EOF ) {
			stat.add(statement());
		}
		isRepetitionState = isRepetitionState - 1;
		
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
				else if(aux.getType() instanceof TypeCianetoClass) {		
					error("Command 'print' does not accept objects");		
				}		
				else if(aux.getType() != Type.intType && aux.getType() != Type.stringType) {		
					error("Command 'print' accept only Int or String expression");		
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
				else if(aux.getType() instanceof TypeCianetoClass) {
					error("Command 'println' does not accept objects");		
				}		
				else if(aux.getType() != Type.intType && aux.getType() != Type.stringType) {		
					error("Command 'println' accept only Int or String expression");		
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
		if(lexer.token == Token.LT || lexer.token == Token.GT || lexer.token == Token.LE || lexer.token == Token.GE) {
			op = lexer.token;
			lexer.nextToken();
			right = simpleExpr();
			if(left.getType() != Type.intType || right.getType() != Type.intType) {
				error("Operator '" + op + "' can only be applied to Int values");
			}
			return new CompositeExpr(op, left, right);
		}
		else if(lexer.token == Token.EQ || lexer.token == Token.NEQ) {
			op = lexer.token;
			lexer.nextToken();
			right = simpleExpr();
			if(left.getType() == Type.nullType && right.getType() == Type.nullType) {
				error("Type nil cannot be compared with Type nil");
			}
			else if(left.getType() instanceof TypeCianetoClass && right.getType() instanceof TypeCianetoClass) {
				TypeCianetoClass leftclasstype = (TypeCianetoClass) left.getType();
				TypeCianetoClass rightclasstype = (TypeCianetoClass) right.getType();
				if(!leftclasstype.searchType(right.getType().getName()) && !rightclasstype.searchType(left.getType().getName())) {
					error("Left is not convertible to RightType and Right is not convertible to LeftType");
				}
			}
			else if(left.getType() != right.getType()) {
				if(left.getType() == Type.stringType && right.getType() != Type.nullType){
					error("LeftType is String, RightType must be String or nil");
				}
				else if(right.getType() == Type.stringType && left.getType() != Type.nullType){
					error("Right is String, LeftType must be String or nil");
				}	
			}	
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
		if(lexer.token == Token.PLUSPLUS) {		
			if(left.getType() != Type.intType && left.getType() != Type.stringType) {		
				error("Illegal types with '++', only Int and String are allowed");		
			}		
		}
		while(lexer.token == Token.PLUSPLUS) {
			op.add(lexer.token);
			lexer.nextToken();
			left = sumSubExpr();
			expr.add(left);		
			if(left.getType() != Type.intType && left.getType() != Type.stringType) {		
				error("Illegal types with '++', only Int and String are allowed");		
			}
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
							error("Cannot initialize undeclared class '" + idName + "'");
							return new ObjectCreation(Type.undefinedType);
						}
					}
					else if(lexer.token == Token.ID) {
						String idMethod = lexer.getStringValue();
						lexer.nextToken();
						Variable var = (Variable)symbolTable.getInLocal(idName);
						if(var == null) {
							error("Variable '" + idName + "' was not declared");
						}
						else {
							Type type = var.getType();
							if(type instanceof TypeCianetoClass) {
								TypeCianetoClass typecianeto = (TypeCianetoClass)type;
								MethodDec md = typecianeto.getMethodPublic(idMethod);
								if(md == null) {
									error("Method '" + idMethod + "' was not declared in class of object '" + idName + "'");
								}
								return new MethodCall(var, md);
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
										error("Method '" + idMethod + "' has different parameters types");
									}
									if(md.getType() == Type.voidType) {
										return new MethodCallPar(var, md, exprs);
									}
									return new MethodCallPar(var, md, exprs);
								}
								else {
									error("Method '" + idMethod + "' was not found in class '" + typecianeto.getName() + "' or its superclasses");
								}
							}
							else {
								error("Variable '" + idName + "' is not an object");
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
						
						TypeCianetoClass superclass = classdec.getSuper();
						MethodDec md = null;
						if(superclass != null) {
							md = superclass.getMethodPublic(idMethod);
							if(md == null) {
								error("superclass '" + superclass.getName() + "' does not have the public method '" + idMethod + "'");
							}
						}
						else {
							error("'super' used in class '" + classdec.getName() + "' that does not have a superclass");
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
						
						TypeCianetoClass superclass = classdec.getSuper();
						MethodDec md = null;
						if(superclass != null) {
							md = superclass.getMethodPublic(idMethod);
							if(md == null) {
								error("Superclass '" + superclass.getName() + "' does not have the public method '" + idMethod + "'");
							}
							else {
								if(md.comparePar(exprs) == false) {
									error("Method '" + idMethod + "' has different parameters types");
								}
							}
						}
						else {
							error("'super' used in class '" + classdec.getName() + "' that does not have a superclass");
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
								error("Method '" + idMethod + "' has different parameters types");
							}
						}
						else {
							error("Method '" + idMethod + "' was not found in self class or its superclasses");
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
												error("Method '" + idMethod + "' has different parameters types");
											}
										}
										else {
											error("Method '" + idMethod + "' has not declared in '" + id + "'");
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
								if(fd == null) {
									error("Field or Method '" + id + "' has not been declared");
								}
								return new SelfField(fd);
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
			error("Method '" + lexer.getStringValue() + "' does not belong to the command 'In.");
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
				if(symbolTable.getInClass(name) != null) {
					error("Field '" + name + "' has same name of an Class");
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
			if(!classdec.getOpen()) {
				error("Cannot declare a final method inside a final class");
			}
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

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;
	private TypeCianetoClass classdec;
	private boolean returnFlag;
	private boolean returnNeed;
	private Type returnType;
	
	private int isRepetitionState;
}
