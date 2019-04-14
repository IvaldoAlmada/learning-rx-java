import rx.Observable;
import twitter4j.Status;

public class Util {

    public static void printObservable(Observable observable) {
        observable.subscribe(System.out::println);
    }

    public static void log(Object msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }

    public static Observable<Status> createTwitterObservable() {
        return Observable.<Status>create(subscriber -> {
            System.out.println("Starting");
        }).publish();
    }
}
