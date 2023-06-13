package com.hotsix.iAmNotAlone.global.auth.jwt;

public interface JwtProperties {

    String SECRET = "gdfhBHDBH23FBDHJFBSdfbhdFGbhbhjbgriopjoigbhbqqa23vfzx342gfmGFDL";
    int ACCESS_EXPIRATION_TIME = 1800; //30분
    int REFRESH_EXPIRATION_TIME = 604800; //일주일
    String ACCESS_TOKEN_SUBJECT = "Authorization";
    String REFRESH_TOKEN_SUBJECT = "Authorization-refresh";
    String TOKEN_PREFIX = "Bearer ";

}
