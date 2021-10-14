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

class Database private constructor() {
    private var patternTable: MyHashtable<Pattern?>? = null
    @Throws(IOException::class)
    fun readFile(FilePath: String?) {
        patternTable = MyHashtable()
        val fr = FileReader(FilePath)
        val br = BufferedReader(fr)
        var lineString: String
        var lineIndex = 0
        while (br.readLine().also { lineString = it } != null) {
            lineIndex++
            readLine(lineString, lineIndex)
        }
    }

    fun readLine(line: String, lineIndex: Int) {
        val length = line.length
        for (start in 0 until length - 5) {
            val pattern = Pattern(line.substring(start, start + 6), lineIndex, start + 1)
            insertPattern(pattern)
        }
    }

    fun searchSlot(index: Int): String {
        val indexSlot = patternTable!!.treeAt(index)
        return indexSlot?.root?.toString() ?: "EMPTY"
    }

    fun searchPattern(operand: String): String? { //첫 패턴을 foundList 에 저장 후 다음 패턴들이 연속되면 유지, 연속되지 않으면 삭제. 남은 foundList 출력.
        var foundList = MyLinkedList<Pattern?>() // 1 ~ n-1번째 패턴들이 연속되는 위치 list
        var n = 0 //입력한 String에 나오는 길이6 패턴의 개수
        for (i in 0 until operand.length - 5) {
            if (i + 6 == operand.length || i == 6 * n) {
                n++
                val pattern = Pattern(operand.substring(i, i + 6))
                if (patternTable!!.search(pattern, hashcode(pattern)) != null) {
                    val newList = patternTable!!.search(pattern, hashcode(pattern)).getList() //n번째 길이6 패턴의 위치
                    if (i == 0) foundList = newList!!.copy() else { // 1 ~ n-1번째까지의 패턴들의 위치와 n번째 패턴이 연속되는지 확인 (속도 향상을 위해 여러 경우로 나누어 구현)
                        val foundListIterator = foundList.iterator
                        while (foundListIterator.curr != null) {
                            val foundPatternLine = foundListIterator.curr.item.getLine()
                            val foundPatternRow = foundListIterator.curr.item.getRow()
                            val newListIterator = newList.iterator
                            while (newListIterator.curr != null) {
                                val newPatternLine = newListIterator.curr.item.getLine()
                                val newPatternRow = newListIterator.curr.item.getRow()
                                //n-1번째까지 연속된 패턴에 연속되는 n번째 패턴이 있는지 탐색, 없으면 삭제, 있으면 유지
                                if (newPatternLine < foundPatternLine) newListIterator!!.next() else if (newPatternLine == foundPatternLine) {
                                    if (newPatternRow < foundPatternRow + i) newListIterator!!.next() else if (newPatternRow == foundPatternRow + i) break else {
                                        foundListIterator!!.remove()
                                        break
                                    }
                                } else {
                                    foundListIterator!!.remove()
                                    break
                                }
                            }
                            if (newListIterator.curr == null) {
                                foundListIterator!!.remove()
                            }
                            foundListIterator!!.next()
                        }
                    }
                } else {
                    foundList = MyLinkedList()
                    break
                }
            }
        }
        return if (foundList.size() == 0) "(0, 0)" else {
            var firstNode = foundList.first()
            var out: String? = ""
            for (j in 0 until foundList.size() - 1) {
                out += firstNode.item.getIndex() + " "
                firstNode = firstNode.next
            }
            out += firstNode.item.getIndex()
            out
        }
    }

    fun insertPattern(pattern: Pattern) {
        patternTable!!.insert(pattern, hashcode(pattern))
    }

    fun hashcode(pattern: Pattern): Int {
        val patternString = pattern.toString()
        var result = 0
        for (i in 0 until patternString.length) result += patternString[i].toInt()
        result = result % 100
        return result
    }

    companion object {
        val instance = Database()
    }
}