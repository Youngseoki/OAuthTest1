package com.mainproject.server.ChatRoom.entity;

import com.mainproject.server.ChatRoom.service.ChatService;
import com.mainproject.server.auditable.Auditable;
import com.mainproject.server.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class ChatRoom extends Auditable {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String roomId;
    @Column
    private String title;
    @Column
    private String content;

    @Column
    private Onair onair = Onair.ON;

    @Column
    private boolean secret;
    @Column
    private String pwd;

    public enum Onair {
        ON, OFF;

        @Getter
        private String onair;
    }

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void addMember(Member member) {
        this.member = member;
    }


    @Builder
    public ChatRoom(String roomId, String title, String content, String pwd) {
        this.roomId = roomId;
        this.title = title;
        this.content = content;
        this.pwd = pwd;
        Onair on = Onair.ON;
    }

    public void handlerActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        Set<WebSocketSession> sessions = new HashSet<>();

        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getMember() + "님이 입장했습니다.");
        }
        sendMessage(chatMessage, chatService);
    }

    private <T> void sendMessage(T message, ChatService chatService) {
        Set<WebSocketSession> sessions = new HashSet<>();
        sessions.parallelStream()
                .forEach(session -> chatService.sendMessage(session, message));
    }

}
