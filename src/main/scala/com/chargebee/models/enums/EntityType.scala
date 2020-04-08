package com.chargebee.models.enums
sealed trait EntityType
case object CUSTOMER extends EntityType
case object SUBSCRIPTION extends EntityType
case object INVOICE extends EntityType
case object QUOTE extends EntityType
case object CREDIT_NOTE extends EntityType
case object TRANSACTION extends EntityType
case object PLAN extends EntityType
case object ADDON extends EntityType
case object COUPON extends EntityType
case object _UNKNOWN extends EntityType
