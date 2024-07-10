package Application.Objects;

public class Color {
    public static final String none = "transparent";
    public static final String blue = "#30a6ff";
    public static final String red = "#ff7373";

    public static class Paint {
        public static final javafx.scene.paint.Paint none = javafx.scene.paint.Color.TRANSPARENT;
        public static final javafx.scene.paint.Paint blue = javafx.scene.paint.Color.web("#30a6ff");
        public static final javafx.scene.paint.Paint red = javafx.scene.paint.Color.web("#ff7373");
    }
}
