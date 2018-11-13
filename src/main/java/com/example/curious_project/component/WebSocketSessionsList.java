package com.example.curious_project.component;

import com.example.curious_project.model.Client;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebSocketSessionsList {
    private List<Client> clinetSessions = new ArrayList<>();
    public List<Client> getClinetSessions() {
        return clinetSessions;
    }
    public void setClinetSessions(List<Client> clinetSessions) {
        this.clinetSessions = clinetSessions;
    }
}
