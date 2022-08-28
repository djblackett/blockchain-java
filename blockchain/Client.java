package blockchain;

public class Client implements Runnable {
    private String name;

    public void writeMessage(String message) {
        String fullMessage = this.name + ": " + message;
        BlockChain.getBlockChain().addMessage(message);
    }

    @Override
    public void run() {



    }
}
