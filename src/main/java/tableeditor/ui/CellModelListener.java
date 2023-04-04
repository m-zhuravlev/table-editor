package tableeditor.ui;

import java.util.Set;

public interface CellModelListener {
    void linkedCellChanged();

    void addListener(CellModelListener listener);

    void addSubscription(CellModelListener listener);

    void removeListener(CellModelListener listener);

    void clearSubscriptions();

    Set<CellModelListener> getSubscriptions();


}
