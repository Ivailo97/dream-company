package dreamcompany.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "friend_requests")
public class FriendRequest extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)
    private User receiver;

    @Column(name = "mutual_friends")
    private long mutualFriends;

    @Column(name = "createdOn", nullable = false)
    private LocalDateTime createdOn;
}
