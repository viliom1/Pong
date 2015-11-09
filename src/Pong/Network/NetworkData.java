package Pong.Network;

/**
 * Created by peter on 11/9/2015.
 */
public class NetworkData {
    private Platform platformOne;
    private Platform platformTwo;
    private Pong pong;

    public NetworkData (Platform platformOne, Platform platformTwo, Pong pong) {
        this.platformOne = platformOne;
        this.platformTwo = platformTwo;
        this.pong = pong;
    }

    public void update (Platform platformOne, Platform platformTwo, Pong pong) {
        this.platformOne = platformOne;
        this.platformTwo = platformTwo;
        this.pong = pong;
    }
}
