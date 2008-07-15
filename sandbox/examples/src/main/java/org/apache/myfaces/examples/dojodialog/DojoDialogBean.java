/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.examples.dojodialog;

/**
 * @author Martin Marinschek
 */
public class DojoDialogBean {

    private int progress=-1;

    public boolean isActionRunning() {
        return progress>=0&&progress<14;
    }


    public int getProgress() {
        return progress;
    }

    public String startAction() {
        Thread thread = new Thread(new Runnable() {

            public void run() {
                try {
                    for(progress=0; progress<15; progress++) {
                        Thread.sleep(900);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();

        return null;
    }
}
