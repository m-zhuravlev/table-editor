package tableeditor.expression;

import org.junit.jupiter.api.Test;
import tableeditor.ui.CellModel;
import tableeditor.ui.MyTableModel;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterpreterTest {

    @Test
    public void simpleTest() {
        Interpreter ipr = new Interpreter(new CellModel(new MyTableModel(), 0, 0));

        BigDecimal result = ipr.getResult("2+2-1");
        assertEquals(result.intValue(), 3);

        result = ipr.getResult("6-2*3");
        assertEquals(result.intValue(), 0);

        result = ipr.getResult("(6-2)*3");
        assertEquals(result.intValue(), 12);

        result = ipr.getResult("1+5/2");
        assertEquals(result.doubleValue(), 3.5);

        result = ipr.getResult("8%3*2%4");
        assertEquals(result.intValue(), 0);

        result = ipr.getResult("1/3");
        assertEquals(result, new BigDecimal("1").divide(new BigDecimal("3"), Constants.MATH_CONTEXT));
    }

    @Test
    public void functionTest() {
        Interpreter ipr = new Interpreter(new CellModel(new MyTableModel(), 0, 0));
        BigDecimal result = ipr.getResult("(10.3 - pow(2,3))*10");
        assertEquals(result.intValue(), 23);

        result = ipr.getResult("pow( 2 , pow(2,2) )");
        assertEquals(result.intValue(), 16);
    }

    @Test
    public void cellLinkTest() {
        MyTableModel tableModel = new MyTableModel();
        Interpreter ipr = new Interpreter(new CellModel(tableModel, 0, 0));
        tableModel.setValueAt(2, 0, MyTableModel.nameToNumber("A"));
        tableModel.setValueAt(4, 0, MyTableModel.nameToNumber("B"));

        BigDecimal result = ipr.getResult("A1");
        assertEquals(result.intValue(), 2);

        result = ipr.getResult("(A1) + (B1)");
        assertEquals(result.intValue(), 6);

        result = ipr.getResult("((A1 - B1))");
        assertEquals(result.intValue(), -2);

        result = ipr.getResult("A1 * B1");
        assertEquals(result.intValue(), 8);

        result = ipr.getResult("pow(A1,B1)");
        assertEquals(result.intValue(), 16);
        result = ipr.getResult("pow( A1, ((A1)) + ((B1)) )");
        assertEquals(result.intValue(), 64);
    }

    @Test
    public void unaryTest() {
        Interpreter ipr = new Interpreter(new CellModel(new MyTableModel(), 0, 0));
        BigDecimal result = ipr.getResult("---1");
        assertEquals(result.intValue(), -1);

        result = ipr.getResult("4+-pow(2,2)");
        assertEquals(result.intValue(), 0);
    }

}
