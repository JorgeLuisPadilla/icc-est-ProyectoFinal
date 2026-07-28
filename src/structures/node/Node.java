package structures.node;

import java.util.Objects;

public class Node<T> {

    private T value;

    public Node(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Node<?>)) return false;

        Node<?> other = (Node<?>) obj;
        return Objects.equals(value, other.value);
    }
}
