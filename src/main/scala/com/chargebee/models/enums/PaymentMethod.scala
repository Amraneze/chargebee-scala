package com.chargebee.models.enums 
sealed trait PaymentMethod 
case object CARD extends PaymentMethod 
case object CASH extends PaymentMethod 
case object CHECK extends PaymentMethod 
case object CHARGEBACK extends PaymentMethod 
case object BANK_TRANSFER extends PaymentMethod 
case object AMAZON_PAYMENTS extends PaymentMethod 
case object PAYPAL_EXPRESS_CHECKOUT extends PaymentMethod 
case object DIRECT_DEBIT extends PaymentMethod 
case object ALIPAY extends PaymentMethod 
case object UNIONPAY extends PaymentMethod 
case object APPLE_PAY extends PaymentMethod 
case object WECHAT_PAY extends PaymentMethod 
case object ACH_CREDIT extends PaymentMethod 
case object SEPA_CREDIT extends PaymentMethod 
case object OTHER extends PaymentMethod 
case object _UNKNOWN extends PaymentMethod 
