package Support;

import java.util.Arrays;

public class SupportFunctions {

    // Generic method to get subarray of a non-primitive array
    // between specified indices
    public static<T> T[] subArray(T[] array, int beg, int end) {
        return Arrays.copyOfRange(array, beg, end + 1);
    }

}
