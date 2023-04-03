import java.math.BigInteger;

public class Montgomery {
    public static final int BASE = 2;

    BigInteger m;
    BigInteger rrm;
    int n;

    public Montgomery(BigInteger m) {
        if (m.compareTo(BigInteger.ZERO) <= 0 || !m.testBit(0)) {
            throw new IllegalArgumentException();
        }
        this.m = m;
        this.n = m.bitLength();
        this.rrm = BigInteger.ONE.shiftLeft(n * 2).mod(m);
    }

    public BigInteger reduce(BigInteger t) {
        BigInteger a = t;
        for (int i = 0; i < n; i++) {
            if (a.testBit(0)) a = a.add(this.m);
            a = a.shiftRight(1);
        }
        if (a.compareTo(m) >= 0) a = a.subtract(this.m);
        return a;
    }

    public BigInteger pow(BigInteger x1, BigInteger x2) {
        BigInteger prod = reduce(rrm);
        BigInteger base = reduce(x1.multiply(rrm));
        BigInteger exp = x2;
        while (exp.bitLength()>0) {
            if (exp.testBit(0))
                prod = reduce(prod.multiply(base));
            exp = exp.shiftRight(1);
            base = reduce(base.multiply(base));
        }
        return reduce(prod);
    }

    public static BigInteger pow(BigInteger x1, BigInteger x2, BigInteger m) {
        return new Montgomery(m).pow(x1, x2);
    }
}