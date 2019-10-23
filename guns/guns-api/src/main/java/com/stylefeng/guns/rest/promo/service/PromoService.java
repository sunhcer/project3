package com.stylefeng.guns.rest.promo.service;


import com.stylefeng.guns.rest.promo.model.PromoToken;
import com.stylefeng.guns.rest.promo.model.PromosVO;

public interface PromoService {
    PromosVO selectAllPromo(Integer cinemaId);

    PromosVO createOrder(PromoToken promoToken, Integer userId);

    void storeStock2Redis();

    boolean createOrderByTransaction(PromoToken promoToken, int userId);

    boolean packageOrderProducer(PromoToken promoToken, int userId);

    String initPromoLog(PromoToken promoToken);

    String generateToken(Integer promoId, String userId);
}
