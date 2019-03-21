import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import twitter4j.*;
import twitter4j.auth.AccessToken;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Chapter2 {

    //26/02 - Pag 60
    //06/03 - Pag 66
    //14/03 - Pag 69
    //18/03 - Pag 75
    //20/03 - Pag 82

    public static void main(String[] args) throws InterruptedException {
//        createJust("Vaue");
//        createFrom(Collections.singletonList("value"));
//        createRange(5, 9);
//        createEmpty();
//        createNever();
//        createError();
//        testLog();
//        createWithoutCache();
//        createWithCache();
//        testloadAll();
//        testTimer();
//        testInterval();
//        testObserve();
//        testWithoutPublishRefCount();
        testPublishRefCount();
        Thread.sleep(10000);
    }

    private static Observable<Status> getTwitterObservable() {
        return Observable.create(subscriber -> {
            System.out.println("Establishing connection");

            AccessToken accessToken = new AccessToken("", "");

            TwitterStream twitterStream = new TwitterStreamFactory().getInstance(accessToken);

            subscriber.add(Subscriptions.create(() -> {
                System.out.println("Disconnecting");
                twitterStream.shutdown();
            }));
            twitterStream.sample();
        });
    }

    private static void testPublishRefCount() {
        Observable<Status> observable = getTwitterObservable();

        Observable<Status> lazy = observable.publish().refCount();

        System.out.println("Before subscribers");
        Subscription sub1 = lazy.subscribe();
        System.out.println("Subscribed 1");
        Subscription sub2 = lazy.subscribe();
        System.out.println("Subscribed 2");
        sub1.unsubscribe();
        System.out.println("Unsubscribed 1");
        sub2.unsubscribe();
        System.out.println("Unsubscribed 2");
    }

    private static void testWithoutPublishRefCount() {
        Observable<Status> observable = getTwitterObservable();


        Subscription sub1 = observable.subscribe();
        System.out.println("Subscribed1");
        Subscription sub2 = observable.subscribe();
        System.out.println("Subscribed2");
        sub1.unsubscribe();
        System.out.println("Unsubscribed1");
        sub2.unsubscribe();
        System.out.println("Unsubscribed2");

    }

    private static void testObserve() {
        observe().subscribe(
                Util::log,
                ex -> Util.log(ex)
        );
    }

    private static Observable<Status> observe() {
        return Observable.create(subscriber -> {
            TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
            twitterStream.addListener(new StatusListener() {
                public void onStatus(Status status) {
                    subscriber.onNext(status);
                }

                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}

                public void onTrackLimitationNotice(int i) {}

                public void onScrubGeo(long l, long l1) { }

                public void onStallWarning(StallWarning stallWarning) {}

                public void onException(Exception e) {
                    subscriber.onError(e);
                }
            });
            subscriber.add(Subscriptions.create(twitterStream::shutdown));
        });
    }

    private static void testInterval() {
        Observable.interval(1_000_000 /60, TimeUnit.MICROSECONDS)
                .subscribe((Long i) -> Util.log(i));

    }

    private static void testTimer() {
        Observable.timer(1, TimeUnit.SECONDS)
                .subscribe(Util::log);
    }

    private static Observable<String> loadAll(Collection<Integer> ids) {
        return Observable.create(subscriber -> {
            ExecutorService pool = Executors.newFixedThreadPool(10);
            AtomicInteger countDown = new AtomicInteger(ids.size());
            //this part violates Rx contract
            ids.forEach(id -> pool.submit(() -> {
                final String value = String.valueOf(id);
                subscriber.onNext(value);
                if (countDown.decrementAndGet() == 0) {
                    pool.shutdown();
                    subscriber.onCompleted();
                }
            }));
        });
    }

    private static void testloadAll() {
        List list  = Arrays.asList(1, 2, 3);
        Util.printObservable(loadAll(list));
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
