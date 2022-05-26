package com.halter.herd.persistence.model;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "COWS")
public class Cow  implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "COWID", unique = true, nullable = false, updatable = false)
  private UUID id;

  @Column(name = "COLLARID", unique = true, nullable = false)
  private Long collarId;

  @Column(name = "COWNUMBER", unique = true, nullable = false)
  private Long cowNumber;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Long getCollarId() {
    return collarId;
  }

  public void setCollarId(Long collarId) {
    this.collarId = collarId;
  }

  public Long getCowNumber() {
    return cowNumber;
  }

  public void setCowNumber(Long cowNumber) {
    this.cowNumber = cowNumber;
  }
}
