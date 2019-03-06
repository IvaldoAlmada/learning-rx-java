import rx.Observable;

public class Util {

    public static void printObservable(Observable observable) {
        observable.subscribe(System.out::println);
    }

    public static void log(Object msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }
}
