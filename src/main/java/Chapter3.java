import rx.Observable;
import twitter4j.Status;



public class Chapter3 {

    //26/02 - Pag 90
    //13/03 - Pag 92

    public static void main(String[] args) {
//        Filter Example
        Observable filtered = Util.createTwitterObservable().filter(Status::isRetweet);

//        Map Example
        Observable tweetCreateDate = Util.createTwitterObservable().map(value -> {
            return value.getCreatedAt();
        });


    }
}
