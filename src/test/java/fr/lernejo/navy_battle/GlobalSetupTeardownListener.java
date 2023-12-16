package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;
import fr.lernejo.navy_battle.domains.Cell;
import fr.lernejo.navy_battle.handler.GameFireHandler;
import fr.lernejo.navy_battle.handler.GameStartHandler;
import fr.lernejo.navy_battle.handler.PingHandler;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicInteger;

import static java.net.InetAddress.getLoopbackAddress;

public class GlobalSetupTeardownListener implements LauncherSessionListener {

    private Fixture fixture;

    @Override
    public void launcherSessionOpened(LauncherSession session) {
        // Avoid setup for test discovery by delaying it until tests are about to be executed
        session.getLauncher().registerTestExecutionListeners(new TestExecutionListener() {
            @Override
            public void testPlanExecutionStarted(TestPlan testPlan) {
                if (fixture == null) {
                    fixture = new Fixture();
                    fixture.setUp();
                }
            }
        });
    }

    @Override
    public void launcherSessionClosed(LauncherSession session) {
        if (fixture != null) {
            fixture.tearDown();
            fixture = null;
        }
    }


    static class Fixture {

        private HttpServer server;
        private ExecutorService executorService;

        void setUp() {
            try {
                server = HttpServer.create(new InetSocketAddress(getLoopbackAddress(), 0), 0);
            }
            catch (IOException e) {
                throw new UncheckedIOException("Failed to start HTTP server", e);
            }

            Cell sea [][] = new Cell[10][10]; //Création de la mer (carte du jeu 10x10)

            server.createContext("/ping", new PingHandler());
            GameStartHandler startHandler = new GameStartHandler(0);
            server.createContext("/api/game/start", startHandler);
            server.createContext("/api/game/fire", new GameFireHandler(sea, new AtomicInteger(0), startHandler));
            executorService = Executors.newCachedThreadPool();
            server.setExecutor(executorService);
            server.start();
            int port = server.getAddress().getPort();
            System.setProperty("http.server.host", getLoopbackAddress().getHostAddress());
            System.setProperty("http.server.port", String.valueOf(port));
        }

        void tearDown() {
            server.stop(0);
            executorService.shutdownNow();
        }
    }
}
