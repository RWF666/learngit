package com.weige.utils;

import org.apache.http.conn.HttpClientConnectionManager;

public class IdleConnectionEvictor extends Thread {

    private final HttpClientConnectionManager connMgr;

    private volatile boolean shutdown;

    public IdleConnectionEvictor(HttpClientConnectionManager connMgr) {
        this.connMgr = connMgr;
        this.start();
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    // �ر�ʧЧ������
                    connMgr.closeExpiredConnections();
                }
            }
        } catch (InterruptedException ex) {
            // ����
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}
