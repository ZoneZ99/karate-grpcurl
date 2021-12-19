package examples.greetings;

import com.intuit.karate.junit5.Karate;

class GreetingsRunner {

    @Karate.Test
    Karate testGreetings() {
        return Karate.run().relativeTo(getClass());
    }
}
