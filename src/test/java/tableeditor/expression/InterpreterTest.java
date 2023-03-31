package tableeditor.expression;

import org.junit.jupiter.api.Test;

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
}
