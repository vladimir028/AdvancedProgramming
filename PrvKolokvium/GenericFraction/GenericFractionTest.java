package PrvKolokvium.GenericFraction;

import java.util.Scanner;

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

// вашиот код овде
class GenericFraction <T extends Number, U extends Number>{
    private T numerator;
    private U denominator;

    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        if (denominator.doubleValue() == 0){
            throw new ZeroDenominatorException("Denominator cannot be zero");
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @SuppressWarnings("unchecked")
    public GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        Double newNum = this.numerator.doubleValue() * gf.denominator.doubleValue();
        Double newDem = gf.numerator.doubleValue() * this.denominator.doubleValue();

        return new GenericFraction<>(newNum+newDem, gf.denominator.doubleValue()*this.denominator.doubleValue());
    }

    public double NZD(double a, double b){
        if(a == 0){
            return b;
        }

        return NZD(b%a, a);
    }

    public double toDouble(){
        return numerator.doubleValue() / denominator.doubleValue();
    }

    @Override
    public String toString() {
//        transform();
        double simplify = NZD(numerator.doubleValue(), denominator.doubleValue());
        return String.format("%.2f / %.2f", numerator.doubleValue()/simplify, denominator.doubleValue()/simplify);
    }
}
class ZeroDenominatorException extends Exception{

    public ZeroDenominatorException(String s) {
        super(s);
    }
}