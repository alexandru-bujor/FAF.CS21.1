import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class TestHashSign {

    public static void main(String[] args) {
        String msg = "Alexandru BUJOR";

        BigInteger decimalStr = HashUtil.messageToDecimal(msg);

        BigInteger hashDec = HashUtil.hashDecimal(decimalStr);
        String hashHex = HashUtil.sha256Hex(decimalStr.toString().getBytes(StandardCharsets.UTF_8));

        System.out.println("Initial message:\n" + decimalStr + " -> " + hashHex + " ~ " + hashDec);

        int cipher = 0;  // 1 = RSA, 0 = ElGamal

        if (cipher == 1) {
            System.out.println("RSA:");

            RSA A = new RSA();
            RSA B = new RSA();

            BigInteger cryptogram = A.encrypt(msg, B.getPublicKey());
            RSASignedMessage signedCryptogram = A.sign(msg, cryptogram);

            System.out.println("Encrypted message:\n" + cryptogram);
            System.out.println("Signature:\n" + signedCryptogram.getSignature());

            // signature verification (signature ^ e mod n)
            BigInteger[] aPub = A.getPublicKey();
            BigInteger sigCheck = signedCryptogram.getSignature()
                    .modPow(aPub[1], aPub[0]);
            System.out.println("Signature verification (signature ^ e mod n) - VALID:\n" + sigCheck);

            boolean valid = B.verify(signedCryptogram, A.getPublicKey());
            System.out.println("Verification result - VALID: " + valid);
        } else {
            System.out.println("ElGamal:");

            ElGamal A = new ElGamal();
            ElGamal B = new ElGamal();

            ElGamalCiphertext cryptogram = A.encrypt(msg, B.getPublicKey());
            ElGamalSignedMessage signedCryptogram = A.sign(msg, cryptogram);

            System.out.println("Encrypted message:\n" + cryptogram);
            System.out.println("Signature:\n(" + signedCryptogram.getR() + ", " + signedCryptogram.getS() + ")");

            boolean valid = B.verify(signedCryptogram, A.getPublicKey());
            System.out.println("Signature verification - VALID: " + valid);
        }
    }
}


class HashUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static BigInteger messageToDecimal(String msg) {
        StringBuilder sb = new StringBuilder();
        for (char c : msg.toCharArray()) {
            sb.append((int) c);
        }
        return new BigInteger(sb.toString());
    }

    public static String sha256Hex(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(data);
            return toHex(out);
        } catch (NoSuchAlgorithmException e) {
            // Should never happen for SHA-256 in a normal JVM
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    public static BigInteger hashDecimal(BigInteger decimal) {
        String s = decimal.toString();
        String hex = sha256Hex(s.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (char c : hex.toCharArray()) {
            sb.append((int) c);   // ord(c) then concat as decimal
        }
        return new BigInteger(sb.toString());
    }

    public static SecureRandom getRandom() {
        return RANDOM;
    }

    public static BigInteger randomInRange(BigInteger min, BigInteger max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min > max");
        }
        BigInteger range = max.subtract(min).add(BigInteger.ONE); // (max - min + 1)
        BigInteger result;
        do {
            result = new BigInteger(range.bitLength(), RANDOM);
        } while (result.compareTo(range) >= 0);
        return result.add(min);
    }

    private static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}



class RSASignedMessage {
    private final BigInteger ciphertext;
    private final BigInteger signature;

    public RSASignedMessage(BigInteger ciphertext, BigInteger signature) {
        this.ciphertext = ciphertext;
        this.signature = signature;
    }

    public BigInteger getCiphertext() {
        return ciphertext;
    }

    public BigInteger getSignature() {
        return signature;
    }
}

class RSA {
    private static final int PRIME_BITS = 1536;
    private static final BigInteger TWO = BigInteger.valueOf(2);

    private final BigInteger n;
    private final BigInteger phiN;
    private final BigInteger e;
    private final BigInteger d;

    private final BigInteger[] publicKey; // (n, e)

    public RSA() {
        SecureRandom rnd = HashUtil.getRandom();
        BigInteger p1 = BigInteger.probablePrime(PRIME_BITS, rnd);
        BigInteger p2 = BigInteger.probablePrime(PRIME_BITS, rnd);

        this.n = p1.multiply(p2);
        this.phiN = p1.subtract(BigInteger.ONE).multiply(p2.subtract(BigInteger.ONE));

        this.e = generatePubExp();
        this.d = e.modInverse(phiN);

        this.publicKey = new BigInteger[]{n, e};
    }


    private BigInteger generatePubExp() {
        BigInteger e = BigInteger.valueOf(3);
        while (!e.gcd(phiN).equals(BigInteger.ONE) ||
                e.mod(TWO).equals(BigInteger.ZERO)) {
            e = e.add(BigInteger.ONE);
        }
        return e;
    }

    public BigInteger[] getPublicKey() {
        return publicKey;
    }

    public BigInteger getModulus() {
        return n;
    }

    public BigInteger getPrivateExponent() {
        return d;
    }


    public BigInteger encrypt(String msg, BigInteger[] receiverPublicKey) {
        BigInteger decimal = HashUtil.messageToDecimal(msg);
        BigInteger nR = receiverPublicKey[0];
        BigInteger eR = receiverPublicKey[1];
        return decimal.modPow(eR, nR);
    }


    public BigInteger decrypt(BigInteger ciphertext) {
        return ciphertext.modPow(d, n);
    }


    public RSASignedMessage sign(String msg, BigInteger transmission) {
        BigInteger decimal = HashUtil.messageToDecimal(msg);
        BigInteger hash = HashUtil.hashDecimal(decimal);
        BigInteger sig = hash.modPow(d, n);
        return new RSASignedMessage(transmission, sig);
    }


    public boolean verify(RSASignedMessage transmission, BigInteger[] senderPublicKey) {
        BigInteger x = decrypt(transmission.getCiphertext());
        BigInteger s = transmission.getSignature()
                .modPow(senderPublicKey[1], senderPublicKey[0]);

        BigInteger hashDec = HashUtil.hashDecimal(x);
        return hashDec.equals(s);
    }
}



class ElGamalCiphertext {
    private final BigInteger kE;
    private final BigInteger y;

    public ElGamalCiphertext(BigInteger kE, BigInteger y) {
        this.kE = kE;
        this.y = y;
    }

    public BigInteger getkE() {
        return kE;
    }

    public BigInteger getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + kE + ", " + y + ")";
    }
}

class ElGamalSignedMessage {
    private final ElGamalCiphertext ciphertext;
    private final BigInteger r;
    private final BigInteger s;

    public ElGamalSignedMessage(ElGamalCiphertext ciphertext, BigInteger r, BigInteger s) {
        this.ciphertext = ciphertext;
        this.r = r;
        this.s = s;
    }

    public ElGamalCiphertext getCiphertext() {
        return ciphertext;
    }

    public BigInteger getR() {
        return r;
    }

    public BigInteger getS() {
        return s;
    }
}

class ElGamal {
    private static final int PRIME_BITS = 3072;
    private static final BigInteger TWO = BigInteger.valueOf(2);

    private final BigInteger p;
    private final BigInteger a;
    private final BigInteger z; // private exponent
    private final BigInteger b; // a^z mod p

    private final BigInteger[] publicKey; // (p, a, b)

    public ElGamal() {
        SecureRandom rnd = HashUtil.getRandom();
        this.p = BigInteger.probablePrime(PRIME_BITS, rnd);
        this.a = TWO;
        this.z = HashUtil.randomInRange(TWO, p.subtract(TWO));
        this.b = a.modPow(z, p);

        this.publicKey = new BigInteger[]{p, a, b};
    }

    public BigInteger[] getPublicKey() {
        return publicKey;
    }


    public ElGamalCiphertext encrypt(String msg, BigInteger[] receiverPublicKey) {
        BigInteger decimal = HashUtil.messageToDecimal(msg);
        BigInteger pR = receiverPublicKey[0];
        BigInteger aR = receiverPublicKey[1];
        BigInteger bR = receiverPublicKey[2];

        BigInteger i = HashUtil.randomInRange(TWO, pR.subtract(TWO));
        BigInteger kE = aR.modPow(i, pR);
        BigInteger kM = bR.modPow(i, pR);

        BigInteger y = decimal.multiply(kM).mod(pR);
        return new ElGamalCiphertext(kE, y);
    }


    public BigInteger decrypt(ElGamalSignedMessage transmission) {
        ElGamalCiphertext cipher = transmission.getCiphertext();
        BigInteger pSelf = this.publicKey[0];

        BigInteger kM = cipher.getkE().modPow(z, pSelf);
        BigInteger invKM = kM.modInverse(pSelf);

        return cipher.getY().multiply(invKM).mod(pSelf);
    }


    private BigInteger generateK() {
        BigInteger pMinus1 = p.subtract(BigInteger.ONE);
        BigInteger k;
        do {
            k = HashUtil.randomInRange(TWO, pMinus1);
        } while (!k.gcd(pMinus1).equals(BigInteger.ONE));
        return k;
    }


    public ElGamalSignedMessage sign(String msg, ElGamalCiphertext transmission) {
        BigInteger decimal = HashUtil.messageToDecimal(msg);

        BigInteger k = generateK();
        BigInteger r = a.modPow(k, p);

        BigInteger hashDec = HashUtil.hashDecimal(decimal);

        BigInteger pMinus1 = p.subtract(BigInteger.ONE);
        BigInteger invK = k.modInverse(pMinus1);
        BigInteger s = invK.multiply(hashDec.subtract(z.multiply(r))).mod(pMinus1);

        return new ElGamalSignedMessage(transmission, r, s);
    }


    public boolean verify(ElGamalSignedMessage transmission, BigInteger[] senderPublicKey) {
        BigInteger x = decrypt(transmission);
        BigInteger hashDec = HashUtil.hashDecimal(x);

        BigInteger pS = senderPublicKey[0];
        BigInteger aS = senderPublicKey[1];
        BigInteger bS = senderPublicKey[2];

        BigInteger p1 = bS.modPow(transmission.getR(), pS);
        BigInteger p2 = transmission.getR().modPow(transmission.getS(), pS);
        BigInteger v1 = p1.multiply(p2).mod(pS);

        BigInteger v2 = aS.modPow(hashDec, pS);

        System.out.println("v1 = v2 =\n" + v2);

        return v1.mod(pS).equals(v2.mod(pS));
    }
}
