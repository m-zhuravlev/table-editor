package tableeditor.ui;

public class ColumnUtils {
    public static String numberToName(int column) {
        StringBuilder result = new StringBuilder();
        column--;
        for (; column >= 0; column = column / 26 - 1) {
            result.insert(0, (char) ((char) (column % 26) + 'A'));
        }
        return result.toString();
    }

    public static int nameToNumber(String name) {
        return name.chars().sum() - 'A' * name.length();
    }
}
