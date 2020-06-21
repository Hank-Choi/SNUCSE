package src;

public class AVL<T extends Comparable<T>> {
    private AVLNode<T> root;

    public AVL() {
        root = new AVLNode<>(null);
    }

    public void insert(T item) {
        AVLNode<T> curr = root;
        if (root.key == null) {
            root = new AVLNode<>(item);
        } else {
            while (true) {
                if (curr.key.compareTo(item) == 0) {
                    curr.list.insert(item);
                    break;
                } else if (curr.key.compareTo(item) > 0) {
                    if (curr.leftChild == null) {
                        curr.setLeft(new AVLNode<>(item));
                        curr.rearrange(-1);
                        if (root.nodePosition != 0)
                            root = root.parent;
                        break;
                    } else
                        curr = curr.leftChild;
                } else {
                    if (curr.rightChild == null) {
                        curr.setRight(new AVLNode<>(item));
                        curr.rearrange(1);
                        if (root.nodePosition != 0)
                            root = root.parent;
                        break;
                    } else
                        curr = curr.rightChild;
                }
            }
        }
    }

    public AVLNode<T> search(T Key) {
        AVLNode<T> curr = root;
        if (root.key == null) {
            return null;
        } else {
            while (true) {
                if (curr.key.compareTo(Key) > 0)
                    if (curr.leftChild == null)
                        return null;
                    else
                        curr = curr.leftChild;
                else if (curr.key.compareTo(Key) < 0) {
                    if (curr.rightChild == null)
                        return null;
                    else
                        curr = curr.rightChild;
                } else
                    return curr;
            }
        }
    }

    public AVLNode<T> getRoot() {
        return root;
    }


    static class AVLNode<T extends Comparable<T>> {
        T key;
        AVLNode<T> parent;
        AVLNode<T> rightChild = null;
        AVLNode<T> leftChild = null;
        MyLinkedList<T> list;
        int nodePosition; //left -1 right 1 headnode=0;
        int heightDiff = 0; //rightHeight - leftHeight (값이 -k 이면 왼쪽 노드높이가 오른쪽 노드보다 k 높고 +k이면 오른쪽이 k높다.)

        public static final int RIGHT = 1;
        public static final int LEFT = -1;
        public static final int HEAD = 0;

        public AVLNode(T obj) {
            this.key = obj;
            this.list = new MyLinkedList<>();
            this.list.insert(obj);
            this.parent = null;
        }

        public void setRight(AVLNode<T> child) {
            this.rightChild = child;
            if (child != null) {
                child.parent = this;
                child.nodePosition = RIGHT;
            }
        }

        public void setLeft(AVLNode<T> child) {
            this.leftChild = child;
            if (child != null) {
                child.parent = this;
                child.nodePosition = LEFT;
            }
        }

        public final void rearrange(int direction) { //노드가 추가 됐을 때 부모 노드들의 높이 차를 update, 높이 차이가 2이상일 때 재정렬
            heightDiff += direction;
            if (heightDiff < -1) {
                if (leftChild.heightDiff != -1) {
                    leftChild.leftRotate();
                }
                rightRotate();
            } else if (heightDiff > 1) {
                if (rightChild.heightDiff != 1) {
                    rightChild.rightRotate();
                }
                leftRotate();
            } else if (heightDiff != 0 && parent != null) //불균형 - 노드의 높이가 바뀌므로 parent의 높이도 바꿈
                this.parent.rearrange(nodePosition);
        }

        public void rightRotate() { //왼쪽 높이가 더 높을 때
            int originalPosition = this.nodePosition;
            AVLNode<T> originalParent = this.parent;
            AVLNode<T> originalLeftChild = this.leftChild;
            AVLNode<T> rightChildOfLeftChild = originalLeftChild.rightChild;
            this.setLeft(rightChildOfLeftChild);
            originalLeftChild.setRight(this);
            if (heightDiff == -2) { //RightRotate가 호출되는 다섯가지 경우에 대해 높이정보 update
                if (originalLeftChild.heightDiff == -2) {
                    this.heightDiff += 3;
                    originalLeftChild.heightDiff += 2;
                } else if (originalLeftChild.heightDiff == -1) {
                    this.heightDiff += 2;
                    originalLeftChild.heightDiff += 1;
                } else { //diff==0
                    this.heightDiff += 1;
                    originalLeftChild.heightDiff += 1;
                }
            } else { //diff==-1
                if (originalLeftChild.heightDiff == -1) {
                    this.heightDiff += 2;
                    originalLeftChild.heightDiff += 2;
                } else { //diff==0
                    this.heightDiff += 1;
                    originalLeftChild.heightDiff += 1;
                }
            }
            if (originalPosition == LEFT)
                originalParent.setLeft(originalLeftChild);
            else if (originalPosition == RIGHT)
                originalParent.setRight(originalLeftChild);
            else {
                originalLeftChild.parent = null;
                originalLeftChild.nodePosition = HEAD;
            }
        }

        public void leftRotate() { //오른쪽 높이가 더 높을 때
            int originalPosition = this.nodePosition;
            AVLNode<T> originalParent = this.parent;
            AVLNode<T> originalRightChild= this.rightChild;
            AVLNode<T> leftChildOfRightChild = originalRightChild.leftChild;
            this.setRight(leftChildOfRightChild);
            originalRightChild.setLeft(this);
            if (heightDiff == 2) {//LeftRotate가 호출되는 다섯가지 경우에 대해 높이정보 update
                if (originalRightChild.heightDiff == 2) {
                    this.heightDiff -= 3;
                    originalRightChild.heightDiff -= 2;
                } else if (originalRightChild.heightDiff == 1) {
                    this.heightDiff -= 2;
                    originalRightChild.heightDiff -= 1;
                } else {
                    this.heightDiff -= 1;
                    originalRightChild.heightDiff -= 1;
                }
            } else { //diff==1
                if (originalRightChild.heightDiff == 1) {
                    this.heightDiff -= 2;
                    originalRightChild.heightDiff -= 2;
                } else {
                    this.heightDiff -= 1;
                    originalRightChild.heightDiff -= 1;
                }
            }
            if (originalPosition == LEFT)
                originalParent.setLeft(originalRightChild);
            else if (originalPosition == RIGHT)
                originalParent.setRight(originalRightChild);
            else {
                originalRightChild.parent = null;
                originalRightChild.nodePosition = HEAD;
            }
        }

        @Override
        public String toString() { //preorder traversal 방식으로 출력
            if (this.key != null) {
                String rootString = this.key.toString();
                String LeftString = "";
                String RightString = "";
                if (this.leftChild != null) {
                    LeftString = " " + leftChild.toString();
                }
                if (this.rightChild != null) {
                    RightString = " " + rightChild.toString();
                }
                return rootString + LeftString + RightString;
            } else
                return "EMPTY";
        }

        public MyLinkedList<T> getList() {
            return list;
        }
    }
}
