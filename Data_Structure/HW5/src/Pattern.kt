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

class Pattern : Comparable<Pattern> {
    private val patternString: String
    var line = 0
        private set
    var row = 0
        private set

    internal constructor(patternString: String) {
        this.patternString = patternString
    }

    internal constructor(patternString: String, line: Int, row: Int) {
        this.patternString = patternString
        this.line = line
        this.row = row
    }

    val index: String
        get() = "($line, $row)"

    override fun compareTo(pattern: Pattern): Int {
        return patternString.compareTo(pattern.patternString)
    }

    override fun toString(): String {
        return patternString
    }
}