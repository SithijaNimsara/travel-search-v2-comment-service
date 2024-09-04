package com.example.commentservice.entity;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id
    @Column(name="post_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int postId;

    @Column
    private String caption;

    @Column(insertable = false)
    @CreationTimestamp
    private Timestamp time;

    @Lob
    @Column(name="image", columnDefinition="BLOB")
    private byte[] image;;

    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "user_id")
    private User hotelId;

    @ManyToMany(mappedBy = "userPosts")
    private Set<User> postUsers = new HashSet<>();

    @Override
    public String toString() {
        return "Post{" +
                "postUsers=" + postUsers +
                ", hotelId=" + hotelId +
                ", image=" + Arrays.toString(image) +
                ", time=" + time +
                ", caption='" + caption + '\'' +
                ", postId=" + postId +
                '}';
    }
}
