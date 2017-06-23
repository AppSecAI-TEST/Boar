package com.join.serialport;

/**
 * Created by join on 2017/5/5.
 */

public class SerialPortCommand {
    public static byte[] handshake = new byte[]{(byte) 0xaa, 0x55, 0x08, 0x00, 0x10, 0x01, 0x43, 0x4f, 0x4e, 0x54, 0x4d};
    public static byte[] one = new byte[]{(byte) 0xAA, 0x55, 0x08, 0x00, 0x10, 0x01, 0x01, 0x01, 0x00, 0x00, 0x1b};
    public static byte[] two = new byte[]{(byte) 0xAA, 0x55, 0x08, 0x00, 0x10, 0x01, 0x01, 0x01, 0x00, 0x01, 0x1c};
    public static byte[] three = new byte[]{(byte) 0xAA, 0x55, 0x08, 0x00, 0x10, 0x01, 0x01, 0x01, 0x00, 0x02, 0x1d};
    public static byte[] four = new byte[]{(byte) 0xAA, 0x55, 0x08, 0x00, 0x10, 0x01, 0x01, 0x01, 0x00, 0x03, 0x1e};
    public static byte[] guan = new byte[]{(byte) 0xAA, 0x55, 0x08, 0x00, 0x10, 0x01, 0x02, 0x06,0x00, 0x00, 0x21};
    public static byte[] shutdown = new byte[]{(byte) 0xAA, 0x55, 0x08, 0x00, 0x10, 0x01, 0x05, 0x02, 0x00, 0x01, 0x21};

}
