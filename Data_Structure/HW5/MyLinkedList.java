package src;

public class MyLinkedList<T> {
    LinkedListNode<T> head; //dummy head
    int numItems;

    MyLinkedList() {
        head = new LinkedListNode<>(null);
    }

    public int size() {
        return numItems;
    }

    public MyLinkedListIterator<T> getIterator(){return new MyLinkedListIterator<>(this);}

    public LinkedListNode<T> first() {
        return head.getNext();
    }

    public void insert(T item) {
        LinkedListNode<T> last = head;
        while (last.getNext() != null) {
            last = last.getNext();
        }
        last.insertNext(item);
        numItems += 1;
    }

    public MyLinkedList<T> copy() {
        MyLinkedList<T> cloneList = new MyLinkedList<>();
        LinkedListNode<T> cloneLast = cloneList.head;
        LinkedListNode<T> last = head;
        while (last.getNext() != null) {
            last = last.getNext();
            cloneLast.insertNext(last.getItem());
            cloneLast = cloneLast.getNext();
            cloneList.numItems++;
        }
        return cloneList;
    }

    static class LinkedListNode<T> {
        private LinkedListNode<T> next;
        private final T item;

        public LinkedListNode(T obj) {
            this.item = obj;
            this.next = null;
        }

        public T getItem() {
            return item;
        }

        public void setNext(LinkedListNode<T> next) {
            this.next = next;
        }

        public LinkedListNode<T> getNext() {
            return this.next;
        }

        public void removeNext() {
            setNext(next.getNext());
        }

        public void insertNext(T item) {
            setNext(new LinkedListNode<>(item));
        }
    }


    static class MyLinkedListIterator<T> {
        private final MyLinkedList<T> list;
        private LinkedListNode<T> curr;
        private LinkedListNode<T> prev;

        public LinkedListNode<T> getCurr() {
            return curr;
        }

        public MyLinkedListIterator(MyLinkedList<T> list) {
            this.list = list;
            this.curr = list.first();
            this.prev = list.head;
        }

        public void next() {
            prev = curr;
            curr = curr.getNext();
        }

        public void remove() {
            prev.removeNext();
            list.numItems--;
            curr = prev;
            prev = null;
        }
    }
}


