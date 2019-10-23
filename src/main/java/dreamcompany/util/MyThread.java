package dreamcompany.util;

public class MyThread extends Thread {

    private long millisecondsDelay;

    private Runnable method;

    public MyThread(long millisecondsDelay, Runnable method) {

        this.millisecondsDelay = millisecondsDelay;
        this.method = method;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(millisecondsDelay);
        } catch (InterruptedException ignored) {

        }

        method.run();
    }
}
