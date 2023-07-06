package academy.mindswap.p1g2.casino.server.games.poker;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.poker.rule.*;

import java.util.ArrayList;
import java.util.List;

public class Poker {
    private final Evaluator evaluatorChain;
    private final List<Player> players;
    private final Dealer dealer;

    public Poker(List<ClientHandler> clientHandlers) {
        this.dealer = new Dealer();
        evaluatorChain = new RoyalFlushEvaluator();
        players = new ArrayList<>();

        clientHandlers.forEach(clientHandler -> this.players.add(new Player(clientHandler)));
        createEvaluatorChain();
    }

    private void createEvaluatorChain() {
        evaluatorChain.setNextEvaluator(new StraightFlushEvaluator());
        evaluatorChain.setNextEvaluator(new FourOfAKindEvaluator());
        evaluatorChain.setNextEvaluator(new FullHouseEvaluator());
        evaluatorChain.setNextEvaluator(new FlushEvaluator());
        evaluatorChain.setNextEvaluator(new StraightEvaluator());
        evaluatorChain.setNextEvaluator(new ThreeOfAKindEvaluator());
        evaluatorChain.setNextEvaluator(new TwoPairEvaluator());
        evaluatorChain.setNextEvaluator(new OnePairEvaluator());
        evaluatorChain.setNextEvaluator(new HighCardEvaluator());
    }

    public void finishHand() {
        evaluatorChain.evaluateHand();
    }

    public void play() {
        dealer.shuffle();
        dealer.distributeCards(players);
    }
}
