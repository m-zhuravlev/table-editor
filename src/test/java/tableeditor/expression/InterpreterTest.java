package tableeditor.expression;

import org.junit.jupiter.api.Test;
import tableeditor.ui.MyTableModel;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterpreterTest {

    @Test
    public void simpleTest() {
        BigDecimal result = Interpreter.getResult("2+2-1");
        assertEquals(result.intValue(), 3);

        result = Interpreter.getResult("6-2*3");
        assertEquals(result.intValue(), 0);

        result = Interpreter.getResult("(6-2)*3");
        assertEquals(result.intValue(), 12);

        result = Interpreter.getResult("1+5/2");
        assertEquals(result.doubleValue(), 3.5);

        result = Interpreter.getResult("8%3*2%4");
        assertEquals(result.intValue(), 0);
    }

    @Test
    public void functionTest() {
        BigDecimal result = Interpreter.getResult("(10.3 - pow(2,3))*10");
        assertEquals(result.intValue(), 23);

        result = Interpreter.getResult("pow( 2 , pow(2,2) )");
        assertEquals(result.intValue(), 16);
    }

    @Test
    public void cellLinkTest() {
        MyTableModel tableModel = new MyTableModel();
        Interpreter.setTableModel(tableModel);
        tableModel.setValueAt(2, 1, tableModel.nameToNumber("A"));
        tableModel.setValueAt(4, 1, tableModel.nameToNumber("B"));

        BigDecimal result = Interpreter.getResult("A1");
        assertEquals(result.intValue(), 2);

        result = Interpreter.getResult("(A1) + (B1)");
        assertEquals(result.intValue(), 6);

        result = Interpreter.getResult("((A1 - B1))");
        assertEquals(result.intValue(), -2);

        result = Interpreter.getResult("A1 * B1");
        assertEquals(result.intValue(), 8);

        result = Interpreter.getResult("pow(A1,B1)");
        assertEquals(result.intValue(), 16);
        result = Interpreter.getResult("pow( A1, ((A1)) + ((B1)) )");
        assertEquals(result.intValue(), 64);
    }

    @Test
    public void unaryTest() {
        BigDecimal result = Interpreter.getResult("---1");
        assertEquals(result.intValue(), -1);

        result = Interpreter.getResult("4+-pow(2,2)");
        assertEquals(result.intValue(), 0);
    }

}
