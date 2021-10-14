import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.TreeMap;


/*
1.	ÀÔ·ÂÀº ¼ýÀÚÀÇ ³ª¿­·Î ÀÌ·ç¾îÁö¸ç, Ã¹ ÁÙ¿¡ ÃÑ ¼ýÀÚÀÇ °¹¼ö, µÑÂ°ÁÙºÎÅÍ °¢°¢ÀÇ ¼ýÀÚ°¡ ÀÔ·ÂµË´Ï´Ù.
    9              (ÃÑ ¼ýÀÚÀÇ °¹¼ö)
    100
    7
    925
    -234
    10
    -9999
    12738
    239
    -2391          (¿©±â±îÁö ÃÑ 9°³)

    ÀÔ·ÂÀÌ ³¡³ª¸é Á¤·ÄµÈ ¹è¿­ÀÌ Ãâ·ÂµË´Ï´Ù.

2.	¼ýÀÚÀÇ ³ª¿­ ´ë½Å ³­¼ö¸¦ ÀÔ·ÂÇÒ ¼öµµ ÀÖ½À´Ï´Ù. ÀÌ ¶§´Â Ã¹ ÁÙ¿¡ r (°¹¼ö) (ÃÖ¼Ò°ª) (ÃÖ´ë°ª) ÀÌ ¿À°í ³¡³³´Ï´Ù.
    r 1000 -30000 30000        (-30000 ¿¡¼­ 30000 ±îÁö(-30000°ú 30000µµ Æ÷ÇÔ) 1000°³ÀÇ ¼ýÀÚ¸¦ ·£´ýÇÏ°Ô »ý¼ºÇÏ°í ±×°ÍÀ» ÀÔ·ÂÀ¸·Î ´ë½ÅÇÑ´Ù)

    ÀÔ·ÂÀÌ ³¡³ª¸é °¢ Á¤·ÄÀÇ ¼öÇà½Ã°£ÀÌ Ãâ·ÂµË´Ï´Ù.
 */

public class Sorting {
    public static void main(String args[]) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            boolean isRandom = false;
            int[] value;    // ÀÔ·Â ¹ÞÀ» ¼ýÀÚµéÀÇ ¹è¿­
            String nums = br.readLine();    // Ã¹ ÁÙÀ» ÀÔ·Â ¹ÞÀ½
            if (nums.charAt(0) == 'r') {
                isRandom = true;    // ³­¼öÀÓÀ» Ç¥½Ã

                String[] numsArg = nums.split(" ");

                int numSize = Integer.parseInt(numsArg[1]);    // ÃÑ °¹¼ö
                int rMinimum = Integer.parseInt(numsArg[2]);    // ÃÖ¼Ò°ª
                int rMaximum = Integer.parseInt(numsArg[3]);    // ÃÖ´ë°ª

                Random rand = new Random();    // ³­¼ö ÀÎ½ºÅÏ½º¸¦ »ý¼ºÇÑ´Ù.

                value = new int[numSize];    // ¹è¿­À» »ý¼ºÇÑ´Ù.
                for (int i = 0; i < value.length; i++)    // °¢°¢ÀÇ ¹è¿­¿¡ ³­¼ö¸¦ »ý¼ºÇÏ¿© ´ëÀÔ
                    value[i] = rand.nextInt(rMaximum - rMinimum + 1) + rMinimum;
            } else {
                // ³­¼ö°¡ ¾Æ´Ò °æ¿ì
                int numsize = Integer.parseInt(nums);

                value = new int[numsize];    // ¹è¿­À» »ý¼ºÇÑ´Ù.
                for (int i = 0; i < value.length; i++)    // ÇÑÁÙ¾¿ ÀÔ·Â¹Þ¾Æ ¹è¿­¿ø¼Ò·Î ´ëÀÔ
                    value[i] = Integer.parseInt(br.readLine());
            }

            while (true) {
                int[] newvalue = value.clone();    // ¿ø·¡ °ªÀÇ º¸È£¸¦ À§ÇØ º¹»çº»À» »ý¼ºÇÑ´Ù.

                String command = br.readLine();

                long t = System.currentTimeMillis();
                switch (command.charAt(0)) {
                    case 'M':    // Merge Sort
                        newvalue = doMergeSort(newvalue);
                        break;
                    case 'Q':    // Quick Sort
                        newvalue = doQuickSort(newvalue);
                        break;
                    case 'X':
                        return;
                    default:
                        throw new IOException("Àß¸øµÈ Á¤·Ä ¹æ¹ýÀ» ÀÔ·ÂÇß½À´Ï´Ù.");
                }
                if (isRandom) {
                    // ³­¼öÀÏ °æ¿ì ¼öÇà½Ã°£À» Ãâ·ÂÇÑ´Ù.
                    System.out.println((System.currentTimeMillis() - t) + " ms");
                } else {
                    // ³­¼ö°¡ ¾Æ´Ò °æ¿ì Á¤·ÄµÈ °á°ú°ªÀ» Ãâ·ÂÇÑ´Ù.
                    for (int i = 0; i < newvalue.length; i++) {
                        System.out.println(newvalue[i]);
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("ÀÔ·ÂÀÌ Àß¸øµÇ¾ú½À´Ï´Ù. ¿À·ù : " + e.toString());
        }
    }

    private static int[] doMergeSort(int[] value) {
        int [] tempArray = new int[value.length];

        return mergeSort(value, 0, value.length - 1);
    }

    static int[] mergeSort(int[] a, int l, int r) {
        if (l < r) {
            int avr = (l + r) / 2;

            mergeSort(a, l, avr);
            mergeSort(a, avr + 1, r);
            int [] tempArray = new int[a.length];

            int i;
            int p = 0;
            int k = l;
            int j = 0;
            for (i = l; i <= avr; i++) {
                tempArray[p++] = a[i];
            }

            while (i <= r && j < p) {
                a[k++] = (tempArray[j] <= a[i]) ? tempArray[j++] : a[i++];
            }

            while (j < p) {
                a[k++] = tempArray[j++];
            }
        }
        return a;
    }

    private static int[] doQuickSort(int[] value) {
        return quickSort(value, 0, value.length - 1);
    }

    private static int[] quickSort(int[] value, int i, int j) {
        int pointerL = i;
        int pointerR = j;

        int pivot = value[pointerR];

        do {
            while (value[pointerL] < pivot) {
                pointerL++;
            }
            while (value[pointerR] > pivot) {
                pointerR--;
            }
            if (pointerL <= pointerR) {
                int temp = value[pointerL];
                value[pointerL] = value[pointerR];
                value[pointerR] = temp;
            }
        } while (pointerL <= pointerR);

        if (i < pointerR) quickSort(value, i, pointerR);
        if (pointerL < j) quickSort(value, pointerL, j);

        return value;
    }

}