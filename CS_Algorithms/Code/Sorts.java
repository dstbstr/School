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
        if(array == null || array.length < 2) return;
        Merge(array, 0, array.length);
    }

    public static <T extends Comparable<T> > void Merge(T[] array, int start, int end) {
        if(end - start < 2) return;
        int n = (end - start) / 2;
        int mid = start + n;
        Merge(array, start, mid);
        Merge(array, mid, end);
        Combine(array, start, mid, end);
    }

     public static <T extends Comparable<T> > void WideMerge(T[] array) {
        if(array == null || array.length < 2) return;
        WideMerge(array, 0, array.length);
     }
    public static <T extends Comparable<T> > void WideMerge(T[] array, int start, int end) {
        int n = end - start + 1;
        if(n < 2) return;
        if(n < 4) {
            Merge(array, start, end + 1);
            return;
        }
        int q = (end - start) / 4;
        int m1 = start + q;
        int m2 = m1 + q;
        int m3 = m2 + q;

        WideMerge(array, start, m1);
        WideMerge(array, m1, m2);
        Combine(array, start, m1, m2);

        WideMerge(array, m2, m3);
        WideMerge(array, m3, end);
        Combine(array, m2, m3, end);

        Combine(array, start, m2, end);
    }

    private static <T extends Comparable<T> > void Combine(T[] array, int start, int mid, int end) {
        if(start >= mid || mid >= end) return;
        T[] left = Arrays.copyOfRange(array, start, mid);
        T[] right = Arrays.copyOfRange(array, mid, end);
        int i = 0, j = 0, k = start;
        while(i < left.length && j < right.length) {
            if(left[i].compareTo(right[j]) <= 0) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }
        while(i < left.length) {
            array[k++] = left[i++];
        }
        while(j < right.length) {
            array[k++] = right[j++];
        }
    }
}
