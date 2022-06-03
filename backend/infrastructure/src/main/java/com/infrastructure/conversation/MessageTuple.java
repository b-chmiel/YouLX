package com.infrastructure.conversation;

import com.domain.conversation.Message;
import com.domain.user.UserId;
import com.infrastructure.user.UserTuple;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LX_MESSAGE")
@Getter
@Setter
@ToString
@NoArgsConstructor
class MessageTuple {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserTuple user;

    MessageTuple(Message message, UserTuple user) {
        this.content = message.getContent();
        this.time = message.getTime();
        this.user = user;
    }

    public Message toDomain() {
        return new Message(this.content, this.time, new UserId(this.user.getId()));
    }
}
