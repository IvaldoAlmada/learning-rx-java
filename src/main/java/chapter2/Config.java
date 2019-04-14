package chapter2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.observables.ConnectableObservable;
import twitter4j.Status;

@Configuration
public class Config implements ApplicationListener<ContextRefreshedEvent> {

    private final ConnectableObservable<Status> observable = Observable.<Status>create(subscriber -> {
        System.out.println("Starting");
    }).publish();

    @Bean
    public Observable<Status> observable() {
        return observable;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("Starting");
        observable.connect();
    }
}

@Component
class Foo {

    @Autowired
    public Foo(Observable<Status> tweets) {
        tweets.subscribe(status -> {
            System.out.println(status.getText());
        });
        System.out.println("Subscribed");
    }
}

@Component
class Bar {
    @Autowired
    public Bar(Observable<Status> tweets) {
        tweets.subscribe(status -> {
            System.out.println(status.getText());
        });
        System.out.println("Subscribed");
    }
}
