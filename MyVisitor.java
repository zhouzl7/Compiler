import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

enum Type {
    ATOM, LIST
}

class StructEntryDescriptor {
    Type type;
    String name;
    String size;

    StructEntryDescriptor(Type type, String name, String size) {
        this.type = type;
        this.name = name;
        this.size = size;
    }
}

class MyVisitor extends TinyCBaseVisitor {
  private StringBuilder builder;
  private ArrayList<StructEntryDescriptor> structEntryList;
  private Stack<ArrayList<StructEntryDescriptor>> structEntryStack;

  MyVisitor() {
    this.builder = new StringBuilder();
    this.structEntryList = new ArrayList<>();
    this.structEntryStack = new Stack<>();
  }

  @Override
  public Object visitExpressionToBitAnd(TinyCParser.ExpressionToBitAndContext ctx) {
    visit(ctx.expression(0));
    builder.append('&');
    visit(ctx.expression(1));
    return null;
  }

  @Override
  public Object visitExpressionToIndex(TinyCParser.ExpressionToIndexContext ctx) {
    visit(ctx.expression(0));
    if (ctx.LBracket() != null) {
      builder.append('[');
      visit(ctx.expression(1));
      builder.append(']');
    }
    if (ctx.LParenthesis() != null) {
      builder.append('(');
      if (ctx.argumentExpressionList() != null)
        visit(ctx.argumentExpressionList());
      builder.append(')');
    } else if (ctx.Dot() != null) {
      builder.append('.');
      builder.append(ctx.Identifier().getText());
    }
    return null;
  }

  @Override
  public Object visitExpressionToAnd(TinyCParser.ExpressionToAndContext ctx) {
    visit(ctx.expression(0));
    builder.append("&&");
    visit(ctx.expression(1));
    return null;
  }

  @Override
  public Object visitExpressionToConstant(TinyCParser.ExpressionToConstantContext ctx) {
    builder.append(ctx.Constant().getText());
    return null;
  }

  @Override
  public Object visitExpressionToSelect(TinyCParser.ExpressionToSelectContext ctx) {
    visit(ctx.expression(0));
    builder.append('?');
    visit(ctx.expression(1));
    builder.append(':');
    visit(ctx.expression(2));
    return null;
  }

  @Override
  public Object visitExpressionToBitOr(TinyCParser.ExpressionToBitOrContext ctx) {
    visit(ctx.expression(0));
    builder.append('|');
    visit(ctx.expression(1));
    return null;
  }

  @Override
  public Object visitExpressionToSelf(TinyCParser.ExpressionToSelfContext ctx) {
    visit(ctx.expression());
    builder.append(ctx.Type.getText());
    return null;
  }

  @Override
  public Object visitExpressionToAdd_Sub(TinyCParser.ExpressionToAdd_SubContext ctx) {
    visit(ctx.expression(0));
    builder.append(ctx.Type.getText());
    visit(ctx.expression(1));
    return null;
  }

  @Override
  public Object visitExpressionToLess_Greater_LEq_GEq(TinyCParser.ExpressionToLess_Greater_LEq_GEqContext ctx) {
    visit(ctx.expression(0));
    builder.append(ctx.Type.getText());
    visit(ctx.expression(1));
    return null;
  }

  @Override
  public Object visitExpressionToAssign(TinyCParser.ExpressionToAssignContext ctx) {
    visit(ctx.expression(0));
    builder.append(ctx.Type.getText());
    visit(ctx.expression(1));
    return null;
  }

  @Override
  public Object visitExpressionToIdentifier(TinyCParser.ExpressionToIdentifierContext ctx) {
    builder.append(ctx.Identifier().getText());
    return null;
  }

  @Override
  public Object visitExpressionToString(TinyCParser.ExpressionToStringContext ctx) {
    builder.append(ctx.String().getText());
    return null;
  }

  @Override
  public Object visitExpressionToMul_Div_Mod(TinyCParser.ExpressionToMul_Div_ModContext ctx) {
    visit(ctx.expression(0));
    builder.append(ctx.Type.getText());
    visit(ctx.expression(1));
    return null;
  }

  @Override
  public Object visitExpressionToParenthesis(TinyCParser.ExpressionToParenthesisContext ctx) {
    builder.append('(');
    visit(ctx.expression());
    builder.append(')');
    return null;
  }

  @Override
  public Object visitExpressionToUnary(TinyCParser.ExpressionToUnaryContext ctx) {
    builder.append(ctx.Type.getText());
    visit(ctx.expression());
    return null;
  }

  @Override
  public Object visitExpressionToMoreExpression(TinyCParser.ExpressionToMoreExpressionContext ctx) {
    visit(ctx.expression(0));
    builder.append(", ");
    visit(ctx.expression(1));
    return null;
  }

  @Override
  public Object visitExpressionToBitXor(TinyCParser.ExpressionToBitXorContext ctx) {
    visit(ctx.expression(0));
    builder.append('^');
    visit(ctx.expression(1));
    return null;
  }

  @Override
  public Object visitExpressionToOr(TinyCParser.ExpressionToOrContext ctx) {
    visit(ctx.expression(0));
    builder.append("||");
    visit(ctx.expression(1));
    return null;
  }

  @Override
  public Object visitExpressionToEq_NEq(TinyCParser.ExpressionToEq_NEqContext ctx) {
    visit(ctx.expression(0));
    builder.append(ctx.Type.getText());
    visit(ctx.expression(1));
    return null;
  }

  @Override
  public Object visitArgumentExpressionListToExpression(TinyCParser.ArgumentExpressionListToExpressionContext ctx) {
    visit(ctx.expression());
    return null;
  }

  @Override
  public Object visitArgumentExpressionListToArgumentExpressionList(
          TinyCParser.ArgumentExpressionListToArgumentExpressionListContext ctx) {
    visit(ctx.argumentExpressionList());
    builder.append(", ");
    visit(ctx.expression());
    return null;
  }

  @Override
  public Object visitDeclarationToDeclarationSpecifierWithInit(
          TinyCParser.DeclarationToDeclarationSpecifierWithInitContext ctx) {
    visit(ctx.atomVariableType());
    visit(ctx.initDeclaratorList());
    builder.append(';');
    return null;
  }

  @Override
  public Object visitDeclarationToStruct(TinyCParser.DeclarationToStructContext ctx) {
    builder.append("var ");
    String name = ctx.Identifier(0).getText();
    builder.append(ctx.Identifier(1)).append(" = new ").append(name).append("()");
    for (int i = 2; i < ctx.Identifier().size(); i++)
      builder.append(", ").append(ctx.Identifier(i)).append("= new ").append(name).append("()");
    return null;
  }

  @Override
  public Object visitDeclarationToStructDeclarationSpecifier(
          TinyCParser.DeclarationToStructDeclarationSpecifierContext ctx) {
    builder.append("class ");
    visit(ctx.structVariableType());
    builder.append('{').append("constructor").append('(');
    structEntryStack.push(structEntryList);
    structEntryList.clear();
    for (TinyCParser.StructDeclaratorContext subCtx : ctx.structDeclarator())
      visit(subCtx);
    if (structEntryList.size() != 0) {
      builder.append(structEntryList.get(0).name);
      for (int i = 1; i < structEntryList.size(); i++)
        builder.append(", ").append(structEntryList.get(i).name);
    }
    builder.append(')').append('{');
    for (StructEntryDescriptor descriptor : structEntryList) {
      builder.append("this.").append(descriptor.name).append(" = ");
      if (descriptor.type == Type.ATOM)
        builder.append(descriptor.name).append(';');
      else if (descriptor.type == Type.LIST)
        builder.append("new Array").append('(').append(descriptor.size).append(')').append(';');
    }
    builder.append('}').append('}').append(';');
    structEntryList = structEntryStack.pop();
    return null;
  }

  @Override
  public Object visitInitDeclaratorListToInitDeclarator(TinyCParser.InitDeclaratorListToInitDeclaratorContext ctx) {
    visit(ctx.initDeclarator());
    return null;
  }

  @Override
  public Object visitInitDeclaratorListToInitDeclaratorList(TinyCParser.InitDeclaratorListToInitDeclaratorListContext ctx) {
    visit(ctx.initDeclaratorList());
    builder.append(", ");
    visit(ctx.initDeclarator());
    return null;
  }

  @Override
  public Object visitInitDeclaratorToDeclarator(TinyCParser.InitDeclaratorToDeclaratorContext ctx) {
    builder.append(ctx.Identifier().getText());
    return null;
  }

  @Override
  public Object visitInitDeclaratorToArrayDeclarator(TinyCParser.InitDeclaratorToArrayDeclaratorContext ctx) {
    builder.append(ctx.Identifier().getText()).append(" = ");
    builder.append("new Array").append('(');
    if (ctx.expression() != null)
      builder.append(ctx.expression().getText());
    else
      builder.append('0');
    builder.append(')');
    return null;
  }

  @Override
  public Object visitInitDeclaratorToDeclaratorWithAssign(TinyCParser.InitDeclaratorToDeclaratorWithAssignContext ctx) {
    builder.append(ctx.Identifier().getText()).append('=').append(ctx.expression().getText());
    return null;
  }

  @Override
  public Object visitStructDeclaratorToIdentifier(TinyCParser.StructDeclaratorToIdentifierContext ctx) {
    StructEntryDescriptor descriptor = new StructEntryDescriptor(Type.ATOM, ctx.Identifier().getText(), "0");
    structEntryList.add(descriptor);
    return null;
  }

  @Override
  public Object visitStructDeclaratorToBracketExpression(TinyCParser.StructDeclaratorToBracketExpressionContext ctx) {
    StructEntryDescriptor descriptor = new StructEntryDescriptor(Type.LIST, ctx.Identifier().getText(), "0");
    if (ctx.expression() != null)
      descriptor.size = ctx.expression().getText();
    structEntryList.add(descriptor);
    return null;
  }

  @Override
  public Object visitParameterTypeList(TinyCParser.ParameterTypeListContext ctx) {
    visit(ctx.parameterDeclaration(0));
    for (int i = 1; i < ctx.parameterDeclaration().size(); i++) {
      builder.append(", ");
      visit(ctx.parameterDeclaration(i));
    }
    return null;
  }

  @Override
  public Object visitParameterDeclaration(TinyCParser.ParameterDeclarationContext ctx) {
    visit(ctx.parameterDeclarator());
    return null;
  }

  @Override
  public Object visitParameterDeclarator(TinyCParser.ParameterDeclaratorContext ctx) {
    builder.append(ctx.Identifier().getText());
    return null;
  }

  @Override
  public Object visitStatementToLabeledStatement(TinyCParser.StatementToLabeledStatementContext ctx) {
    visit(ctx.labeledStatement());
    return null;
  }

  @Override
  public Object visitStatementToCompoundStatement(TinyCParser.StatementToCompoundStatementContext ctx) {
    visit(ctx.compoundStatement());
    return null;
  }

  @Override
  public Object visitStatementToExpressionStatement(TinyCParser.StatementToExpressionStatementContext ctx) {
    visit(ctx.expressionStatement());
    return null;
  }

  @Override
  public Object visitStatementToSelectionStatement(TinyCParser.StatementToSelectionStatementContext ctx) {
    visit(ctx.selectionStatement());
    return null;
  }

  @Override
  public Object visitStatementToIterationStatement(TinyCParser.StatementToIterationStatementContext ctx) {
    visit(ctx.iterationStatement());
    return null;
  }

  @Override
  public Object visitStatementToJumpStatement(TinyCParser.StatementToJumpStatementContext ctx) {
    visit(ctx.jumpStatement());
    return null;
  }

  @Override
  public Object visitLabeledStatementToIdentifier(TinyCParser.LabeledStatementToIdentifierContext ctx) {
    builder.append(ctx.Identifier().getText()).append(':');
    visit(ctx.statement());
    return null;
  }

  @Override
  public Object visitLabeledStatementToCase(TinyCParser.LabeledStatementToCaseContext ctx) {
    builder.append("case ");
    visit(ctx.expression());
    builder.append(": ");
    visit(ctx.statement());
    return null;
  }

  @Override
  public Object visitLabeledStatementToDefault(TinyCParser.LabeledStatementToDefaultContext ctx) {
    builder.append("default: ");
    visit(ctx.statement());
    return null;
  }

  @Override
  public Object visitCompoundStatement(TinyCParser.CompoundStatementContext ctx) {
    builder.append('{');
    for (TinyCParser.BlockItemContext subCtx : ctx.blockItem())
      visit(subCtx);
    builder.append('}');
    return null;
  }

  @Override
  public Object visitBlockItemToStatement(TinyCParser.BlockItemToStatementContext ctx) {
    visit(ctx.statement());
    return null;
  }

  @Override
  public Object visitBlockItemToDeclaration(TinyCParser.BlockItemToDeclarationContext ctx) {
    visit(ctx.declaration());
    return null;
  }

  @Override
  public Object visitExpressionStatement(TinyCParser.ExpressionStatementContext ctx) {
    if (ctx.expression() != null)
      visit(ctx.expression());
    builder.append(';');
    return null;
  }

  @Override
  public Object visitSelectionStatementToIf(TinyCParser.SelectionStatementToIfContext ctx) {
    builder.append("if  ").append('(');
    visit(ctx.expression());
    builder.append(')');
    visit(ctx.statement(0));
    if (ctx.Else() != null) {
      builder.append("else ");
      visit(ctx.statement(1));
    }
    return null;
  }

  @Override
  public Object visitSelectionStatementToSwitch(TinyCParser.SelectionStatementToSwitchContext ctx) {
    builder.append("switch ").append('(');
    visit(ctx.expression());
    builder.append(')');
    visit(ctx.statement());
    return null;
  }

  @Override
  public Object visitIterationStatementToWhile(TinyCParser.IterationStatementToWhileContext ctx) {
    builder.append("while ").append('(');
    visit(ctx.expression());
    builder.append(')');
    visit(ctx.statement());
    return null;
  }

  @Override
  public Object visitIterationStatementToDo(TinyCParser.IterationStatementToDoContext ctx) {
    builder.append("do ");
    visit(ctx.statement());
    builder.append("while ").append('(');
    visit(ctx.expression());
    builder.append(')').append(';');
    return null;
  }

  @Override
  public Object visitIterationStatementToFor(TinyCParser.IterationStatementToForContext ctx) {
    builder.append("for ").append('(');
    visit(ctx.forCondition());
    builder.append(')');
    visit(ctx.statement());
    return null;
  }

  @Override
  public Object visitForConditionToForDeclaration(TinyCParser.ForConditionToForDeclarationContext ctx) {
    visit(ctx.forDeclaration());
    builder.append(';');
    if (ctx.forExpression(0) != null)
      visit(ctx.forExpression(0));
    builder.append(';');
    if (ctx.forExpression(1) != null)
      visit(ctx.forExpression(1));
    return null;
  }

  @Override
  public Object visitForConditionToExpression(TinyCParser.ForConditionToExpressionContext ctx) {
    if (ctx.expression() != null)
      visit(ctx.expression());
    builder.append(';');
    if (ctx.forExpression(0) != null)
      visit(ctx.forExpression(0));
    builder.append(';');
    if (ctx.forExpression(1) != null)
      visit(ctx.forExpression(1));
    return null;
  }

  @Override
  public Object visitForDeclarationToInitDeclarationSpecifier(TinyCParser.ForDeclarationToInitDeclarationSpecifierContext ctx) {
    visit(ctx.atomVariableType());
    visit(ctx.initDeclaratorList());
    return null;
  }

  @Override
  public Object visitForExpressionToForExpression(TinyCParser.ForExpressionToForExpressionContext ctx) {
    visit(ctx.forExpression());
    builder.append(", ");
    visit(ctx.expression());
    return null;
  }

  @Override
  public Object visitForExpressionToExpression(TinyCParser.ForExpressionToExpressionContext ctx) {
    visit(ctx.expression());
    return null;
  }

  @Override
  public Object visitJumpStatementToContinue(TinyCParser.JumpStatementToContinueContext ctx) {
    builder.append("continue;");
    return null;
  }

  @Override
  public Object visitJumpStatementToBreak(TinyCParser.JumpStatementToBreakContext ctx) {
    builder.append("break;");
    return null;
  }

  @Override
  public Object visitJumpStatementToReturn(TinyCParser.JumpStatementToReturnContext ctx) {
    builder.append("return");
    if (ctx.expression() != null) {
      builder.append(' ');
      visit(ctx.expression());
    }
    builder.append(';');
    return null;
  }

  @Override
  public Object visitCompilationUnit(TinyCParser.CompilationUnitContext ctx) {
    for (TinyCParser.ExternalDeclarationContext subCtx : ctx.externalDeclaration())
      visit(subCtx);
    //builder.append("main();");
    System.out.println(builder.toString());
    File file = new File("out.txt");
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    FileWriter writer = null;
    try {
      writer = new FileWriter(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      writer.write(builder.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Object visitExternalDeclarationToFunctionDefinition(TinyCParser.ExternalDeclarationToFunctionDefinitionContext ctx) {
    visit(ctx.functionDefinition());
    return null;
  }

  @Override
  public Object visitExternalDeclarationToDeclaration(TinyCParser.ExternalDeclarationToDeclarationContext ctx) {
    visit(ctx.declaration());
    return null;
  }

  @Override
  public Object visitExternalDeclarationToSemicolon(TinyCParser.ExternalDeclarationToSemicolonContext ctx) {
    builder.append(';');
    return null;
  }

  @Override
  public Object visitFunctionDefinition(TinyCParser.FunctionDefinitionContext ctx) {
    builder.append("function ");
    visit(ctx.functionDeclarator());
    visit(ctx.compoundStatement());
    return null;
  }

  @Override
  public Object visitVariableType(TinyCParser.VariableTypeContext ctx) {
    return null;
  }

  @Override
  public Object visitAtomVariableType(TinyCParser.AtomVariableTypeContext ctx) {
    builder.append("var ");
    return null;
  }

  @Override
  public Object visitStructVariableType(TinyCParser.StructVariableTypeContext ctx) {
    builder.append(ctx.Identifier().getText());
    return null;
  }

  @Override
  public Object visitFunctionDeclaratorToParameterList(TinyCParser.FunctionDeclaratorToParameterListContext ctx) {
    builder.append(ctx.Identifier()).append('(');
    if (ctx.parameterTypeList() != null)
      visit(ctx.parameterTypeList());
    builder.append(')');
    return null;
  }
}