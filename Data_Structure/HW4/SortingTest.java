import java.io.*;
import java.util.*;

public class SortingTest {
    public static void main(String args[]) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            boolean isRandom = false;    // 입력받은 배열이 난수인가 아닌가?
            int[] value;    // 입력 받을 숫자들의 배열
            String nums = br.readLine();    // 첫 줄을 입력 받음
            if (nums.charAt(0) == 'r') {
                // 난수일 경우
                isRandom = true;    // 난수임을 표시

                String[] numsArg = nums.split(" ");

                int numSize = Integer.parseInt(numsArg[1]);    // 총 갯수
                int rMinimum = Integer.parseInt(numsArg[2]);    // 최소값
                int rMaximum = Integer.parseInt(numsArg[3]);    // 최대값

                Random rand = new Random();    // 난수 인스턴스를 생성한다.

                value = new int[numSize];    // 배열을 생성한다.
                for (int i = 0; i < value.length; i++)    // 각각의 배열에 난수를 생성하여 대입
                    value[i] = rand.nextInt(rMaximum - rMinimum + 1) + rMinimum;
            } else {
                // 난수가 아닐 경우
                int numsize = Integer.parseInt(nums);

                value = new int[numsize];    // 배열을 생성한다.
                for (int i = 0; i < value.length; i++)    // 한줄씩 입력받아 배열원소로 대입
                    value[i] = Integer.parseInt(br.readLine());
            }

            // 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
            while (true) {
                int[] newvalue = (int[]) value.clone();    // 원래 값의 보호를 위해 복사본을 생성한다.

                String command = br.readLine();

                long t = System.currentTimeMillis();
                switch (command.charAt(0)) {
                    case 'B':    // Bubble Sort
                        newvalue = DoBubbleSort(newvalue);
                        break;
                    case 'I':    // Insertion Sort
                        newvalue = DoInsertionSort(newvalue);
                        break;
                    case 'H':    // Heap Sort
                        newvalue = DoHeapSort(newvalue);
                        break;
                    case 'M':    // Merge Sort
                        newvalue = DoMergeSort(newvalue);
                        break;
                    case 'Q':    // Quick Sort
                        newvalue = DoQuickSort(newvalue);
                        break;
                    case 'R':    // Radix Sort
                        newvalue = DoRadixSort(newvalue);
                        break;
                    case 'X':
                        return;    // 프로그램을 종료한다.
                    default:
                        throw new IOException("잘못된 정렬 방법을 입력했습니다.");
                }
                if (isRandom) {
                    // 난수일 경우 수행시간을 출력한다.
                    System.out.println((System.currentTimeMillis() - t) + " ms");
                } else {
                    // 난수가 아닐 경우 정렬된 결과값을 출력한다.
                    for (int i = 0; i < newvalue.length; i++) {
                        System.out.println(newvalue[i]);
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoBubbleSort(int[] value) {
        for (int i = 0; i < value.length - 1; ++i) {
            for (int j = 0; j < value.length - 1 - i; ++j) {
                if (value[j] > value[j + 1]) {
                    int temp = value[j];
                    value[j] = value[j + 1];
                    value[j + 1] = temp;
                }
            }
        }
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoInsertionSort(int[] value) {
        for (int i = 1; i < value.length; ++i) {
            for (int j = i; j > 0; --j) {
                if (value[j] < value[j - 1]) {
                    int temp = value[j];
                    value[j] = value[j - 1];
                    value[j - 1] = temp;
                }
            }
        }
        return (value);
    }//정렬된 배열에서 자신보다 작은 원소가 나올 때까지 앞 원소와 자리를 바꾼다.

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoHeapSort(int[] value) {
        int length = value.length;
        for (int i = length / 2 - 1; i >= 0; --i)
            percolateDown(value, i, length);
        for (int j = length - 1; j > 0; --j) {
            int temp = value[j];
            value[j] = value[0];
            value[0] = temp;
            percolateDown(value, 0, j);
        }
        return (value);
    }

    private static void percolateDown(int[] value, int i, int length) {
        if (i <= length / 2 - 1) {
            int child1 = 2 * i + 1;
            int child2 = 2 * i + 2;
            int child = (child2 < length && value[child2] > value[child1]) ? child2 : child1;
            if (value[i] < value[child]) {
                int swapTemp = value[child];
                value[child] = value[i];
                value[i] = swapTemp;
                percolateDown(value, child, length);
            }
        }
    }//root의 원소를 힙의 성질을 만족시킬 때까지 내려보낸다.

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoMergeSort(int[] value) {
        final int length=value.length;
        if (length == 1)
            return (value);
        int[] value1 = new int[length / 2];
        int[] value2 = new int[length - length / 2];
        System.arraycopy(value, 0, value1, 0, length / 2);
        System.arraycopy(value, length / 2, value2, 0, length - length / 2);
        value1 = DoMergeSort(value1);
        value2 = DoMergeSort(value2);
        value = Merge(value1, value2);
        return (value);
    }//array를 반으로 나눠 각각 array1,array2로 두고 두 정렬된 합친다.

    private static int[] Merge(int[] value1, int[] value2) {
        int[] MergedValue = new int[value1.length + value2.length];
        int i = 0, j = 0;
        while (i < value1.length && j < value2.length) {
            if (value1[i] < value2[j]) {
                MergedValue[i + j] = value1[i];
                ++i;
            } else {
                MergedValue[i + j] = value2[j];
                ++j;
            }
        }
        if (i < value1.length)
            System.arraycopy(value1, i, MergedValue, i + j, value1.length - i);
        else
            System.arraycopy(value2, j, MergedValue, i + j, value2.length - j);
        return MergedValue;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoQuickSort(int[] value) {
        int length = value.length;
        QuickSort(value, 0, length - 1);
        return (value);
    }

    private static void QuickSort(int[] value, int i, int j) {
        if (i < j) {
            int pivot = value[j];
            int pivotPosition = i;
            for (int k = i; k < j; ++k) {
                if (value[k] < pivot) {
                    int temp = value[k];
                    value[k] = value[pivotPosition];
                    value[pivotPosition] = temp;
                    pivotPosition++;
                }
            }
            value[j] = value[pivotPosition];
            value[pivotPosition] = pivot;
            QuickSort(value, i, pivotPosition - 1);
            QuickSort(value, pivotPosition + 1, j);
        }
    }//j번째 원소를 pivot으로 잡고 i부터 j까지의 원소를 pivot보다 큰 원소,작은 원소로 나눈다.

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoRadixSort(int[] value) {
        int valueLength = value.length;
        int maxDigitNumber = 0;
        int maxDigitLength = 1;
        for (int k = 0; k < valueLength; ++k) {
            int absoluteValue = (value[k] > 0) ? value[k] : -value[k];
            if (absoluteValue > maxDigitNumber)
                maxDigitNumber = absoluteValue;
        }//절대값이 가장 큰 수를 찾는다.
        while (maxDigitNumber >= 10) {
            maxDigitLength++;
            maxDigitNumber /= 10;
        }//절대값이 가장 큰 수의 자릿수를 구한다.
        for (int j = 1; j <= maxDigitLength; ++j) {
            int[] newValue = new int[valueLength];
            int[] digit = new int[valueLength];
            int[] digitIndex = new int[19];
            int[] digitCount = new int[19];
            for (int i = 0; i < valueLength; ++i) {
                int digitTemp = (int) (value[i] / Math.pow(10, j - 1) % 10);
                digit[i] = digitTemp + 9;
                digitCount[digit[i]]++;
            }//i번째 배열의 i번째 자릿수를 digit[i]에 넣고 그 자릿수에 대응하는 digitCount를 증가시킨다.
            //digit에는 0~18의 값이 들어가는데 이는 각각 -9 ~ +9에 대응한다. 
            digitIndex[0] = 0;
            for (int k = 1; k < 19; ++k) {
                digitIndex[k] = digitIndex[k - 1] + digitCount[k - 1];
            }//digitIndex는 각 자릿수를 가지고 있는 숫자들이 들어가야할 배열의 위치를 나타낸다.
            for (int i = 0; i < valueLength; ++i) {
                newValue[digitIndex[digit[i]]] = value[i];
                digitIndex[digit[i]]++;
            }
            value = newValue;
        }
        return (value);
    }
}