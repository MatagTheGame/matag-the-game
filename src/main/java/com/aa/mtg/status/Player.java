package com.aa.mtg.status;

import lombok.Data;
import org.apache.tomcat.jni.Library;

@Data
public class Player {

    private String sessionId;
    private Library library;
    private Hand hand;

    public Player(String sessionId) {
        this.sessionId = sessionId;
    }

}
