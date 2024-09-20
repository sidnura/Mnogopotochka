class DivisibleByTenThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i <= 100; i++) {
            if (i % 10 == 0) {
                System.out.println("Thread: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class DivisibleByTenRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i <= 100; i++) {
            if (i % 10 == 0) {
                System.out.println("Runnable: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class TimerThread extends Thread {
    private int seconds = 0;

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // Пауза на 1 секунду
                seconds++;
                System.out.println("Прошло секунд: " + seconds);

                // Уведомление потоков, выводящих сообщения каждые 5 и 7 секунд
                synchronized (this) {
                    notifyAll();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getSeconds() {
        return seconds;
    }
}

class FiveSecondMessageThread extends Thread {
    private final TimerThread timerThread;

    public FiveSecondMessageThread(TimerThread timerThread) {
        this.timerThread = timerThread;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (timerThread) {
                try {
                    // Ожидаем уведомления от TimerThread
                    timerThread.wait();

                    if (timerThread.getSeconds() % 5 == 0) {
                        System.out.println("Прошло 5 секунд");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class SevenSecondMessageThread extends Thread {
    private final TimerThread timerThread;

    public SevenSecondMessageThread(TimerThread timerThread) {
        this.timerThread = timerThread;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (timerThread) {
                try {
                    timerThread.wait();

                    if (timerThread.getSeconds() % 7 == 0) {
                        System.out.println("Прошло 7 секунд");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        DivisibleByTenThread thread1 = new DivisibleByTenThread();
        thread1.start();

        Thread thread2 = new Thread(new DivisibleByTenRunnable());
        thread2.start();

        TimerThread timerThread = new TimerThread();
        FiveSecondMessageThread fiveSecondThread = new FiveSecondMessageThread(timerThread);
        SevenSecondMessageThread sevenSecondThread = new SevenSecondMessageThread(timerThread);

        timerThread.start();
        fiveSecondThread.start();
        sevenSecondThread.start();
    }
}
