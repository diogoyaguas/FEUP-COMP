/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
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
  protected String return_type;

  public SimpleNode(int i) {
    id = i;
  }

  public SimpleNode(Program p, int i) {
    this(i);
    parser = p;
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
    case "NewAux":
      return this.name;
    case "NewNode":
      return "Identifier (Name: " + this.name + ")";
    case "Class":
    case "Main":
    case "Extends":
    case "Statement":
    case "Aux":
      if(this.name != null)
        return ProgramTreeConstants.jjtNodeName[id] + " (Name: " + this.name + ")";
      else 
        return ProgramTreeConstants.jjtNodeName[id];
    case "Id":
      if(this.type != null)
        return ProgramTreeConstants.jjtNodeName[id] + " (Name: " + this.name + " | Type: " + this.type + ")";
      else 
        return ProgramTreeConstants.jjtNodeName[id] + " (Name: " + this.name + ")";
    case "Type":
      return ProgramTreeConstants.jjtNodeName[id] + " (Type: " + this.type + ")";
    case "Identifier":
    case "Term":
      return ProgramTreeConstants.jjtNodeName[id] + " (Value: " + this.node_value + ")";
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

  public int getId() {
    return id;
  }
}

/*
 * JavaCC - OriginalChecksum=0f800bc037f1395a8218b1dc52635425 (do not edit this
 * line)
 */
