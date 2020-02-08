package patience.frontend.javafx;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaFxBoard extends Application {

    private static final int MAIN_PILE = 0;

    private static final List<Integer> FOUNDATION_PILES = Arrays.asList(1, 2, 3, 4);

    private static final List<Integer> PLAY_PILES = Arrays.asList(5, 6, 7, 8, 9, 10, 11);

    private Map<Button, Card> cardButtonMap = new HashMap<>();

    private Map<Integer, Pane> panelPerPlayPile = new HashMap<>();

    private PatienceGameRestClient client;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.client = new PatienceGameRestClient();

        primaryStage.setTitle("Patience");
        primaryStage.getIcons().add(new Image("https://apprecs.org/ios/images/app-icons/256/ef/1103438575.jpg"));

        GridPane board = JavaFxComponentUtil.createMainGrid(1, 10);
        GridPane menu = createMenu();
        board.add(menu, 0, 0, 1, 1);

        GridPane top = createTopPiles();
        board.add(top, 0, 1, 1, 2);

        GridPane bottom = createBottomPiles();
        board.add(bottom, 0, 3, 1, 7);

        primaryStage.setScene(new Scene(board, 750, 650));
        primaryStage.show();
    }

    private GridPane createMenu() {
        GridPane menu = JavaFxComponentUtil.createGrid(4);
        Button startButton = JavaFxComponentUtil.createMenuButton("new game");
        startButton.setOnAction(e -> {
            client.start();
            panelPerPlayPile.forEach(this::drawPile);
        });
        Button stopButton = JavaFxComponentUtil.createMenuButton("stop game");
        stopButton.setOnAction(e -> {
            client.stop();
            panelPerPlayPile.forEach(this::drawPile);
        });
        menu.add(startButton, 1, 0);
        menu.add(stopButton, 2, 0);
        return menu;
    }

    private GridPane createTopPiles() {
        GridPane topPane = JavaFxComponentUtil.createMainGrid(7, 1);

        Button closedCards = JavaFxComponentUtil.createButton();
        closedCards.setOnAction(e -> {
            client.flip(MAIN_PILE);
            drawPile(MAIN_PILE, topPane);
        });
        topPane.add(closedCards, 0, 0, 1, 1);
        Button openCards = JavaFxComponentUtil.createButton();
        addListener(openCards, MAIN_PILE, topPane);
        topPane.add(openCards, 1, 0, 1, 1);

        drawPile(MAIN_PILE, topPane);
        panelPerPlayPile.put(MAIN_PILE, topPane);

        createFoundationPiles(topPane);
        return topPane;
    }

    private void createFoundationPiles(GridPane topPane) {
        AtomicInteger colIndex = new AtomicInteger(3);
        FOUNDATION_PILES.forEach(card -> {
            GridPane foundationPile = JavaFxComponentUtil.createGrid(1);
            Button cardButton = JavaFxComponentUtil.createButton();
            addListener(cardButton, card, foundationPile);
            foundationPile.getChildren().add(cardButton);
            drawPile(card, foundationPile);

            panelPerPlayPile.put(card, foundationPile);
            topPane.add(foundationPile, colIndex.get(), 0, 1, 1);
            colIndex.getAndIncrement();
        });
    }

    private GridPane createBottomPiles() {
        GridPane playPiles = JavaFxComponentUtil.createMainGrid(7, 1);
        AtomicInteger colIndex = new AtomicInteger(0);
        PLAY_PILES.forEach(card -> {
            VBox playPile = new VBox();
            drawPile(card, playPile);

            panelPerPlayPile.put(card, playPile);
            playPiles.add(playPile, colIndex.get(), 0, 1, 1);
            colIndex.getAndIncrement();
        });
        return playPiles;
    }

    public void addListener(Button button, int place, Pane pane) {
        button.setOnAction(e -> {
            Card card = cardButtonMap.get(button);
            Integer selectionPlace = client.getSelectionPlace();
            client.click(place, card);

            // draw changed place
            if (selectionPlace != null) {
                drawPile(selectionPlace, panelPerPlayPile.get(selectionPlace));
            }
            // draw clicked place
            drawPile(place, pane);
        });
    }

    private void drawPile(int placing, Pane pane) {
        if (placing == MAIN_PILE) {
            ObservableList<Node> piles = pane.getChildren();
            drawCard(piles.get(0), client.getTopCard(placing, true), true, false);
            drawCard(piles.get(1), client.getTopCard(placing, false), false, false);
        }
        if (FOUNDATION_PILES.contains(placing)) {
            ObservableList<Node> piles = pane.getChildren();
            drawCard(piles.get(0), client.getTopCard(placing, false), false, false);
        }
        if (PLAY_PILES.contains(placing)) {
            pane.getChildren().clear();
            List<Card> closedCards = client.getCards(placing, true);
            List<Card> openCards = client.getCards(placing, false);

            // draw small closed cards AND open Cards (if there are)
            closedCards.forEach(closedCard -> {
                Button smallCardButton = JavaFxComponentUtil.createButton(true, true, closedCard);
                pane.getChildren().add(smallCardButton);
            });
            openCards.forEach(openCard -> {
                Button smallCardButton = JavaFxComponentUtil.createButton(true, false, openCard);
                drawCard(smallCardButton, openCard, false, true);
                registerButton(smallCardButton, openCard);
                pane.getChildren().add(smallCardButton);
                addListener(smallCardButton, placing, pane);
            });

            if (closedCards.isEmpty() && openCards.isEmpty()) {
                Button emptyPile = JavaFxComponentUtil.createButton(false, false, null);
                pane.getChildren().add(emptyPile);
                addListener(emptyPile, placing, pane);
            } else if (openCards.isEmpty()) {
                Button topCardFlipButton = (Button) pane.getChildren().get(closedCards.size() - 1);
                drawCard(topCardFlipButton, client.getTopCard(placing, true), true, false);
                topCardFlipButton.setOnAction(e -> {
                    client.flip(placing);
                    drawPile(placing, pane);
                });
            } else {
                Button topCardButton = (Button) pane.getChildren().get(closedCards.size() + openCards.size() - 1);
                addListener(topCardButton, placing, pane);
                Card topCard = client.getTopCard(placing, false);
                drawCard(topCardButton, topCard, false, false);
                registerButton(topCardButton, topCard);
            }
            panelPerPlayPile.put(placing, pane);
        }
    }

    private void drawCard(Node node, Card card, boolean isClosed, boolean isSmall) {
        Button button = (Button) node;
        boolean isSelected = !isClosed && card != null && client.getSelection().contains(card);
        JavaFxComponentUtil.alterButton(button, isSmall, isClosed, isSelected, card);
        registerButton(button, card);
    }

    private void registerButton(Button button, Card card) {
        if (card == null) {
            return;
        }
        String cardId = card.toString();
        cardButtonMap.keySet().stream()
                .filter(b -> b.getId().equals(cardId))
                .findFirst().ifPresent(value -> cardButtonMap.remove(value));
        button.setId(cardId);
        cardButtonMap.put(button, card);
    }
}
