package com.examples.test;

public class Test {
    private static String sss = "111";

    public static void main(String[] args){
//        String deviceId = "DS-2XD8A47F/CF-LZS20200717AACHE59107220";
//        deviceId = deviceId.substring(deviceId.length()-8, deviceId.length());
//        System.out.println(deviceId);

        // 测试多线程 oom
//        int size = 10000;
//        for(int i=0;i<size;i++){
//            final String xx = i+"";
//            Thread t1 = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    int i1 = Integer.valueOf(xx) % 2;
//                    if(i1 ==0){
//                        sss = null;
//                    }else{
//                        sss = xx;
//                    }
//                }
//            });
//        }
//        while (true){
//            if(sss==null){
//                try{
//                    Thread.sleep(200);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }

        // 测试多线程 oom
//        int size = 10000;
//        for(int i=0;i<size;i++){
//            Thread t1 = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println(Thread.currentThread().getName());
//                    while (true){
//
//                    }
//                }
//            });
//            t1.start();
//            try {
//                Thread.sleep(5000);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        while (true){
//            System.out.println(111);
//        }

    }

}
