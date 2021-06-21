package com.shrikant.spring.hellospring.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SocEventDetails {

  @NotNull(message = "Soc version(v) can't be null.")
  @NotBlank(message = "Soc version(v) can't be empty.")
  private String v = "1";

  public String getV() {
    return v;
  }

  public void setV(String v) {
    this.v = v;
  }

  public SocEventDetails v(String v) {
    this.v = v;
    return this;
  }

  @NotNull(message = "Timestamp on soc details can't be null.")
  @NotBlank(message = "Timestamp on soc details can't be empty.")
  private String ts;

  public String getTS() {
    return ts;
  }

  public void setTS(String ts) {
    this.ts = ts;
  }

  public SocEventDetails TS(String ts) {
    this.ts = ts;
    return this;
  }

  @NotNull(message = "Message on soc details can't be null.")
  @NotBlank(message = "Message on soc details can't be empty.")
  private String msg;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public SocEventDetails msg(String msg) {
    this.msg = msg;
    return this;
  }

  private String org;

  public String getOrg() {
    return org;
  }

  public void setOrg(String org) {
    this.org = org;
  }

  public SocEventDetails org(String org) {
    this.org = org;
    return this;
  }
}
