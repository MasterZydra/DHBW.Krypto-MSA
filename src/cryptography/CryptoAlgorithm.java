package cryptography;

public enum CryptoAlgorithm {
    Shift,
    RSA;

    public static CryptoAlgorithm valueOfCaseIgnore(String algoName) {

        for (CryptoAlgorithm algo : CryptoAlgorithm.values()
                ) {
            if (algo.toString().equalsIgnoreCase(algoName)) {
                return algo;
            }
        }
        return null;
    }
}

