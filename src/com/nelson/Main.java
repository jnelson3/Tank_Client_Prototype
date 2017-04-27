package com.nelson;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random random = new Random();

        SocketAddress address = new InetSocketAddress("127.0.0.1", 5000);
        try (SocketChannel socketChannel = SocketChannel.open(address)) {

            socketChannel.configureBlocking(false);

            JsonObject value = Json.createObjectBuilder()
                    .add("action", "register")
                    .build();
            ByteBuffer buffer = ByteBuffer.allocate(64);
            buffer.put(value.toString().getBytes());
            buffer.flip();
            while(buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }

            while (true) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(64);;
                int bytesRead = socketChannel.read(byteBuffer);
                while (bytesRead > 0) {
                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()) {
                        System.out.print((char) byteBuffer.get());
                    }
                    System.out.println(" hello");
                    bytesRead = socketChannel.read(byteBuffer);
                }

            }
        } catch (ClosedChannelException ex) {
            ex.printStackTrace();
        }catch (IOException ex) {
            //ex.printStackTrace();
        }
    }
}
