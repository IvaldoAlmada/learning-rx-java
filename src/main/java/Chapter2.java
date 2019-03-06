import rx.Observable;

import java.util.List;

public class Chapter2 {

    //26/02 - Pag 60
    //06/03 - Pag 66

    public static void main(String[] args) {
//        createJust("Vaue");
//        createFrom(Collections.singletonList("value"));
//        createRange(5, 9);
//        createEmpty();
//        createNever();
//        createError();
//        testLog();
//        createWithoutCache();
        createWithCache();
    }

    private static void createJust(String value) {
        Observable<String> observable = Observable.just(value);
        Util.printObservable(observable);
    }

    private static void createFrom(List<String> values) {
        Observable<String> observable = Observable.from(values);
        Util.printObservable(observable);
    }

    private static void createRange(int from, int n) {
        Observable<Integer> observable = Observable.range(from, n);
        Util.printObservable(observable);
    }

    private static void createEmpty() {
        Observable<Integer> observable = Observable.empty();
        Util.printObservable(observable);
    }

    private static void createNever() {
        Observable<Integer> observable = Observable.never();
        Util.printObservable(observable);
    }

    private static void createError() {
        Observable<Integer> observable = Observable.error(new Exception());
        Util.printObservable(observable);
    }

    private static void testLog() {
        Util.log("Before");
        Observable.range(5, 4).subscribe(Util::log);
        Util.log("After");
    }

    private static void createWithoutCache() {
        Observable<Integer> ints = Observable.create(subscriber -> {
            Util.log("Create");
            subscriber.onNext(42);
            subscriber.onCompleted();

        });
        Util.log("Starting");
        ints.subscribe(integer -> Util.log("Element A: " + integer));
        ints.subscribe(integer -> Util.log("Element B: " + integer));
        Util.log("Exit");
    }

    private static void createWithCache() {
        Observable<Object> ints = Observable.create(subscriber -> {
            Util.log("Create");
            subscriber.onNext(42);
            subscriber.onCompleted();

        }).cache();
        Util.log("Starting");
        ints.subscribe(integer -> Util.log("Element A: " + integer));
        ints.subscribe(integer -> Util.log("Element B: " + integer));
        Util.log("Exit");
    }
}
