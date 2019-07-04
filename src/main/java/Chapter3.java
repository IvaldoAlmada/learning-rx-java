import rx.Observable;
import twitter4j.Status;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static rx.Observable.*;

import org.apache.commons.lang3.tuple.Pair;

public class Chapter3 {

//    26/02 - Pag 90
//    13/04 - Pag 92
//    15/04 - Pag 96
//    05/06 - Pag 99
//    10/06 - Pag 101
//    10/06 - Pag 108
//    03/07 - Pag 117
//    04/07 - Pag 128

    public static void main(String[] args) {
//        Filter Example
        Observable filtered = Util.createTwitterObservable().filter(Status::isRetweet);

//        Map Example
        Observable tweetCreateDate = Util.createTwitterObservable().map(value -> {
            return value.getCreatedAt();
        });

//        FlatMapExample
        Observable<Sound> morseCode = Observable.just('S', 'p', 'a', 'R', 't', 'a')
                .map(Character::toLowerCase)
                .flatMap(Chapter3::toMorseCode);

//        TimerExample
        Observable<String> timerExample = Observable.just("Abacate", "Banana", "Cajá", "Manga", "pêra", "Maçâ").flatMap(word ->
                timer(word.length(), SECONDS).map(x -> word));

//        Cartesian product example
        Observable<Integer> oneToEight = Observable.range(1, 8);

        Observable<String> ranks = oneToEight.map(Objects::toString);
        Observable<String> files = oneToEight.map(x -> 'a' + x - 1)
                .map(ascii -> (char) ascii.intValue())
                .map(ch -> Character.toString(ch));

        Observable<String> squares = files.flatMap(file -> ranks.map(rank -> file + rank));


//        CombineLatest example
        Observable<String> combineLatest = Observable.combineLatest(
                interval(17, MILLISECONDS).map(x -> "S" + x),
                interval(10, MILLISECONDS).map(x -> "F" + x),
                (s, f) -> f + ":" + s
        );

//        WithLatestFrom example
        Observable<String> fast = interval(10, MILLISECONDS).map(x -> "F" + x);
        Observable<String> slow = interval(17, MILLISECONDS).map(x -> "S" + x);

        Observable<String> withLatestFrom = slow.withLatestFrom(fast, (s, f) -> s + ":" + f);

//        Amb example
        Observable<String> amb = Observable.amb(
                stream(100, 17, "S"),
                stream(200, 10, "F")
        );

//        Scan example
        Observable<BigInteger> factorials = Observable
                .range(2, 100)
                .scan(BigInteger.ONE, (big, cur) ->
                        big.multiply(BigInteger.valueOf(cur)));

//        Collect example
        Observable<List<Integer>> all = Observable
                .range(10, 20)
                .collect(ArrayList::new, List::add);

        Observable<String> alice = speak("As pessoa boas devem amar seu inimigos", 110);

        Observable<String> bob = speak("A vingança nunca é plena, mata a alma e envenena", 90);

        Observable<String> jane = speak("Pauliiiiiiinha, me diz o que eu faço.", 100);

//        Merge example
        Observable<String> mergeExample = merge(
                alice.map(w -> "Alice: " + w),
                bob.map(w -> "Bob: " + w),
                jane.map(w -> "Jane: " + w)
        );

//        Concat example
        Observable<String> concatExample = concat(
                alice.map(w -> "Alice: " + w),
                bob.map(w -> "Bob: " + w),
                jane.map(w -> "Jane: " + w)
        );

        Util.printObservable(concatExample);
    }

    static Observable<String> stream(int initDelay, int interval, String name) {
        return Observable
                .interval(initDelay, interval, MILLISECONDS)
                .map(x -> name + x)
                .doOnSubscribe(() ->
                        System.out.println("Subscribed to " + name))
                .doOnUnsubscribe(() ->
                        System.out.println("Unsubscribe from: " + name));
    }

    enum Sound {DI, DAH}

    static Observable<Sound> toMorseCode(char ch) {
        switch (ch) {
            case 'a':
                return Observable.just(Sound.DI, Sound.DAH);
            case 'b':
                return Observable.just(Sound.DAH, Sound.DI, Sound.DI, Sound.DI);
            case 'c':
                return Observable.just(Sound.DAH, Sound.DI, Sound.DAH, Sound.DI);
            case 'p':
                return Observable.just(Sound.DI, Sound.DAH, Sound.DAH, Sound.DI);
            case 'r':
                return Observable.just(Sound.DI, Sound.DAH, Sound.DI);
            case 's':
                return Observable.just(Sound.DI, Sound.DI, Sound.DI);
            case 't':
                return Observable.just(Sound.DAH);
            default:
                return Observable.empty();

        }
    }

    static Observable<String> speak(String quote, long millisPerChar) {
        String[] tokens = quote.replaceAll("[:,]", "").split(" ");
        Observable<String> words = Observable.from(tokens);
        Observable<Long> absoluteDelay = words
                .map(String::length)
                .map(len -> len * millisPerChar)
                .scan((total, current) -> total + current);

        return words
                .zipWith(absoluteDelay.startWith(0L), Pair::of)
                .flatMap(pair -> just(pair.getLeft())
                        .delay(pair.getRight(), MILLISECONDS));
    }
}
