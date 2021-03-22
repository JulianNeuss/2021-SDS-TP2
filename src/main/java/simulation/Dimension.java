package simulation;

public enum Dimension {
    TWO_D("2D"),
    THREE_D("3D");

    private final String string;

    Dimension(String string) {
        this.string = string;
    }

    public static Dimension fromString(String string){
        for (Dimension dimension : Dimension.values()){
            if(dimension.string.equals(string))
                return dimension;
        }
        throw new IllegalArgumentException("No Dimension can be recognized with: " + string);
    }

    @Override
    public String toString() {
        return string;
    }
}
