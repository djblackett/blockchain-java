package blockchain;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {


    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, NoSuchProviderException {



        BlockChain blockChain = BlockChain.getBlockChain();

        try {
            blockChain.initializeKeys();
        } catch(NoSuchAlgorithmException | NoSuchProviderException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 50, 120, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

        for (int i = 0; i < 50; i++) {
//            blockChain.addBlock(blockChain.getLastNodeHash(), input);
            executor.submit(new Miner());
        }


//
//        while (blockChain.blockChain.size() < 15) {
//            Thread.sleep(500);
////            System.out.println("Waiting...");
//        }
//
//        for (int i = 0; i < 15; i++) {
//            blockChain.blockChain.get(i).printBlock();
//        }

//        System.exit(0);
    }


}
