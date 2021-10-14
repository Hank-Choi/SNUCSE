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

class MyHashtable<T : Comparable<T>?> {
    private val table: Array<AVL<T>?>
    fun insert(item: T, Hashcode: Int) {
        if (table[Hashcode] == null) {
            table[Hashcode] = AVL() //hashcode에 대응하는 첫 값이 들어오면 src.AVL tree 생성
        }
        table[Hashcode]!!.insert(item)
    }

    fun search(item: T, Hashcode: Int): AVLNode<T?>? {
        return if (table[Hashcode] == null) {
            null
        } else table[Hashcode]!!.search(item)
    }

    fun treeAt(index: Int): AVL<T>? {
        return table[index % 100]
    }

    init {
        table = arrayOfNulls<AVL<*>?>(100) // 배열은 모두 null로 초기화.
    }
}