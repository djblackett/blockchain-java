package blockchain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockChain {

    private PrivateKey privateKey;
    public PublicKey publicKey;

    private String keyFile = "KeyPair/privateKey";
    public static AtomicInteger ID = new AtomicInteger(1);

    private AtomicInteger N = new AtomicInteger(0);
    private static BlockChain instance = new BlockChain();

    List<Block> blockChain = new LinkedList<>();

    List<String> messages = new ArrayList<>();

    private BlockChain() {
    }

    public static BlockChain getBlockChain() {
        return instance;
    }

    public void verify(String hash) {

    }

    public void addMessage(String message) {
        synchronized (this) {
            this.messages.add(message);
        }
    }

    public void clearMessages() {
        this.messages.clear();
    }

    public boolean addBlock(String startingHash, String minerName) throws Exception {


        Block newBlock = new Block(getLastNodeHash());
        String numberOfZeroes = "0".repeat(N.get());


        long startTime = System.currentTimeMillis();

        String hash = StringUtil.applySha256(newBlock.toString());
//        System.out.println(hash);

        while (!hash.startsWith(numberOfZeroes)) {
            newBlock.generateNewMagicNumber();
            hash = StringUtil.applySha256(newBlock.toString());
//            System.out.println(hash);
        }
        newBlock.setHash(hash);

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        long timeInSeconds = elapsedTime / 1000;
        newBlock.setCreationTime(timeInSeconds);
        newBlock.setTimeStamp(new Date().getTime());
        newBlock.setMiner(minerName);

        synchronized (this) {
            if (startingHash.equals(getLastNodeHash()) && hash.substring(0, N.get()).equals("0".repeat(N.get()))) {


                blockChain.add(newBlock);

//                newBlock.setData(String.join("\n", messages));
                String data = String.join("\n", messages);
                clearMessages();
                newBlock.setID(ID.getAndIncrement());
                newBlock.setN(this.N.get());

                Message message = new Message(data, keyFile);
                newBlock.setData(message.getDataWithSignature());

                long previousCreationTime = blockChain.get(blockChain.size() - 1).getCreationTime();
                if (previousCreationTime < 10) {
                    N.incrementAndGet();
                } else if (previousCreationTime > 60) {
                    N.decrementAndGet();
                }

                newBlock.printBlock();
                return true;
            }
        }
        return false;
    }

    public String getLastNodeHash() {
        if (!blockChain.isEmpty()) {
            return blockChain.get(blockChain.size() - 1).getHash();
        }
        return "0";
    }

    public long[] getPreviousCreationTimeAndN(Block block) {
        int prevIndex = blockChain.indexOf(block) - 1;

        try {
            Block prevBlock = blockChain.get(prevIndex);
            return new long[]{prevBlock.getCreationTime(), prevBlock.getN()};
        } catch (IndexOutOfBoundsException e) {
            return new long[]{0, 0};
        }
    }


    public void initializeKeys() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        GenerateKeys gk = new GenerateKeys(512);
        gk.createKeys();
        this.publicKey = gk.getPublicKey();
        this.privateKey = gk.getPrivateKey();
        try {
            gk.writeToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
            gk.writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }




}
