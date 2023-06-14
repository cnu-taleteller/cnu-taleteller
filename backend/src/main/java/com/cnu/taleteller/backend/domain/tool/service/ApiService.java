package com.cnu.taleteller.backend.domain.tool.service;

import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ApiService {
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private final Queue<Runnable> requestQueue = new LinkedList<>();
    private final int MAX_REQUESTS_PER_MINUTE = 3;
    private final long REQUEST_INTERVAL = 60000L; // 1분
    private int numRequests = 0;
    private final Object lock = new Object();

    public void enqueueRequest(Runnable request) {
        synchronized (lock) {
            if (numRequests >= MAX_REQUESTS_PER_MINUTE) {
                requestQueue.add(request);
            } else {
                numRequests++;
                executorService.execute(() -> {
                    try {
                        request.run();
                    } finally {
                        synchronized (lock) {
                            numRequests--;
                            processQueue();
                        }
                    }
                });
            }
        }
    }

    private void processQueue() {
        synchronized (lock) {
            if (numRequests < MAX_REQUESTS_PER_MINUTE) {
                while (!requestQueue.isEmpty()) {
                    Runnable request = requestQueue.poll();
                    numRequests++;
                    executorService.execute(() -> {
                        try {
                            request.run();
                        } finally {
                            synchronized (lock) {
                                numRequests--;
                                processQueue();
                            }
                        }
                    });
                }
            } else if (!requestQueue.isEmpty()) {
                Runnable request = requestQueue.poll();
                executorService.execute(request);
            }
        }
    }
}
