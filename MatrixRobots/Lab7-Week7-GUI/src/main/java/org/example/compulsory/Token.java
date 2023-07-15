package org.example.compulsory;

public class Token {
    private final int number ;

    public Token(int number) {
        this.number = number;
    }

    public int getNumber(){
        return this.number;
    }

    @Override
    public String toString() {
        return "TOKEN(" + this.number + ");";
    }
}
