package blockchain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Block {

    public String getMiner() {
        return miner;
    }

    public void setMiner(String miner) {
        this.miner = miner;
    }

    private String miner;
    private int ID;

    private long coinBalance;
    byte[] signature;
    private String hash;
    private String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;
    private Random random = new Random();
    private int N;

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    private long creationTime;

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    private long magicNumber;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.data = "no messages";
        this.hash = calculateBlockHash();

        this.magicNumber = random.nextInt();
    }
    // standard getters and setters

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String calculateBlockHash() {
        String dataToHash = previousHash
                + timeStamp
                + nonce
                + data;
        MessageDigest digest = null;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes(UTF_8));
        } catch (NoSuchAlgorithmException ex) {
//            logger.log(Level.SEVERE, ex.getMessage());
        }
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public void generateNewMagicNumber() {
        this.magicNumber = random.nextInt();
    }

    @Override
    public String toString() {
        return "Block{" +
                "ID=" + ID +
                ", hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", data='" + data + '\'' +
                ", timeStamp=" + timeStamp +
                ", nonce=" + nonce +
                ", random=" + random +
                ", creationTime=" + creationTime +
                ", magicNumber=" + magicNumber +
                '}';
    }

    public void printBlock() {
        BlockChain blockChain = BlockChain.getBlockChain();
        long[] prevArray = blockChain.getPreviousCreationTimeAndN(this);

        int prevN = (int) prevArray[1];

        int prevCreationTime = (int) prevArray[0];

        System.out.println("Block: ");
        System.out.println("Created by: " + miner);
        System.out.println(miner + " gets 100 VC");
        System.out.println("Id: " + ID);
        System.out.println("Timestamp: " + timeStamp);
        System.out.println("Magic number: " + magicNumber);
        System.out.println("Hash of the previous block: \n" + getPreviousHash());
        System.out.println("Hash of the block: \n" + getHash());
        System.out.println("Block data: " + data);
        System.out.println("Block was generating for " + creationTime + " seconds");

        // figure out N stuff
        if (prevCreationTime < 10) {

                System.out.println("N was increased to " + (N + 1));
        }

        else if (prevCreationTime > 60) {
            System.out.println("N was increased to " + (N - 1));
        }  else {
                System.out.println("N stayed the same");
        }


        System.out.println();
    }
}
