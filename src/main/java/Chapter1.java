import rx.Observable;
import rx.Scheduler;
import rx.Single;
import rx.schedulers.Schedulers;

public class Chapter1 {

    public static void main(String[] args) throws InterruptedException {
//        concurrencyWithMerge();
        mergeTwoSingles();
    }

    private static void concurrencyWithMerge() {
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
        c.subscribe(s -> System.out.println("Value " + s));
    }

    private static void mergeTwoSingles() throws InterruptedException {
        Observable<String> a_merge_b = getDataA().mergeWith(getDataB());
        a_merge_b.subscribe(s -> System.out.println("Single value: " + s));
        Thread.sleep(1000l);
    }



    private static Single<String> getDataA() {
        return Single.<String> create(o -> {
            o.onSuccess("DataA");
        }).subscribeOn(Schedulers.io());
    }

    private static Single<String> getDataB() {
        return Single.<String> create(o -> {
            o.onSuccess("DataB");
        }).subscribeOn(Schedulers.io());
    }

}
