import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Key length:");
        int len = scanner.nextInt();

        RSA(len);
    }

    public static void RSA(int keyLength) {
        BigInteger p;
        BigInteger q;
        BigInteger n;
        BigInteger t;
        BigInteger e;
        BigInteger d;

        SecureRandom randomGenerator = new SecureRandom();

        p = new BigInteger(keyLength, 100, randomGenerator).nextProbablePrime();
        q = new BigInteger(keyLength, 100, randomGenerator).nextProbablePrime();

        n = p.multiply(q);
        t = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        e = getCoPrime(t);
        d = e.modInverse(t);

        System.out.println();
        System.out.println("n: " + n.toString());
        System.out.println("e: " + e.toString());
        System.out.println("d: " + d.toString());
    }

    public static BigInteger getCoPrime(BigInteger n) {
        BigInteger result = new BigInteger(n.toString());
        BigInteger one = new BigInteger("1");
        BigInteger two = new BigInteger("2");
        result = result.subtract(two);

        while (result.intValue() > 1) {
            if (result.gcd(n).equals(one))
                break;
            result = result.subtract(one);
        }

        return result;
    }
}