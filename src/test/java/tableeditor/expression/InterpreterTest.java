package tableeditor.expression;

import org.junit.jupiter.api.Test;
import tableeditor.expression.exception.ExpressionException;
import tableeditor.ui.CellModel;
import tableeditor.ui.MyTableModel;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterpreterTest {

    @Test
    public void simpleTest() throws ExpressionException {
        Interpreter ipr = new Interpreter(new CellModel(new MyTableModel(), 0, 0));

        BigDecimal result = (BigDecimal) ipr.getResult("2+2-1");
        assertEquals(result.intValue(), 3);

        result = (BigDecimal) ipr.getResult("6-2*3");
        assertEquals(result.intValue(), 0);

        result = (BigDecimal) ipr.getResult("(6-2)*3");
        assertEquals(result.intValue(), 12);

        result = (BigDecimal) ipr.getResult("1+5/2");
        assertEquals(result.doubleValue(), 3.5);

        result = (BigDecimal) ipr.getResult("8%3*2%4");
        assertEquals(result.intValue(), 0);

        result = (BigDecimal) ipr.getResult("1/3");
        assertEquals(result, new BigDecimal("1").divide(new BigDecimal("3"), Constants.MATH_CONTEXT));
    }

    @Test
    public void functionTest() throws ExpressionException {
        Interpreter ipr = new Interpreter(new CellModel(new MyTableModel(), 0, 0));
        BigDecimal result = (BigDecimal) ipr.getResult("(10.3 - pow(2,3))*10");
        assertEquals(result.intValue(), 23);

        result = (BigDecimal) ipr.getResult("pow( 2 , pow(2,2) )");
        assertEquals(result.intValue(), 16);
    }

    @Test
    public void cellLinkTest() throws ExpressionException {
        MyTableModel tableModel = new MyTableModel();
        Interpreter ipr = new Interpreter(new CellModel(tableModel, 0, 0));
        tableModel.setValueAt(2, 0, MyTableModel.nameToNumber("A"));
        tableModel.setValueAt(4, 0, MyTableModel.nameToNumber("B"));

        BigDecimal result = (BigDecimal) ipr.getResult("A1");
        assertEquals(result.intValue(), 2);

        result = (BigDecimal) ipr.getResult("(A1) + (B1)");
        assertEquals(result.intValue(), 6);

        result = (BigDecimal) ipr.getResult("((A1 - B1))");
        assertEquals(result.intValue(), -2);

        result = (BigDecimal) ipr.getResult("A1 * B1");
        assertEquals(result.intValue(), 8);

        result = (BigDecimal) ipr.getResult("pow(A1,B1)");
        assertEquals(result.intValue(), 16);
        result = (BigDecimal) ipr.getResult("pow( A1, ((A1)) + ((B1)) )");
        assertEquals(result.intValue(), 64);


        Boolean bRes = (Boolean) ipr.getResult("A1 > B1");
        assertEquals(bRes,false);

        bRes = (Boolean) ipr.getResult("A1 + 2  == B1");
        assertEquals(bRes,true);

        bRes = (Boolean) ipr.getResult("A1 == B1 -2");
        assertEquals(bRes,true);
    }

    @Test
    public void unaryTest() throws ExpressionException {
        Interpreter ipr = new Interpreter(new CellModel(new MyTableModel(), 0, 0));
        BigDecimal result = (BigDecimal) ipr.getResult("---1");
        assertEquals(result.intValue(), -1);

        result = (BigDecimal) ipr.getResult("4+-pow(2,2)");
        assertEquals(result.intValue(), 0);

        result = (BigDecimal) ipr.getResult("---1-+--1");
        assertEquals(result.intValue(), -2);

        result = (BigDecimal) ipr.getResult("-1--1--1");
        assertEquals(result.intValue(), 1);
    }

    @Test
    public void compareTset() throws ExpressionException {
        Interpreter ipr = new Interpreter(new CellModel(new MyTableModel(), 0, 0));

        Boolean result = (Boolean) ipr.getResult("2!=1");
        assertEquals(result, true);

        result = (Boolean) ipr.getResult("2 == 1");
        assertEquals(result, false);

        result = (Boolean) ipr.getResult("2 >= 1");
        assertEquals(result, true);

        result = (Boolean) ipr.getResult("2 > 1");
        assertEquals(result, true);
    }

}
