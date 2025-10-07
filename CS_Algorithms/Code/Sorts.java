package Code;

import java.util.Arrays;

public class Sorts {
    public static <T extends Comparable<T> > void Insert(T[] array) {
        for(int i = 1; i < array.length; i++) {
            T temp = array[i];
            int j = i - 1;
            while(j >= 0 && array[j].compareTo(temp) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = temp;
        }
    }

    
    public static <T extends Comparable<T> > void Merge(T[] array) {
        if(array.length < 2) return;
        int mid = array.length / 2; // 1
        T[] left = Arrays.copyOfRange(array, 0, mid); // n / 2
        T[] right = Arrays.copyOfRange(array, mid, array.length); // n / 2
        Merge(left); // log(n)
        Merge(right); // log(n)
        Combine(left, right, array); // n
    }

    /*
    WideMerge(array):
        if length(array) < 2: return
        if length(array) < 4: NormalMerge(array); return
        dist = length(array) / 4

        first = array[0:dist]
        second = array[dist+1:2*dist]
        result1 = array[0:2*dist]
        WideMerge(first)
        WideMerge(second)
        Combine(first, second, result1)

        third = array[2*dist+1:3*dist]
        fourth = array[3*dist+1:length(array)]
        result2 = array[2*dist+1:length(array)]
        WideMerge(third)
        WideMerge(fourth)
        Combine(third, fourth, result2)

        Combine(result1, result2, array)
     */
    public static <T extends Comparable<T> > void WideMerge(T[] array) {
        if(array.length < 2) return;
        if(array.length < 4) {
            Merge(array);
            return;
        }
        int a = array.length / 4;
        int b = a * 2;
        int c = a * 3;
        T[] first = Arrays.copyOfRange(array, 0, a);
        T[] second = Arrays.copyOfRange(array, a, b);
        T[] r1 = Arrays.copyOfRange(array, 0, b);
        WideMerge(first);
        WideMerge(second);
        Combine(first, second, r1);

        T[] third = Arrays.copyOfRange(array, b, c);
        T[] fourth = Arrays.copyOfRange(array, c, array.length);
        T[] r2 = Arrays.copyOfRange(array, b, array.length);
        WideMerge(third);
        WideMerge(fourth);
        Combine(third, fourth, r2);

        Combine(r1, r2, array);
    }

    private static <T extends Comparable<T> > void Combine(T[] lhs, T[] rhs, T[] out) {
        int i = 0, j = 0, k = 0;
        while(i < lhs.length && j < rhs.length) {
            if(lhs[i].compareTo(rhs[j]) <= 0) {
                out[k++] = lhs[i++];
            } else {
                out[k++] = rhs[j++];
            }
        }
        while(i < lhs.length) {
            out[k++] = lhs[i++];
        }
        while(j < rhs.length) {
            out[k++] = rhs[j++];
        }
    }
}
