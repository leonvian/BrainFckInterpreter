package lvc.interpreter;

import java.util.ArrayList;

public class Interpreter {

    byte[] data = new byte[3000];
    ArrayList<Token> instructionList;

    int dataIndex = 0;

    int executionIndex = 0;
    int startLoopExecutionIndex = -1;
    int endLoopExecutionIndex = -1;

    //+++++[>+++++++++++++<-]>.+++++[>+++++++++++++<-]>-.
    public void execute(String code) {
        instructionList =  Tokenizer.createTokens(code);
        int size = instructionList.size();
        while (executionIndex < size) {
            Token token  = instructionList.get(executionIndex);
            switch (token) {
                case PRINT -> {
                    printAsChar();
                }
                case INCREMENT -> {
                    increment();
                }
                case DECREMENT -> {
                    decrement();
                }
                case MOVE_POINT_LEFT -> {
                    moveLeft();
                }

                case MOVE_POINT_RIGHT -> {
                    moveRight();
                }

                case START_LOOP -> {
                    startLoop();
                }

                case END_LOOP -> {
                    endLoop();
                }
            }
            executionIndex ++;
        }
    }

    private void increment() {
        data[dataIndex] += 1;
    }

    private void decrement() {
        data[dataIndex] -= 1;
    }

    private void moveRight() {
        dataIndex++;
    }

    private void moveLeft() {
        dataIndex--;
    }

    private void startLoop() {
        if (startLoopExecutionIndex == -1) {
            startLoopExecutionIndex = executionIndex;
        }

        if (endLoopExecutionIndex == -1) {
            for (int i = executionIndex; i < instructionList.size(); i ++) {
                if (instructionList.get(i) == Token.END_LOOP) {
                    endLoopExecutionIndex = i;
                    break;
                }
            }
        }

        if (data[dataIndex] == 0) {
            executionIndex = endLoopExecutionIndex;
            endLoopExecutionIndex = -1;
            startLoopExecutionIndex = -1;
        }
    }

    private void endLoop() {
        if (endLoopExecutionIndex != -1) {
            executionIndex = startLoopExecutionIndex -1;
        }
    }


    private void printDataPointer() {
        System.out.print(data[dataIndex]);
    }


    private void printAsChar() {
        System.out.print((char)data[dataIndex]);
    }


}
