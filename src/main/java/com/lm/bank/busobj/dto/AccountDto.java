package com.lm.bank.busobj.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "account")
public class AccountDto {
    private Long id;
    private BigDecimal amount;
    private UserDto owner;
    private Set<TransactionDto> transactionsFromAccount;
    private Set<TransactionDto> transactionsToAccount;

    @Id
    @GeneratedValue(generator="account_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="account_id_seq",sequenceName="account_id_seq", allocationSize=1)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "amount", nullable = false)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountDto)) return false;
        AccountDto that = (AccountDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, owner);
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name="Fk_user_account"))
    @JsonIgnore
    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    @OneToMany(mappedBy = "fromAccount")
    @JsonIgnore
    public Set<TransactionDto> getTransactionsFromAccount() {
        return transactionsFromAccount;
    }

    public void setTransactionsFromAccount(Set<TransactionDto> transactionsFromAccount) {
        this.transactionsFromAccount = transactionsFromAccount;
    }

    @OneToMany(mappedBy = "toAccount")
    @JsonIgnore
    public Set<TransactionDto> getTransactionsToAccount() {
        return transactionsToAccount;
    }

    public void setTransactionsToAccount(Set<TransactionDto> transactionsToAccount) {
        this.transactionsToAccount = transactionsToAccount;
    }

    @PrePersist
    private void setDefaultValues() {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
    }

    @PreRemove
    public void removeTransactionRefs() {
        Collection<TransactionDto> transactionsFromAccount = getTransactionsFromAccount();
        if (transactionsFromAccount != null) {
            transactionsFromAccount.forEach((t) -> {
                t.setFromAccountId(null);
                t.setFromAccount(null);
            });
        }
        Collection<TransactionDto> transactionsToAccount = getTransactionsToAccount();
        if (transactionsToAccount != null) {
            transactionsToAccount.forEach((t) -> {
                t.setToAccountId(null);
                t.setToAccount(null);
            });
        }
    }
}
