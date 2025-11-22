import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class PublicKeyCryptoLab5 {

    private static final SecureRandom random = new SecureRandom();



    // Rough equivalent of generate_prime(bits) from Python
    private static BigInteger generatePrime(int bits) {
        return BigInteger.probablePrime(bits, random);
    }

    // ASCII/UTF-8 -> decimal (similar idea to Python version)
    private static BigInteger messageToDecimal(String message) {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        return new BigInteger(1, bytes); // positive BigInteger
    }

    // decimal -> String (UTF-8), similar to decimal_to_message
    private static String decimalToMessage(BigInteger number) {
        byte[] bytes = number.toByteArray();
        // BigInteger may prepend a 0 byte for sign - remove it
        if (bytes.length > 1 && bytes[0] == 0) {
            byte[] trimmed = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, trimmed, 0, trimmed.length);
            bytes = trimmed;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    // Random BigInteger in [minInclusive, maxExclusive)
    private static BigInteger randomBigInteger(BigInteger minInclusive, BigInteger maxExclusive) {
        BigInteger range = maxExclusive.subtract(minInclusive);
        BigInteger r;
        do {
            r = new BigInteger(range.bitLength(), random);
        } while (r.compareTo(range) >= 0);
        return r.add(minInclusive);
    }


    private static void demoDiffieHellman() {
        System.out.println("========== Diffie–Hellman ==========");

        BigInteger p = new BigInteger(
                "32317006071311007300153513477825163362488057133489075174588434139269806834136210002792056362640164685458556357935330816928829023080573472625273554742461245741026202527916572972862706300325263428213145766931414223654220941111348629991657478268034230553086349050635557712219187890332729569696129743856241741236237225197346402691855797767976823014625397933058015226858730761197532436467475855460715043896844940366130497697812854295958659597567051283852132784468522925504568272879113720098931873959143374175837826000278034973198552060607533234122603254684088120031105907484281003994966956119696956248629032338072839127039"
        );
        BigInteger a = BigInteger.valueOf(2);

        // Private keys for Alice and Bob
        BigInteger prAlice = randomBigInteger(BigInteger.ONE, p);
        BigInteger prBob   = randomBigInteger(BigInteger.ONE, p);

        System.out.println("Private key (Alice): " + prAlice);
        System.out.println("Private key (Bob)  : " + prBob);
        System.out.println();

        // Public keys
        BigInteger pubAlice = a.modPow(prAlice, p);
        BigInteger pubBob   = a.modPow(prBob, p);

        System.out.println("Public key (Alice): " + pubAlice);
        System.out.println("Public key (Bob)  : " + pubBob);
        System.out.println();

        // Shared secret
        BigInteger commonSecretAlice = pubBob.modPow(prAlice, p);
        BigInteger commonSecretBob   = pubAlice.modPow(prBob, p);

        System.out.println("Shared secret (Alice): " + commonSecretAlice);
        System.out.println("Shared secret (Bob)  : " + commonSecretBob);
        System.out.println("Keys identical: " + commonSecretAlice.equals(commonSecretBob));
        System.out.println("Bit length   : " + commonSecretAlice.bitLength());
        System.out.println();
    }


    private static void demoRSA() {
        System.out.println("========== RSA ==========");

        String message = "Bujor Alexandru";
        BigInteger m = messageToDecimal(message);
        System.out.println("Original message      : " + message);
        System.out.println("Message as decimal (m): " + m);
        System.out.println();

        // For the lab they used 16-bit primes; real life: 1024+ bits
        BigInteger p = generatePrime(16);
        BigInteger q = generatePrime(16);
        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger e = BigInteger.valueOf(65537); // public exponent
        BigInteger d = e.modInverse(phi);         // private exponent

        System.out.println("p  : " + p);
        System.out.println("q  : " + q);
        System.out.println("n  : " + n);
        System.out.println("phi: " + phi);
        System.out.println("e  : " + e);
        System.out.println("d  : " + d);
        System.out.println();

        // Encrypt
        BigInteger c = m.modPow(e, n);
        System.out.println("Encrypted (c = m^e mod n): " + c);

        // Decrypt
        BigInteger mDecrypted = c.modPow(d, n);
        String decryptedMessage = decimalToMessage(mDecrypted);

        System.out.println("Decrypted decimal (m'): " + mDecrypted);
        System.out.println("Decrypted message     : " + decryptedMessage);
        System.out.println();
    }


    private static void demoElGamal() {
        System.out.println("========== ElGamal ==========");

        String message = "Bujor Alexandru";
        BigInteger m = messageToDecimal(message);
        System.out.println("Original message      : " + message);
        System.out.println("Message as decimal (m): " + m);
        System.out.println();

        // Parameters p, g
        BigInteger p = new BigInteger(
                "32317006071311007300153513477825163362488057133489075174588434139269806834136210002792056362640164685458556357935330816928829023080573472625273554742461245741026202527916572972862706300325263428213145766931414223654220941111348629991657478268034230553086349050635557712219187890332729569696129743856241741236237225197346402691855797767976823014625397933058015226858730761197532436467475855460715043896844940366130497697812854295958659597567051283852132784468522925504568272879113720098931873959143374175837826000278034973198552060607533234122603254684088120031105907484281003994966956119696956248629032338072839127039"
        );
        BigInteger g = BigInteger.valueOf(2);

        // Private/public key: x, y = g^x mod p
        BigInteger x = randomBigInteger(BigInteger.ONE, p.subtract(BigInteger.ONE)); // 1..p-2
        BigInteger y = g.modPow(x, p);

        System.out.println("Private key x: " + x);
        System.out.println("Public key y : " + y);
        System.out.println();

        // Encryption: pick random k, compute c1, c2
        BigInteger k = randomBigInteger(BigInteger.ONE, p.subtract(BigInteger.ONE));
        BigInteger c1 = g.modPow(k, p);
        BigInteger c2 = m.multiply(y.modPow(k, p)).mod(p);

        System.out.println("Random k : " + k);
        System.out.println("Cipher c1: " + c1);
        System.out.println("Cipher c2: " + c2);
        System.out.println("Ciphertext (c1, c2)");
        System.out.println();

        // Decryption: m' = c2 * (c1^x)^(-1) mod p
        BigInteger s = c1.modPow(x, p);         // shared secret
        BigInteger sInv = s.modInverse(p);      // inverse of s modulo p
        BigInteger mDecrypted = c2.multiply(sInv).mod(p);
        String decryptedMessage = decimalToMessage(mDecrypted);

        System.out.println("Decrypted decimal (m'): " + mDecrypted);
        System.out.println("Decrypted message     : " + decryptedMessage);
        System.out.println();
    }


    public static void main(String[] args) {
        demoDiffieHellman();
        demoRSA();
        demoElGamal();
    }
}
