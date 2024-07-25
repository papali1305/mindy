package com.enspd.mindyback.exception;

public enum ErrorCodes {

  USER_NOT_FOUND(1000),
  USER_NOT_VALID(1001),

  USER_ALREADY_EXIST(1002),

  COMPETENCE_NOT_FOUND(2000),
  COMPETENCE_NOT_VALID(2001),

  CHAPTER_NOT_FOUND(3000),
  CHAPTER_NOT_VALID(3001),

  CHAPTER_CANNOT_BE_VALID(3002),
  CHAPTER_CANNOT_BE_SET_CURRENT(3003),


  LECON_NOT_FOUND(4000),
  LECONS_NOT_FOUND(4001),
  LECON_NOT_VALID(4002),


  BAD_CREDENTIALS(12003),

  IA_RESPONSE_NOT_VALID(13000),
  IA_RESPONSE_NOT_FOUND(13000),

  // Liste des exception techniaues

  IMAGE_SAVE_ERROR(14000),
  UNKNOWN_CONTEXT(14001)
  ;

  private int code;

  ErrorCodes(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
