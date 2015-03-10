package logika.model;

import org.junit.Assert;
import org.junit.Test;

public class ScopingSymbolTableTest {

    @Test
    public void getVarFromParentScope() {
        ScopingSymbolTable parent = symbolTable();
        ScopingSymbolTable subject = new ScopingSymbolTable(parent);
        parent.registerVariable(var("x", "Type1"));
        Assert.assertTrue(subject.varExists("x"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void namingConflictTest() {
        Variable var = var("x", "Type1");
        ScopingSymbolTable subject = symbolTable();
        subject.registerVariable(var);
        subject.registerVariable(var("x", "Type2"));
    }

    @Test
    public void registerVariable() {
        Variable var = var("x", "Type1");
        ScopingSymbolTable subject = symbolTable();
        subject.registerVariable(var);
        Assert.assertTrue(subject.varExists("x"));
    }

    private ScopingSymbolTable symbolTable() {
        return new ScopingSymbolTable(new XMLLoader(getClass().getResourceAsStream("/lang1.xml")).load());
    }

    private Variable var(final String name, final String typeName) {
        return new Variable(name, new Type(typeName));
    }

}
