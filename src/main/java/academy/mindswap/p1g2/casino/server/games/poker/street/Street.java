package academy.mindswap.p1g2.casino.server.games.poker.street;

@FunctionalInterface
public interface Street {
    void nextStreet() throws InterruptedException;
}
