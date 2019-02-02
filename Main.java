import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String fileName;
        try {
            fileName = args[0];
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return;
        }
        File file = new File(fileName);
        FileInputStream fileStream;
        try {
            fileStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        ANTLRInputStream input;
        try {
            input = new ANTLRInputStream(fileStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        TinyCLexer lexer = new TinyCLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TinyCParser parser = new TinyCParser(tokens);
        ParseTree tree = parser.compilationUnit();
        MyVisitor visitor = new MyVisitor();
        visitor.visit(tree);
    }
}