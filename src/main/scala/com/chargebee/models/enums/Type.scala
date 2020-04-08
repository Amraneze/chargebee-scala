package com.chargebee.models.enums 
sealed trait Type 
case object CARD extends Type 
case object PAYPAL_EXPRESS_CHECKOUT extends Type 
case object AMAZON_PAYMENTS extends Type 
case object DIRECT_DEBIT extends Type 
case object GENERIC extends Type 
case object ALIPAY extends Type 
case object UNIONPAY extends Type 
case object APPLE_PAY extends Type 
case object WECHAT_PAY extends Type 
case object _UNKNOWN extends Type 
