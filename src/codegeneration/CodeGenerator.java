package src.codegeneration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


import src.AST.SimpleNode;
import src.AST.Program;
import src.AST.ProgramTreeConstants;

import src.semantic.Symbol;
import src.semantic.Symbol.Type;


 public class CodeGenerator {

 	private SimpleNode root;

 	private String file_name;

 	private PrintWriter output;


 	public CodeGenerator(SimpleNode root, String file_name) throws IOException{
        this.root = (SimpleNode) root.getChildren()[0];
		this.file_name = file_name;
		
		FileWriter filewriter = new FileWriter(file_name, false);
        BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
        
	    this.output = new PrintWriter(bufferedwriter);

 	}

 	public void generateCode(){

 		generateClassHeader();
		generateGlobals(root);
		generateStatic();
		generateFunctions();
		output.close();

 	}

 	private void generateClassHeader() {
		output.println(".source " + this.file_name);
 		output.println(".class public " + root.getName());
        output.println( ".super java/lang/Object"  + "\n");
        
     }

     private void generateGlobals(SimpleNode current_node) {

        for(int i=0; i< current_node.jjtGetNumChildren(); i++)
       {
           generateGlobals((SimpleNode) current_node.jjtGetChild(i));
       }

        if(current_node.getId() == ProgramTreeConstants.JJTMAIN){
			//TODO PRINT STATIC DIFFERENTIATION
			//TODO PRINT FIELD TYPES CORRECTLY
           output.println(".field " + current_node.getName() + " I" );
	 	}
	}

 	private void generateStatic() {
		// TODO Auto-generated method stub


 	}

 	private void generateFunctions() {
		// TODO Auto-generated method stub

     }
     
    }