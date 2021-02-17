package org.elliotnash.razer;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {

    private static RazerController controller;

    public static void main(String[] args) {

        controller = new RazerController();

        controller.drawPicture(new File("assets/test.png"));

    }
}
