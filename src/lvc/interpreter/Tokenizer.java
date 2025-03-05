package lvc.interpreter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/*
<: Decrement the data pointer by one, moving it to the left
+: Increment the byte at the data pointer by one
- Decrement the byte at the data pointer by one
>: Move the data pointer to the right
. Output the byte at the data pointer
[: If the byte at the data pointer is zero, jump to the command after the matching ] instead of moving to the next command
 */
public class Tokenizer {

    public static ArrayList<Token> createTokens(String code) {
        ArrayList<Token> queue = new ArrayList<>();
        char[] array = code.toCharArray();
        for (int i = 0; i < array.length; i ++) {
            if (array[i] == '+') {
                queue.add(Token.INCREMENT);
            } else if (array[i] == '-') {
                queue.add(Token.DECREMENT);
            } else if (array[i] == '>') {
                queue.add(Token.MOVE_POINT_RIGHT);
            } else if (array[i] == '<') {
                queue.add(Token.MOVE_POINT_LEFT);
            } else if (array[i] == '.') {
                queue.add(Token.PRINT);
            } else if (array[i] == '[') {
                queue.add(Token.START_LOOP);
            } else if (array[i] == ']') {
                queue.add(Token.END_LOOP);
            }
        }

        return queue;
    }

}
