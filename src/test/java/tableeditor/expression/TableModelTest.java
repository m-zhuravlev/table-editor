package tableeditor.expression;

import org.junit.jupiter.api.Test;
import tableeditor.ui.MyTableModel;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableModelTest {

    @Test
    void cellNameTest() {
        MyTableModel model = new MyTableModel();

        assertEquals(model.getColumnName(1), "A");
        assertEquals(model.getColumnName(26), "Z");
        assertEquals(model.getColumnName(27), "AA");
        assertEquals(model.getColumnName(28), "AB");
        assertEquals(model.getColumnName(52), "AZ");
        assertEquals(model.getColumnName(53), "BA");

        assertEquals(MyTableModel.nameToNumber("A"), 1);
        assertEquals(MyTableModel.nameToNumber("Z"), 26);
        assertEquals(MyTableModel.nameToNumber("AA"), 27);
        assertEquals(MyTableModel.nameToNumber("AB"), 28);
        assertEquals(MyTableModel.nameToNumber("AZ"), 52);
        assertEquals(MyTableModel.nameToNumber("BA"), 53);
        assertEquals(model.getColumnName(MyTableModel.nameToNumber("AAA")), "AAA");

    }
}
