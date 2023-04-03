import java.math.BigInteger;
import java.util.Random;

public class RSA {
    //_______________________ Default _______________________\\
    private BigInteger p;
    private BigInteger q;
    private BigInteger N;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;

    private int l = 2048;

    private Random r;

    public RSA() {
        this(2048);
    }

    public RSA(int length) {
        l = length;
        BigInteger[] info = Gen(l);
        e = info[0];
        N = info[1];
        d = info[2];
    }

    public BigInteger getN() {
        return N;
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getD() {
        return d;
    }

    //_______________________ Default _______________________\\
    //_______________________         _______________________\\
    //_______________________   Gen   _______________________\\
    public BigInteger[] Gen(int l) {
        p = largePrime(this.l / 2);
        do {
            q = largePrime(this.l / 2);
        } while (!p.equals(q));
        N = p.multiply(q);
        phi = getPhi(p, q);
        e = genE(phi);
        d = extEuclid(e, phi) [1];
        return new BigInteger[] {e, N, d};
    }

    public static BigInteger getPhi(BigInteger p, BigInteger q) {
        return (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
    }

    public static BigInteger genE(BigInteger phi) {
        Random rand = new Random();
        BigInteger e = new BigInteger(1024, rand);
        do {
            e = new BigInteger(1024, rand);
            while (e.min(phi).equals(phi)) { // while phi is smaller than e, look for a new e
                e = new BigInteger(1024, rand);
            }
        } while (!gcd(e, phi).equals(BigInteger.ONE)); // if gcd(e,phi) isnt 1 then stay in loop
        return e;
    }

    public static BigInteger largePrime(int bits) {
        Random randomInteger = new Random();
        return BigInteger.probablePrime(bits, randomInteger);
    }
    //_______________________   Gen   _______________________\\
    //_______________________         _______________________\\
    //_______________________   Encr  _______________________\\
    public static BigInteger encrypt(BigInteger message, BigInteger e, BigInteger n) {
        return message.modPow(e, n);
    }

    public static BigInteger encrypt(String message, BigInteger e, BigInteger n) {
        return stringToBigInteger(message).modPow(e, n);
    }

    public byte[] encrypt(byte[] message)
    {
        return (new BigInteger(message)).modPow(e, N).toByteArray();
    }
    //_______________________   Encr  _______________________\\
    //_______________________         _______________________\\
    //_______________________   Decr  _______________________\\
    public static BigInteger decrypt(BigInteger message, BigInteger d, BigInteger n) {
        return message.modPow(d, n);
    }

    public static BigInteger decrypt(String message, BigInteger d, BigInteger n) {
        return stringToBigInteger(message).modPow(d, n);
    }

    public byte[] decrypt(byte[] message) {
        return (new BigInteger(message)).modPow(d, N).toByteArray();
    }
    //_______________________   Decr  _______________________\\
    //_______________________         _______________________\\
    //_______________________   Utils  _______________________\\
    public static BigInteger stringToBigInteger(String str) {
        byte[] bytes = stringToBytes(str);
        return new BigInteger(bytes);
    }

    public static byte[] stringToBytes(String str) {
        return str.getBytes();
    }

    public static String bytesToString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for(Byte b : bytes) {
            stringBuilder.append(b.toString());
        }
        return stringBuilder.toString();
    }

    public static String bigIntegerToBytesString(BigInteger a) {
        return bytesToString(a.toByteArray());
    }

    public static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return a;
        } else {
            return gcd(b, a.mod(b));
        }
    }

    // Extended Euclid Algorithm
    /**
     * ax + by = d = gcd(a, b)
     * p = x,
     * q = y.
     * @param a first number
     * @param b second number
     * @return [d, p, q]
     */
    public static BigInteger[] extEuclid(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) return new BigInteger[] {
                a, BigInteger.ONE, BigInteger.ZERO
        }; // { a, 1, 0 }
        BigInteger[] vals = extEuclid(b, a.mod(b));
        BigInteger d = vals[0];
        BigInteger p = vals[2];
        BigInteger q = vals[1].subtract(a.divide(b).multiply(vals[2]));
        q.isProbablePrime(1);
        return new BigInteger[] {
                d, p, q
        };
    }

    //_______________________   Utils  _______________________\\

}
