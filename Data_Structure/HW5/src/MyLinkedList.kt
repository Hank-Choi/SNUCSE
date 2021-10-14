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

class MyLinkedList<T> internal constructor() {
    var head //dummy head
            : LinkedListNode<T?>
    var numItems = 0
    fun size(): Int {
        return numItems
    }

    val iterator: MyLinkedListIterator<T>
        get() = MyLinkedListIterator(this)

    fun first(): LinkedListNode<T?>? {
        return head.next
    }

    fun insert(item: T) {
        var last: LinkedListNode<T?>? = head
        while (last!!.next != null) {
            last = last.next
        }
        last.insertNext(item)
        numItems += 1
    }

    fun copy(): MyLinkedList<T?> {
        val cloneList = MyLinkedList<T?>()
        var cloneLast: LinkedListNode<T?>? = cloneList.head
        var last: LinkedListNode<T?>? = head
        while (last!!.next != null) {
            last = last.next
            cloneLast!!.insertNext(last!!.item)
            cloneLast = cloneLast.next
            cloneList.numItems++
        }
        return cloneList
    }

    class LinkedListNode<T>(val item: T) {
        var next: LinkedListNode<T>? = null
        fun removeNext() {
            next = next!!.next
        }

        fun insertNext(item: T) {
            next = LinkedListNode(item)
        }
    }

    class MyLinkedListIterator<T>(private val list: MyLinkedList<T>) {
        var curr: LinkedListNode<T>?
            private set
        private var prev: LinkedListNode<T>?
        operator fun next() {
            prev = curr
            curr = curr!!.next
        }

        fun remove() {
            prev!!.removeNext()
            list.numItems--
            curr = prev
            prev = null
        }

        init {
            curr = list.first()
            prev = list.head
        }
    }

    init {
        head = LinkedListNode(null)
    }
}