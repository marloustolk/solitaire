package patience.frontend.javafx;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.SVGPath;

public class JavaFxComponentUtil {

    private static final double MENU_BUTTON_HEIGHT = 30.0;

    public static final double MENU_BUTTON_WIDTH = 150.0;

    private static final double CARD_WIDTH = 70.0;

    private final static double CARD_HEIGHT = 100.0;

    private final static double CARD_SMALL_HEIGHT = 25.0;

    private final static Color BLACK = Color.rgb(47, 6, 70);

    private final static Color RED = Color.rgb(199, 73, 86);

    private static final CornerRadii CARD_CORNERS = new CornerRadii(5.0);

    private static final CornerRadii CARD_CORNERS_SMALL = new CornerRadii(5.0, 5.0, 0.0, 0.0, false);

    private static final Color EMPTY_SPACE_COLOR = Color.rgb(246, 165, 11, 0.1);

    private static final Stop[] STOPS = new Stop[]{
            new Stop(0, Color.rgb(79, 11, 118)),
            new Stop(1, Color.rgb(246, 165, 11))
    };

    private static final Color MENU_BUTTON_COLOR = Color.rgb(139, 45, 116);

    private static final LinearGradient BACKGROUND_COLOR = new LinearGradient(0, 0, 1.3, 0, true, CycleMethod.NO_CYCLE, STOPS);

    private static final LinearGradient CARD_COLOR = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, STOPS);

    private static final LinearGradient CARD_COLOR2 = new LinearGradient(0, 0, 0, 1.5, true, CycleMethod.NO_CYCLE, STOPS);

    private final static Background BACKGROUND = new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, new Insets(10.0, 10.0, 10.0, 10.0)));

    private final static Background EMPTY_SPACE = new Background(new BackgroundFill(EMPTY_SPACE_COLOR, CornerRadii.EMPTY, Insets.EMPTY));

    private static final Border BORDER_BLACK = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));

    private static final Border BORDER_SELECTED_SMALL = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CARD_CORNERS_SMALL, BorderWidths.DEFAULT));

    private static final Border BORDER_SELECTED = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CARD_CORNERS, BorderWidths.DEFAULT));

    private static final Border BORDER_SMALL = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CARD_CORNERS_SMALL, BorderWidths.DEFAULT));

    private static final Border BORDER = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CARD_CORNERS, BorderWidths.DEFAULT));

    private final static Background CARD_CLOSED = new Background(new BackgroundFill(CARD_COLOR, CARD_CORNERS, Insets.EMPTY));

    private final static Background CARD_CLOSED_SMALL = new Background(new BackgroundFill(CARD_COLOR2, CARD_CORNERS_SMALL, Insets.EMPTY));

    private final static Background CARD = new Background(new BackgroundFill(Color.LIGHTYELLOW, CARD_CORNERS, Insets.EMPTY));

    private final static Background CARD_SMALL = new Background(new BackgroundFill(Color.LIGHTYELLOW, CARD_CORNERS_SMALL, Insets.EMPTY));

    private JavaFxComponentUtil() {

    }

    static Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setTextFill(Color.LIGHTYELLOW);
        button.setPrefWidth(MENU_BUTTON_WIDTH);
        button.setPrefHeight(MENU_BUTTON_HEIGHT);
        button.setBackground(new Background(new BackgroundFill(MENU_BUTTON_COLOR, CARD_CORNERS, Insets.EMPTY)));
        button.setBorder(BORDER_BLACK);
        return button;
    }

    static Button createButton() {
        return createButton(false, true, null);
    }

    static Button createButton(boolean isSmall, boolean isClosed, Card card) {
        Button button = new Button();
        return alterButton(button, isSmall, isClosed, false, card);
    }

    static Button alterButton(Button button, boolean isSmall, boolean isClosed, boolean isSelected, Card card) {
        setHeight(button, isSmall);

        if (card == null) {
            button.setText("");
            button.setBackground(EMPTY_SPACE);
            button.setBorder(BORDER_BLACK);
            button.setGraphic(null);
            return button;
        }

        setText(button, isClosed, isSmall, card);
        setBorder(button, isSelected, isSmall);
        setBackground(button, isClosed, isSmall);

        return button;
    }

    private static void setText(Button button, boolean isClosed, boolean isSmall, Card card) {
        if (isClosed) {
            button.setText("");
        } else {
            button.setText(card.toString());
            button.setTextFill(card.isRed() ? RED : BLACK);
            button.alignmentProperty().setValue(Pos.TOP_CENTER);
            if (!isSmall) {
                button.setGraphic(getSVG(card));
                button.setContentDisplay(ContentDisplay.BOTTOM);
            }
        }
    }

    private static void setBackground(Button button, boolean isClosed, boolean small) {
        if (isClosed && small) {
            button.setBackground(CARD_CLOSED_SMALL);
            return;
        } else if (small) {
            button.setBackground(CARD_SMALL);
            return;
        } else if (isClosed) {
            button.setBackground(CARD_CLOSED);
            return;
        }
        button.setBackground(CARD);
    }

    private static void setBorder(Button button, boolean selected, boolean small) {
        if (selected && small) {
            button.setBorder(BORDER_SELECTED_SMALL);
            return;
        }
        if (small) {
            button.setBorder(BORDER_SMALL);
            return;
        }
        if (selected) {
            button.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CARD_CORNERS, new BorderWidths(2,2,2,2))));
            return;
        }
        button.setBorder(BORDER);
    }

    private static void setHeight(Button button, boolean small) {
        if (small) {
            button.setMaxSize(CARD_WIDTH, CARD_SMALL_HEIGHT);
            button.setMinSize(CARD_WIDTH, CARD_SMALL_HEIGHT);
        } else {
            button.setMaxSize(CARD_WIDTH, CARD_HEIGHT);
            button.setMinSize(CARD_WIDTH, CARD_HEIGHT);
        }
    }

    static GridPane createGrid(int columns) {
        return createGrid(columns, 1, false);
    }

    static GridPane createMainGrid(int columns, int rows) {
        return createGrid(columns, rows, true);
    }

    private static GridPane createGrid(int columns, int rows, boolean background) {
        GridPane pane = new GridPane();
        if (background) {
            pane.setBackground(BACKGROUND);
        }
        pane.setAlignment(Pos.BOTTOM_RIGHT);
        pane.setVgap(10);
        pane.setHgap(10);

        for (int c = 0; c < columns; c++) {
            int width = 100 / columns;
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(width);
            pane.getColumnConstraints().add(col);
        }
        for (int r = 0; r < rows; r++) {
            int height = 100 / rows;
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(height);
            pane.getRowConstraints().add(row);

        }
        return pane;
    }

    private static Group getSVG(Card card) {
        SVGPath svg = new SVGPath();
        String content;
        switch (card.getSuit()) {
            case "\u2660":
                content = SvgSymbols.spades();
                break;
            case "\u2666":
                content = SvgSymbols.diamonds();
                break;
            case "\u2663":
                content = SvgSymbols.clubs();
                break;
            default:
                content = SvgSymbols.hearts();
        }
        svg.setContent(content);
        svg.setFill(card.isRed() ? RED : BLACK);

        Bounds bounds = svg.getBoundsInParent();
        double scale = Math.min(50 / bounds.getWidth(), 50 / bounds.getHeight());
        svg.setScaleX(scale);
        svg.setScaleY(scale);

        return new Group(svg);
    }
}
