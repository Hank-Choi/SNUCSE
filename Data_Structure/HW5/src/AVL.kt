package src

import src.AVL.AVLNode
import src.MyLinkedList
import src.MyHashtable
import kotlin.Throws
import java.io.IOException
import java.io.FileReader
import java.io.BufferedReader
import src.AVL
import src.MyLinkedList.MyLinkedListIterator
import src.MyLinkedList.LinkedListNode
import src.Database
import kotlin.jvm.JvmStatic
import src.Matching
import java.lang.NumberFormatException

class AVL<T : Comparable<T>?> {
    var root: AVLNode<T?>?
        private set

    fun insert(item: T) {
        var curr = root
        if (root!!.key == null) {
            root = AVLNode<T?>(item)
        } else {
            while (true) {
                if (curr!!.key!!.compareTo(item) == 0) {
                    curr.list.insert(item)
                    break
                } else if (curr.key!!.compareTo(item) > 0) {
                    if (curr.leftChild == null) {
                        curr.setLeft(AVLNode<T?>(item))
                        curr.rearrange(-1)
                        if (root!!.nodePosition != 0) root = root!!.parent
                        break
                    } else curr = curr.leftChild
                } else {
                    if (curr.rightChild == null) {
                        curr.setRight(AVLNode<T?>(item))
                        curr.rearrange(1)
                        if (root!!.nodePosition != 0) root = root!!.parent
                        break
                    } else curr = curr.rightChild
                }
            }
        }
    }

    fun search(Key: T): AVLNode<T?>? {
        var curr = root
        if (root!!.key == null) {
            return null
        } else {
            while (true) {
                curr = if (curr!!.key!!.compareTo(Key) > 0) if (curr.leftChild == null) return null else curr.leftChild else if (curr.key!!.compareTo(Key) < 0) {
                    if (curr.rightChild == null) return null else curr.rightChild
                } else return curr
            }
        }
    }

    class AVLNode<T : Comparable<T>?>(var key: T) {
        var parent: AVLNode<T>?
        var rightChild: AVLNode<T>? = null
        var leftChild: AVLNode<T>? = null
        var list: MyLinkedList<T>
        var nodePosition //left -1 right 1 headnode=0;
                = 0
        var heightDiff = 0 //rightHeight - leftHeight (값이 -k 이면 왼쪽 노드높이가 오른쪽 노드보다 k 높고 +k이면 오른쪽이 k높다.)
        fun setRight(child: AVLNode<T>?) {
            rightChild = child
            if (child != null) {
                child.parent = this
                child.nodePosition = RIGHT
            }
        }

        fun setLeft(child: AVLNode<T>?) {
            leftChild = child
            if (child != null) {
                child.parent = this
                child.nodePosition = LEFT
            }
        }

        fun rearrange(direction: Int) { //노드가 추가 됐을 때 부모 노드들의 높이 차를 update, 높이 차이가 2이상일 때 재정렬
            heightDiff += direction
            if (heightDiff < -1) {
                if (leftChild!!.heightDiff != -1) {
                    leftChild!!.leftRotate()
                }
                rightRotate()
            } else if (heightDiff > 1) {
                if (rightChild!!.heightDiff != 1) {
                    rightChild!!.rightRotate()
                }
                leftRotate()
            } else if (heightDiff != 0 && parent != null) //불균형 - 노드의 높이가 바뀌므로 parent의 높이도 바꿈
                parent!!.rearrange(nodePosition)
        }

        fun rightRotate() { //왼쪽 높이가 더 높을 때
            val originalPosition = nodePosition
            val originalParent = parent
            val originalLeftChild = leftChild
            val rightChildOfLeftChild = originalLeftChild!!.rightChild
            setLeft(rightChildOfLeftChild)
            originalLeftChild.setRight(this)
            if (heightDiff == -2) { //RightRotate가 호출되는 다섯가지 경우에 대해 높이정보 update
                if (originalLeftChild.heightDiff == -2) {
                    heightDiff += 3
                    originalLeftChild.heightDiff += 2
                } else if (originalLeftChild.heightDiff == -1) {
                    heightDiff += 2
                    originalLeftChild.heightDiff += 1
                } else { //diff==0
                    heightDiff += 1
                    originalLeftChild.heightDiff += 1
                }
            } else { //diff==-1
                if (originalLeftChild.heightDiff == -1) {
                    heightDiff += 2
                    originalLeftChild.heightDiff += 2
                } else { //diff==0
                    heightDiff += 1
                    originalLeftChild.heightDiff += 1
                }
            }
            if (originalPosition == LEFT) originalParent!!.setLeft(originalLeftChild) else if (originalPosition == RIGHT) originalParent!!.setRight(originalLeftChild) else {
                originalLeftChild.parent = null
                originalLeftChild.nodePosition = HEAD
            }
        }

        fun leftRotate() { //오른쪽 높이가 더 높을 때
            val originalPosition = nodePosition
            val originalParent = parent
            val originalRightChild = rightChild
            val leftChildOfRightChild = originalRightChild!!.leftChild
            setRight(leftChildOfRightChild)
            originalRightChild.setLeft(this)
            if (heightDiff == 2) { //LeftRotate가 호출되는 다섯가지 경우에 대해 높이정보 update
                if (originalRightChild.heightDiff == 2) {
                    heightDiff -= 3
                    originalRightChild.heightDiff -= 2
                } else if (originalRightChild.heightDiff == 1) {
                    heightDiff -= 2
                    originalRightChild.heightDiff -= 1
                } else {
                    heightDiff -= 1
                    originalRightChild.heightDiff -= 1
                }
            } else { //diff==1
                if (originalRightChild.heightDiff == 1) {
                    heightDiff -= 2
                    originalRightChild.heightDiff -= 2
                } else {
                    heightDiff -= 1
                    originalRightChild.heightDiff -= 1
                }
            }
            if (originalPosition == LEFT) originalParent!!.setLeft(originalRightChild) else if (originalPosition == RIGHT) originalParent!!.setRight(originalRightChild) else {
                originalRightChild.parent = null
                originalRightChild.nodePosition = HEAD
            }
        }

        override fun toString(): String { //preorder traversal 방식으로 출력
            return if (key != null) {
                val rootString = key.toString()
                var LeftString = ""
                var RightString = ""
                if (leftChild != null) {
                    LeftString = " " + leftChild.toString()
                }
                if (rightChild != null) {
                    RightString = " " + rightChild.toString()
                }
                rootString + LeftString + RightString
            } else "EMPTY"
        }

        companion object {
            const val RIGHT = 1
            const val LEFT = -1
            const val HEAD = 0
        }

        init {
            list = MyLinkedList()
            list.insert(key)
            parent = null
        }
    }

    init {
        root = AVLNode(null)
    }
}