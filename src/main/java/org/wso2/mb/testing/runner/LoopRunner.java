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

import javax.jms.JMSException;

public class LoopRunner {
    private static Logger log = Logger.getLogger(LoopRunner.class);

    public static void runPublisher(AndesQueuePublisher queueClient, int numberOfMessages) throws JMSException {
        queueClient.connect();
        for (int i = 1; i <= numberOfMessages; i++) {
            String message = "Test Message " + i;
            queueClient.send(message);
            log.info("Sent: " + message);
        }
        queueClient.disconnect();
    }

    public static void runSubscriber(AndesQueueSubscriber queueClient, int numberOfMessages)
            throws JMSException {
        queueClient.connect();
        for (int i = 1; i <= numberOfMessages; i++) {
            log.info("Received: " + queueClient.receive());
        }
        queueClient.disconnect();
    }
}
