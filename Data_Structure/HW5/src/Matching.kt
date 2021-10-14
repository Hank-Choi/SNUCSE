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
import java.io.InputStreamReader
import java.lang.NumberFormatException

object Matching {
    @JvmStatic
    fun main(args: Array<String>) {
        val br = BufferedReader(InputStreamReader(System.`in`))
        while (true) {
            try {
                val input = br.readLine()
                if (input.compareTo("QUIT") == 0) break
                command(input)
            } catch (e: IOException) {
                println("입력이 잘못되었습니다. 오류 : $e")
            }
        }
    }

    @Throws(IOException::class)
    private fun command(input: String) {
        val db: Database = Database.Companion.getInstance()
        val operator = input.substring(0, 2)
        val operand = input.substring(2)
        when (operator) {
            "< " -> db.readFile(operand)
            "@ " -> try {
                val index = operand.toInt()
                println(db.searchSlot(index))
            } catch (e: NumberFormatException) {
                throw IOException(input)
            }
            "? " -> println(db.searchPattern(operand))
            else -> throw IOException(input)
        }
    }
}