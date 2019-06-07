/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */

package src.AST;

import src.semantic.*;
import src.semantic.Symbol.Type;

public class SimpleNode implements Node {

    protected Node parent;
    protected Node[] children;
    protected int id;
    protected Object value;
    protected Program parser;
    protected Token firstToken;
    protected Token lastToken;

    protected String node_value;
    protected String name;
    protected String type;

    protected boolean has_scope;
    protected boolean has_method_scope;
    protected SymbolTable symbols;
    protected MethodTable methods;

    public SimpleNode(int i) {
        id = i;
        this.has_scope = false;
        this.has_method_scope = false;
    }

    public SimpleNode(int i, boolean has_scope, boolean has_method_scope) {
        this(i);
        this.has_scope = has_scope;
        this.has_method_scope = has_method_scope;
    }

    public SimpleNode(Program p, int i) {
        this(i);
        parser = p;
        this.has_scope = false;
        this.has_method_scope = false;
    }

    public SimpleNode(Program p, int i, boolean has_scope, boolean has_method_scope) {
        this(i);
        parser = p;
        this.has_scope = has_scope;
        this.has_method_scope = has_method_scope;

    }

    public SymbolTable getNodeSymbolTable() {

        if (parent == null)
            return null;

        else if (this.has_scope)
            return new SymbolTable(((SimpleNode) this.parent).getSymbols());
        else
            return ((SimpleNode) this.parent).getSymbols();

    }

    public MethodTable getNodeMethodTable() {
        if (parent == null)
            return null;

        else if (this.has_method_scope){
            return new MethodTable(this.name);
        }
        else
            return ((SimpleNode) this.parent).getMethods();
    }

    public int getSymbolIndex(String name) {
        return symbols.getSymbolIndex(name);
    }

    public void printSemanticError(String error) {
        // TODO
        // Get location of where the error is
        // Function + line + column
        // Make an error counter
        System.out.println("Semantic error: " + error);

    }

    public boolean analyse() {
        symbols = getNodeSymbolTable();
        methods = getNodeMethodTable();
        Node[] children = getChildren();

        boolean success = true;

        if (children != null)
            for (Node child : children)
                success = ((SimpleNode) child).analyse();

        success = checkSymbolTable();

        return success;
    }

    public int attributeIndexes(int last_index_attributed) {
        int current_index = last_index_attributed;
        if (this.has_scope)
            current_index = this.symbols.attributeIndexes(current_index);

        if (getChildren() != null) {
            for (Node child : getChildren())
                current_index = ((SimpleNode) child).attributeIndexes(current_index);
        }

        return current_index;
    }

    public boolean checkSymbolTable() {
        return true;
    }

    public void jjtOpen() {
    }

    public void jjtClose() {
    }

    public void jjtSetParent(Node n) {
        parent = n;
    }

    public Node jjtGetParent() {
        return parent;
    }

    public void jjtAddChild(Node n, int i) {
        if (children == null) {
            children = new Node[i + 1];
        } else if (i >= children.length) {
            Node c[] = new Node[i + 1];
            System.arraycopy(children, 0, c, 0, children.length);
            children = c;
        }
        children[i] = n;
    }

    public Node jjtGetChild(int i) {
        return children[i];
    }

    public int jjtGetNumChildren() {
        return (children == null) ? 0 : children.length;
    }

    public void jjtSetValue(Object value) {
        this.value = value;
    }

    public Object jjtGetValue() {
        return value;
    }

    public Token jjtGetFirstToken() {
        return firstToken;
    }

    public void jjtSetFirstToken(Token token) {
        this.firstToken = token;
    }

    public Token jjtGetLastToken() {
        return lastToken;
    }

    public void jjtSetLastToken(Token token) {
        this.lastToken = token;
    }

    /*
     * You can override these two methods in subclasses of SimpleNode to customize
     * the way the node appears when the tree is dumped. If your output uses more
     * than one line you should override toString(String), otherwise overriding
     * toString() is probably all you need to do.
     */

    public String toString() {
        switch (ProgramTreeConstants.jjtNodeName[id]) {
        case "Program":
        case "Return":
        case "StatementAux":
            return ProgramTreeConstants.jjtNodeName[id];
        case "PeriodAux":
            return this.name;
        case "NewNode":
            return "Identifier (Name: " + this.name + ")";
        case "Class":
        case "Main":
        case "Extends":
        case "Statement":
        case "Aux":

            if (this.name != null)
                return ProgramTreeConstants.jjtNodeName[id] + " (Name: " + this.name + ")";
            else
                return ProgramTreeConstants.jjtNodeName[id];
                case "Identifier":
                return ProgramTreeConstants.jjtNodeName[id] + " (Name: " + this.name + " | Type: " + this.type + ")";
        case "NEW":
            if (this.name != null)
                return ProgramTreeConstants.jjtNodeName[id] + " (Name: " + this.name + " | Type: " + this.type + ")";
            else
                return ProgramTreeConstants.jjtNodeName[id] + " (Type: " + this.type + ")";
        case "Type":
            return ProgramTreeConstants.jjtNodeName[id] + " (Type: " + this.type + ")";
        case "Term":
            return ProgramTreeConstants.jjtNodeName[id] + " (Value: " + this.node_value + " | Type: " + this.type + ")";
        case "Argument":
        case "Var":
            return ProgramTreeConstants.jjtNodeName[id] + " (Name: " + this.name + " | Type: " + this.type + ")";
        case "Method":
            return ProgramTreeConstants.jjtNodeName[id] + " (Name: " + this.name + " | Return Type: " + this.type + ")";
        default:
            return ProgramTreeConstants.jjtNodeName[id];
        }
    }

    public String toString(String prefix) {
        return prefix + toString();
    }

    /*
     * Override this method if you want to customize how the node dumps out its
     * children.
     */

    public void dump(String prefix) {
        System.out.println(toString(prefix));
        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                SimpleNode n = (SimpleNode) children[i];
                if (n != null) {
                    n.dump(prefix + " ");
                }
            }
        }
    }

    public void printTables(String symbols_prefix, String methods_prefix) {

        printMethodsTable(methods_prefix);
        printSymbolsTable(symbols_prefix);

        Node[] children = getChildren();

        if (children != null && children.length > 0) {

            for (Node child : children) {

                SimpleNode simple_n = (SimpleNode) child;
                if (simple_n != null)
                    simple_n.printTables(symbols_prefix, methods_prefix);
            }

        }
    }

    public void printSymbolsTable(String prefix) {

        if (!this.has_scope)
            return;

        String msg = prefix + "=> " + ProgramTreeConstants.jjtNodeName[id];

        if (ProgramTreeConstants.jjtNodeName[id].equals("Method"))
            msg += " ( " + this.name + " )";

        System.out.println(msg);
        symbols.printSymbolTable();

    }

    public void printMethodsTable(String prefix) {

        if (!this.has_method_scope)
            return;

        String msg = prefix + "=> " + ProgramTreeConstants.jjtNodeName[id];

        System.out.println(msg);
        methods.printMethodsTable();

    }

    public int getId() {
        return id;
    }

    public String getNodeString() {
        return ProgramTreeConstants.jjtNodeName[this.id];
    }

    public SymbolTable getSymbols() {
        return this.symbols;
    }

    public MethodTable getMethods() {
        return this.methods;
    }

    public Node[] getChildren() {
        return this.children;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getNodeValue() {
        return this.node_value;
    }

    public Symbol.Type getReturnType() {

        switch (this.type) {
        case "int":
            return Symbol.Type.INT;
        case "boolean":
            return Symbol.Type.BOOLEAN;
        case "int[]":
            return Symbol.Type.INT_ARRAY;
        case "id":
            return Symbol.Type.VOID;
        case "":
            return Symbol.Type.UNDEFINED;
        default:
            return Symbol.Type.OBJECT;
        }
    }

    public Symbol.Type getVarType(String var_name) {

        if (!this.symbols.hasSymbolWithName(var_name)) {
            printSemanticError("Variable " + var_name + " undefined");
            return Type.VOID;
        }

        return this.symbols.getSymbolWithName(var_name).getType();
    }

    public boolean isTypeValidForVar(Symbol.Type type) {
        return !type.equals(Symbol.Type.VOID) && !type.equals(Symbol.Type.UNDEFINED);
    }

    public boolean isTypeValidForMethod(Symbol.Type type) {
        return !type.equals(Symbol.Type.UNDEFINED);
    }
}

/*
 * JavaCC - OriginalChecksum=0f800bc037f1395a8218b1dc52635425 (do not edit this
 * line)
 */
