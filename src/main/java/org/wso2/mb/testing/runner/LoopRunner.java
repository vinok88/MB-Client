/*
 * Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.mb.testing.runner;

import org.apache.log4j.Logger;
import org.wso2.mb.testing.client.jms.AndesQueuePublisher;
import org.wso2.mb.testing.client.jms.AndesQueueSubscriber;
import org.wso2.mb.testing.client.jms.AndesTopicSubscriber;

import javax.jms.JMSException;

public class LoopRunner {
    private static Logger log = Logger.getLogger(LoopRunner.class);

    public static void runPublisher(AndesQueuePublisher queueClient, int numberOfMessages)
            throws JMSException {
        queueClient.connect();

        long startTime = System.nanoTime();
        for (int i = 1; i <= numberOfMessages; i++) {
            if (i%100 == 0) {
                log.info(i + " messages sent.");
                System.out.println(i + " messages sent.");
            }

            String message = "Test Message " + i;
            queueClient.send(message);
            if (log.isDebugEnabled()) {
                log.debug("Sent: " + message);
            }
            System.out.println("Sent: " + message);
        }
        long endTime = System.nanoTime();
        queueClient.disconnect();

        logPerformanceStats(endTime - startTime, numberOfMessages);
    }

    private static void logPerformanceStats(long timeSpent, int numberOfMessages) {
        double throughput = (numberOfMessages / (timeSpent * 1.0)) * 1000000000;
        log.info("========================================");
        log.info("TPS: " + throughput + " msg/sec");
    }

    public static void runSubscriber(AndesQueueSubscriber queueClient, int numberOfMessages)
            throws JMSException {
        queueClient.connect();

        long startTime = System.nanoTime();
        for (int i = 1; i <= numberOfMessages; i++) {
            if (i%100 == 0) {
                log.info(i + " messages received.");
                System.out.println(i + " messages received.");
            }

            String message = queueClient.receive();
            if (log.isDebugEnabled()) {
                log.debug("Received: " + message);
            }
            System.out.println("Received: " + message);
        }
        long endTime = System.nanoTime();
        queueClient.disconnect();

        logPerformanceStats(endTime-startTime, numberOfMessages);
    }

    public static void runTopicSubscriber(AndesTopicSubscriber topicClient, int numberOfMessages)
            throws JMSException {
        topicClient.connect();

        long startTime = System.nanoTime();
        for (int i = 1; i <= numberOfMessages; i++) {
            if (i%100 == 0) {
                log.info(i + " messages received.");
                System.out.println(i + " messages received.");
            }

            String message = topicClient.receive();
            if (log.isDebugEnabled()) {
                log.debug("Received: " + message);
            }
            System.out.println("Received: " + message);
        }
        long endTime = System.nanoTime();
        topicClient.disconnect();

        logPerformanceStats(endTime-startTime, numberOfMessages);
    }
}
