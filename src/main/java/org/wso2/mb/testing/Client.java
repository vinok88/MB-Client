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

package org.wso2.mb.testing;

import org.apache.log4j.Logger;
import org.wso2.mb.testing.client.jms.AndesClientUtils;
import org.wso2.mb.testing.client.jms.AndesQueuePublisher;
import org.wso2.mb.testing.client.jms.AndesQueueSubscriber;
import org.wso2.mb.testing.client.jms.AndesTopicPublisher;
import org.wso2.mb.testing.client.jms.AndesTopicSubscriber;
import org.wso2.mb.testing.client.jms.ClientBuilder;
import org.wso2.mb.testing.runner.LoopRunner;

import javax.jms.JMSException;
import javax.naming.NamingException;

/**
 * Main class
 */
public class Client {
    private static Logger log = Logger.getLogger(Client.class);

    public static void main(String[] args) {
        String hostname = args[0];
        int port = Integer.valueOf(args[1]);
        String action = args[2];
        String destination = args[3];
        int numberOfMessages = Integer.valueOf(args[4]);
        String type = AndesClientUtils.QUEUE;
        if (args.length > 5) {
            type = args[5];
        }


        ClientBuilder builder = new ClientBuilder("admin", "admin", destination, type).brokerHost(hostname).port
                (port);
        try {
            if (AndesClientUtils.QUEUE.equals(type)) {
                if ("receive".equals(action)) {
                    AndesQueueSubscriber queueClient = builder.buildSubscriber();
                    LoopRunner.runSubscriber(queueClient, numberOfMessages);
                } else if ("send".equals(action)) {
                    AndesQueuePublisher queueClient = builder.buildPublisher();
                    LoopRunner.runPublisher(queueClient, numberOfMessages);
                } else if ("create".equals(action)) {
                    AndesQueuePublisher queueClient = builder.buildPublisher();
                    queueClient.create(destination);
                    queueClient.send("test");
                }
            } else {
                if ("receive".equals(action)) {
                    AndesTopicSubscriber topicClient = builder.buildTopicSubscriber();
                    LoopRunner.runTopicSubscriber(topicClient, numberOfMessages);
                } else if ("send".equals(action)) {
                    AndesTopicPublisher topicClient = builder.buildTopicPublisher();
                    topicClient.send("test message");
//                    LoopRunner.runPublisher(topicClient, numberOfMessages);
                } else if ("create".equals(action)) {
                    AndesTopicPublisher topicClient = builder.buildTopicPublisher();
                    topicClient.create(destination);
                    topicClient.send("test");
                }
            }
        } catch (NamingException e) {
            log.error("Error while creating client. Please check given arguments.", e);
            showHelp();
        } catch (JMSException e) {
            log.error("Error occurred while receiving/sending message", e);
        }

        System.exit(0);
    }

    private static void showHelp() {
        log.info("Usage:");
        log.info("java -jar target/mb-client-0.0.1.jar <hostname> <port> <send/receive> <Queue name> <Number of messages>");
    }
}