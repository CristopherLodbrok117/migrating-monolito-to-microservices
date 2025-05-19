package app.calendar_service.domain.classes;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TokenGenerator {
    private final byte digitLen = (byte) 61;
    private final byte[] digits = new byte[digitLen];
    private final int targetStringLength = 50;
    private final Random random = new Random();

    public TokenGenerator(){
        //ASCII 0 - 9
        setDigits(0, 10, (byte) 48);

        //ASCII A - Z
        setDigits(10, 36, (byte) 65);

        //ASCII a - z
        setDigits(35, digitLen, (byte) 97);
    }

    private void setDigits(int index, int limit, byte asciiPointer){
        while(index < limit){
            digits[index] = asciiPointer;
            asciiPointer++;
            index++;
        }
    }

    private String generate(int start){
        StringBuilder buffer = new StringBuilder(targetStringLength);

        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = digits[random.nextInt(start,digitLen)];
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }

    public String generateAlphabetic(){
        return generate(10);
    }

    public String generateAlphaNumeric(){
        return generate(0);
    }
}
