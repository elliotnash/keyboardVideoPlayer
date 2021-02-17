package org.elliotnash.razer.lighting;

public class Color {

    public static final Color LAVENDER = new Color(108, 41, 242);
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color BLUE = new Color(0, 0, 255);
    public static final Color PURPLE = new Color(94, 1, 74);

    public byte red;
    public byte green;
    public byte blue;

    public Color(int red, int green, int blue){
        if (!(
                red >= 0 && red <= 255
                && green >= 0 && green <= 255
                && blue >= 0 && blue <= 255
        )){
            throw new IllegalArgumentException("All values must be a positive integer less than 255");
        }

        this.red = (byte) red;
        this.green = (byte) green;
        this.blue = (byte) blue;

    }

    public Color(int rgb, boolean rgba){

        float alpha;
        if (rgba)
            alpha = (Byte.toUnsignedInt((byte) ((rgb >> 24) & 0xFF))/255f);
        else
            alpha = 1.0f;

        this.red = (byte)(int)(((rgb >> 16) & 0xFF)*alpha);
        this.green = (byte)(int)(((rgb >> 8) & 0xFF)*alpha);
        this.blue = (byte)(int)(((rgb) & 0xFF)*alpha);
    }
    public Color(int rgba){
        this(rgba, true);
    }

    @Override
    public String toString(){
        return "("+Byte.toUnsignedInt(this.red)+", "+Byte.toUnsignedInt(this.green)+", "+Byte.toUnsignedInt(this.blue)+")";
    }
}
