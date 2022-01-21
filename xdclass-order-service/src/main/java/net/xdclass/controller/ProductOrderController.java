package net.xdclass.controller;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.ClientType;
import net.xdclass.enums.ProductOrderPayTypeEnum;
import net.xdclass.request.ConfirmOrderRequest;
import net.xdclass.service.ProductOrderService;
import net.xdclass.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tanshiwei
 * @since 2022-01-14
 */
@Api("订单模块")
@RestController
@RequestMapping("/api/order/v1")
@Slf4j
public class ProductOrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @ApiOperation("提交订单")
    @PostMapping("confirm")
    public void confirmOrder(@ApiParam("订单对象") @RequestBody ConfirmOrderRequest orderRequest, HttpServletResponse response) {
        JsonData jsonData = productOrderService.confirmOrder(orderRequest);
        if (jsonData.getCode() == 0) {

            String clientType = orderRequest.getClientType();
            String payType = orderRequest.getPayType();
            if (payType.equalsIgnoreCase(ProductOrderPayTypeEnum.ALIPAY.name())) {

                if (clientType.equalsIgnoreCase(ClientType.H5.name())) {
                    writeData(response, jsonData);
                } else {
                    //APP SDK支付  TODO
                }


            } else if (payType.equalsIgnoreCase(ProductOrderPayTypeEnum.WECHAT.name())) {

                //todo 微信支付

            }
        } else {
            log.error("创建订单失败:{}", jsonData.getData());
        }


    }

    private void writeData(HttpServletResponse response, JsonData jsonData) {
        try {
            response.setContentType("text/html;charset=UTF8");
            response.getWriter().write(JSON.toJSONString(jsonData.getData()));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            log.error("写出HTML异常:{}", e);
        }
    }
}
