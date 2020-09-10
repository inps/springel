package cn.inps.springel.future;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestCompletableFuture {
    @Test
    public   void main(){
//        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//            try {
//                Thread.sleep(5000);
//            System.out.println("Hello1");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//            System.out.println("Hello");
//        });


        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello1");
            try {
                Thread.sleep(5000);
                System.out.println("Hello2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello";
        });

//        try {
//            throw  new Exception();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.println("CompletableFuture11");
        future.thenAcceptAsync(str -> {

           System.out.println( str+"dddd");
           //return str+"aaa";
        });
         //future.complete("World");


        System.out.println("CompletableFuture22");
        try {
          // future.join();
          System.out.println( future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (
                ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("CompletableFuture33");
    }
}
