package de.ikarion.xps.engine.test.threesons;

import java.util.Arrays;

public class Ages {
    
    private boolean correct = false;
    private final Age a, b, c;

    public Ages(Age a, Age b, Age c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public int getProduct() {
        return a.getValue() * b.getValue() * c.getValue();
    }

    public int getSum() {
        return a.getValue() + b.getValue() + c.getValue();
    }

    public boolean contains(Age[] ages) {        
        for (Age age : ages) {
            if (!this.contains(age)) return false;
        }
        return true;
    }

    private boolean contains(Age age) {        
        return a == age || b == age || c == age;
    }

    public Age[] getAges() {
        Age[] result = new Age[] {a, b, c};
        Arrays.sort(result);
        return result;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean value) {
        this.correct = value;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        sb.append(this.getAges()[0]);
        sb.append(", ");
        sb.append(this.getAges()[1]);
        sb.append(", ");
        sb.append(this.getAges()[2]);
        sb.append("], (product: ");
        sb.append(getProduct());
        sb.append(", sum: ");
        sb.append(getSum());
        sb.append(")");
        return sb.toString();
    }
}
