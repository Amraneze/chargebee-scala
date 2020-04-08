package com.chargebee.models.enums 
sealed trait PaymentMethodType 
case object CARD extends PaymentMethodType 
case object PAYPAL_EXPRESS_CHECKOUT extends PaymentMethodType 
case object AMAZON_PAYMENTS extends PaymentMethodType 
case object DIRECT_DEBIT extends PaymentMethodType 
case object GENERIC extends PaymentMethodType 
case object ALIPAY extends PaymentMethodType 
case object UNIONPAY extends PaymentMethodType 
case object APPLE_PAY extends PaymentMethodType 
case object WECHAT_PAY extends PaymentMethodType 
case object _UNKNOWN extends PaymentMethodType 
