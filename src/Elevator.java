import error.InvalidFloorsException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class Elevator implements Runnable {

    private int currentFloor;
    private final int MIN_FLOOR;
    private final int MAX_FLOOR;
    private ArrayList<Integer> orders;
    private Direction direction;
    private boolean running = true;

    public Elevator(int min, int max) {
        if (min >= max) {
            throw new InvalidFloorsException("Min floor must be less than max floor!");
        }
        MIN_FLOOR = min;
        MAX_FLOOR = max;
        currentFloor = min;
        direction = Direction.UP;
        orders = new ArrayList<>();
    }

    private void insertInQueue(int item) {
        boolean inserted = false;
                for (int i = 0; i < orders.size(); i++) {
            if (direction == Direction.UP) {
                if ( ( i-1 < 0 || orders.get(i - 1) < item) && orders.get(i) > item) {
                    orders.add(i, item);
                    inserted = true;
                    break;
                }
            } else {
                if ((i - 1 < 0 || orders.get(i - 1) > item) && orders.get(i) < item) {
                    orders.add(i, item);
                    inserted = true;
                    break;
                }
            }
        }
                if (!inserted) {
            orders.add(item);
        }
    }


    //Call elevator to current floor, planning to go up or down.
    public synchronized void call(int from) {
        if (from == currentFloor) {
            System.out.println("Already at current floor!");
            return;
        }
        if (from < MIN_FLOOR || from > MAX_FLOOR) {
            System.out.println("Floor out of bounds!");
            return;
        }

        if (!orders.isEmpty() && getDirection(from) == direction && !orders.contains(from)) {
            insertInQueue(from);
        } else {
            orders.add(from);
        }
    }

    private Direction getDirection(int pos) {
        Direction dir = Direction.DOWN;
        if (pos - currentFloor > 0) {
            direction = Direction.UP;
        }
        return dir;
    }

    private void setDirection(int pos) {
        if (pos - currentFloor > 0) {
            direction = Direction.UP;
        } else {
            direction = Direction.DOWN;
        }
    }

    //Inside elevator, select which floor to go to.

    public synchronized void select(int to) {
        if (to == currentFloor) {
            System.out.println("Cannot select current floor!");
            return;
        }
        if (to < MIN_FLOOR || to > MAX_FLOOR) {
            System.out.println("Floor out of bounds!");
            return;
        }
        if (!orders.contains(to)) {
            insertInQueue(to);
        }
    }

    //Simulate one time step of elevator (go to floor or go to bottom floor). Ideally this function would always happen in a background process.
    //Could run simulate on another thread (need to make sure edits are synchronized though).
    public synchronized void simulate() {
        if (!orders.isEmpty()) {
            int nextFloor = orders.removeFirst();
            setDirection(nextFloor);
            currentFloor = nextFloor;
            System.out.println("Elevator moved to floor: " + currentFloor + "!");
        } else {
            System.out.println("Elevator idle at floor: " + currentFloor + "!");
        }
    }

    @Override
    public void run() {
        while (running) {
            simulate();
            try {
                Thread.sleep(2000); // simulate one step every 2 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stop() {
        running = false;
    }

    private void printOrders() {
        System.out.print("DEBUG");
        for (int i = 0; i < orders.size(); i++) {
            System.out.print(" " + orders.get(i));
        }
        System.out.print('\n');
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getCurrentDirection() {
        return direction;
    }
}