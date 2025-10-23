public class Main {
    public static void main(String[] args) throws InterruptedException {
        Elevator elevator = new Elevator(1, 10);

        Thread elevatorThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    elevator.simulate();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        elevatorThread.start();

        elevator.call(3);
        elevator.call(5);
        elevator.call(2);

        Thread.sleep(2000);

        elevator.call(8);
        elevator.call(7);

        Thread.sleep(3000);

        elevator.select(9);
        elevator.select(4);

        Thread.sleep(2000);

        elevator.call(10);
        elevator.call(6);
        elevator.call(1);

        Thread.sleep(4000);

        elevator.call(3);
        elevator.select(9);
        elevator.call(2);

        Thread.sleep(4000);

        elevatorThread.interrupt();
        elevatorThread.join();

        System.out.println("\n=== TEST COMPLETE ===");
    }
}