import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

public class Chapter1 {

    //12/02 - Pag 32
    //13/02 - Pag 45
    //25/02 - Pag 47

    public static void main(String[] args) throws InterruptedException {
//        helloWorld();
//        mapExample();
//        assyncObservablesExample();
//        zipExample();
        mergeSingleExample();
    }

    private static void helloWorld() {
        Observable.create(s -> {
            s.onNext("Hello World!");
            s.onCompleted();
        }).subscribe(System.out::println);
    }

    private static void mapExample() {
        Observable<Integer> o = Observable.create(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onCompleted();
        });

        o.map(i -> "Number " + i);

        Util.printObservable(o);
    }

    private static void assyncObservablesExample() {
        Observable<String> a = Observable.create(s -> {
            new Thread(() -> {
                s.onNext("one");
                s.onNext("two");
                s.onCompleted();
            }).start();
        });

        Observable<String> b = Observable.create(s -> {
            new Thread(() -> {
                s.onNext("three");
                s.onNext("four");
                s.onCompleted();
            }).start();
        });

        Observable<String> c = Observable.merge(a, b);
        Util.printObservable(c);
    }

    private static void zipExample() {
        Observable<String> o1 = getDataAsObservable(1);
        Observable<String> o2 = getDataAsObservable(2);

        Observable o3 = Observable.zip(o1, o2, (x, y) -> {
            return x + y;
        });

        Util.printObservable(o3);
    }

    private static void mergeSingleExample() throws InterruptedException {
        Observable<String> aMergeB = getDataAsSingle("A").mergeWith(getDataAsSingle("B"));
        Util.printObservable(aMergeB);
        Thread.sleep(10000);
    }

    private static Observable getDataAsObservable(Object data) {
        return Observable.create(s -> {
            s.onNext(String.valueOf(data));
            s.onCompleted();
        });
    }

    private static Single getDataAsSingle(Object data) {
        return Single.create(o -> {
            o.onSuccess(data);
        }).subscribeOn(Schedulers.io());
    }
}
