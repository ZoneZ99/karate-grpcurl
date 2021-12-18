package examples.greetings;

import com.intuit.karate.junit5.Karate;

class GreetingsRunner {

    @Karate.Test
    Karate testUsers() {
        return Karate.run("greetings").relativeTo(getClass());
    }
}