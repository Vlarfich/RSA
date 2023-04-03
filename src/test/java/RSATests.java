import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class RSATests {

    private static final Logger LOGGER = LogManager.getLogger(RSATests.class);

    @Test
    public void testRSA1_first() {
        BigInteger plainText = RSA.largePrime(20);
        LOGGER.info("Plain text: " + plainText);
        RSA rsa = new RSA();
        BigInteger e = rsa.getE();
        BigInteger d = rsa.getD();
        BigInteger N = rsa.getN();

        LOGGER.info("N: " + N + "\ne: " + e +"\nd: " + d);

        BigInteger cypherText = RSA.encrypt(plainText, e, N);

        String cypher = RSA.bigIntegerToBytesString(cypherText);
        LOGGER.info("Cypher text: " + cypher);

        BigInteger decr = RSA.decrypt(cypherText, d, N);

        String decrText = RSA.bigIntegerToBytesString(decr);
        LOGGER.info("Decr text: " + decrText);

        Assert.assertEquals(plainText, decrText);
    }
}
