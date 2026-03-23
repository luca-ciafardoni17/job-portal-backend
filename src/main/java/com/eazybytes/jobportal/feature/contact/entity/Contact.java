package com.eazybytes.jobportal.feature.contact.entity;

import com.eazybytes.jobportal.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "CONTACTS")
@Getter @Setter
@NamedQueries(
        @NamedQuery(
                name= "Contact.updateStatusById",
                query = "UPDATE Contact c SET c.status = :status, c.updatedAt = CURRENT_TIMESTAMP, c.updatedBy = :updatedBy WHERE c.id = :id"
        )
)
public class Contact extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Lob
    @Column(name = "MESSAGE", nullable = false)
    private String message;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ColumnDefault("'NEW'")
    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;

    @Column(name = "SUBJECT", nullable = false)
    private String subject;

    @Column(name = "USER_TYPE", nullable = false, length = 50)
    private String userType;


}