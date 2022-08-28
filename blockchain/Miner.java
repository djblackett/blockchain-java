package blockchain;

import java.util.concurrent.atomic.AtomicInteger;

public class Miner implements Runnable {
    private static AtomicInteger ID = new AtomicInteger(0);
    private String startingHash;
    private int id;
    private int numberOfZeroes;
    private String name;

    boolean isMiningSuccessful = false;

    public Miner() {

        this.id = ID.getAndIncrement();
        this.numberOfZeroes = numberOfZeroes;
        this.name = "Miner # " + id;

    }


//
//    public String mineBlock(int prefix) {
//        String prefixString = new String(new char[prefix]).replace('\0', '0');
//        while (!hash.substring(0, prefix).equals(prefixString)) {
//            nonce++;
//            hash = calculateBlockHash();
//        }
//        return hash;
//    }

    @Override
    public void run() {


        BlockChain blockChain = BlockChain.getBlockChain();

        while (!isMiningSuccessful) {
            try {
                isMiningSuccessful = blockChain.addBlock(blockChain.getLastNodeHash(), name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
