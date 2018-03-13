package com.lm.bank.busobj.dto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "transaction")
public class TransactionDto {
    private Long id;
    private String description;
    private BigDecimal amount;
    private BigDecimal fromAccountAmountAfter;
    private BigDecimal toAccountAmountAfter;
    private Long fromAccountId;
    private Long toAccountId;
    private AccountDto fromAccount;
    private AccountDto toAccount;
    private Date date;

    @Id
    @GeneratedValue(generator="transaction_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="transaction_id_seq",sequenceName="transaction_id_seq", allocationSize=1)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 256)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof TransactionDto)) return false;
        TransactionDto that = (TransactionDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(fromAccountAmountAfter, that.fromAccountAmountAfter) &&
                Objects.equals(toAccountAmountAfter, that.toAccountAmountAfter) &&
                Objects.equals(fromAccountId, that.fromAccountId) &&
                Objects.equals(toAccountId, that.toAccountId) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromAccountId, toAccountId, date);
    }

    @ManyToOne
    @JoinColumn(name = "from_account", referencedColumnName = "id",
            foreignKey = @ForeignKey(name="Fk_from_account_transaction", foreignKeyDefinition = " /*FOREIGN KEY in sql that sets ON DELETE SET NULL*/"), insertable = false, updatable = false)
    public AccountDto getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(AccountDto accountByFromAccount) {
        this.fromAccount = accountByFromAccount;
    }

    @ManyToOne
    @JoinColumn(name = "to_account", referencedColumnName = "id", nullable = false,
            foreignKey = @ForeignKey(name="Fk_to_account_transaction", foreignKeyDefinition = " /*FOREIGN KEY in sql that sets ON DELETE SET NULL*/"), insertable = false, updatable = false)
    public AccountDto getToAccount() {
        return toAccount;
    }

    public void setToAccount(AccountDto accountByToAccount) {
        this.toAccount = accountByToAccount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Basic
    @Column(name = "to_account")
    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    @Basic
    @Column(name = "from_account")
    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    @Basic
    @Column(name = "from_account_amount_after")
    public BigDecimal getFromAccountAmountAfter() {
        return fromAccountAmountAfter;
    }

    public void setFromAccountAmountAfter(BigDecimal fromAccountAmountAfter) {
        this.fromAccountAmountAfter = fromAccountAmountAfter;
    }

    @Basic
    @Column(name = "to_account_amount_after", nullable = false)
    public BigDecimal getToAccountAmountAfter() {
        return toAccountAmountAfter;
    }

    public void setToAccountAmountAfter(BigDecimal toAccountAmountAfter) {
        this.toAccountAmountAfter = toAccountAmountAfter;
    }

    @PrePersist
    public void setIds() {
        if (fromAccount != null) {
            fromAccountId = fromAccount.getId();
        }
        if (toAccount != null) {
            toAccountId = toAccount.getId();
        }
        date = Calendar.getInstance().getTime();
    }
}
