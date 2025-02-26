/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package tndn.app.nyam.alipay;

public final class Keys {


    // 商户PID
    public static final String PARTNER = "2088121195792483";
    //2088021887311445
    //test : 2088101122136241
    // 商户收款账号
    public static final String SELLER = "2088121195792483";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICXAIBAAKBgQDWTB0SHpcrxbMrRLbu0KhCdV/D8dwtXOFCFSHeRR5LKwoiybFOAD/WhQfT/8abo0NJQkiZ9cT4BpZE8NUz0xPWpoUDl5yb6zlNJYtAiJujhJ+EJwyjK9o6XVu6TCQNh8SU7bK3fUKDjZxoOuc7fDm9JHwsCKrS77MoogibVEGPOwIDAQABAoGAY7bxBABl+UE5+JU1xfQjhyEUmZmOCTGhw10P8FwbF4EEa2GMdi4ZhllKZjQrFzql0Y3dOH6q03pFK/kgS8gY/ztYn+1LWth5HlJX26KWoOqfwpDNq14Xh71YUJSPAyuRcifItG4D6onxNARd9w2Qns/MbvhX+NTZpHnZaHW7GvkCQQDwqBDKDn1TDxUiyEeaMFb/0UGm2KPPTCrwYejLxmBc1J+kqKhZCf2ol823Z6FBnvsNdn3BUZMp11ADnvVRIoaHAkEA4/XR687GdWkhnAqFiEtAa3KageFOnnjOVVPC1jTUW7bmoEM8BcUW8QQvJcU75FNIEVY7IyJFHQ1BXX8ch3GqrQJBAN6ga1+whclXk5LouixueZ0QJO7HTMrRSIu115jjPHPEEqdNiudLscfMHveCu4+sFc0/h1hWMZCsaY3f+EsFaHMCQHYuIr5VXJ3R+xKigce++464w1FCNTgC+TVTT1ct3EoP8FB1itCS9OnOeYLjVcFxS5NxWQdF5P7CuHh4RZoSW6ECQHWMdXOBZvloZHmWHVX5bz33D565kESefUsOTB2+tjU5xr8bvUASCk9fkgd5P+Klfa79I1t/5E8MwvzefmxTChs=";
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDWTB0SHpcrxbMrRLbu0KhCdV/D8dwtXOFCFSHeRR5LKwoiybFOAD/WhQfT/8abo0NJQkiZ9cT4BpZE8NUz0xPWpoUDl5yb6zlNJYtAiJujhJ+EJwyjK9o6XVu6TCQNh8SU7bK3fUKDjZxoOuc7fDm9JHwsCKrS77MoogibVEGPOwIDAQAB";

    /*

    -----BEGIN PUBLIC KEY-----
    용오빠가 새로 만든 퍼블릭키
    MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDWTB0SHpcrxbMrRLbu0KhCdV/D8dwtXOFCFSHeRR5LKwoiybFOAD/WhQfT/8abo0NJQkiZ9cT4BpZE8NUz0xPWpoUDl5yb6zlNJYtAiJujhJ+EJwyjK9o6XVu6TCQNh8SU7bK3fUKDjZxoOuc7fDm9JHwsCKrS77MoogibVEGPOwIDAQAB

    용오빠가 만든 프라이빗키
    -----BEGIN RSA PRIVATE KEY-----
MIICXAIBAAKBgQDWTB0SHpcrxbMrRLbu0KhCdV/D8dwtXOFCFSHeRR5LKwoiybFOAD/WhQfT/8abo0NJQkiZ9cT4BpZE8NUz0xPWpoUDl5yb6zlNJYtAiJujhJ+EJwyjK9o6XVu6TCQNh8SU7bK3fUKDjZxoOuc7fDm9JHwsCKrS77MoogibVEGPOwIDAQABAoGAY7bxBABl+UE5+JU1xfQjhyEUmZmOCTGhw10P8FwbF4EEa2GMdi4ZhllKZjQrFzql0Y3dOH6q03pFK/kgS8gY/ztYn+1LWth5HlJX26KWoOqfwpDNq14Xh71YUJSPAyuRcifItG4D6onxNARd9w2Qns/MbvhX+NTZpHnZaHW7GvkCQQDwqBDKDn1TDxUiyEeaMFb/0UGm2KPPTCrwYejLxmBc1J+kqKhZCf2ol823Z6FBnvsNdn3BUZMp11ADnvVRIoaHAkEA4/XR687GdWkhnAqFiEtAa3KageFOnnjOVVPC1jTUW7bmoEM8BcUW8QQvJcU75FNIEVY7IyJFHQ1BXX8ch3GqrQJBAN6ga1+whclXk5LouixueZ0QJO7HTMrRSIu115jjPHPEEqdNiudLscfMHveCu4+sFc0/h1hWMZCsaY3f+EsFaHMCQHYuIr5VXJ3R+xKigce++464w1FCNTgC+TVTT1ct3EoP8FB1itCS9OnOeYLjVcFxS5NxWQdF5P7CuHh4RZoSW6ECQHWMdXOBZvloZHmWHVX5bz33D565kESefUsOTB2+tjU5xr8bvUASCk9fkgd5P+Klfa79I1t/5E8MwvzefmxTChs=


-----BEGIN RSA PRIVATE KEY-----
MIICXQIBAAKBgQDCxSK6h3tXGPly9Bd2Xew4I6FJ9boqxB+3mMiVVOja0OSmomCvjKsoO6wo7elm7m8ww/YD2VqKmECx5yc2e8C+nKfRy2lYwbwt4I+DoTB7JAuR/uaNOWchBAXfe8RATcGGKiIFr7KW/uKwBVM7obZJzJdF2SRk2RQxes15H/zC7QIDAQABAoGAAyi9anceGgiOqz2oT8QwO6zgRdnVfjIWLZ0ty7ZH3Jy8yLMjjFVkxU9KeJN7UK8XYJwdFmzlGhBkJD6SlUCmnKmRvbUr/dSL7do1KEwwyH6hSLIq/5Ql/D7/s5d23+lNNZbT6yIGKMwbPFMu7ZXdpRQAD/5ATGUL0cbLKMz/7mUCQQD+SJ2S566YWSXqW5JdjzhNxmeI4Vg/uO8nDeg1tR+KN1DXBGndGdSBjCMJM2EdMXSSDXVZM0hgIvgzS0cRESKLAkEAxBWvTgM6xQUDHLulzR+H2uYzgO5jqRutK7tgPBKmYX5vHUnuMxkp+0h6lL+UATZWKDOZg/c3ml2jk93OzIQ3ZwJAF8TV5dt+OINsxeFVZyVyD+MR3nuh8iLpR+9G225YymStS8v/0UkO0iU1STDiTQZIbTaQ14uxojrilHje5buCTQJBALvLQN/oO+rhR//Gv55aalR9UCpVJeGZ/hn6FMqz2eFZo7VbTuRlX02oNq4rZOZcUduiv44hwID42WcOcbZxklUCQQCSGLWaEb7AYyG4AMqOaqyGUolRyYt76ytRB0vvHyDhFE2rZdWVdlrwkhx8hrFKxmSRvX1bw+OCEo55AjHw44UF
-----END RSA PRIVATE KEY-----
1
새로발급받은 퍼블릭
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCxSK6h3tXGPly9Bd2Xew4I6FJ9boqxB+3mMiVVOja0OSmomCvjKsoO6wo7elm7m8ww/YD2VqKmECx5yc2e8C+nKfRy2lYwbwt4I+DoTB7JAuR/uaNOWchBAXfe8RATcGGKiIFr7KW/uKwBVM7obZJzJdF2SRk2RQxes15H/zC7QIDAQAB
새로발급받은 프라이빗
MIICXQIBAAKBgQDCxSK6h3tXGPly9Bd2Xew4I6FJ9boqxB+3mMiVVOja0OSmomCvjKsoO6wo7elm7m8ww/YD2VqKmECx5yc2e8C+nKfRy2lYwbwt4I+DoTB7JAuR/uaNOWchBAXfe8RATcGGKiIFr7KW/uKwBVM7obZJzJdF2SRk2RQxes15H/zC7QIDAQABAoGAAyi9anceGgiOqz2oT8QwO6zgRdnVfjIWLZ0ty7ZH3Jy8yLMjjFVkxU9KeJN7UK8XYJwdFmzlGhBkJD6SlUCmnKmRvbUr/dSL7do1KEwwyH6hSLIq/5Ql/D7/s5d23+lNNZbT6yIGKMwbPFMu7ZXdpRQAD/5ATGUL0cbLKMz/7mUCQQD+SJ2S566YWSXqW5JdjzhNxmeI4Vg/uO8nDeg1tR+KN1DXBGndGdSBjCMJM2EdMXSSDXVZM0hgIvgzS0cRESKLAkEAxBWvTgM6xQUDHLulzR+H2uYzgO5jqRutK7tgPBKmYX5vHUnuMxkp+0h6lL+UATZWKDOZg/c3ml2jk93OzIQ3ZwJAF8TV5dt+OINsxeFVZyVyD+MR3nuh8iLpR+9G225YymStS8v/0UkO0iU1STDiTQZIbTaQ14uxojrilHje5buCTQJBALvLQN/oO+rhR//Gv55aalR9UCpVJeGZ/hn6FMqz2eFZo7VbTuRlX02oNq4rZOZcUduiv44hwID42WcOcbZxklUCQQCSGLWaEb7AYyG4AMqOaqyGUolRyYt76ytRB0vvHyDhFE2rZdWVdlrwkhx8hrFKxmSRvX1bw+OCEo55AjHw44UF
알리페이한테 보낸 퍼블릭
-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQX0tNOzoAtXC2mM563t7GaKRAuiGdfmknKgwVialPhKJqJgsaD9lWw6OV7h4ZojrIhTqN/KK3PDg0UfcnNb6MShaVinmqV9zlDISMm0J6IkJri9xnNXMYDDLONwAztmknofIbrLuhvhG6Pudw0WK+dyLtqYJcwqaHWNHDQgOuwwIDAQAB


//내가쓰던거
//MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQX0tNOzoAtXC2mM563t7GaKRAiGdfmknKgwVialPhKJqJgsaD9lWw6OV7h4ZojrIhTqN/xJdFmhuBKpThLiIH9T7pTRlICA3TizJhysoY8ZBL767uPgQJXJ838WzcbvXEjXHhMMnwD/6ZVs5Fb8GT89y1+Vv979EDhwT4QCVoEEgme2yR3+xau902JQyencUqEOyyKWjv42gQIDAQAB
알리페이 웹사이트에
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQX0tNOzoAtXC2mM563t7GaKRAuiGdfmknKgwVialPhKJqJgsaD9lWw6OV7h4ZojrIhTqN/KK3PDg0UfcnNb6MShaVinmqV9zlDISMm0J6IkJri9xnNXMYDDLONwAztmknofIbrLuhvhG6Pudw0WK+dyLtqYJcwqaHWNHDQgOuwwIDAQAB
써있는 알리페이 공개키
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB
알리페이가 준거
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB
알리페이웹사이트 개방형플랫폼
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQX0tNOzoAtXC2mM563t7GaKRAuiGdfmknKgwVialPhKJqJgsaD9lWw6OV7h4ZojrIhTqN/KK3PDg0UfcnNb6MShaVinmqV9zlDISMm0J6IkJri9xnNXMYDDLONwAztmknofIbrLuhvhG6Pudw0WK+dyLtqYJcwqaHWNHDQgOuwwIDAQAB
써있는 알리페이 공개키
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB


     */
//    public static final String PARTNER = "2088021887311445";
//    //2088021887311445
//    //test : 2088101122136241
//    // 商户收款账号
//    public static final String SELLER = "2088021887311445";
//    // 商户私钥，pkcs8格式
//    public static final String RSA_PRIVATE = "MIICWwIBAAKBgQDQX0tNOzoAtXC2mM563t7GaKRAuiGdfmknKgwVialPhKJqJgsa" +
//            "D9lWw6OV7h4ZojrIhTqN/KK3PDg0UfcnNb6MShaVinmqV9zlDISMm0J6IkJri9xn" +
//            "NXMYDDLONwAztmknofIbrLuhvhG6Pudw0WK+dyLtqYJcwqaHWNHDQgOuwwIDAQAB" +
//            "AoGAJm+9mzR2oZTWgsgzJlUgMOcyjUIIW13rFa9a5Uosch+cOgpkd90+kROrhEeV" +
//            "Q9M6Fq0EvRZCCZzRHU2VUqVGTZNTjfWNZ+54XrKfD2Y3eh4VXwwtpwCejT5ew+eQ" +
//            "+NlIN2J+5WRe74ooXkUjrxWmkTAMSI9ngk559iz4lbNHtzECQQDoGnD21caxvLWK" +
//            "C2doUY/tFeRuVOFH7TPmnYT9n04NKMi8D26XA0+EX8P8IXd0aDAgsjpq6p8DuOJZ" +
//            "49v+e5S5AkEA5dNfXBu3Lm5X+ksxPg+tSvmjcARwLI2UwjPj+Sy1wUj/GkUFZ8rU" +
//            "xm9E0Ga36+I06xvgVd0vL/uV+7cw2q3ZWwJAPAPqRckNMZQsFN/SA0Vjw9gvUuAb" +
//            "yG9rRixg4Pu6QOjUztvmqqGug/oHQUCeFLmECFBNjqvQJjQ+QyQKFIl78QJAQzm6" +
//            "8RRiVsCMF3ibJM4tIHs5JXEG5uFbHBtVmfNMDwetJx/9XsjjyE+Pz23carE2TXdP" +
//            "/Y2/Wce7rW9+aNjldQJAR+uxYtBpc+Flvf6RtWQTsSqcQnLtqn9n8QKcb81nadF4" +
//            "NxJyxdraaxFUKe4rtdTm51GSu7/j6F7H+2S3rwFnsQ==";
    // 支付宝公钥

//    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQX0tNOzoAtXC2mM563t7GaKRAiGdfmknKgwVialPhKJqJgsaD9lWw6OV7h4ZojrIhTqN/xJdFmhuBKpThLiIH9T7pTRlICA3TizJhysoY8ZBL767uPgQJXJ838WzcbvXEjXHhMMnwD/6ZVs5Fb8GT89y1+Vv979EDhwT4QCVoEEgme2yR3+xau902JQyencUqEOyyKWjv42gQIDAQAB";

}

