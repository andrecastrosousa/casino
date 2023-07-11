package academy.mindswap.p1g2.casino.server.games.slotMachine;

import academy.mindswap.p1g2.casino.server.ClientHandler;
import academy.mindswap.p1g2.casino.server.games.PlayerImpl;

import java.io.IOException;

public class SlotPlayer extends PlayerImpl {
    SlotMachine slotmachine;

    public SlotPlayer (ClientHandler clientHandler, SlotMachine slotMachine) {
        super(clientHandler, 20);
        this.slotmachine = slotMachine;
        this.slotmachine.sitPlayer(this);
    }

    public void doubleBet() throws IOException, InterruptedException {
        slotmachine.setBet(2);
        slotmachine.play();
    }

    public void spin() throws IOException, InterruptedException {
        slotmachine.setBet(1);
        slotmachine.play();
    }
}
