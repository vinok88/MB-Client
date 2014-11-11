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
import org.wso2.mb.testing.jms.AndesQueuePublisher;
import org.wso2.mb.testing.jms.AndesQueueSubscriber;

import javax.jms.JMSException;
import javax.naming.NamingException;

/**
 * Main class
 */
public class Client {
    private static Logger log = Logger.getLogger(Client.class);

    public static void main(String[] args) {
        String action = args[0];
        int numberOfMessages = Integer.valueOf(args[1]);

        try {
            if ("receive".equals(action)) {
                AndesQueueSubscriber queueClient = new AndesQueueSubscriber.Builder("admin",
                                                                                    "admin",
                                                                                    "TestQueue")
                        .build();
                queueClient.connect();

                for (int i = 0; i < numberOfMessages; i++) {
                    log.info(queueClient.receive());
                }
            } else if ("send".equals(action)) {
                AndesQueuePublisher queueClient = new AndesQueuePublisher.Builder("admin",
                                                                                  "admin",
                                                                                  "TestQueue")
                        .build();
                queueClient.connect();
                for (int i = 0; i < numberOfMessages; i++) {
                    queueClient.send("Test Message");
                }
            }
        } catch (NamingException e) {
            log.error("Error while creating receiving client. Please check given arguments.", e);
        } catch (JMSException e) {
            log.error("Error occurred while receiving message", e);
        }

        System.exit(0);
    }
}
