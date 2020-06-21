package src;

public class MyHashtable<T extends Comparable<T>> {
    private final AVL<T>[] table;

    @SuppressWarnings("unchecked")
    public MyHashtable() {
        this.table = new AVL[100]; // 배열은 모두 null로 초기화.
    }

    public void insert(T item, int Hashcode) {
        if (table[Hashcode] == null) {
            table[Hashcode] = new AVL<>(); //hashcode에 대응하는 첫 값이 들어오면 src.AVL tree 생성
        }
        table[Hashcode].insert(item);
    }

    public AVL.AVLNode<T> search(T item, int Hashcode) {
        if (table[Hashcode] == null) {
            return null;
        }
        return table[Hashcode].search(item);
    }

    public AVL<T> treeAt(int index) {
        return table[index % 100];
    }
}