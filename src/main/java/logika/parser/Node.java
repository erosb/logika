package logika.parser;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private final List<Node> childNodes = new ArrayList<>();

    public void add(final Node child) {
        childNodes.add(child);
    }

    public Node child(final int i) {
        return childNodes.get(i);
    }

    public int childCount() {
        return childNodes.size();
    }

}
