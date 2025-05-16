package ir.ac.kntu.helper;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DealerCodeGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private static final Set<String> usedCodes = new HashSet<>();
    private static final Random random = new Random();

    public static String generateUniqueCode() {
        String code;
        do {
            code = generateRandomCode();
        } while (usedCodes.contains(code)); 
        usedCodes.add(code);
        return code;
    }

    private static String generateRandomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
    }

