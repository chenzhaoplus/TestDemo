package com.examples.test.listener;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * java实现监听器，适用于少量监听任务，如果多了，再添加任务会有延迟，不添加任务，只读，性能可以保证
 *
 * @author pancc
 * @version 1.0
 */
public abstract class AbstractNotifyingThread extends Thread {
    /**
     * COW 模式
     */
    CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<>();

    private String msg = "success";

    public String getMsg() {
        return msg;
    }

    /**
     * The interface Listener.
     */
    interface Listener {
        /**
         * Call back.
         *
         * @param notifyObject the notify object
         */
        void callBack(final AbstractNotifyingThread notifyObject);
    }

    public void addListener(Listener listener) {
        this.listeners.add(Objects.requireNonNull(listener));
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(Objects.requireNonNull(Objects.requireNonNull(listener)));
    }

    @Override
    public void run() {
        try {
            doRun();
        } catch (Exception e) {
            // 设置标识为失败
            msg = "fail";
        } finally {
            notifyListeners();
        }
    }

    private void notifyListeners() {
        this.listeners.forEach(listener -> listener.callBack(this));
    }

    /**
     * 实际执行的内容。无论是否抛出异常，{@link #notifyListeners}都会正常执行
     *
     * @see Runnable#run()
     */
    protected abstract void doRun();

    public static void main(String[] args) {
        AbstractNotifyingThread thread = new AbstractNotifyingThread() {
            @Override
            protected void doRun() {
                int count = 5;
                for (int i = 0; i < count; i++) {
                    if (i == 3) {
                        throw new RuntimeException("=3");
                    }
                    System.out.println(i);
                }
            }
        };
        thread.addListener(t -> System.out.println("task status: " + t.getMsg()));
        thread.start();
    }
}