import rx.Observable;

public class Chapter1 {

    //Pag 32

    public static void main(String[] args) {
        helloWorld();
    }

    private static void helloWorld() {
        Observable.create(s -> {
            s.onNext("Hello World!");
            s.onCompleted();
        }).subscribe(System.out::println);
    }
}
