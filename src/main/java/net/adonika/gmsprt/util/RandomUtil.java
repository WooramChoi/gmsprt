package net.adonika.gmsprt.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.validation.constraints.NotNull;

public class RandomUtil {
    
    private static final String[] LOWER_CASE_ALPHABET = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

    private static final String[] UPPER_CASE_ALPHABET = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    
    private static final String[] NUMBER = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    
    public enum Mode {LOWER, UPPER, NUMBER};
    
    public RandomUtil() {
    }
    
    public static <T> List<T> getRandom(@NotNull T[] source, int size) {
        return getRandom(source, size, true);
    }
    
    public static <T> List<T> getRandom(@NotNull T[] source, int size, boolean allowsDuplicate) {
        
        if (!allowsDuplicate && source.length < size) {
            throw new IllegalArgumentException();
        }
        
        List<T> arrResult = new ArrayList<T>();
        List<T> arrSource = new LinkedList<T>(Arrays.asList(source));
        Random random = new Random();
        
        for(int i=0; i<size; i++) {
            int selected = random.nextInt(arrSource.size());
            arrResult.add(arrSource.get(selected));
            if (!allowsDuplicate) {
                arrSource.remove(selected);
            }
        }
        
        return arrResult;
    }
    
    public static String getRandomString(int length) {
        return getRandomString(length, true);
    }
    
    public static String getRandomString(int length, boolean allowsDuplicate) {
        return getRandomString(length, allowsDuplicate, Mode.UPPER, Mode.LOWER, Mode.NUMBER);
    }
    
    public static String getRandomString(int length, boolean allowsDuplicate, @NotNull Mode... mode) {
        
        List<String> arrSource = new ArrayList<String>();
        boolean useUpper = false;
        boolean useLower = false;
        boolean useNumber = false;
        
        for(Mode selectedMode: mode) {
            if (selectedMode.equals(Mode.UPPER)) {
                useUpper = true;
            }else if(selectedMode.equals(Mode.LOWER)) {
                useLower = true;
            }else if(selectedMode.equals(Mode.NUMBER)) {
                useNumber = true;
            }
        }
        
        if(useUpper) {
            arrSource.addAll(Arrays.asList(UPPER_CASE_ALPHABET));
        }
        if(useLower) {
            arrSource.addAll(Arrays.asList(LOWER_CASE_ALPHABET));
        }
        if(useNumber) {
            arrSource.addAll(Arrays.asList(NUMBER));
        }
        
        List<String> arrResult = getRandom(arrSource.toArray(new String[0]), length, allowsDuplicate);
        
        return String.join("", arrResult);
    }
    
    public static void main(String[] args) {
        
        Integer[] lottoNumbers = new Integer[45];
        for(int i=0; i<45; i++) {
            lottoNumbers[i] = (i+1);
        }
        
        List<Integer> selectedNumbers = getRandom(lottoNumbers, 7, false);
        List<Integer> win = selectedNumbers.subList(0, 6);
        win.sort((Integer o1, Integer o2) -> {
            return o1 - o2;
        });
        
        System.out.print("Winner: " + win);
        System.out.println(" + " + selectedNumbers.get(6));
        
    }
}
