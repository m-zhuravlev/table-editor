package tableeditor.ui;

public interface CellModelListener {
    void linkedCellChanged();

    void addListener(CellModelListener listener);

    void addSubscription(CellModelListener listener);

    void removeListener(CellModelListener listener);

    void clearSubscriptions();

    boolean isContainsListener(CellModelListener listener);
}
