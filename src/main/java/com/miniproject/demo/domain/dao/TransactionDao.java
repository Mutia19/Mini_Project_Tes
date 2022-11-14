package com.miniproject.demo.domain.dao;

import com.miniproject.demo.domain.common.BaseDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "m_transaction")
@SQLDelete(sql = "UPDATE m_transaction SET deleted_at = CURRENT_TIMESTAMP WHERE id = ? ")
@Where(clause = "deleted_at IS NULL")
public class TransactionDao extends BaseDao implements Serializable {

    private static final long serialVersionUID = 1799740307115560979L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

   @Column(name = "SourceAccountId", nullable = false)
   private long SourceAccountId;

    @Column(name = "TargetAccountId", nullable = false)
    private long TargetAccountId;


    @Column(name = "amount", nullable = false)
    private double amount;

    @ManyToOne
    private UserDao user;



}
