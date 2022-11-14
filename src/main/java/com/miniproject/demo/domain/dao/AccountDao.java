package com.miniproject.demo.domain.dao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accountDao" )
@SQLDelete(sql = "UPDATE m_users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class AccountDao {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "SortCode", nullable = false)
    private String SortCode;

    @Column(name = "AccountNumber", nullable = false)
    private String AccountNumber;

    @Column(name = "CurrentBalance", nullable = false)
    private String CurrentBalance;

    @Column(name = "BankName", nullable = false)
    private String BankName;

    @Column(name = "OwnerName", nullable = false)
    private String OwnerName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private String users;




}
